import java.util.*;

import com.google.common.collect.ImmutableList;

public class Board {
    private final char[][] gameBoard;
    private final Collection<Car> allCars; //3234

    public Board() {
        this.gameBoard = new char
                [BoardUtils.NUM_TILES][BoardUtils.NUM_TILES];
        this.allCars = new ArrayList<>();
        initBoard();
        printBoard();
    }

    public char[][] getGameBoard() {
        return this.gameBoard;
    }

    public Collection<Car> getOtherCars() {
        return this.allCars;
    }

    private void initBoard() {
        for (int row = 0; row < BoardUtils.NUM_TILES; row++) {
            for (int col = 0; col < BoardUtils.NUM_TILES; col++) {
                this.gameBoard[row][col] = '-';
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
        placeVehicles();
    }

    private void placeVehicles() {
        for (Car car : allCars) {
            int row = car.getRow();
            int col = car.getCol();
            for (int i = 0; i < car.getLength(); i++) {
                gameBoard[row][col] = car.getColor();
                if (car.isVertical()) {
                    row++;
                } else {
                    col++;
                }
            }
        }
    }

    public void printBoard() {
        for (int row = 0; row < BoardUtils.NUM_TILES; row++) {
            for (int col = 0; col < BoardUtils.NUM_TILES; col++) {
                System.out.print(gameBoard[row][col]);
            }
            System.out.println();
        }
    }
}
