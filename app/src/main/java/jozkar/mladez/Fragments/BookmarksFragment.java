package jozkar.mladez.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import jozkar.mladez.Adapters.HymnalsAdapter;
import jozkar.mladez.App;
import jozkar.mladez.DataStructures.Record;
import jozkar.mladez.DataStructures.RowData;
import jozkar.mladez.App;
import jozkar.mladez.R;

/**
 * Created by Jozkar on 29.11.2015.
 */
public class BookmarksFragment extends Fragment{

    private View view;
    RecyclerView.LayoutManager layoutManager;

    public BookmarksFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        App.songs.sorting(App.byName);

        view = inflater.inflate(R.layout.fragment_number_table, container, false);

        ((Toolbar)view.findViewById(R.id.toolbar)).setTitle(R.string.bookmarks);

        List<RowData> rowListItem = new ArrayList<>();


        String number = App.SP.getString("goldBookmark", "000");

        if (App.songs.getIndexByName(number) >= 0) {
            Record r = App.songs.getById(number);
            rowListItem.add(new RowData(r.getId(), r.getName(), R.drawable.bookmark_gold));
        }

        number = App.SP.getString("redBookmark", "000");

        if (App.songs.getIndexByName(number) >= 0) {
            Record r = App.songs.getById(number);
            rowListItem.add(new RowData(r.getId(), r.getName(), R.drawable.bookmark_red));
        }

        number = App.SP.getString("greenBookmark", "000");

        if (App.songs.getIndexByName(number) >= 0) {
            Record r = App.songs.getById(number);
            rowListItem.add(new RowData(r.getId(), r.getName(), R.drawable.bookmark_green));
        }

        number = App.SP.getString("blueBookmark", "000");

        if (App.songs.getIndexByName(number) >= 0) {
            Record r = App.songs.getById(number);
            rowListItem.add(new RowData(r.getId(), r.getName(), R.drawable.bookmark_blue));
        }

        number = App.SP.getString("purpleBookmark", "000");

        if (App.songs.getIndexByName(number) >= 0) {
            Record r = App.songs.getById(number);
            rowListItem.add(new RowData(r.getId(), r.getName(), R.drawable.bookmark_purple));
        }
        number = App.SP.getString("grayBookmark", "000");

        if (App.songs.getIndexByName(number) >= 0) {
            Record r = App.songs.getById(number);
            rowListItem.add(new RowData(r.getId(), r.getName(), R.drawable.bookmark_gray));
        }

        if(rowListItem.size() < 1){
            Snackbar snack = Snackbar.make(getActivity().findViewById(R.id.placeSnackBar), getString(R.string.emptyBookmarksList), Snackbar.LENGTH_SHORT).setAction("Action", null);
            snack.getView().setBackgroundColor(getResources().getColor(R.color.red));
            snack.show();
        }

        layoutManager = new LinearLayoutManager(getActivity());
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(layoutManager);
        HymnalsAdapter adapter = new HymnalsAdapter(getActivity(), rowListItem, true);
        recyclerView.setAdapter(adapter);
        return view;

    }
}