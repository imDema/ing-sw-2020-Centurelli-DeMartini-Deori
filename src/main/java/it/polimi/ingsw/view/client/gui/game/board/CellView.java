package it.polimi.ingsw.view.client.gui.game.board;

import it.polimi.ingsw.Resources;
import it.polimi.ingsw.view.client.state.CellViewModel;
import it.polimi.ingsw.view.client.state.PawnViewModel;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import java.util.function.Function;

public class CellView extends StackPane {
    private final DoubleProperty cellWidthProperty = new SimpleDoubleProperty();
    private final DoubleProperty cellHeightProperty = new SimpleDoubleProperty();
    private final Function<PawnViewModel, ImageView> pawnRenderer;

    private final ImageView highlight;

    private final CellViewModel viewModel;

    public CellView(CellViewModel viewModel, Function<PawnViewModel, ImageView> pawnRenderer) {
        this.viewModel = viewModel;
        this.pawnRenderer = pawnRenderer;

        minHeightProperty().bind(cellHeightProperty);
        maxHeightProperty().bind(cellHeightProperty);

        minWidthProperty().bind(cellWidthProperty);
        maxWidthProperty().bind(cellWidthProperty);

        highlight = Resources.loadCellHighlight(this);

        highlight.fitHeightProperty().bind(cellHeightProperty);
        highlight.fitWidthProperty().bind(cellWidthProperty);
        this.getChildren().add(highlight);
        highlight.setVisible(false);
    }

    public DoubleProperty cellHeightPropertyProperty() {
        return cellHeightProperty;
    }

    public DoubleProperty cellWidthPropertyProperty() {
        return cellWidthProperty;
    }

    private ImageView renderPawn(PawnViewModel pawn) {
        ImageView pawnView = pawnRenderer.apply(pawn);
        pawnView.fitHeightProperty().bind(cellHeightProperty);
        pawnView.fitWidthProperty().bind(cellWidthProperty);
        return pawnView;
    }

    public void updateView() {
        this.getChildren().clear();
        ImageView building = Resources.loadBuilding(this, viewModel.getBuilding());

        building.fitHeightProperty().bind(cellHeightProperty);
        building.fitWidthProperty().bind(cellWidthProperty);

        this.getChildren().add(building);

        viewModel.getPawn()
                .map(this::renderPawn)
                .ifPresent(this.getChildren()::add);

        this.getChildren().add(highlight);
        highlight.setVisible(false);
    }

    public void highlight(boolean on) {
        highlight.setVisible(on);
    }
}
