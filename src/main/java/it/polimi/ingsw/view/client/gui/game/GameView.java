package it.polimi.ingsw.view.client.gui.game;

import it.polimi.ingsw.controller.messages.User;
import it.polimi.ingsw.model.board.Coordinate;
import it.polimi.ingsw.view.client.ServerHandler;
import it.polimi.ingsw.view.client.controls.BoardViewState;
import it.polimi.ingsw.view.client.controls.GameControl;
import it.polimi.ingsw.view.client.gui.game.board.*;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

public class GameView extends BorderPane {
    private final GameControl gameControl;

    private final BoardClickHandlerContext boardClickHandler;

    private final BoardView boardView;
    private final PlayerListView playerListView;
    private final Label testLabel = new Label();
    private final ButtonBar buttonBar = new ButtonBar();

    public Label getTestLabel() {
        return testLabel;
    }

    public ButtonBar getButtonBar() {
        return buttonBar;
    }

    public void highlight(Coordinate c, boolean on) {
        boardView.highlightCell(c, on);
    }

    public GameView(ServerHandler server, BoardViewState boardViewState, User firstUser) {
        this.gameControl = new GameControl(server, boardViewState);
        this.boardView = new BoardView(boardViewState);
        this.playerListView = new PlayerListView(gameControl);
        this.boardClickHandler = new BoardClickHandlerContext(this, gameControl);

        if (gameControl.getBoardViewState().getMyUser().map(firstUser::equals).orElse(false)) {
            boardClickHandler.setState(new PlacePawnState());
        } else {
            boardClickHandler.setState(new WaitingState());
        }


        initView();
        bindViewState();
    }

    private void bindViewState() {
        boardView.setOnCellClick(boardClickHandler::handleClick);
        gameControl.addRedrawListener(() -> Platform.runLater(boardView::updateView));
        gameControl.setOnRequestPlaceListener(() ->
                boardClickHandler.setState(new PlacePawnState()));
        gameControl.setOnRequestWaitListener(() ->
                boardClickHandler.setState(new WaitingState()));
        gameControl.setOnActionsReadyListener(actions ->
                boardClickHandler.setState(new ExecuteActionState(actions)));
    }

    private void initView() {
        boardView.boardHeightProperty().bind(heightProperty().multiply(0.8));

        setAlignment(testLabel, Pos.CENTER);
        setAlignment(buttonBar, Pos.CENTER);
        this.setLeft(playerListView);
        this.setTop(testLabel);
        this.setCenter(boardView);
        this.setBottom(buttonBar);
    }
}
