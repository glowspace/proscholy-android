package jozkar.mladez.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import jozkar.mladez.DataStructures.RowData;
import jozkar.mladez.Fragments.HomeFragment;
import jozkar.mladez.R;
import jozkar.mladez.Song;
import jozkar.mladez.Table;

/**
 * Created by Gowtham Chandrasekar on 31-07-2015.
 */
public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView name;
    public TextView number;
    public ImageView favorite, checkbox;
    public String id, songbook, search;
    public Context context;
    public boolean favorites, source;
    public int position;

    public RecyclerViewHolder(View itemView, Context c, boolean favorite) {
        super(itemView);

        //implementing onClickListener
        itemView.setOnClickListener(this);
        name = (TextView) itemView.findViewById(R.id.name);
        number = (TextView) itemView.findViewById(R.id.number);
        this.favorite = (ImageView) itemView.findViewById(R.id.star);
        checkbox = (ImageView) itemView.findViewById(R.id.checkbox);
        context = c;
        favorites = favorite;

        checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeFragment.toggleSelection(position);
            }
        });
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int p = getLayoutPosition();
                Log.d("LONGPRESS", p+"");
                if(!favorites){
                    HomeFragment.enableActionMode(p);
                }
                return true;
            }
        });
    }

    @Override
    public void onClick(View view) {
        //Every time you click on the row toast is displayed

        Intent i = new Intent(context, Song.class);
        i.putExtra("id", id);
        if (search != null) {
            i.putExtra("songbook", "");
            ArrayList<String> list = new ArrayList<>();
            if(source){
                for (RowData r : HomeFragment.adapter.itemList) {
                    list.add(r.getId());
                }
            } else {
                for (RowData r : Table.adapter.itemList) {
                    list.add(r.getId());
                }
            }
            i.putExtra("list", list);
        } else {
            i.putExtra("songbook", songbook);
        }
        i.putExtra("favorites", favorites);
        context.startActivity(i);

    }
}