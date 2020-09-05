package pl.michalwilk.business;

import java.io.*;

public class GameSavedData implements Serializable {
    private GameGrid gameGrid;
    private long highscore;
    private final static String save_destination="data.ser";

    public static GameSavedData load_from_memory() {
        GameSavedData gm = null;
         //deserializing the object from the file
        try {
            FileInputStream fin = new FileInputStream(save_destination);
            ObjectInputStream in = new ObjectInputStream(fin);
            gm = (GameSavedData) in.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("FILE WAS NOT FOUND!");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("FILE PROBLEM");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("GAMEGRID CLASS NOT FOUND");
            e.printStackTrace();
        }

        return gm;
    }

    public void save_data(){
        try{
            FileOutputStream fos = new FileOutputStream(save_destination);
            ObjectOutputStream out = new ObjectOutputStream(fos);
            out.writeObject(this);
            out.close();
            fos.close();
        } catch (FileNotFoundException e) {
            System.out.println("FILE WAS NOT FOUND! yikes");
            e.printStackTrace();
        } catch (IOException e){
            System.out.println("FILE PROBLEM");
            e.printStackTrace();
        }
    }

    public GameSavedData(){highscore = 0;}

    public void setGameGrid(GameGrid gameGrid){ this.gameGrid = gameGrid;}

    public GameGrid getGameGrid(){return gameGrid;}

    public Long get_highest_score() { return highscore;}

    public void set_highest_score(long score){this.highscore = score;}
}
