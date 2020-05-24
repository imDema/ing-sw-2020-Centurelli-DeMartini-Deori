package it.polimi.ingsw.view.client.gui.game;

import it.polimi.ingsw.view.client.ServerHandler;
import it.polimi.ingsw.view.client.gui.game.board.BoardClickHandlerContext;
import it.polimi.ingsw.view.client.gui.game.board.BoardView;
import it.polimi.ingsw.view.client.gui.game.board.PlacePawnState;
import it.polimi.ingsw.view.client.state.BoardViewModel;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

public class GameView extends BorderPane {
    private final GameViewModel gameViewModel;

    private final BoardClickHandlerContext boardClickHandler = new BoardClickHandlerContext();

    private final BoardView boardView;
    private final PlayerListView playerListView;
    private final Label testLabel = new Label();

    public GameView(ServerHandler server, BoardViewModel boardViewModel) {
        this.gameViewModel = new GameViewModel(server, boardViewModel);
        this.boardView = new BoardView(boardViewModel);
        this.playerListView =new PlayerListView(gameViewModel);

        initView();
        bindViewModel();
    }

    private void bindViewModel() {
        boardClickHandler.setState(new PlacePawnState(gameViewModel));
        boardView.setOnCellClick(boardClickHandler::handleClick);
        gameViewModel.addRedrawListener(boardView::updateView);
    }

    private void initView() {
        boardView.boardHeightProperty().bind(heightProperty());


        this.setLeft(playerListView);
        this.setTop(testLabel);
        this.setCenter(boardView);
    }
}
