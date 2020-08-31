package jozkar.mladez.Adapters;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import jozkar.mladez.App;
import jozkar.mladez.DataStructures.RowData;
import jozkar.mladez.MainScreen;
import jozkar.mladez.R;

public class HymnalsAdapter extends RecyclerView.Adapter<HymnalsHolder> {

    private List<RowData> itemList;
    private Context context;
    private boolean bookmark;

    public HymnalsAdapter(Context context, List<RowData> itemList, boolean bookmark) {
        this.itemList = itemList;
        this.context = context;
        this.bookmark = bookmark;
    }

    @Override
    public HymnalsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hymnal_row, null);
        return new HymnalsHolder(view, context);
    }

    @Override
    public void onBindViewHolder(HymnalsHolder holder, int position) {
        if (bookmark) {
            holder.logo.setVisibility(View.VISIBLE);
            holder.shortcut.setVisibility(View.GONE);
            holder.id = itemList.get(position).getNumber();
            holder.logo.setImageResource(itemList.get(position).getMipmap());
            holder.bookmark = true;
        } else {
            holder.logo.setVisibility(View.GONE);
            holder.shortcut.setVisibility(View.VISIBLE);
            holder.shortcut.setText(itemList.get(position).getNumber());
            holder.shortcut.getBackground().setColorFilter(itemList.get(position).getColor(), PorterDuff.Mode.OVERLAY);
            holder.id = itemList.get(position).getId();
        }
        holder.name.setText(itemList.get(position).getName());
        holder.name.setTextColor(context.getResources().getColor(nameColor()));

        if (MainScreen.serif) {
            holder.name.setTypeface(Typeface.SERIF);
            holder.shortcut.setTypeface(Typeface.SERIF);
        }
    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }

    public int nameColor() {
        if (MainScreen.night) {
            return R.color.white;
        } else {
            return R.color.black;
        }
    }
}

