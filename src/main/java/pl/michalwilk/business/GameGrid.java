package pl.michalwilk.business;

import javafx.scene.layout.GridPane;

import java.util.*;

public class GameGrid extends GridPane {
    public static final byte GRID_WIDTH = 4, GRID_HEIGHT = 4;
    private ArrayList<GameTile> tile_components_list ;
    private int number_of_tile_components;
    private Integer[][] tile_array;
    private Random generator;

    public void add_tile(int row, int column, int value){
        tile_components_list.add(new GameTile(row, column, value));
        tile_array[row][column] = value;
    }

    private static int emptyval(){  return 0; }

    public void add_tile(){
        // generate tile on random empty location
        ArrayList<Coordinates> coords = new ArrayList<>();

        for(int row=0;row<4;row++){
            for(int column=0 ; column<4 ; column++){
                if (tile_array[row][column] == emptyval()){
                    coords.add(new Coordinates(row, column));
                }
            }
        }
        int random_val = generator.nextInt(coords.size());
        int aa = coords.get(random_val).row;
        add_tile(coords.get(random_val).row,
                coords.get(random_val).column, (random_val % 2 ) * 2);
    }

    private class Coordinates{
        public int row, column;
        public Coordinates(int row, int column){
            this.row = row; this.column = column;
        }
    }

    public GameGrid(){
        tile_array = new Integer[GRID_HEIGHT][GRID_WIDTH];
        for (Integer[] int_arr : tile_array)
            for(Integer element : int_arr)  element = 0;

        tile_components_list = new ArrayList<GameTile>();
        generator = new Random();
    }

}
