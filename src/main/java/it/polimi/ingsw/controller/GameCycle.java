package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.events.*;
import it.polimi.ingsw.controller.messages.ActionIdentifier;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Lobby;
import it.polimi.ingsw.model.action.Action;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.board.Coordinate;
import it.polimi.ingsw.controller.messages.User;
import it.polimi.ingsw.model.board.InvalidActionException;
import it.polimi.ingsw.model.board.events.OnBuildListener;
import it.polimi.ingsw.model.board.events.OnMoveListener;
import it.polimi.ingsw.model.player.Pawn;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.view.events.OnChoosePawnListener;
import it.polimi.ingsw.view.events.OnExecuteActionListener;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class GameCycle implements OnExecuteActionListener, OnChoosePawnListener {
    private Lobby lobby;
    private Game game;
    private Pawn currentPawn;
    private Action[] actions;
    private State state = State.CHOOSE_PAWN;
    private OnActionsReadyListener actionsReadyListener;
    private OnServerErrorListener serverErrorListener;
    private OnEliminationListener eliminationListener;
    private OnTurnChangeListener turnChangeListener;
    private OnWinListener winListener;

    public GameCycle(Lobby lobby) {
        this.lobby = lobby;
        this.game = lobby.getGame();
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
            User user = new User(player);
            List<ActionIdentifier> actionIds = Arrays.stream(actions)
                    .map(ActionIdentifier::new)
                    .collect(Collectors.toList());
            actionsReadyListener.onActionsReady(user,actionIds);
        }
    }

    @Override
    public boolean onExecuteAction(ActionIdentifier actionIdentifier, Coordinate coordinate) {
        if (state == State.CHOOSE_PAWN)
            return false;
        Player player = game.getCurrentPlayer();
        Board board = game.getBoard();
        Optional<Action> chosenAction = Arrays.stream(actions)
                .filter(actionIdentifier::matches)
                .findFirst();
        if (chosenAction.isPresent()) {
            Action a = chosenAction.get();
            // The chosen Action has to be checked
            if (board.checkAction(a, currentPawn, coordinate)) {
                try {
                    // The chosen Action is executed
                    if (board.executeAction(a, currentPawn, coordinate) && winListener != null) {
                        winListener.onWin(new User(player));
                    }
                    actions = player.nextStep(a);
                    // If the turn is ended the next player has to choose the pawn
                    if (Arrays.equals(actions, new Action[] {Action.endTurn})) {
                        game.nextTurn();
                        state = State.CHOOSE_PAWN;
                        startTurn();
                    } else {
                        onActionsReady(player, actions);
                    }
                    return true;
                } catch (InvalidActionException e) {
                    if (serverErrorListener != null)
                        serverErrorListener.onServerError("Invalid board action", "An error occurred while executing the action");
                    e.printStackTrace();
                    return false;
                }
            }
        }
        return false;
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
        onActionsReady(currentPlayer, actions);
    }

    @Override
    public void onChoosePawn(User user, int id) {
        Player currentPlayer = game.getCurrentPlayer();
        if (user.matches(currentPlayer) && state == State.CHOOSE_PAWN) {
            currentPawn = currentPlayer.getPawn(id);
            state = State.ACTION;
        }
    }

    enum State {
        CHOOSE_PAWN,
        ACTION
    }
}
