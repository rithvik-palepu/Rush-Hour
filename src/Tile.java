public abstract class Tile {
    private final int row;
    private final int col;
    private final Car carOnTile;

    private Tile(int row, int col, Car carOnTile) {
        this.carOnTile = carOnTile;
        this.row = row;
        this.col = col;
    }

    public static Tile createTile(int row, int col, Car car) {
        return car != null ? new OccupiedTile(row, col, car) : new EmptyTile(row, col);
    }

    public abstract boolean isTileOccupied();
    public abstract Car getCar();

    public static final class EmptyTile extends Tile {
        EmptyTile(int row, int col) {
            super(row, col, null);
        }

        @Override
        public boolean isTileOccupied() {
            return false;
        }

        @Override
        public Car getCar() {
            return null;
        }
    }

    public static final class OccupiedTile extends Tile {
        private final Car carOnTile;

        public OccupiedTile(int row, int col, Car carOnTile) {
            super(row, col, carOnTile);
            this.carOnTile = carOnTile;
        }
        @Override
        public boolean isTileOccupied() {
            return true;
        }

        @Override
        public Car getCar() {
            return this.carOnTile;
        }
    }
}
