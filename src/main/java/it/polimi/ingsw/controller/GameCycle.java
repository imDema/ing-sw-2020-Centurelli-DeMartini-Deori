package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.events.OnGameFinishedListener;
import it.polimi.ingsw.controller.events.OnServerEventListener;
import it.polimi.ingsw.controller.messages.ActionIdentifier;
import it.polimi.ingsw.controller.messages.User;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Lobby;
import it.polimi.ingsw.model.action.Action;
import it.polimi.ingsw.model.board.Board;
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

/**
 * The GameCycle class implements the controller for the in-game events, such as the start of the turn,
 * the actions and all the other in-game events.
 * GameCycle forward the events from the view to the model and vice versa
 */
public class GameCycle implements OnExecuteActionListener, OnCheckActionListener, OnMoveListener, OnBuildListener {
    private final Lobby lobby;
    private final Game game;
    private boolean pawnSelected = false;
    private Pawn currentPawn;
    private Action[] actions;
    private final GameController gameController;
    private final List<OnServerEventListener> serverEventListeners = new ArrayList<>();
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

    void addServerEventListener(OnServerEventListener onServerEventListener) {
        serverEventListeners.add(onServerEventListener);
    }

    void removeServerEventsListener(OnServerEventListener onServerEventListener) {
        serverEventListeners.remove(onServerEventListener);
    }

    /**
     * Forward model events to all listeners
     */
    @Override
    public void onBuild(Building building, Coordinate coordinate) {
        serverEventListeners.forEach(l -> l.onBuild(building, coordinate));
    }

    /**
     * Forward model events to all listeners
     */
    @Override
    public void onMove(Coordinate from, Coordinate to) {
        serverEventListeners.forEach(l -> l.onMove(from, to));
    }

    private void onActionsReady(Player player, Action[] actions) {
        User user = lobby.getUser(player).orElse(new User(player));
        List<ActionIdentifier> actionIds = Arrays.stream(actions)
                .map(ActionIdentifier::new)
                .collect(Collectors.toList());
        serverEventListeners.forEach(l -> l.onActionsReady(user, actionIds));
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

    /**
     * Checks if an action can be executed
     * @param user The user that wants to execute the action
     * @param pawnId The pawn selected to execute the action
     * @param actionIdentifier The actionIdentifier that corresponds to the chosen action
     * @param coordinate The coordinate where the user wants to execute the action
     * @return true if the action is executable
     */
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

    /**
     * When a user wants to execute an action the method checks if the action is allowed,
     * then the action is executed while checking if it's a winning action or not, in case
     * an event of Win is launched.
     * If the action executed was the last of the turn the game goes on to the next turn, otherwise
     * the user goes one with its next action
     * @param user The user that wants to execute the action
     * @param pawnId The pawn selected by the user that wants to execute the action
     * @param actionIdentifier The ActionIdentifier corresponding to the chosen action
     * @param coordinate The coordinate where the user wants to execute the action
     * @return true if the action is executed correctly
     */
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
                            serverEventListeners.forEach(l -> l.onWin(new User(player)));

                            if(gameFinishedListener != null)
                                gameFinishedListener.onGameFinished(gameController);
                        }

                        // Progress through the steps
                        actions = player.nextStep(chosenAction);
                        if (Arrays.equals(actions, new Action[]{Action.endTurn})) {
                            // Start the next turn
                            game.nextTurn();
                            startTurn();
                        } else {
                            // Check if the player is eliminated and progress
                            if (!canDoAnything(currentPawn, actions)) {
                                elimination(user);
                            } else {
                                onActionsReady(player, actions);
                            }
                        }
                        return true;
                    } catch (InvalidActionException e) {
                        // This should never happen because action is checked before being executed
                        serverEventListeners.forEach(l -> l.onServerError("Invalid board action", "An error occurred while executing the action"));
                        e.printStackTrace();
                        return false;
                    }
                }
            }
            return false;
        }
    }

    /**
     * The user's turn starts if it has not been eliminated
     */
    void startTurn() {
        Player currentPlayer = game.getCurrentPlayer();
        actions = currentPlayer.nextStep(Action.start);
        Optional<User> user = lobby.getUser(currentPlayer);
        boolean canDoAnything = canDoAnything(currentPlayer.getPawn(0), actions) ||
                                canDoAnything(currentPlayer.getPawn(1), actions);

        if (user.isPresent()) {
            serverEventListeners.forEach(l -> l.onTurnChange(user.get(), game.getTurn()));
            pawnSelected = false;
            onActionsReady(currentPlayer, actions);

            if (!canDoAnything){
                elimination(user.get());
            }
        } else {
            serverEventListeners.forEach(l -> l.onServerError("Error retrieving user", "No user matches current player"));
        }

    }

    /**
     * @return true if the given pawn can execute at least one action
     */
    private boolean canDoAnything(Pawn pawn, Action[] actions) {
        Coordinate coordinate = pawn.getPosition();
        final Board board = game.getBoard();
        for (Action a: actions) {
            if (coordinate.anyNeighbouring(c -> board.checkAction(a, pawn, c))){
                return true;
            }
        }
        return false;
    }

    /**
     * Eliminates the user from the game. If only one user remains in the game,
     * that is the winner
     * @param user The user that is eliminated
     */
    private void elimination(User user) {
        Optional<Player> player = lobby.getPlayer(user);
        player.ifPresent(p -> {
            game.elimination(p);
            serverEventListeners.forEach(l -> l.onElimination(user));
            game.nextTurn();
            startTurn();
            if (game.getPlayerNumber() == 1) {
                User winner = lobby.getUser(game.getPlayers().get(0)).orElseThrow();
                serverEventListeners.forEach(l -> l.onWin(winner));
            }
        });
    }
}
