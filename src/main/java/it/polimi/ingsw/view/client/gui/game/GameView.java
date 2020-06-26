package it.polimi.ingsw.view.client.gui.game;

import it.polimi.ingsw.controller.messages.User;
import it.polimi.ingsw.model.board.Coordinate;
import it.polimi.ingsw.view.client.ServerHandler;
import it.polimi.ingsw.view.client.controls.BoardViewState;
import it.polimi.ingsw.view.client.controls.GameControl;
import it.polimi.ingsw.view.client.gui.game.board.*;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

/**
 * Main game window, contains board, list of players and controls
 * @see BoardView
 * @see PlayerListView
 */
public class GameView extends AnchorPane {
    private final GameControl gameControl;

    private final BoardClickHandlerContext boardClickHandler;

    private final BoardView boardView;
    private final PlayerListView playerListView;
    private final Label infoLabel = new Label();
    private final HBox buttonBar = new HBox();

    public Label getInfoLabel() {
        return infoLabel;
    }

    public HBox getButtonBar() {
        return buttonBar;
    }

    /**
     * Highlight a cell on the board
     * @param c Coordinate of the cell to highlight
     * @param on true if the cell should be highlighted, false otherwise
     */
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
        buttonBar.setSpacing(10.0);

        setTopAnchor(infoLabel, 8.0);
        setLeftAnchor(infoLabel, 8.0);
        setRightAnchor(infoLabel, 8.0);
        infoLabel.setAlignment(Pos.CENTER);
        infoLabel.setStyle("-fx-font-size: 16pt;");

        setTopAnchor(boardView, 0.0);
        setBottomAnchor(boardView, 0.0);
        setLeftAnchor(boardView, 0.0);
        setRightAnchor(boardView, 0.0);

        setBottomAnchor(buttonBar, 8.0);
        setLeftAnchor(buttonBar, 8.0);
        setRightAnchor(buttonBar, 8.0);
        buttonBar.setAlignment(Pos.CENTER);
        buttonBar.setStyle("-fx-font-size: 24pt;");

        setLeftAnchor(playerListView, 8.0);
        playerListView.prefHeightProperty().bind(heightProperty());
        playerListView.prefWidthProperty().bind(widthProperty().multiply(0.125));

        Label instructionsLabel = new Label("Left click: select\nRight click: deselect");
        setRightAnchor(instructionsLabel, 8.0);
        setBottomAnchor(instructionsLabel, 8.0);

        this.getChildren().addAll(boardView, playerListView, infoLabel, instructionsLabel, buttonBar);
    }
}
