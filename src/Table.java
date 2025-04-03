import org.w3c.dom.css.RGBColor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

// Table class to visually represent the game board using Java swing
public class Table extends JFrame implements KeyListener, ActionListener {
    // grid of buttons for tiles so we can listen for clicks on
    // tiles and convert that into a selected car
    private JButton[][] grid = new JButton[BoardUtils.NUM_TILES][BoardUtils.NUM_TILES];
    private List<Car> cars;
    private final Board gameBoard;
    // map car symbols to actual java color objects
    private final Map<Character, Color> colorMap;

    private Car movedCar;

    // dimensions for outer frame, gameBoard, and individual tiles
    private final static Dimension OUTER_FRAME_DIMENSION =
            new Dimension(600, 600);
    //todo - make a defaultPieceImagesPath String

    // defines Table object, initializes gameBoard, frame, and dimensions
    public Table(int level) {
        setTitle("Rush Hour");
        // todo - tableMenuBar
        // initialize table size and layout
        setSize(OUTER_FRAME_DIMENSION);
        setLayout(new GridLayout(BoardUtils.NUM_TILES, BoardUtils.NUM_TILES));

        // initialize car color to actual color map
        colorMap = new HashMap<>();
        colorMap.put('R', Color.RED);
        colorMap.put('G', Color.GREEN);
        colorMap.put('Y', Color.YELLOW);
        colorMap.put('B', Color.BLUE);
        colorMap.put('O', Color.ORANGE);
        colorMap.put('M', Color.MAGENTA);
        colorMap.put('P', Color.PINK);
        colorMap.put('g', Color.GRAY);
        colorMap.put('C', Color.CYAN);
        colorMap.put('L', Color.getHSBColor(117, 63, 100));
        colorMap.put('T', Color.getHSBColor(47, 48, 98));

        this.gameBoard = new Board();
        initBoard(level);

        // make the table visible and able to receive input
        addKeyListener(this);
        setFocusable(true);
        setVisible(true);
    }

    // initialize board with JButton grids, standard color, load
    // in basic position
    private void initBoard(int level) {
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
        this.cars = Board.chooseLevel(level);
        // place all cars just added onto the board
        gameBoard.setCars(this.cars);
        transitionBoard();
    }

    // clear board, loop through cars, place cars on board
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

    // given row and col coordinates select car on tile
    private void selectCar(int row, int col) {
        for (Car car : cars) {
            int carRow = car.getRow();
            int carCol = car.getCol();
            // check all tiles that the car covers
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

    // move car in given direction
    private void moveCar(int rowDisplacement, int colDisplacement) {
        if (this.movedCar == null) {
            return;
        }

        if (canMove(rowDisplacement, colDisplacement)) {
            // add car back to cars arraylist with updated coordinates
            cars.remove(this.movedCar);
            cars.add(new Car(this.movedCar.getRow() + rowDisplacement,
                    this.movedCar.getCol() + colDisplacement, this.movedCar.getLength(),
                    this.movedCar.isVertical(), this.movedCar.getColor()));
            // update board object to store car in new coordinate
            gameBoard.setCars(this.cars);
            transitionBoard();
            // check if red car is on win tile
            if (checkWin(this.movedCar.getRow() + rowDisplacement,
                    this.movedCar.getCol() + colDisplacement)) {
                // print win message and exit game
                JOptionPane.showMessageDialog(null,
                        "You have won the game!",
                        "Win Message", JOptionPane.INFORMATION_MESSAGE);
                System.exit(0);
            }
        }
        // default selected car to the car just moved
        selectCar(this.movedCar.getRow() + rowDisplacement,
                this.movedCar.getCol() + colDisplacement);
    }

    // check if a designated car movement is legal
    private boolean canMove(int rowDisplacement,
                             int colDisplacement) {
        // make sure that the car is being designated to move in the right direction
        if ((this.movedCar.isVertical() && colDisplacement != 0) ||
                (!this.movedCar.isVertical() && rowDisplacement != 0)) {
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

    // check if the most recently moved car is red and
    // is on the win square
    private boolean checkWin(int newRow, int newCol) {
        if (this.movedCar.getColor() != 'R') {
            return false;
        }

        // victory tile coordinate
        if (this.movedCar.getLength() == 3) {
            return (newRow == 2) && (newCol == 3);
        }
        return (newRow == 2) && (newCol == 4);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    // designate response for given key input
    @Override
    public void keyPressed(KeyEvent e) {
        if (this.movedCar == null) {
            return;
        }

        switch (e.getKeyCode()) {
            // move car in designated direction based on arrow input
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

    // Create Welcome Screen with play button to start the game
    public static class WelcomeScreen extends JFrame {
        public WelcomeScreen() {
            JFrame frame = new JFrame("Welcome Screen");
            JLabel welcomeLabel = new JLabel("Welcome to Rush Hour!", JLabel.CENTER);
            JButton welcomeButton = new JButton("Play");

            // buttons for each level
            JButton level1Button = new JButton("Level 1");
            JButton level2Button = new JButton("Level 2");
            JButton level3Button = new JButton("Level 3");
            JButton level4Button = new JButton("Level 4");
            JButton level5Button = new JButton("Level 5");

            // on click, the welcome button starts the game
            welcomeButton.addActionListener(e -> new Table(1));

            // initialize board with given level
            level1Button.addActionListener(e -> new Table(1));
            level2Button.addActionListener(e -> new Table(2));
            level3Button.addActionListener(e -> new Table(3));
            level4Button.addActionListener(e -> new Table(4));
            level5Button.addActionListener(e -> new Table(5));

            // todo - add them in position so that they aren't on top of each other
            frame.setSize(300, 300);
            frame.add(welcomeLabel);
            frame.add(welcomeButton);
            frame.add(level1Button);
            frame.add(level2Button);
            frame.add(level3Button);
            frame.add(level4Button);
            frame.add(level5Button);

            frame.setVisible(true);
        }
    }
}
