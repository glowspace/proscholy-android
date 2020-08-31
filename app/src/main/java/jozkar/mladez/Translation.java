package jozkar.mladez;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import jozkar.mladez.Adapters.RecyclerViewAdapter;
import jozkar.mladez.DataStructures.Record;
import jozkar.mladez.DataStructures.RowData;

public class Translation extends AppCompatWrapper {

    public static RecyclerView RVOriginal, RVAuth, RVOthers;
    public static RecyclerViewAdapter adapterOriginal, adapterAuth, adapterOthers;
    public static List<RowData> rowListItem;
    enum AltType {ORIGINAL,OTHER,AUTH};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_translation);
        String song = getIntent().getStringExtra("song");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setTitle(R.string.empty);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.up_arrow);

        RVOriginal = findViewById(R.id.original_list);
        RVOriginal.setLayoutManager(new LinearLayoutManager(this));
        RVAuth = findViewById(R.id.authorized_list);
        RVAuth.setLayoutManager(new LinearLayoutManager(this));
        RVOthers = findViewById(R.id.others_list);
        RVOthers.setLayoutManager(new LinearLayoutManager(this));

        Record r = App.songs.getById(song);

        rowListItem = getData(r.getAlternative(AltType.ORIGINAL.ordinal()));
        if(rowListItem.size() > 0) {
            adapterOriginal = new RecyclerViewAdapter(this, rowListItem, false, true);
            RVOriginal.setAdapter(adapterOriginal);
        } else {
            findViewById(R.id.original).setVisibility(View.GONE);
            Log.d("STATE", "ORIG GONE");
        }

        rowListItem = getData(r.getAlternative(AltType.AUTH.ordinal()));
        if(rowListItem.size() > 0) {
            adapterAuth = new RecyclerViewAdapter(this, rowListItem, false, true);
            RVAuth.setAdapter(adapterAuth);
        } else {
            findViewById(R.id.authorized).setVisibility(View.GONE);
            Log.d("STATE", "AUTH GONE");
        }

        rowListItem = getData(r.getAlternative(AltType.OTHER.ordinal()));
        if(rowListItem.size() > 0) {
            adapterOthers = new RecyclerViewAdapter(this, rowListItem, false, true);
            RVOthers.setAdapter(adapterOthers);
        } else {
            findViewById(R.id.others).setVisibility(View.GONE);
            Log.d("STATE", "OTHR GONE");
        }
    }

    public List<RowData> getData(ArrayList<Integer> ids){
        ArrayList<RowData> res = new ArrayList<RowData>();
        for(int i:ids){
            Record r = App.songs.getById(String.valueOf(i));
            if(r != null){
                res.add(new RowData(r.getId(), r.getName(), "", r.getId()));
            }
        }
        return res;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int Id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (Id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}