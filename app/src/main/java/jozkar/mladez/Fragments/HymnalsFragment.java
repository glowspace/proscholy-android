package jozkar.mladez.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import jozkar.mladez.Adapters.HymnalsAdapter;
import jozkar.mladez.App;
import jozkar.mladez.DataStructures.RowData;
import jozkar.mladez.DataStructures.SongBook;
import jozkar.mladez.MainScreen;
import jozkar.mladez.R;

/**
 * Created by Jozkar on 29.11.2015.
 */
public class HymnalsFragment extends Fragment {

    private View view;
    RecyclerView.LayoutManager layoutManager;

    public HymnalsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        view = inflater.inflate(R.layout.fragment_number_table, container, false);

        ((Toolbar) view.findViewById(R.id.toolbar)).setTitle(R.string.hymnals);
        List<RowData> rowListItem = new ArrayList<>();

        for (SongBook s : App.songbooks.db) {
            rowListItem.add(new RowData(s.getShortcut(), s.getName(), s.getId(), s.getColor()));

        }

        layoutManager = new LinearLayoutManager(getActivity());
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(layoutManager);
        HymnalsAdapter adapter = new HymnalsAdapter(getActivity(), rowListItem, false);
        recyclerView.setAdapter(adapter);
        return view;

    }
}