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

import jozkar.mladez.Adapters.RecyclerViewAdapter;
import jozkar.mladez.App;
import jozkar.mladez.DataStructures.Favorites;
import jozkar.mladez.DataStructures.Record;
import jozkar.mladez.DataStructures.RowData;
import jozkar.mladez.MainScreen;
import jozkar.mladez.R;

/**
 * Created by Jozkar on 29.11.2015.
 */

public class FavoritsFragment extends Fragment {

    private View view;
    RecyclerView.LayoutManager layoutManager;

    public FavoritsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        App.songs.sorting(App.byName);

        view = inflater.inflate(R.layout.fragment_number_table, container, false);

        ((Toolbar) view.findViewById(R.id.toolbar)).setTitle(R.string.favorits);

        List<RowData> rowListItem = new ArrayList<>();

        for (Favorites f : App.songs.favorites) {
            Record r = App.songs.getById(f.getNumber());
            rowListItem.add(new RowData(r.getId(), r.getName(), r.getId(), 0));
        }

        if (rowListItem.size() < 1) {
            Snackbar snack = Snackbar.make(getActivity().findViewById(R.id.placeSnackBar), getString(R.string.emptyList), Snackbar.LENGTH_SHORT).setAction("Action", null);
            snack.getView().setBackgroundColor(getResources().getColor(R.color.red));
            snack.show();
        }

        layoutManager = new LinearLayoutManager(getActivity());
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(getActivity(), rowListItem, true, false);
        recyclerView.setAdapter(adapter);
        return view;

    }
}