public abstract class Car {
    protected final PieceType pieceType;
    protected final int piecePosition;

    private Car(final PieceType pieceType, final int piecePosition) {
        this.pieceType = pieceType;
        this.piecePosition = piecePosition;
    }

    public PieceType getPieceType() {
        return this.pieceType;
    }

    public int getPiecePosition() {
        return this.piecePosition;
    }

    public enum PieceType {
        RED_CAR("Red") {

        },
        //TODO - make other car colors
        OTHER_CAR("Other") {

        };

        private final String pieceColor;

        PieceType(final String pieceColor) {
            this.pieceColor = pieceColor;
        }

        @Override
        public String toString() {
            return this.pieceColor;
        }
    }
}
