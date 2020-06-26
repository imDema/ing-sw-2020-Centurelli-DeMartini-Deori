package it.polimi.ingsw.view.client.gui.game.board;

import it.polimi.ingsw.Resources;
import it.polimi.ingsw.model.board.Coordinate;
import it.polimi.ingsw.view.client.controls.BoardViewState;
import it.polimi.ingsw.view.client.controls.PawnViewState;
import it.polimi.ingsw.view.client.controls.PlayerViewState;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * View for the board and the pieces that are placed on it
 */
public class BoardView extends StackPane {
    private final int SIZE = 5;

    private final BoardViewState boardViewState;
    private final ImageView backgroundImage = Resources.loadBoardBackground(this);
    private final ImageView foregroundImage = Resources.loadBoardForeground(this);

    private final GridPane grid = new GridPane();
    private final CellView[][] cells = new CellView[SIZE][SIZE];

    private final Map<PlayerViewState, Image> pawnImageMap = new HashMap<>();

    private BiConsumer<MouseButton, Coordinate> cellClickListener = null;

    public void setOnCellClick(BiConsumer<MouseButton, Coordinate> onCellClickListener) {
        this.cellClickListener = onCellClickListener;
    }

    public BoardView(BoardViewState boardViewState) {
        this.boardViewState = boardViewState;
        initView();
    }

    private ImageView renderPawn(PawnViewState pawn) {
        PlayerViewState player = pawn.getOwner();
        Image img = pawnImageMap.get(player);
        if (img == null) {
            img = Resources.loadPawn(this,  pawnImageMap.size()).orElseThrow();
            pawnImageMap.put(player, img);
        }
        return new ImageView(img);
    }

    /**
     * Update the Views for all the cells on the board
     */
    public void updateView() {
        for(CellView[] ar : cells) {
            for (CellView c : ar) {
                c.updateView();
            }
        }
    }

    public void updateView(Coordinate c) {
        cells[c.getX()][c.getY()].updateView();
    }

    /**
     * Highlight a cell
     * @param c Coordinate of the cell to highlight
     * @param on true if the cell should be highlighted, false otherwise
     */
    public void highlightCell(Coordinate c, boolean on) {
        cells[c.getX()][c.getY()].highlight(on);
    }

    private void initView() {
        this.prefHeightProperty().bind(this.prefWidthProperty().multiply(0.5625));

        backgroundImage.setPreserveRatio(true);
        backgroundImage.fitHeightProperty().bind(this.heightProperty());

        foregroundImage.setPreserveRatio(true);
        foregroundImage.fitHeightProperty().bind(this.heightProperty());

        grid.prefHeightProperty().bind(this.heightProperty().multiply(0.6));
        grid.prefWidthProperty().bind(grid.prefHeightProperty());
        grid.setAlignment(Pos.CENTER);

        for (int i = 0; i < SIZE ; i++) {
            for (int j = 0; j < SIZE; j++) {
                CellView cell = new CellView(boardViewState.cellAt(new Coordinate(i,j)), this::renderPawn);
                cell.cellWidthPropertyProperty().bind(grid.prefHeightProperty().divide(5.0));

                final int ii = i, jj = j;
                cell.setOnMouseClicked(click -> onBoardClick(click.getButton(), ii, jj));
                grid.add(cell, i, 4 - j);
                cells[i][j] = cell;
            }
        }

        this.setAlignment(Pos.CENTER);
        this.getChildren().addAll(backgroundImage, foregroundImage, grid);
    }

    private void onBoardClick(MouseButton button, int i, int j) {
        Coordinate c = new Coordinate(i, j);

        cells[i][j].updateView();

        if (cellClickListener != null) {
            cellClickListener.accept(button, c);
        }
    }
}
