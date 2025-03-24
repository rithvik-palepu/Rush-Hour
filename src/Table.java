import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

// Table class to visually represent the game board using Java swing
public class Table extends JFrame implements KeyListener, ActionListener {
    private JButton[][] grid = new JButton[BoardUtils.NUM_TILES][BoardUtils.NUM_TILES];
    private List<Car> cars;
    // private final BoardPanel boardPanel;
    private Board gameBoard;
    private final Map<Character, Color> colorMap;

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

    // defines Table object, initializes gameBoard, frame, and dimensions
    public Table() {
        setTitle("Rush Hour");
        //todo tableMenuBar
        setSize(OUTER_FRAME_DIMENSION);
        setLayout(new GridLayout(BoardUtils.NUM_TILES, BoardUtils.NUM_TILES));

        colorMap = new HashMap<>();
        colorMap.put('R', Color.RED);
        colorMap.put('G', Color.GREEN);
        colorMap.put('Y', Color.YELLOW);
        colorMap.put('B', Color.BLUE);
        colorMap.put('O', Color.ORANGE);

        this.gameBoard = new Board();
        initBoard();
        // this.boardPanel = new BoardPanel();

        addKeyListener(this);
        setFocusable(true);
        setVisible(true);
    }

    // public void show() {
       // Table.get().boardPanel.drawBoard(Table.get().gameBoard);
    // }

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
                this.add(grid[i][j]);
            }
        }

        // add standard cars in standard positions
        cars = new ArrayList<>();
        cars.add(new Car(2, 0, 2, false, 'R'));
        cars.add(new Car(0, 3, 2, false, 'G'));
        cars.add(new Car(3, 1, 3, true, 'Y'));
        cars.add(new Car(5, 3, 3, false, 'B'));
        cars.add(new Car(2, 4, 2, true, 'O'));
        // place all cars just added onto the board
        transitionBoard();
    }

    private void transitionBoard() {
        // clear board
        for (int r = 0; r < BoardUtils.NUM_TILES; r++) {
            for (int c = 0; c < BoardUtils.NUM_TILES; c++) {
                grid[r][c].setText("");
                grid[r][c].setBackground(Color.LIGHT_GRAY);
            }
        }

        for (Car car : cars) {
            int row = car.getRow();
            int col = car.getCol();
            for (int i = 0; i < car.getLength(); i++) {
                grid[row][col].setText(car.getColor() + "");
                grid[row][col].setBackground(
                        colorMap.get(car.getColor()));
                if (car.isVertical()) {
                    row++;
                } else {
                    col++;
                }
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

    private void moveCar(int row, int col, int newRow, int newCol) {
        this.movedCar = gameBoard.getTile(row, col).getCar();
        this.targetTile = gameBoard.getTile(newRow, newCol);
        if (movedCar != null) {
            if (canMove(movedCar, targetTile, newRow, newCol)) {

                row = (newRow > row) ? row + movedCar.getLength() : row;
                col = (newCol > col) ? col + movedCar.getLength() : col;
                int displacement = (movedCar.isVertical()) ? Math.abs(newRow - row)
                        : Math.abs(newCol - col);
                cars.remove(movedCar);
                if (movedCar.isVertical()) {
                    cars.add(new Car(row + displacement, col,
                            movedCar.getLength(), true, movedCar.getColor()));
                } else {
                    cars.add(new Car(row, col + displacement,
                            movedCar.getLength(), false, movedCar.getColor()));
                }
                transitionBoard();
            }
        }
    }

    private boolean canMovee(Car movedCar, int rowDisplacement,
                             int colDisplacement) {
        int newRow = movedCar.getRow() + rowDisplacement;
        int newCol = movedCar.getCol() + colDisplacement;

        if (newRow < 0 || newRow >= BoardUtils.NUM_TILES
                || newCol < 0 || newCol >= BoardUtils.NUM_TILES) {
            return false;
        }

        return true;
    }

    private boolean canMove(Car movedCar, Tile targetTile, int newRow, int newCol) {
        int row = movedCar.getRow();
        int col = movedCar.getCol();
        int displacement = (movedCar.isVertical()) ? newRow - row : newCol - col;
        this.sourceTile = gameBoard.getTile(row, col);
        if (movedCar.isVertical()) {
            row = (displacement > 0) ? movedCar.getLength()-1 : row;
            // todo extract method
            displacement = newRow - row;
            for (int i = 0; i < Math.abs(displacement); i++) {
                row = (displacement > 0) ? row + 1 : row - 1;
                if (gameBoard.getTile(row, col).getCar() != null) {
                    return false;
                }
            }
            return true;
        }
        displacement = newCol - col;
        for (int i = 0; i < Math.abs(displacement); i++) {
            col = (displacement > 0) ? col + 1 : col - 1;
            if (gameBoard.getTile(row, col).getCar() != null) {
                return false;
            }
        }
        return true;
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
}
