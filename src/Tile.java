// abstract parent tile class with two childs, occupiedTile
// and emptyTile to easily manage and represent tiles on the gameBoard
public abstract class Tile {
    private final int row;
    private final int col;
    private final Car carOnTile;

    // defines 3 easily trackable states for all til,es
    private Tile(int row, int col, Car carOnTile) {
        this.carOnTile = carOnTile;
        this.row = row;
        this.col = col;
    }

    // abstract methods to be defined in child classwa
    public abstract boolean isTileOccupied();
    public abstract Car getCar();

    // first child class, empty tile, has no car on it
    public static final class EmptyTile extends Tile {
        // passes in null for car value
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

    // second child class, occupied tile, has a car on it
    public static final class OccupiedTile extends Tile {
        private final Car carOnTile;

        // passes in carOnTile, keeps track of carOnTile
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
