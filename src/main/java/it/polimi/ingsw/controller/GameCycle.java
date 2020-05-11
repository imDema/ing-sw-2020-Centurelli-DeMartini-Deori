package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.events.*;
import it.polimi.ingsw.controller.messages.ActionIdentifier;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Lobby;
import it.polimi.ingsw.model.action.Action;
import it.polimi.ingsw.model.board.Coordinate;
import it.polimi.ingsw.controller.messages.User;
import it.polimi.ingsw.model.board.InvalidActionException;
import it.polimi.ingsw.model.board.events.OnBuildListener;
import it.polimi.ingsw.model.board.events.OnMoveListener;
import it.polimi.ingsw.model.player.Pawn;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.view.events.OnCheckActionListener;
import it.polimi.ingsw.view.events.OnExecuteActionListener;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class GameCycle implements OnExecuteActionListener, OnCheckActionListener {
    private final Lobby lobby;
    private final Game game;
    private boolean pawnSelected = false;
    private Pawn currentPawn;
    private Action[] actions;
    private OnActionsReadyListener actionsReadyListener;
    private OnServerErrorListener serverErrorListener;
    private OnEliminationListener eliminationListener;
    private OnTurnChangeListener turnChangeListener;
    private OnWinListener winListener;

    public GameCycle(Lobby lobby) {
        this.lobby = lobby;
        this.game = lobby.getGame();
    }

    public void setServerEventListener(ServerEventsListener serverEventsListener) {
        actionsReadyListener = serverEventsListener;
        serverErrorListener = serverEventsListener;
        eliminationListener = serverEventsListener;
        turnChangeListener = serverEventsListener;
        winListener = serverEventsListener;
        game.getBoard().setOnMoveListener(serverEventsListener);
        game.getBoard().setOnBuildListener(serverEventsListener);
    }

    public void setEliminationListener(OnEliminationListener eliminationListener) {
        this.eliminationListener = eliminationListener;
    }

    public void setTurnChangeListener(OnTurnChangeListener turnChangeListener) {
        this.turnChangeListener = turnChangeListener;
    }

    public void setWinListener(OnWinListener winListener) {
        this.winListener = winListener;
    }

    public void setServerErrorListener(OnServerErrorListener serverErrorListener) {
        this.serverErrorListener = serverErrorListener;
    }

    public void setActionsReadyListener(OnActionsReadyListener actionsReadyListener) {
        this.actionsReadyListener = actionsReadyListener;
    }

    public void setMoveListener(OnMoveListener moveListener) {
        game.getBoard().setOnMoveListener(moveListener);
    }

    public void setBuildListener(OnBuildListener buildListener) {
        game.getBoard().setOnBuildListener(buildListener);
    }

    private void onActionsReady(Player player, Action[] actions) {
        if (actionsReadyListener != null) {
            User user = lobby.getUser(player).orElse(new User(player));
            List<ActionIdentifier> actionIds = Arrays.stream(actions)
                    .map(ActionIdentifier::new)
                    .collect(Collectors.toList());
            actionsReadyListener.onActionsReady(user, actionIds);
        }
    }

    private Optional<Action> actionFromId(Action[] array, ActionIdentifier actionIdentifier) {
        return Arrays.stream(actions)
                .filter(actionIdentifier::matches)
                .findFirst();
    }

    private boolean checkAction(Player player, Pawn pawn, Action action, Coordinate coordinate) {
        if (player.equals(game.getCurrentPlayer()) &&
                game.getBoard().checkAction(action, currentPawn, coordinate)) {
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
                        if (game.getBoard().executeAction(chosenAction, currentPawn, coordinate) && winListener != null) {
                            winListener.onWin(new User(player));
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
                        if (serverErrorListener != null)
                            serverErrorListener.onServerError("Invalid board action", "An error occurred while executing the action");
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
        if (turnChangeListener != null) {
            if (user.isPresent()) {
                turnChangeListener.onTurnChange(user.get(), game.getTurn());
            } else if (serverErrorListener != null)
                serverErrorListener.onServerError("Error retrieving user", "No user matches current player");
        }
        pawnSelected = false;
        onActionsReady(currentPlayer, actions);
    }
}
