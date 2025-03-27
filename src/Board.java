import java.util.*;

// Board class, grid object of Tiles that contains all cars
public class Board {
    private final Tile[][] gameBoard;
    private Collection<Car> allCars;

    // constructor, defines the state of a board object
    public Board() {
        this.gameBoard = new Tile
                [BoardUtils.NUM_TILES][BoardUtils.NUM_TILES];
        this.allCars = new ArrayList<>();
        // standard board setup
        // todo - replace this with leveled positions
        initBoard();
        printBoard();
    }

    // initialize board with standard set up for MVP
    private void initBoard() {
        // for loop to erase board
        for (int row = 0; row < BoardUtils.NUM_TILES; row++) {
            for (int col = 0; col < BoardUtils.NUM_TILES; col++) {
                this.gameBoard[row][col] = new Tile.EmptyTile(row, col);
            }
        }

    }

    // essentially makes a new board when a car moves
    private void transitionBoard() {
        for (int row = 0; row < BoardUtils.NUM_TILES; row++) {
            for (int col = 0; col < BoardUtils.NUM_TILES; col++) {
                    this.gameBoard[row][col] = new Tile.EmptyTile(row, col);
            }
        }
        placeCars();
    }

    // loop through all vehicles and place them on the game board
    private void placeCars() {
        for (Car car : allCars) {
            int row = car.getRow();
            int col = car.getCol();
            // makes sure cars are placed at each tile according to length
            for (int i = 0; i < car.getLength(); i++) {
                gameBoard[row][col] = new Tile.OccupiedTile(row, col, car);
                // acoount for car's direction
                if (car.isVertical()) {
                    row++;
                } else {
                    col++;
                }
            }
        }
    }

    // moves a given car specified by the user as far as the user specifies
    public void moveCar(char color, int displacement) {
        // finds car user wants to move
        for (Car car : allCars) {
            // checks if the color of the car matches the color the user specified
            // if it passes that check, it calls canMove method to check if the car
            // can even move with given displacement
            if (car.getColor() == color && canMove(car, displacement)) {
                int row = car.getRow();
                int col = car.getCol();
                // removes car from allCars and puts it back in moved position
                allCars.remove(car);
                // account for direction
                if (car.isVertical()) {
                    allCars.add(new Car(
                            row + displacement, col,
                            car.getLength(), true, car.getColor()));
                } else {
                    allCars.add(new Car(
                            row, col + displacement,
                            car.getLength(), false, car.getColor()));
                }
                // "create new board" based on moved car
                transitionBoard();
                break;
            }
        }
    }

    // checks if a given car can move with given displacement
    private boolean canMove(Car car, int displacement) {
        int row = car.getRow();
        int col = car.getCol();
        if (car.isVertical()) {
            row = (displacement > 0) ? car.getLength()-1 : row;
            // checks that all tiles in the cars path are empty
            // if not, return false
            for (int i = 0; i < Math.abs(displacement); i++) {
                row = (displacement > 0) ? row + 1 : row - 1;
                if (gameBoard[row][col].getCar() != null) {
                    return false;
                }
            }
            return true;
        }
        //todo extract method here maybe
        // does the same thing here but horizontally
        col = (displacement > 0) ? car.getLength()-1 : col;
        for (int i = 0; i < Math.abs(displacement); i++) {
            col = (displacement > 0) ? col + 1 : col - 1;
            if (gameBoard[row][col].getCar() != null) {
                return false;
            }
        }
        return true;
    }

    public Tile getTile(int row, int col) {
        return gameBoard[row][col];
    }

    public void setCars(Collection<Car> cars) {
        this.allCars = cars;
        transitionBoard();
    }

    // loops through board and prints visual representation
    // will become practically obsolete with the java swing
    // based gui
    public void printBoard() {
        for (int row = 0; row < BoardUtils.NUM_TILES; row++) {
            for (int col = 0; col < BoardUtils.NUM_TILES; col++) {
                System.out.print(gameBoard[row][col]);
            }
            System.out.println();
        }
        System.out.println();
    }
}
