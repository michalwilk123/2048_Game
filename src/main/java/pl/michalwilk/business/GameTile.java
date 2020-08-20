package pl.michalwilk.business;

import javafx.scene.layout.AnchorPane;

public class GameTile extends AnchorPane {
    private int column;
    private int row;
    private int value;

    public int getValue() { return value; }

    public void setValue(int value) {this.value = value; }

    public int getRow() {   return row; }

    public void setRow(int row) {   this.row = row; }

    public int getColumn() {    return column;  }

    public void setColumn(int column) { this.column = column;}

    public GameTile(int row, int column, int value){
        setRow(row); setColumn(column); setValue(value);
    }
}
