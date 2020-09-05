package pl.michalwilk.business;

import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import pl.michalwilk.App;
import pl.michalwilk.GameViewController;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;

public class GameGrid extends GridPane implements Serializable {
    public static final byte GRID_WIDTH = 4, GRID_HEIGHT = 4;
    private static final int TILE_SIZE = 120;
    private static final byte NUM_OF_SAVED_MOVES = 5;
    private final Random generator;
    private GameTile[][] tile_array;
    private long current_round_score;
    private boolean gameInterrupted;
    private ArrayDeque<Integer[][]> previous_moves;

    public void add_tile(int row, int column, int value){
        if (tile_array[row][column] == null){
            tile_array[row][column] = new GameTile(row, column, value);
            this.add(tile_array[row][column], column, row);
        }
        else{
            tile_array[row][column].setRow(row);
            tile_array[row][column].setColumn(column);
            tile_array[row][column].setValue(value);
            tile_array[row][column].update();
        }
    }

    public void rollback(){
        if (previous_moves.size() == 0){
            System.out.println("cannot rollback that far :(");
            return;
        }
        Integer[][] value_grid = previous_moves.getFirst();
        previous_moves.removeFirst();

        for(int i=0 ; i<GRID_HEIGHT ; i++){
            for(int j=0 ; j<GRID_WIDTH ; j++){
                // if previously value was null lets be
                // sure, now it will be the same
                if (value_grid[i][j] == null){
                    if(tile_array[i][j] != null ){
                        getChildren().remove(tile_array[i][j]);
                        tile_array[i][j] = null;
                    }
                }
                else    add_tile(i,j, value_grid[i][j]);
            }
        }
    }

    private void store_move() {
        if(previous_moves.size() >= NUM_OF_SAVED_MOVES)
            previous_moves.removeLast();
        previous_moves.addFirst(create_representation());
    }

    private Integer[][] create_representation() {
        Integer[][] saved_grid = new Integer[GRID_HEIGHT][GRID_WIDTH];
        for( int i=0 ; i<GRID_HEIGHT ; i++){
            for( int j=0 ; j<GRID_WIDTH ; j++){
                if (tile_array[i][j] != null)
                    saved_grid[i][j] = tile_array[i][j].getValue();
                else
                    saved_grid[i][j] = null;
            }
        }
        return saved_grid;
    }

    public void add_tile(){
        // creating a list of all empty coordinates
        ArrayList<Coordinates> coordinates = new ArrayList<>();

        // looking for empty spaces in grid, then adding them to the temporary list
        for(int row=0;row<4;row++){
            for(int column=0 ; column<4 ; column++){
                if (tile_array[row][column] == null){
                    coordinates.add(new Coordinates(row, column));
                }
            }
        }

        if (coordinates.size() == 0){
            game_over();
        }
        else {
            int random_val = generator.nextInt(coordinates.size());
            add_tile(coordinates.get(random_val).row,
                    coordinates.get(random_val).column, random_val % 2 == 0 ? 2 : 4);
        }
    }

    private void game_over() {
        gameInterrupted = false;
        try {
            App.setRoot("game_over");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void remove_tile_from_board(GameTile gameTile){
        this.getChildren().remove(gameTile);
        tile_array[gameTile.getRow()][gameTile.getColumn()] = null;
    }

    private int merge_tile(int row, int col, Coordinates previous_coords){
        // updating value of previous tile
        tile_array[previous_coords.row][previous_coords.column].double_value();

        previous_coords.value = -1;

        // deleting the tile
        remove_tile_from_board(tile_array[row][col]);
        return calculate_score(tile_array[previous_coords.row][previous_coords.column]
                .getValue());
    }

    public void move(GameViewController.Moves move) {
        // Firstly we need to save this move before any of below
        // instructions occur
        store_move();

        Coordinates previous_coords = new Coordinates();
        int current_score = 0;
        boolean move_occurred = false;

        if (move.equals(GameViewController.Moves.UP)){
            for( int j=0 ; j<GRID_WIDTH ; j++){
                // resetting previous values
                previous_coords.set(-1,-1, -1);
                for( int i=0 ; i<GRID_HEIGHT ; i++){
                    if(tile_array[i][j] == null)
                        continue;
                    // if previous tile value was the same
                    // we merge the tiles
                   if ( tile_array[i][j].getValue() == previous_coords.value){
                       current_score = merge_tile(i,j,previous_coords);
                       move_occurred = true;
                   }
                   // if previous was not the neighbour of present tile
                   else {
                       if((previous_coords.row >= 0 && previous_coords.row + 1 < i)
                            || (previous_coords.row == -1 && i > 0)){
                           relocate_tile(tile_array[i][j], previous_coords.row + 1, j);
                           move_occurred = true;
                           previous_coords.value = tile_array[previous_coords.row + 1][j].getValue();
                           previous_coords.set(previous_coords.row + 1, j);
                       }
                       else{
                           // if this tile wasn't deleted, then we are
                           // updating previous tile value
                           previous_coords.set(i, j);
                           previous_coords.value = tile_array[i][j].getValue();
                       }
                   }
                }
            }

        }
        else if (move.equals(GameViewController.Moves.DOWN)){
            for( int j=0 ; j<GRID_WIDTH ; j++){
                // resetting previous values
                previous_coords.set(-1,-1, -1);
                for( int i=GRID_HEIGHT-1 ; i>=0 ; i--){
                    if(tile_array[i][j] == null)    continue;
                    // if previous tile value was the same
                    // we merge the tiles
                    if ( tile_array[i][j].getValue() == previous_coords.value){
                        current_score = merge_tile(i,j,previous_coords);
                        move_occurred = true;
                    }
                    // if previous was not the neighbour of present tile
                    else {
                        if((previous_coords.row >= 0 && previous_coords.row - 1 > i)
                                || (previous_coords.row == -1 && i < GRID_HEIGHT - 1)){
                            if (previous_coords.row == -1){
                                relocate_tile(tile_array[i][j], GRID_HEIGHT - 1, j);
                                previous_coords.value = tile_array[GRID_HEIGHT - 1][j].getValue();
                                previous_coords.set(GRID_HEIGHT - 1, j);
                            }
                            else{
                                relocate_tile(tile_array[i][j], previous_coords.row - 1, j);
                                previous_coords.value = tile_array[previous_coords.row - 1][j].getValue();
                                previous_coords.set(previous_coords.row - 1, j);
                            }
                            move_occurred = true;
                        }
                        else{
                            // if this tile wasn't deleted, then we are
                            // updating previous tile value
                            previous_coords.set(i, j);
                            previous_coords.value = tile_array[i][j].getValue();
                        }
                    }
                }
            }
        }
        else if (move.equals(GameViewController.Moves.RIGHT)){
            for( int i=0 ; i<GRID_HEIGHT ; i++){
                // resetting previous values
                previous_coords.set(-1,-1, -1);
                for( int j=GRID_WIDTH-1 ; j>=0 ; j--){
                    if(tile_array[i][j] == null)    continue;
                    // if previous tile value was the same
                    // we merge the tiles
                    if ( tile_array[i][j].getValue() == previous_coords.value){
                        current_score = merge_tile(i,j,previous_coords);
                        move_occurred = true;

                    }
                    // if previous was not the neighbour of present tile
                    else {
                        if((previous_coords.column >= 0 && previous_coords.column - 1 > j)
                                || (previous_coords.column == -1 && j < GRID_WIDTH - 1)){
                            if (previous_coords.column == -1){
                                relocate_tile(tile_array[i][j], i, GRID_WIDTH - 1);
                                previous_coords.value = tile_array[i][GRID_WIDTH - 1].getValue();
                                previous_coords.set(i, GRID_WIDTH - 1);
                            }
                            else{
                                relocate_tile(tile_array[i][j], i, previous_coords.column - 1);
                                previous_coords.value = tile_array[i][previous_coords.column - 1].getValue();
                                previous_coords.set(i, previous_coords.column - 1);
                            }
                            move_occurred = true;
                        }
                        else{
                            // if this tile wasn't deleted, then we are
                            // updating previous tile value
                            previous_coords.set(i, j);
                            previous_coords.value = tile_array[i][j].getValue();
                        }
                    }
                }
            }
        }
        else if (move.equals(GameViewController.Moves.LEFT)){
            for( int i=0 ; i<GRID_HEIGHT ; i++){
                // resetting previous values
                previous_coords.set(-1,-1, -1);
                for( int j=0 ; j<GRID_WIDTH ; j++){
                    if(tile_array[i][j] == null)    continue;
                    // if previous tile value was the same
                    // we merge the tiles
                    if ( tile_array[i][j].getValue() == previous_coords.value){
                        current_score = merge_tile(i,j,previous_coords);
                        move_occurred = true;
                    }
                    // if previous was not the neighbour of present tile
                    else {
                        if((previous_coords.column >= 0 && previous_coords.column + 1 < j)
                                || (previous_coords.column == -1 && j > 0)){
                            relocate_tile(tile_array[i][j], i, previous_coords.column + 1);
                            move_occurred = true;
                            previous_coords.value = tile_array[i][previous_coords.column + 1].getValue();
                            previous_coords.set( i, previous_coords.column + 1);
                        }
                        else{
                            // if this tile wasn't deleted, then we are
                            // updating previous tile value
                            previous_coords.set(i, j);
                            previous_coords.value = tile_array[i][j].getValue();
                        }
                    }
                }
            }

        }

        // updating score / adding to data trace move
        if (move_occurred){
            set_round_score(current_score);
            add_tile();
        }
        else{
            previous_moves.removeFirst();
            // we check if game was ended
            boolean empty_space_found = false;
            for(int i=0 ; i<GRID_HEIGHT ; i++){
                if (empty_space_found) break;
                for(int j=0 ; j<GRID_WIDTH ; j++){
                    if (tile_array[i][j] == null){
                        empty_space_found = true;
                        break;
                    }
                }
            }
            if (!empty_space_found) game_over();
        }
    }

    public void set_round_score(int value) {
        current_round_score = value;
    }

    private int calculate_score(int value){
        // can add something like multiplier there
        return value;
    }

    private void relocate_tile(GameTile tile, int row, int column) {
        assert tile != null;

        if(tile_array[row][column] != null){
            System.out.println("WRONG TILE DESTINATION: tile filled");
            System.out.println(tile_array[row][column].getValue());
        }
        tile_array[row][column] = tile_array[tile.getRow()][tile.getColumn()];
        tile_array[tile.getRow()][tile.getColumn()] = null;

        // changing visual location of the tile
        setRowIndex(tile, row); setColumnIndex(tile, column);
        tile.setColumn(column); tile.setRow(row);
    }

    public boolean isGameInterrupted() {
        return gameInterrupted;
    }

    public void rebuild_grid() {
        int i=0;
        for( GameTile[] row : tile_array){
            for(GameTile tile : row){
                if (tile != null){
                    tile.build_component();
                    this.add(tile, tile.getColumn(), tile.getRow());
                }
            }
        }
    }

    private static class Coordinates{
        public int row, column, value;
        public Coordinates(int row, int column){
            this.row = row; this.column = column;
        }
        public Coordinates(){ }
        public void set(int row, int column){
            this.row = row; this.column = column;
        }
        public void set(int row, int column, int value){
            this.row = row; this.column = column; this.value = value;
        }
    }

    public long get_current_round_score(){
        return current_round_score;
    }

    public GameGrid() {
        previous_moves = new ArrayDeque<Integer[][]>();
        generator = new Random();
        gameInterrupted = true;
        tile_array = new GameTile[GRID_HEIGHT][GRID_HEIGHT];
        create_gamegrid();

        requestFocus();
    }

    public  void create_gamegrid(){
        set_same_cell_size();

        setStyle("-fx-background-color : #A2AEBB;");
        gridLinesVisibleProperty().set(true);
    }

    private void set_same_cell_size() {
        for (int i=0 ; i<GRID_WIDTH ; i++){
            getColumnConstraints().add(new ColumnConstraints(TILE_SIZE));
            getRowConstraints().add(new RowConstraints(TILE_SIZE));
        }
    }
}
