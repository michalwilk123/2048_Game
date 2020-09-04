package pl.michalwilk.business;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.io.Serializable;

public class GameTile extends AnchorPane implements Serializable {
    private int column, row, value;
    transient private Rectangle rectangle;
    transient private Label number_label;
    private RGBValue rgbValue;
    private final int rect_size = 110, rect_arc_width = 60;

    private class RGBValue implements Serializable{
        private final Integer[] color = {0, 255, 255};
        protected int color_multiplier;
        private final int color_constant = 16;

        public RGBValue() {}
        @Override
        public String toString() {
            raise_color();
            return "rgb(" + (color[0] + (color_constant * color_multiplier))+", " +
                    (color[1] - (color_constant * color_multiplier)) +
                    ", " + (color[2] - (color_constant * color_multiplier)) + ")";
        }

        private void raise_color(){
            // decided to go from cyan to red
            color_multiplier = (int) (Math.log(value) / Math.log(2));
        }
    }

    public int getValue() { return value; }

    public void setValue(int value) {this.value = value; }

    public void update(){
        this.number_label.setText(Integer.toString(value));
        rectangle.setFill(Color.valueOf(rgbValue.toString()));
    }

    public int getRow() {   return row; }

    public void setRow(int row) {   this.row = row; }

    public int getColumn() {    return column;  }

    public void setColumn(int column) { this.column = column;}

    public GameTile(int row, int column, int value){
        setRow(row); setColumn(column); setValue(value);
        this.build_component();
    }

    public void build_component(){
        rgbValue = new RGBValue();
        rectangle = new Rectangle(rect_size, rect_size, Color.valueOf(rgbValue.toString()));
        rectangle.setArcHeight(60); rectangle.setArcWidth(60);
        rectangle.setX(5); rectangle.setY(5);
        rectangle.setStroke(Color.BLACK);

        number_label = new Label(Integer.toString(value));
        number_label.setFont(Font.font("Lato", FontWeight.BOLD, 22));
        number_label.setMinHeight(120); number_label.setMinWidth(120);
        number_label.setAlignment(Pos.CENTER);
        this.getChildren().addAll(rectangle, number_label);
    }

    public void double_value(){
        value *= 2;
        number_label.setText(Integer.toString(value));
        rectangle.setFill(Color.valueOf(rgbValue.toString()));
    }
}
