import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Observable;

// Table class to visually represent the game board using Java swing
public class Table extends Observable {
    private final JFrame frame;
    // private final BoardPanel boardPanel;
    private Board gameBoard;

    private Tile sourceTile;
    private Tile targetTile;
    private Car movedCar;

    // dimensions for outer frame, gameBoard, and individual tiles
    private final static Dimension OUTER_FRAME_DIMENSION =
            new Dimension(600, 600);
    private final static Dimension BOARD_PANEL_DIMENSION =
            new Dimension(400, 350);
    private final static Dimension TILE_PANEL_DIMENSION =
            new Dimension(10, 10);
    //todo - make a defaultPieceImagesPath String

    private static final Table INSTANCE = new Table();

    // defines Table object, initializes gameBoard, frame, and dimensions
    private Table() {
        this.frame = new JFrame("Rush Hour");
        this.frame.setLayout(new BorderLayout());
        //todo tableMenuBar
        this.frame.setSize(OUTER_FRAME_DIMENSION);

        this.gameBoard = new Board();

        this.frame.setVisible(true);
    }

    public static Table get() {
        return INSTANCE;
    }

    private class BoardPanel extends JPanel {

    }

    private class TilePanel extends JPanel {
        private int tileRow;
        private int tileCol;
        private boolean highlightLegals;
        TilePanel(final BoardPanel boardPanel, int row, int col) {
            super(new GridBagLayout());
            this.tileRow = row;
            this.tileCol = col;
            // highlight legal moves
            this.highlightLegals = false;
            setPreferredSize(TILE_PANEL_DIMENSION);
            // todo assignTileColor();
            // todo assignTilePieceIcon(chessBoard);

            // listen to user's mouse activity
            addMouseListener(new MouseListener() {

                @Override
                public void mouseClicked(MouseEvent e) {

                }

                @Override
                public void mousePressed(MouseEvent e) {

                }

                @Override
                public void mouseReleased(MouseEvent e) {

                }

                @Override
                public void mouseEntered(MouseEvent e) {

                }

                @Override
                public void mouseExited(MouseEvent e) {

                }
            });
        }
    }
}
