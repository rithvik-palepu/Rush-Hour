import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.common.collect.ImmutableList;

public class Board {
    private List<Tile> gameBoard;
    private Car redCar;
    private Collection<Car> otherCars;

    private Board(final Builder builder) {
        this.gameBoard = createGameBoard(builder);

    }

    private static List<Tile> createGameBoard(final Builder builder) {
        final Tile[] tiles = new Tile[BoardUtils.NUM_TILES];
        for (int i = 0; i < tiles.length; i++) {
            tiles[i] = Tile.createTile(i, builder.boardMap.get(i));
        }
        return ImmutableList.copyOf(tiles);
    }

    private List<Tile> getGameBoard() {
        return this.gameBoard;
    }

    private Car getRedCar() {
        return this.redCar;
    }

    private Collection<Car> getOtherCars() {
        return this.otherCars;
    }

    public static class Builder {
        Map<Integer, Car> boardMap;
        Car redCar;

        public Builder() {
            boardMap = new HashMap<>();
        }

        public Builder setCar(final Car car) {
            this.boardMap.put(car.getPiecePosition(), car);
            return this;
        }

        public Board build() {
            return new Board(this);
        }
    }
}
