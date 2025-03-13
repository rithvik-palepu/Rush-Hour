import java.util.*;

import com.google.common.collect.ImmutableList;

public class Board {
    private final Tile[][] gameBoard;
    private final Collection<Car> allCars;

    public Board() {
        this.gameBoard = new Tile
                [BoardUtils.NUM_TILES][BoardUtils.NUM_TILES];
        this.allCars = new ArrayList<>();
        initBoard();
        printBoard();
    }

    private void initBoard() {
        for (int row = 0; row < BoardUtils.NUM_TILES; row++) {
            for (int col = 0; col < BoardUtils.NUM_TILES; col++) {
                this.gameBoard[row][col] = new Tile.EmptyTile(row, col);
            }
        }
        allCars.add(new Car
                (2, 0, 2, false, 'R'));
        allCars.add(new Car
                (0, 3, 2, false, 'G'));
        allCars.add(new Car
                (3, 1, 3, true, 'Y'));
        allCars.add(new Car
                (5, 3, 3, false, 'B'));
        allCars.add(new Car
                (2, 4, 2, true, 'O'));
        placeCars();
    }

    private void transitionBoard() {
        for (int row = 0; row < BoardUtils.NUM_TILES; row++) {
            for (int col = 0; col < BoardUtils.NUM_TILES; col++) {
                    this.gameBoard[row][col] = new Tile.EmptyTile(row, col);
            }
        }
        placeCars();
    }

    private void placeCars() {
        for (Car car : allCars) {
            int row = car.getRow();
            int col = car.getCol();
            for (int i = 0; i < car.getLength(); i++) {
                gameBoard[row][col] = new Tile.OccupiedTile(row, col, car);
                if (car.isVertical()) {
                    row++;
                } else {
                    col++;
                }
            }
        }
    }

    public void moveCar(int color, int displacement) {
        for (Car car : allCars) {
            if (car.getColor() == color && canMove(car, displacement)) {
                int row = car.getRow();
                int col = car.getCol();
                allCars.remove(car);
                if (car.isVertical()) {
                    allCars.add(new Car(
                            row + displacement, col,
                            car.getLength(), true, car.getColor()));
                } else {
                    allCars.add(new Car(
                            row, col + displacement,
                            car.getLength(), false, car.getColor()));
                }
                transitionBoard();
                break;
            }
        }
    }

    private boolean canMove(Car car, int displacement) {
        int row = car.getRow();
        int col = car.getCol();
        if (car.isVertical()) {
            row = (displacement > 0) ? car.getLength()-1 : row;
            for (int i = 0; i < Math.abs(displacement); i++) {
                row = (displacement > 0) ? row + 1 : row - 1;
                if (gameBoard[row][col].getCar() != null) {
                    return false;
                }
            }
            return true;
        }
        //todo extract method here maybe
        col = (displacement > 0) ? car.getLength()-1 : col;
        for (int i = 0; i < Math.abs(displacement); i++) {
            col = (displacement > 0) ? col + 1 : col - 1;
            if (gameBoard[row][col].getCar() != null) {
                return false;
            }
        }
        return true;
    }

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
