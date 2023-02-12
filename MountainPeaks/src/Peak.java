public class Peak {
    private final int value; // the value of the peak
    private final int row;  // the row position of the peak
    private final int col; // the column position of the peak

    public Peak(int value, int row, int col) {
        this.value = value;
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getValue() {
        return value;
    }

    public int getCol() {
        return col;
    }

    public String toString(){
        return value+"("+row+","+col+")";
    }
}