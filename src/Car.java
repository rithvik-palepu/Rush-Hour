// Car object for easily movable and controllable cars
public class Car {
    private int row;
    private int col;
    private final int length;
    private final boolean isVertical;
    private final char color;

    // Defines car with 5 trackable and manageable states
    public Car(int row, int col, int length,
                boolean isVertical, char color) {
        this.row = row;
        this.col = col;
        this.length = length;
        this.isVertical = isVertical;
        this.color = color;
    }

    // getter methods
    public int getRow() {return this.row;}
    public int getCol() {return this.col;}
    public int getLength() {return this.length;}
    public boolean isVertical() {return this.isVertical;}
    public char getColor() {return this.color;}
}
