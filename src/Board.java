import java.util.*;

import com.google.common.collect.ImmutableList;

public class Board {
    private final char[][] gameBoard;
    private Collection<Car> allCars;

    private Board() {
        this.gameBoard = new char
                [BoardUtils.NUM_TILES][BoardUtils.NUM_TILES];
        this.allCars = new ArrayList<>();
        initBoard();
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
                (2, 0, 2, true, 'R'));
        allCars.add(new Car
                (1, 4, 2, true, 'G'));
        allCars.add(new Car
                (5, 4, 3, false, 'Y'));
        allCars.add(new Car
                (5, 2, 3, false, 'B'));
        allCars.add(new Car
                (3, 4, 2, true, 'O'));
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
}
