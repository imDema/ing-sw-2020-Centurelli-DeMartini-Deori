package it.polimi.ingsw.view.client.cli;

import it.polimi.ingsw.model.board.Coordinate;
import it.polimi.ingsw.view.client.state.BoardView;

public class CLIBoardView extends BoardView {
    private final int BOARD_SIZE = 5;
    private final int DIM_ROW = 8;
    private final int DIM_COL = 4;
    private StringBuilder firstRow = new StringBuilder();
    private StringBuilder divisorRow = new StringBuilder();
    private StringBuilder lastRow = new StringBuilder();
    // ╔ ╗ ║ ╚  ╝ ═ ╦ ╩ ╬ ╣ ╠

    public CLIBoardView() {
        //Stationary row setup
        firstRow.append("╔");
        divisorRow.append("╠");
        lastRow.append("╚");
        for (int i = 1; i < DIM_ROW * BOARD_SIZE ; i++) {
            if(i % DIM_ROW == 0 ) {
                firstRow.append("╦");
                divisorRow.append("╬");
                lastRow.append("╩");
            }else {
                firstRow.append("═");
                divisorRow.append("═");
                lastRow.append("═");
            }
        }
        firstRow.append("╗");
        divisorRow.append("╣");
        lastRow.append("╝");
    }

    public String renderBoard() {
        StringBuilder builder = new StringBuilder();
        builder.append("\n\n");
        builder.append(firstRow);
        builder.append("\n");
        for (int i = 0; i < BOARD_SIZE; i++) {
            StringBuilder line1 = new StringBuilder("║");
            StringBuilder line2 = new StringBuilder("║");
            StringBuilder line3 = new StringBuilder("║");
            for(int j = 0; j < BOARD_SIZE; j++) {
                Coordinate c = new Coordinate(i,j);
                String s = cellAt(c).setUpStringBuilder(DIM_ROW);
                String[] array = s.split(",");
                line1.append(array[0]).append("║");
                line2.append(array[1]).append("║");
                line3.append(array[2]).append("║");
            }
            builder.append(line1).append("\n")
                    .append(line2).append("\n")
                    .append(line3).append("\n");
            if(i != BOARD_SIZE -1 ) {
                builder.append(divisorRow);
                builder.append("\n");
            }else
                builder.append(lastRow);
        }
        return builder.toString();
    }
}
