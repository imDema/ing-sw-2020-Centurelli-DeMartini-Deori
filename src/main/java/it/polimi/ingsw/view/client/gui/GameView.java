package it.polimi.ingsw.view.client.gui;

import it.polimi.ingsw.Resources;
import it.polimi.ingsw.view.client.ServerHandler;
import it.polimi.ingsw.view.client.state.BoardViewModel;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

public class GameView extends BorderPane {
    private final GameViewModel gameViewModel;

    public GameView(ServerHandler server, BoardViewModel boardViewModel) {
        this.gameViewModel = new GameViewModel(server, boardViewModel);

        initView();
    }

    private void bindViewModel() {

    }

    private void initView() {
        this.setLeft(new PlayerListView(gameViewModel));


        ImageView testBg = Resources.loadBoardBackground(this);
        testBg.setPreserveRatio(true);
        testBg.setFitHeight(600.0);

        this.setCenter(testBg);
    }
}
