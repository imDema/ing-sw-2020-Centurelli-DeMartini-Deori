package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.events.OnActionsReadyListener;
import it.polimi.ingsw.controller.events.OnServerErrorListener;
import it.polimi.ingsw.controller.events.OnWinListener;
import it.polimi.ingsw.controller.messages.ActionIdentifier;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.action.Action;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.board.Coordinate;
import it.polimi.ingsw.controller.messages.User;
import it.polimi.ingsw.model.board.InvalidActionException;
import it.polimi.ingsw.model.player.Pawn;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.view.events.OnChoosePawnListener;
import it.polimi.ingsw.view.events.OnExecuteActionListener;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class GameCycle implements OnExecuteActionListener, OnChoosePawnListener {
    private Game game;
    private Pawn currentPawn;
    private Action[] actions;
    private State state = State.CHOOSE_PAWN;
    private OnActionsReadyListener actionsReadyListener;
    private OnServerErrorListener serverErrorListener;
    private OnWinListener winListener;

    public GameCycle(Game game) {
        this.game = game;
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
        Player player = game.getCurrentPlayer();
        Board board = game.getBoard();
        Optional<Action> chosenAction = Arrays.stream(actions)
                .filter(actionIdentifier::matches)
                .findFirst();
        if (chosenAction.isPresent()) {
            Action a = chosenAction.get();
            if (board.checkAction(a, currentPawn, coordinate)) {
                try {
                    if (board.executeAction(a, currentPawn, coordinate)) {
                        if (winListener != null)
                            winListener.onWin(new User(player));
                    }
                    actions = player.nextStep(a);

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
