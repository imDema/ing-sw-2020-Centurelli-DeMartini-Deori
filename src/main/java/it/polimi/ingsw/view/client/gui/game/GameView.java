package it.polimi.ingsw.view.client.gui.game;

import it.polimi.ingsw.controller.messages.User;
import it.polimi.ingsw.model.board.Coordinate;
import it.polimi.ingsw.view.client.ServerHandler;
import it.polimi.ingsw.view.client.gui.game.board.*;
import it.polimi.ingsw.view.client.state.BoardViewModel;
import it.polimi.ingsw.view.client.state.GameViewModel;
import javafx.application.Platform;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

public class GameView extends BorderPane {
    private final GameViewModel gameViewModel;

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

    public void highlight(Coordinate c) {
        boardView.highlightCell(c);
    }

    public GameView(ServerHandler server, BoardViewModel boardViewModel, User firstUser) {
        this.gameViewModel = new GameViewModel(server, boardViewModel);
        this.boardView = new BoardView(boardViewModel);
        this.playerListView = new PlayerListView(gameViewModel);
        this.boardClickHandler = new BoardClickHandlerContext(this, gameViewModel);

        if (gameViewModel.getBoardViewModel().getMyUser().map(firstUser::equals).orElse(false)) {
            boardClickHandler.setState(new PlacePawnState());
        } else {
            boardClickHandler.setState(new WaitingState());
        }


        initView();
        bindViewModel();
    }

    private void bindViewModel() {
        boardView.setOnCellClick(boardClickHandler::handleClick);
        gameViewModel.addRedrawListener(() -> Platform.runLater(boardView::updateView));
        gameViewModel.setOnRequestPlaceListener(() ->
                boardClickHandler.setState(new PlacePawnState()));
        gameViewModel.setOnRequestWaitListener(() ->
                boardClickHandler.setState(new WaitingState()));
        gameViewModel.setOnActionsReadyListener(actions ->
                boardClickHandler.setState(new ExecuteActionState(actions)));
    }

    private void initView() {
        boardView.boardHeightProperty().bind(heightProperty().multiply(0.8));

        this.setLeft(playerListView);
        this.setTop(testLabel);
        this.setCenter(boardView);
        this.setBottom(buttonBar);
    }
}
