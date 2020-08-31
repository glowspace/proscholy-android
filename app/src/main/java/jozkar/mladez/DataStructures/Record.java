package jozkar.mladez.DataStructures;

import android.util.Log;

import java.text.Normalizer;
import java.util.ArrayList;

import jozkar.mladez.App;

/**
 * Created by Jozkar on 11.11.2015.
 */
public class Record {
    String id, lid, name, text, language, search, searchName, noty, acordText, soloText, author;
    String[] tags, alternatives;
    boolean original;

    ArrayList<SongBook> songbooks;


    public Record(String id, String name, String language, String author, String alternatives, String original, String tags, String songbooks, String lyrics, String lyricsNoChord) {
        this.id = id;
        this.name = name;
        this.text = lyricsNoChord;
        this.noty = "";
        this.acordText = lyrics;
        this.soloText = lyricsNoChord;
        this.language = language;
        this.author = author;
        this.alternatives = alternatives.split(",");
        this.original = original.equals("1");
        this.tags = tags.split(",");

        this.text = "<h1>" + this.name + "</h1>" + this.text;
        this.acordText = "<h1>" + this.name + "</h1>" + this.acordText;
        this.soloText = "<h1>" + this.name + "</h1>" + this.soloText;

        this.lid = id;
        if (this.lid.length() < 4) {
            for (int i = this.lid.length(); i < 4; i++) {
                this.lid = "0" + this.lid;
            }
        }
        if (!author.equals("")) {
            String aut = getAuthors();
            this.text += "<br><small>" + aut + "</small>";
            this.soloText += "<br><small>" + aut + "</small>";
            this.acordText += "<br><small>" + aut + "</small>";
        }

        this.songbooks = new ArrayList<>();
        if (!songbooks.equals("")) {
            fillSongbooks(songbooks);
        }

        this.searchName = Normalizer.normalize(this.name, Normalizer.Form.NFD);
        this.searchName = App.pattern.matcher(this.searchName).replaceAll("");

        this.search = Normalizer.normalize(this.text, Normalizer.Form.NFD);
        this.search = App.pattern.matcher(this.search).replaceAll("");

        App.mydb.insertSongRow(this.id, this.name, this.text, this.acordText, this.soloText, this.language, alternatives, this.original, tags, songbooks, this.lid, this.search, this.searchName);
    }

    public Record(String id, String name, String text, String acordText, String soloText, String language, String alternatives, String tags, String songbooks, String lid, String search, String searchName, int original) {
        this.id = id;
        this.name = name;
        this.text = text;
        this.acordText = acordText;
        this.soloText = soloText;
        this.language = language;
        this.alternatives = alternatives.split(",");
        this.tags = tags.split(",");
        this.songbooks = new ArrayList<>();
        if (!songbooks.equals("")) {
            fillSongbooks(songbooks);
        }
        this.lid = lid;
        this.search = search;
        this.searchName = searchName;
        this.original = (original == 1);
        this.noty = "";

        Log.d("ALT", id + " " + alternatives);
    }

    public Record(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public Record(String id, String parent, String name) {
        Log.d("PARENT", parent + " " + (parent == null));
        this.id = id;
        this.lid = parent;
        this.name = name;
        this.original = false;
    }

    public String getAuthors() {
        String[] val = author.split(",");
        String result = "";
        Record r;
        for (String v : val) {
            r = App.authors.getById(v);
            if (r != null) {
                if (result.equals("")) {
                    result = r.name;
                } else {
                    result = result + ", " + r.name;
                }
            }
        }
        return result;
    }

    public void fillSongbooks(String sb) {
        String[] val = sb.split(",");
        SongBook r;
        for (String v : val) {
            String[] vals = v.split(":");
            r = App.songbooks.getById(vals[0]);
            if (r != null) {
                songbooks.add(new SongBook(r.id, vals[1]));
            }
        }
    }

    public void setChecked(boolean value) {
        this.original = value;
        if(value){
            App.tags.noChecked++;
        } else {
            App.tags.noChecked--;
        }
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSearchName() {
        return searchName;
    }

    public String getSearch() {
        return search;
    }

    public String getNoty() {
        return noty;
    }

    public String getSoloText() {
        return soloText;
    }

    public String getAcordText() {
        return acordText;
    }

    public String getText() {
        return text;
    }

    public String getParent() {return (this.lid == null || this.lid.equals("")? "" : this.lid); }

    public boolean getChecked() { return this.original; }

    public ArrayList<SongBook> getSongbooks() {
        return songbooks;
    }

    public boolean containsAll(ArrayList<String> filter){
        for(String t: filter){
            boolean found = false;
            for(String f: tags){
                if(f.equals(t)){
                    found = true;
                }
            }
            if(!found){
                return false;
            }
        }

        return true;
    }

    public ArrayList<Integer> getAlternative(int type){
        Log.d("ALT", ""+type);
        ArrayList<Integer> ret = new ArrayList<>();
        for(String s:this.alternatives){
            if(s.contains(":"+type)){
                ret.add(Integer.parseInt(s.split(":")[0]));
            }
        }
        return ret;
    }
}
