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
                // account for car's direction
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

    // update board to include and place all cars
    // after a car has been moved
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

    public static List<Car> chooseLevel(int level) {
        List<Car> cars = new ArrayList<>();
        switch(level) {
            case 1:
                cars = levelOne();
                return cars;
            case 2:
                cars = levelTwo();
                return cars;
             case 3:
                 cars = levelThree();
                 return cars;
             case 4:
                 cars = levelFour();
                 return cars;
             case 5:
                 cars = levelFive();
                 return cars;
            default:
                throw new IllegalArgumentException("Sorry! That level isn't supported yet!");
        }
    }

    private static List<Car> levelOne() {
        ArrayList<Car> cars = new ArrayList<>();
        cars.add(new Car(2, 0, 2, false, 'R'));
        cars.add(new Car(0, 2, 3, true, 'Y'));
        cars.add(new Car(3, 5, 3, true, 'B'));
        cars.add(new Car(3, 0, 3, false, 'M'));
        cars.add(new Car(0, 4, 2, false, 'G'));
        return cars;
    }

    private static List<Car> levelTwo() {
        ArrayList<Car> cars = new ArrayList<>();
        cars.add(new Car(2, 3, 2, false, 'R'));
        cars.add(new Car(0, 2, 3, true, 'Y'));
        cars.add(new Car(3, 0, 3, false, 'B'));
        cars.add(new Car(1, 5, 3, true, 'M'));
        cars.add(new Car(3, 3, 2, true, 'G'));
        cars.add(new Car(4, 4, 2, false, 'O'));
        return cars;
    }

    private static List<Car> levelThree() {
        ArrayList<Car> cars = new ArrayList<>();
        cars.add(new Car(2, 0, 2, false, 'R'));
        cars.add(new Car(0, 0, 2, true, 'L'));
        cars.add(new Car(0, 1, 2, true, 'O'));
        cars.add(new Car(0, 2, 2, false, 'B'));
        cars.add(new Car(1, 2, 2, false, 'M'));
        cars.add(new Car(0, 4, 2, true, 'P'));
        cars.add(new Car(2, 2, 2, true, 'G'));
        cars.add(new Car(4, 2, 2, true, 'T'));
        cars.add(new Car(2, 3, 3, true, 'Y'));
        cars.add(new Car(3, 4, 2, true, 'g'));
        return cars;
    }

    private static List<Car> levelFour() {
        ArrayList<Car> cars = new ArrayList<>();
        cars.add(new Car(2, 0, 2, false, 'R'));
        cars.add(new Car(1, 2, 2, true, 'L'));
        cars.add(new Car(3, 0, 3, false, 'M'));
        cars.add(new Car(4, 0, 2, true, 'B'));
        cars.add(new Car(4, 1, 2, true, 'P'));
        cars.add(new Car(4, 2, 2, true, 'T'));
        cars.add(new Car(1, 4, 3, true, 'Y'));
        cars.add(new Car(2, 5, 2, true, 'O'));
        cars.add(new Car(4, 4, 2, false, 'G'));
        cars.add(new Car(5, 4, 2, false, 'g'));
        return cars;
    }

    private static List<Car> levelFive() {
        ArrayList<Car> cars = new ArrayList<>();
        cars.add(new Car(2, 0, 2, false, 'R'));
        cars.add(new Car(0, 2, 3, false, 'Y'));
        cars.add(new Car(1, 0, 3, false, 'T'));
        cars.add(new Car(3, 0, 3, false, 'C'));
        cars.add(new Car(4, 0, 2, true, 'O'));
        cars.add(new Car(4, 2, 2, true, 'g'));
        cars.add(new Car(1, 4, 3, true, 'B'));
        cars.add(new Car(2, 5, 2, true, 'G'));
        cars.add(new Car(4, 4, 2, false, 'P'));
        cars.add(new Car(5, 4, 2, false, 'M'));
        return cars;
    }
}
