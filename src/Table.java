import javax.swing.*;
import java.awt.*;
import java.util.Observable;

public class Table extends Observable {
    private final JFrame frame;
    // private final BoardPanel boardPanel;

    private Board gameBoard;

    private final static Dimension OUTER_FRAME_DIMENSION =
            new Dimension(600, 600);
    private final static Dimension BOARD_PANEL_DIMENSION =
            new Dimension(400, 350);
    private final static Dimension TILE_PANEL_DIMENSION =
            new Dimension(10, 10);
    //todo - make a defaultPieceImagesPath String

    private static final Table INSTANCE = new Table();

    private Table() {
        this.frame = new JFrame("Rush Hour");
        this.frame.setLayout(new BorderLayout());
        //todo tableMenuBar
        this.frame.setSize(OUTER_FRAME_DIMENSION);

        this.gameBoard = new Board();
    }
}
