package it.polimi.ingsw.view.client.state;

import it.polimi.ingsw.model.board.Building;
import it.polimi.ingsw.view.cli.CLI;
import it.polimi.ingsw.view.cli.Colors;

import java.util.Optional;

public class CellView {
    private boolean hasPawn = false;
    private PawnView pawn = null;
    private Building building = new Building();

    public Optional<PawnView> getPawn() {
        if (hasPawn) {
            return Optional.of(pawn);
        } else {
            return Optional.empty();
        }
    }

    public void putPawn(PawnView pawn) {
        this.pawn = pawn;
        this.hasPawn = true;
    }

    public void removePawn() {
        this.pawn = null;
        this.hasPawn = false;
    }

    public Building getBuilding() {
        return building;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }

    public String setUpStringBuilder(int size) {
        if(building.hasDome()) {
            return "▓▓" + CLI.color("███", Colors.BLUE) + "▓▓,▓" +
                    CLI.color("█████", Colors.BLUE) + "▓,▓▓" +
                    CLI.color("███", Colors.BLUE) + "▓▓";
        }
        String top, fill, bot;
        switch (building.getLevel()) {
            case LEVEL0 -> {
                top = "       ,   ";
                fill = "  ";
                bot = "  ,       ";
            }
            case LEVEL1 -> {
                top = "░░░░░░░,░░░";
                fill = "░░";
                bot = "░░,░░░░░░░";
            }
            case LEVEL2 -> {
                top = "███████,█░░";
                fill = "░░";
                bot = "░█,███████";
            }
            case LEVEL3 -> {
                top = "▓▓███▓▓,▓██";
                fill = "██";
                bot = "█▓,▓▓███▓▓";
            }
            default -> throw new IllegalStateException("Unexpected value: " + building.getLevel());
        }
        return top +
                getPawn().map(PawnView::getSymbol).orElse(fill) +
                bot;
    }
}
