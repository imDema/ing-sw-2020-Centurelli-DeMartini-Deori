package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.events.OnGameFinishedListener;
import it.polimi.ingsw.controller.events.ServerEventsListener;
import it.polimi.ingsw.controller.messages.ActionIdentifier;
import it.polimi.ingsw.controller.messages.User;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Lobby;
import it.polimi.ingsw.model.action.Action;
import it.polimi.ingsw.model.board.Building;
import it.polimi.ingsw.model.board.Coordinate;
import it.polimi.ingsw.model.board.InvalidActionException;
import it.polimi.ingsw.model.board.events.OnBuildListener;
import it.polimi.ingsw.model.board.events.OnMoveListener;
import it.polimi.ingsw.model.player.Pawn;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.view.events.OnCheckActionListener;
import it.polimi.ingsw.view.events.OnExecuteActionListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class GameCycle implements OnExecuteActionListener, OnCheckActionListener, OnMoveListener, OnBuildListener {
    private final Lobby lobby;
    private final Game game;
    private boolean pawnSelected = false;
    private Pawn currentPawn;
    private Action[] actions;
    private  final GameController gameController;
    private final List<ServerEventsListener> serverEventsListeners = new ArrayList<>();
    private OnGameFinishedListener gameFinishedListener = null;

    public GameCycle(Lobby lobby, GameController gameController) {
        this.lobby = lobby;
        this.game = lobby.getGame();
        this.gameController = gameController;
        // Register this as listener for model events to allow forwarding to view
        game.getBoard().setOnMoveListener(this);
        game.getBoard().setOnBuildListener(this);
    }

    void setGameFinishedListener(OnGameFinishedListener gameFinishedListener) {
        this.gameFinishedListener = gameFinishedListener;
    }

    void addServerEventListener(ServerEventsListener serverEventsListener) {
        serverEventsListeners.add(serverEventsListener);
    }

    void removeServerEventsListener(ServerEventsListener serverEventsListener) {
        serverEventsListeners.remove(serverEventsListener);
    }

    // Forward model events to all listeners
    @Override
    public void onBuild(Building building, Coordinate coordinate) {
        serverEventsListeners.forEach(l -> l.onBuild(building, coordinate));
    }

    @Override
    public void onMove(Coordinate from, Coordinate to) {
        serverEventsListeners.forEach(l -> l.onMove(from, to));
    }

    private void onActionsReady(Player player, Action[] actions) {
        User user = lobby.getUser(player).orElse(new User(player));
        List<ActionIdentifier> actionIds = Arrays.stream(actions)
                .map(ActionIdentifier::new)
                .collect(Collectors.toList());
        serverEventsListeners.forEach(l -> l.onActionsReady(user, actionIds));
    }

    private Optional<Action> actionFromId(Action[] array, ActionIdentifier actionIdentifier) {
        return Arrays.stream(actions)
                .filter(actionIdentifier::matches)
                .findFirst();
    }

    private boolean checkAction(Player player, Pawn pawn, Action action, Coordinate coordinate) {
        if (player.equals(game.getCurrentPlayer()) &&
                game.getBoard().checkAction(action, pawn, coordinate)) {
            return !pawnSelected || pawn.equals(currentPawn);
        } else {
            return false;
        }
    }

    @Override
    public boolean onCheckAction(User user, int pawnId, ActionIdentifier actionIdentifier, Coordinate coordinate) {
        synchronized (lobby) {
            Optional<Player> player = lobby.getPlayer(user);
            if (player.isPresent() && pawnId >= 0 && pawnId < lobby.PAWN_N) {
                Pawn pawn = player.get().getPawn(pawnId);
                Optional<Action> chosenAction = actionFromId(actions, actionIdentifier);

                return chosenAction.isPresent() && checkAction(player.get(), pawn, chosenAction.get(), coordinate);
            } else {
                return false;
            }
        }
    }

    @Override
    public boolean onExecuteAction(User user, int pawnId, ActionIdentifier actionIdentifier, Coordinate coordinate) {
        synchronized (lobby) {
            Optional<Player> optionalPlayer = lobby.getPlayer(user);
            Optional<Action> optionalAction = actionFromId(actions, actionIdentifier);

            // Check if the action is allowed
            if (optionalPlayer.isPresent() && optionalAction.isPresent() &&
                    pawnId >= 0 && pawnId < lobby.PAWN_N) {

                Player player =  optionalPlayer.get();
                Pawn pawn = player.getPawn(pawnId);
                Action chosenAction = optionalAction.get();

                if (checkAction(player, pawn, chosenAction, coordinate)) {
                    if (!pawnSelected) {
                        pawnSelected = true;
                    }
                    currentPawn = pawn;

                    try {
                        // Execute the action, call winListener if it was a winning move
                        if (game.getBoard().executeAction(chosenAction, currentPawn, coordinate)) {
                            serverEventsListeners.forEach(l -> l.onWin(new User(player)));

                            if(gameFinishedListener != null)
                                gameFinishedListener.onGameFinished(gameController);
                        }

                        // Progress through the steps
                        actions = player.nextStep(chosenAction);
                        if (Arrays.equals(actions, new Action[]{Action.endTurn})) {
                            // Progress the turn
                            game.nextTurn();
                            startTurn();
                        } else {
                            onActionsReady(player, actions);
                        }
                        return true;
                    } catch (InvalidActionException e) {
                        // This should never happen because action is checked before being executed
                        serverEventsListeners.forEach(l -> l.onServerError("Invalid board action", "An error occurred while executing the action"));
                        e.printStackTrace();
                        return false;
                    }
                }
            }
            return false;
        }
    }

    void startTurn() {
        Player currentPlayer = game.getCurrentPlayer();
        actions = currentPlayer.nextStep(Action.start);
        Optional<User> user = lobby.getUser(currentPlayer);
        if (user.isPresent()) {
            serverEventsListeners.forEach(l -> l.onTurnChange(user.get(), game.getTurn()));
        } else {
            serverEventsListeners.forEach(l -> l.onServerError("Error retrieving user", "No user matches current player"));
        }
        pawnSelected = false;
        onActionsReady(currentPlayer, actions);
    }
}
