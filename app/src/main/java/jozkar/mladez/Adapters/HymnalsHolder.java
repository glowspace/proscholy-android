package jozkar.mladez.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import jozkar.mladez.R;
import jozkar.mladez.Song;
import jozkar.mladez.Table;

public class HymnalsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView name;
    public TextView shortcut;
    public ImageView logo;
    public Context context;
    public String id;
    public boolean bookmark = false;

    public HymnalsHolder(View itemView, Context c) {
        super(itemView);

        //implementing onClickListener
        itemView.setOnClickListener(this);
        name = (TextView) itemView.findViewById(R.id.name);
        shortcut = (TextView) itemView.findViewById(R.id.shortcut);
        logo = (ImageView) itemView.findViewById(R.id.logo);
        context = c;
    }

    @Override
    public void onClick(View view) {

        if (bookmark) {
            Intent i = new Intent(context, Song.class);
            i.putExtra("id", id);
            i.putExtra("songbook", "");
            context.startActivity(i);
        } else {
            Intent i = new Intent(context, Table.class);
            i.putExtra("songbook", id);
            context.startActivity(i);
        }
    }
}