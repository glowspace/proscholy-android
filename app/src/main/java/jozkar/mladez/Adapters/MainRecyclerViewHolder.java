package jozkar.mladez.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import jozkar.mladez.About;
import jozkar.mladez.MainScreen;
import jozkar.mladez.NavigationHelper;
import jozkar.mladez.R;
import jozkar.mladez.Splash;

/**
 * Created by Jozkar on 14.11.2015.
 */
public class MainRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView name, num;
    public ImageView number;
    public String id;
    int position;
    public Context context;

    public MainRecyclerViewHolder(View itemView, Context c) {
        super(itemView);

        //implementing onClickListener
        itemView.setOnClickListener(this);
        name = (TextView) itemView.findViewById(R.id.name);
        number = (ImageView) itemView.findViewById(R.id.number);
        context = c;
    }

    @Override
    public void onClick(View view) {
        //Every time you click on the row toast is displayed

        Intent i;

        try {
            switch (position) {
                case 1:
                    MainScreen.navigationHelper.navigate(NavigationHelper.Screen.SETTINGS);
                    break;
                case 2:
                    i = new Intent(context, About.class);
                    i.putExtra("type", 0);
                    context.startActivity(i);
                    break;
                case 3:
                    i = new Intent(context, About.class);
                    i.putExtra("type", 1);
                    context.startActivity(i);
                    break;
                case 4:
                    MainScreen.navigationHelper.navigate(NavigationHelper.Screen.BOOKMARKS);
                    break;

                default:
            }

        } catch (NullPointerException e) {
            Intent k = new Intent(context, Splash.class);
            context.startActivity(k);
        }
    }
}
