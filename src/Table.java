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
    private final Board gameBoard;
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

        addKeyListener(this);
        setFocusable(true);
        setVisible(true);
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
                this.grid[i][j].addActionListener(e -> selectCar(finalI, finalJ));
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
        gameBoard.setCars(this.cars);
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

    private void selectCar(int row, int col) {
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

    private void moveCar(int rowDisplacement, int colDisplacement) {
        if (this.movedCar == null) {
            return;
        }

        if (canMove(rowDisplacement, colDisplacement)) {
            cars.remove(this.movedCar);
            cars.add(new Car(this.movedCar.getRow() + rowDisplacement,
                    this.movedCar.getCol() + colDisplacement, this.movedCar.getLength(),
                    this.movedCar.isVertical(), this.movedCar.getColor()));
            // this.movedCar = null;
            gameBoard.setCars(this.cars);
            transitionBoard();
        }
        // gameBoard.setCars(this.cars);
    }

    // check if a designated car movement is legal
    private boolean canMove(int rowDisplacement,
                             int colDisplacement) {
        // make sure that the car is being designated to move in the right direction
        if ((this.movedCar.isVertical() && colDisplacement != 0) || (!this.movedCar.isVertical() && rowDisplacement != 0)) {
            return false;
        }
        // save row and col indeces of where the car is designated to move
        // if car is moving forward account for length of car, if moving backwards
        // just add displacement
        int newRow = (rowDisplacement > 0) ? this.movedCar.getRow() + this.movedCar.getLength()
                + rowDisplacement - 1 : this.movedCar.getRow() + rowDisplacement;
        int newCol = (colDisplacement > 0) ? this.movedCar.getCol() + this.movedCar.getLength()
                + colDisplacement - 1 : this.movedCar.getCol() + colDisplacement;

        // ensure car is not being told to move out of bounds
        if (newRow < 0 || newRow >= BoardUtils.NUM_TILES
                || newCol < 0 || newCol >= BoardUtils.NUM_TILES) {
            return false;
        } else return gameBoard.getTile(newRow, newCol).getCar() == null;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (this.movedCar == null) {
            return;
        }

        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP -> moveCar(-1, 0);
            case KeyEvent.VK_DOWN -> moveCar(1, 0);
            case KeyEvent.VK_LEFT -> moveCar(0, -1);
            case KeyEvent.VK_RIGHT -> moveCar(0, 1);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
