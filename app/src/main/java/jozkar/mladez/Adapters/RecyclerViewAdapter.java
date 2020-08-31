package jozkar.mladez.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.futuremind.recyclerviewfastscroll.SectionTitleProvider;

import java.util.List;

import jozkar.mladez.DataStructures.RowData;
import jozkar.mladez.App;
import jozkar.mladez.R;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolder> implements SectionTitleProvider {

    public List<RowData> itemList;
    private Context context;
    private boolean favorite, mainFragment;
    private int currentSelectedIndex = -1;
    private SparseBooleanArray selectedItems, animationItemsIndex;

    public RecyclerViewAdapter(Context context, List<RowData> itemList, boolean favorite, boolean MainFragment) {
        this.itemList = itemList;
        this.context = context;
        this.favorite = favorite;
        this.mainFragment = MainFragment;
        this.selectedItems = new SparseBooleanArray();
        this.animationItemsIndex = new SparseBooleanArray();
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, null);
        return new RecyclerViewHolder(view, context, favorite);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        if (!favorite) {

            if(App.songs.checkFavorite(itemList.get(position).getId())){
                holder.favorite.setVisibility(View.VISIBLE);
            } else {
                holder.favorite.setVisibility(View.GONE);
            }
        } else {
            holder.favorite.setVisibility(View.GONE);
        }
        holder.number.setText(itemList.get(position).getNumber());
        holder.number.setTextColor(context.getResources().getColor(nameColor()));
        holder.source = mainFragment;
        holder.name.setText(itemList.get(position).getName());
        holder.name.setTextColor(context.getResources().getColor(nameColor()));
        holder.id = itemList.get(position).getId();
        holder.songbook = itemList.get(position).getSongBook();
        holder.search = itemList.get(position).getSearch();
        holder.position = position;
        if(currentSelectedIndex != -1){
            holder.checkbox.setVisibility(View.VISIBLE);
            if(selectedItems.get(position, false)){
                holder.checkbox.setImageResource(R.drawable.radio_checked);
            } else {
                holder.checkbox.setImageResource(R.drawable.radio_empty);
            }
        } else {
            holder.checkbox.setVisibility(View.GONE);
        }
        if (App.SP.getBoolean("serif", false)) {
            holder.name.setTypeface(Typeface.SERIF);
            holder.number.setTypeface(Typeface.SERIF);
        }
    }

    public void toggleSelection(int pos) {
        currentSelectedIndex = pos;
        if (selectedItems.get(pos, false)) {
            selectedItems.delete(pos);
        } else {
            selectedItems.put(pos, true);
        }
        if(selectedItems.size() == 1){
            notifyDataSetChanged();
        } else {
            notifyItemChanged(pos);
        }
    }

    public void setSelection(int pos, boolean value){
        currentSelectedIndex = pos;
        if (selectedItems.get(pos, false)) {
            if(!value) {
                selectedItems.delete(pos);
            }
        } else {
            if(value) {
                selectedItems.put(pos, true);
            }
        }
        notifyItemChanged(pos);
    }

    public boolean isSelected(int id){
        return selectedItems.get(id, false);
    }

    public void cleanSelectedIndex(){
        currentSelectedIndex = -1;
        selectedItems.clear();
        animationItemsIndex.clear();
        notifyDataSetChanged();
    }

    public int getSelectedItemCount(){
        return selectedItems.size();
    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }

    @Override
    public String getSectionTitle(int position) {
        //this String will be shown in a bubble for specified position
        return itemList.get(position).getName().substring(0, 1);
    }

    public int nameColor() {
        if (App.SP.getBoolean("night",false)) {
            return R.color.white;
        } else {
            return R.color.black;
        }
    }
}

