import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

// Table class to visually represent the game board using Java swing
public class Table extends JFrame implements KeyListener, ActionListener {
    private final JFrame frame;
    private JButton[][] grid = new JButton[BoardUtils.NUM_TILES][BoardUtils.NUM_TILES];
    private List<Car> cars;
    private final BoardPanel boardPanel;
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
        this.frame = new JFrame();
        setTitle("Rush Hour");
        //todo tableMenuBar
        setSize(OUTER_FRAME_DIMENSION);
        setLayout(new GridLayout(BoardUtils.NUM_TILES, BoardUtils.NUM_TILES));

        this.gameBoard = new Board();
        initBoard();
        this.boardPanel = new BoardPanel();

        addKeyListener(this);
        setVisible(true);
    }

    public static Table get() {
        return INSTANCE;
    }

    public void show() {
        Table.get().boardPanel.drawBoard(Table.get().gameBoard);
    }

    private void initBoard() {
        for (int i = 0; i < BoardUtils.NUM_TILES; i++) {
            for (int j = 0; j < BoardUtils.NUM_TILES; j++) {
                this.grid[i][j] = new JButton();
                this.grid[i][j].setBackground(Color.LIGHT_GRAY);
                this.grid[i][j].setFocusable(false);
                this.grid[i][j].setOpaque(true);
                this.grid[i][j].setBorder(BorderFactory.createLineBorder(Color.BLACK));
                int finalI = i;
                int finalJ = j;
                this.grid[i][j].addActionListener(e -> selectVehicle(finalI, finalJ));
            }
        }
    }

    private void selectVehicle(int row, int col) {
        for (Car car : cars) {
            int carRow = car.getRow();
            int carCol = car.getCol();
            for (int i = 0; i < car.getLength(); i++) {
                if (carRow == row && carCol == col) {
                    this.movedCar = car;
                    return;
                }
                if (car.isVertical()) {
                    carRow++;
                } else {
                    carCol++;
                }
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    private class BoardPanel extends JPanel {
        final List<TilePanel> boardTiles;
        public BoardPanel() {
            super(new GridLayout
                    (BoardUtils.NUM_TILES, BoardUtils.NUM_TILES));
            this.boardTiles = new ArrayList<>();
            for (int i = 0; i < BoardUtils.NUM_TILES; i++) {
                for (int j = 0; j < BoardUtils.NUM_TILES; j++) {
                    final TilePanel tilePanel = new
                            TilePanel(this, i, j);
                    this.boardTiles.add(tilePanel);
                    add(tilePanel);
                }
            }
            setPreferredSize(BOARD_PANEL_DIMENSION);
            validate();
        }

        public void drawBoard(final Board board) {
            removeAll();
            for (final TilePanel tilePanel : boardTiles) {
                tilePanel.drawTile(board);
                add(tilePanel);
            }
            validate();
            repaint();
        }
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
            setBackground(Color.LIGHT_GRAY);
            assignColor(gameBoard);

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

            validate();
        }

        public void drawTile(final Board board) {
            assignColor(board);
            validate();
            repaint();
        }

        // MVP: assign basic color for tile based on car
        private void assignColor(final Board board) {
            Tile tile = board.getTile(tileRow, tileCol);
            Car tileCar = tile.getCar();
            if (tileCar == null) {
                setBackground(Color.LIGHT_GRAY);
            } else {
                char color = tileCar.getColor();
                if (color == 'R') {
                    setBackground(Color.red);
                } else if (color == 'G') {
                    setBackground(Color.green);
                } else if (color == 'Y') {
                    setBackground(Color.yellow);
                } else if (color == 'B') {
                    setBackground(Color.blue);
                } else if (color == 'O') {
                    setBackground(Color.orange);
                } else {
                    throw new IllegalStateException(
                            "Unknown color: " + color
                    );
                }
            }

        }
    }
}
