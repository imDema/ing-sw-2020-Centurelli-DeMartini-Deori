package it.polimi.ingsw.view.client.gui.game;

import it.polimi.ingsw.controller.messages.User;
import it.polimi.ingsw.view.client.ServerHandler;
import it.polimi.ingsw.view.client.gui.game.board.*;
import it.polimi.ingsw.view.client.state.BoardViewModel;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

public class GameView extends BorderPane {
    private final GameViewModel gameViewModel;

    private final BoardClickHandlerContext boardClickHandler;

    private final BoardView boardView;
    private final PlayerListView playerListView;
    private final Label testLabel = new Label();

    public Label getTestLabel() {
        return testLabel;
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
        gameViewModel.addRedrawListener(boardView::updateView);
        gameViewModel.setOnRequestPlaceListener(() ->
                boardClickHandler.setState(new PlacePawnState()));
        gameViewModel.setOnRequestWaitListener(() ->
                boardClickHandler.setState(new WaitingState()));
        gameViewModel.setOnActionsReadyListener(actions ->
                boardClickHandler.setState(new ExecuteActionState(actions)));
    }

    private void initView() {
        boardView.boardHeightProperty().bind(heightProperty());


        this.setLeft(playerListView);
        this.setTop(testLabel);
        this.setCenter(boardView);
    }
}
