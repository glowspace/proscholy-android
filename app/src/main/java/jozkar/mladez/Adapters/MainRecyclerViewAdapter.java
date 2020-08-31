package jozkar.mladez.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import jozkar.mladez.DataStructures.MainRowData;
import jozkar.mladez.MainScreen;
import jozkar.mladez.R;

/**
 * Created by Jozkar on 14.11.2015.
 */
public class MainRecyclerViewAdapter extends RecyclerView.Adapter<MainRecyclerViewHolder> {

    private List<MainRowData> itemList;
    private Context context;

    public MainRecyclerViewAdapter(Context context, List<MainRowData> itemList) {
        this.itemList = itemList;
        this.context = context;
    }

    @Override
    public MainRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_row, null);
        return new MainRecyclerViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(MainRecyclerViewHolder holder, int position) {
        holder.number.setImageResource(itemList.get(position).icon);
        holder.name.setText(itemList.get(position).name);
        holder.position = itemList.get(position).position;
        if (MainScreen.serif) {
            holder.name.setTypeface(Typeface.SERIF);
        }
    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }
}
