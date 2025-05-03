import org.w3c.dom.css.RGBColor;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
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
    private final Map<Character, String> colorMap;


    private Car movedCar;

    // dimensions for outer frame, gameBoard, and individual tiles
    private final static Dimension OUTER_FRAME_DIMENSION =
            new Dimension(600, 600);

    protected static final String pathToImagesURL = "Rush-Hour-Art/";

    // defines Table object, initializes gameBoard, frame, and dimensions
    public Table(int level) {
        setTitle("Rush Hour");
        // todo - tableMenuBar
        // initialize table size and layout
        setSize(OUTER_FRAME_DIMENSION);
        setLayout(new GridLayout(BoardUtils.NUM_TILES, BoardUtils.NUM_TILES));

        // map to initialize car color to matching car png
        colorMap = new HashMap<>();
        colorMap.put('R', "red");
        colorMap.put('G', "green");
        colorMap.put('Y', "yellow");
        colorMap.put('B', "blue");
        colorMap.put('O', "mustard");
        colorMap.put('M', "purple");
        // colorMap.put('P', Color.PINK);
        colorMap.put('g', "grey");
        colorMap.put('C',"navy");
        colorMap.put('L', "lime");

        /* colorMap.put('R', new ImageIcon(pathToImagesURL + "red-car.png"));
        colorMap.put('G', new ImageIcon(pathToImagesURL + "green-car.png"));
        colorMap.put('Y', new ImageIcon(pathToImagesURL + "yellow-car.png"));
        colorMap.put('B', new ImageIcon(pathToImagesURL + "blue-car.png"));
        colorMap.put('O', new ImageIcon(pathToImagesURL + "mustard-car.png"));
        colorMap.put('M', new ImageIcon(pathToImagesURL + "purple-car.png"));
        // colorMap.put('P', Color.PINK);
        colorMap.put('g', new ImageIcon(pathToImagesURL + "grey-car.png"));
        colorMap.put('C', new ImageIcon(pathToImagesURL + "navy-car.png"));
        colorMap.put('L', new ImageIcon(pathToImagesURL + "lime-car.png")); */
        // colorMap.put('T', Color.getHSBColor(47, 48, 98));

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
                // this.grid[i][j].setBackground(Color.WHITE);
                grid[i][j].setBackground(new Color(240,236,236));
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
                // grid[r][c].setBackground(Color.WHITE);
                grid[r][c].setBackground(new Color(240,236,236));
                grid[r][c].setIcon(null);
                grid[r][c].setContentAreaFilled(true);
                grid[r][c].setBorderPainted(true);
                grid[r][c].setOpaque(true);
            }
        }

        // loop through cars and place on board
        for (Car car : cars) {
            int row = car.getRow();
            int col = car.getCol();

            for (int i = 0; i < car.getLength(); i++) {
                int r = row + (car.isVertical() ? i : 0);
                int c = col + (car.isVertical() ? 0 : i);

                String imagePath = pathToImagesURL + colorMap.get(car.getColor()) + "-car-"
                        + car.getLength();

                if (car.isVertical()) {
                    if (i == 0) {
                        imagePath += "-top";
                    } else if (i == 1) {
                        imagePath += (car.getLength() == 2) ? "-bottom" : "-mid";
                    } else {
                        imagePath += "-bottom";
                    }
                } else {
                    if (i == 0) {
                        imagePath += "-left";
                    } else if (i == 1) {
                        imagePath += (car.getLength() == 2) ? "-right" : "-middle";
                    } else {
                        imagePath += "-right";
                    }
                }
                imagePath += ".png";

                ImageIcon icon = new ImageIcon(imagePath);
                Image image = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);

                this.grid[r][c].setIcon(new ImageIcon(image));
                grid[r][c].setContentAreaFilled(false);
                this.grid[r][c].setBorderPainted(false);
                this.grid[r][c].setOpaque(false);
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
                this.dispose();
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
        public WelcomeScreen() throws IOException {
            JFrame frame = new JFrame("Welcome Screen");
            // panel for buttons with ordered layout
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

            // BufferedImage instead of ImageIcon so we can put tbe logo inside of a JLabel
            // and center it
            BufferedImage logoImage = ImageIO.read(new File(
                    Table.pathToImagesURL + "logo.png"
            ));
            JLabel logoLabel = new JLabel(new ImageIcon(logoImage));
            logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(logoLabel);

            // for loop to create 5 buttons
            for (int i = 1; i <= 5; i++) {
                ImageIcon levelImage = new ImageIcon(
                        Table.pathToImagesURL +
                                "level-" + i + ".png"
                );
                JButton levelButton = new JButton(levelImage);
                int finalI = i;
                // create game object with given level
                levelButton.addActionListener(e -> new Table(finalI));
                // set button size and alignment
                levelButton.setMaximumSize(new Dimension(300, 50));
                levelButton.setAlignmentX(Component.CENTER_ALIGNMENT);
                panel.add(levelButton);
            }
            ImageIcon quitImage = new ImageIcon(
                    Table.pathToImagesURL + "quit.png"
            );
            // quit button to close program
            JButton quitButton = new JButton(quitImage);
            quitButton.addActionListener(e -> System.exit(0));
            quitButton.setMaximumSize(new Dimension(300, 50));
            quitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(quitButton);

            frame.setSize(600, 600);
            frame.add(panel);
            frame.setVisible(true);
        }
    }
}
