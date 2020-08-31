package jozkar.mladez.DataStructures;

/**
 * Created by Jozkar on 4.4.2016.
 */
public class Favorites {
    private String number;
    private boolean remove;

    public Favorites(String number){
        this.number = number;
        remove = false;
    }

    public String getNumber(){
        return this.number;
    }

    public void remove(){
        this.remove = !this.remove;
    }

    public boolean getRemove(){
        return this.remove;
    }
}
