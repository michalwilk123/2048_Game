package pl.michalwilk.business;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

public class GameTile extends AnchorPane {
    private int column;
    private int row;
    private int value;
    private Rectangle rectangle;
    private Label number_label;
    private final int rect_size = 110, rect_arc_width = 60;
    private final String rect_color = "ffe7a6";

    public int getValue() { return value; }

    public void setValue(int value) {this.value = value; }

    public int getRow() {   return row; }

    public void setRow(int row) {   this.row = row; }

    public int getColumn() {    return column;  }

    public void setColumn(int column) { this.column = column;}

    public GameTile(int row, int column, int value){
        setRow(row); setColumn(column); setValue(value);
        rectangle = new Rectangle(rect_size, rect_size, Color.valueOf(rect_color));
        rectangle.setArcHeight(60); rectangle.setArcWidth(60);
        rectangle.setX(5); rectangle.setY(5);
        rectangle.setStroke(Color.BLACK);

        number_label = new Label("2048");
        number_label.setFont(new Font("Lato", 20));
        number_label.setMinHeight(120); number_label.setMinWidth(120);
        number_label.setAlignment(Pos.CENTER);
        this.getChildren().addAll(rectangle, number_label);
    }

    public void set_label_text(int number){
        if (number % 2 == 1)
            System.out.println("wrong text input");

        number_label.setText(Integer.toString(number));
    }
}
