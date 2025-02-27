public abstract class Tile {
    protected final int tileCoordinate;

    private Tile(int tileCoordinate) {
        this.tileCoordinate = tileCoordinate;
    }

    public static Tile createTile(int tileCoordinate, Car car) {
        return car != null ? new OccupiedTile(tileCoordinate, car) : new EmptyTile(tileCoordinate);
    }

    public abstract boolean isTileOccupied();
    public abstract Car getCar();

    public static final class EmptyTile extends Tile {
        EmptyTile(int tileCoordinate) {
            super(tileCoordinate);
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

        public OccupiedTile(int tileCoordinate, Car carOnTile) {
            super(tileCoordinate);
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
