package jozkar.mladez.DataStructures;

/**
 * Created by Jozkar on 10.11.2015.
 */
public class RowData {

    private String number;
    private String name;
    private String id;
    private String songbook;
    private int color;
    private boolean search = false;
    private boolean bookmark = false;


    public RowData(String number, String name, String id, int color) {
        this.name = name;
        this.number = number;
        this.id = id;
        this.color = color;
    }

    public RowData(String number, String name, int mipmap) {
        this.name = name;
        this.number = number;
        this.color = mipmap;
        this.bookmark = true;
    }

    public RowData(String number, String name, String songbook, String id){
        this.name = name;
        this.number = number;
        this.id = id;
        this.songbook = songbook;
    }

    public RowData(String number, String name, String search, String id, boolean s) {
        this.name = name;
        this.number = number;
        this.id = id;
        this.songbook = search;
        this.search = s;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public String getNumber() {

        return number;
    }

    public void setNumber(String number) {

        this.number = number;
    }

    public String getId() {
        return id;
    }
    public String getSongBook() {
        return songbook;
    }

    public int getColor() {
        return color;
    }

    public int getMipmap() {
        if(bookmark){
            return color;
        }

        return 0;
    }

    public String getSearch(){
        if(this.search){
            return songbook;
        }

        return null;
    }
}


