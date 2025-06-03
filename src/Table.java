import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;
import javax.swing.Timer;

// Table class to visually represent the game board using Java swing
public class Table extends JFrame implements KeyListener, ActionListener {
    // grid of buttons for tiles so we can listen for clicks on
    // tiles and convert that into a selected car
    private final JButton[][] grid = new JButton[BoardUtils.NUM_TILES][BoardUtils.NUM_TILES + 1];
    private final JPanel gridPanel;
    private List<Car> cars;
    private final Board gameBoard;
    // map car symbols to actual java color objects
    private final Map<Character, String> colorMap;
    private final int level;
    private static int minutes;
    private static int seconds;
    private Timer timer;

    private JLabel moveLabel;
    private JLabel timerLabel;

    private Car movedCar;
    private int numMoves;

    // dimensions for outer frame, gameBoard, and individual tiles
    private final static Dimension OUTER_FRAME_DIMENSION =
            new Dimension(900, 600);

    protected static final String pathToImagesURL = "Rush-Hour-Art/";

    protected static boolean isFirstRun;

    // defines Table object, initializes gameBoard, frame, and dimensions
    public Table(int level) throws IOException {
        setTitle("Rush Hour");
        // todo - tableMenuBar
        // initialize table size and layout
        setSize(OUTER_FRAME_DIMENSION);
        setLayout(new BorderLayout());
        this.gridPanel = new JPanel(
                new GridLayout(BoardUtils.NUM_TILES, BoardUtils.NUM_TILES + 1));
        gridPanel.setPreferredSize(new Dimension(700, 600));
        this.numMoves = 0;
        this.level = level;

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
        colorMap.put('P', "pink");
        colorMap.put('T', "tan");

        this.gameBoard = new Board();
        initBoard(level);

        final JMenuBar tableMenuBar = createTableMenuBar();
        this.setJMenuBar(tableMenuBar);

        // make the table visible and able to receive input
        addKeyListener(this);
        setFocusable(true);
        setVisible(true);

        if (isFirstRun) {
            new TutorialScreen();
            isFirstRun = false;
        }
    }

    private JMenuBar createTableMenuBar() {
        // add file menu to JMenuBar
        final JMenuBar tableMenuBar = new JMenuBar();
        tableMenuBar.add(helpMenu());

        return tableMenuBar;
    }

    // file option in JMenuBar, gives user option to view tutorial or quit level
    private JMenu helpMenu() {
        // File Menu that stores tutorial and exit items
        final JMenu fileMenu = new JMenu("Help");

        final JMenuItem tutorialMenuItem = new JMenuItem("Tutorial");
        // load tutorial screen
        tutorialMenuItem.addActionListener(e -> {
            try {
                new TutorialScreen();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        final JMenuItem exitMenuItem = new JMenuItem("Exit");
        // close level window, keep main menu up
        exitMenuItem.addActionListener(e -> {
            // reset timer
             seconds = 0;
             minutes = 0;
             timer.stop();
             this.dispose();
        });

        fileMenu.add(tutorialMenuItem);
        fileMenu.add(exitMenuItem);
        return fileMenu;
    }

    private void addSidePanel() throws IOException {
        // Side Panel to hold information including, level, total moves, time, etc.
        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
        sidePanel.setPreferredSize(new Dimension(200, 600));
        sidePanel.setBackground(new Color(220, 220, 220)); // optional

        // Label to display current level
        // ImageIcon loaded from art file matching to corresponding level
        BufferedImage levelImage = ImageIO.read(new File(
                Table.pathToImagesURL + "level-" + this.level + ".png"
        ));
        // label fields to control label state and conveniently manually update when needed
        JLabel levelLabel = new JLabel(new ImageIcon(levelImage));
        levelLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidePanel.add(levelLabel);

        // Label to display current amount of moves
        moveLabel = new JLabel("Moves: " + numMoves);
        moveLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        // spacing
        sidePanel.add(Box.createRigidArea(new Dimension(0, 10)));

        sidePanel.add(moveLabel);

        // Label to display time elapsed to solve the level
        timerLabel = new JLabel("Time: 00:00");
        timerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidePanel.add(Box.createRigidArea(new Dimension(0, 10)));
        sidePanel.add(timerLabel);

        // Label to display controls
        BufferedImage controlImage = ImageIO.read(new File(
                Table.pathToImagesURL + "arrowKeys.png"
        ));
        JLabel controlLabel = new JLabel(new ImageIcon(controlImage.getScaledInstance
                (200, 100, Image.SCALE_SMOOTH)));
        controlLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Text under controlLabel
        JLabel controlText = new JLabel("USE ARROW KEYS TO");
        controlText.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Second label to ensure no words get cut off
        JLabel controlText2 = new JLabel("CONTROL MOVEMENT");
        controlText2.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Third Label to ensure no words get cut off
        JLabel controlText3 = new JLabel("RED CAR NEEDS TO ESCAPE");
        controlText3.setAlignmentX(Component.CENTER_ALIGNMENT);

        // spacing
        sidePanel.add(Box.createRigidArea(new Dimension(0, 10)));

        sidePanel.add(controlLabel);
        sidePanel.add(controlText);
        sidePanel.add(controlText2);
        sidePanel.add(controlText3);

        this.add(sidePanel, BorderLayout.WEST);
    }

    // initialize board with JButton grids, standard color, load
    // in basic position
    private void initBoard(int level) throws IOException {
        for (int i = 0; i < BoardUtils.NUM_TILES; i++) {
            // account for extra row with arrow on it
            for (int j = 0; j < BoardUtils.NUM_TILES + 1; j++) {
                this.grid[i][j] = new JButton();
                // if on extra row, set background to dark gray so users know that it's
                // off limits
                if (j == BoardUtils.NUM_TILES) {
                    this.grid[i][j].setBackground(Color.DARK_GRAY);
                } else {
                    this.grid[i][j].setBackground(new Color(240,236,236));
                }
                this.grid[i][j].setFocusable(false);
                this.grid[i][j].setOpaque(true);
                this.grid[i][j].setBorder(BorderFactory.createLineBorder(Color.BLACK));
                int finalI = i;
                int finalJ = j;
                this.grid[i][j].addActionListener(e -> selectCar(finalI, finalJ));
                this.gridPanel.add(grid[i][j]);
            }
        }

        // create scaled image of arrow
        ImageIcon defaultArrowIcon = new ImageIcon(pathToImagesURL + "arrow.png");
        Image defaultArrowImage = defaultArrowIcon.getImage().
                getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        ImageIcon arrowIcon = new ImageIcon(defaultArrowImage);

        // set tile adjacent to escape tile to have the arrow on it
        this.grid[2][6].setIcon(arrowIcon);
        this.grid[2][6].setOpaque(false);
        this.grid[2][6].setContentAreaFilled(false);
        this.grid[2][6].setBorderPainted(false);

        // add standard cars in standard positions
        this.cars = Board.chooseLevel(level);
        // place all cars just added onto the board
        gameBoard.setCars(this.cars);
        this.add(gridPanel, BorderLayout.CENTER);
        addSidePanel();
        // initialize timer after initializing board
        timer = new Timer(1000, e -> updateTimer());
        timer.start();
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
            this.numMoves++;
            moveLabel.setText("Moves: " + this.numMoves);
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

    // helper method to update timer
    private void updateTimer() {
        seconds++;
        if (seconds >= 60) {
            seconds = 0;
            minutes += 1;
        }
        String secondString = (seconds < 10 ? "0" : "") + seconds;
        String minuteString = (minutes < 10 ? "0" : "") + minutes;
        timerLabel.setText("Time: " + minuteString + ":" + secondString);
    }

    // check if the most recently moved car is red and
    // is on the win square
    private boolean checkWin(int newRow, int newCol) {
        if (this.movedCar.getColor() != 'R') {
            return false;
        }

        // victory tile coordinate
        if ((newRow == 2) && (newCol == 4)) {
            seconds = 0;
            minutes = 0;
            timer.stop();
            return true;
        }
        return false;
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
            setTitle("Welcome Screen");
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

            Table.isFirstRun = true;

            // for loop to create 5 buttons
            for (int i = 1; i <= 5; i++) {
                ImageIcon levelImage = new ImageIcon(
                        Table.pathToImagesURL +
                                "level-" + i + ".png"
                );
                JButton levelButton = new JButton(levelImage);
                int finalI = i;
                // create game object with given level
                levelButton.addActionListener(e -> {
                    try {
                        new Table(finalI);
                        Table.isFirstRun = false;
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                });
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

            setSize(600, 600);
            add(panel);
            setVisible(true);
        }
    }

    public static class TutorialScreen extends JFrame {
        public TutorialScreen() throws IOException {
            setTitle("Tutorial");

            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

            // BufferedImage instead of ImageIcon so we can put tbe tutorial image
            // and tutorialText image inside a JLabel and center it
            BufferedImage tutorialTextImage = ImageIO.read(new File(
                    Table.pathToImagesURL + "tutorialText.png"
            ));
            BufferedImage tutorialImage = ImageIO.read(new File(
                    Table.pathToImagesURL + "tutorial.png"
            ));

            // JLabels for tutorial text and tutorial image, centering, sizing
            JLabel tutorialTextLabel = new JLabel(new ImageIcon(tutorialTextImage));
            JLabel tutorialLabel = new JLabel(new ImageIcon(
                    tutorialImage.getScaledInstance(500, 300, Image.SCALE_SMOOTH)));
            tutorialTextLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            tutorialLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            tutorialTextLabel.setMaximumSize(new Dimension(500, 50));
            tutorialLabel.setMaximumSize(new Dimension(500, 300));
            panel.add(tutorialTextLabel);
            panel.add(tutorialLabel);

            // okay button to dispose tutorial screen
            ImageIcon okayImage = new ImageIcon(
                    Table.pathToImagesURL + "okay.png"
            );
            JButton okayButton = new JButton(okayImage);
            okayButton.addActionListener(e -> this.dispose());
            okayButton.setMaximumSize(new Dimension(300, 50));
            okayButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(okayButton);

            setSize(900, 600);
            add(panel);
            setVisible(true);
        }
    }
}
