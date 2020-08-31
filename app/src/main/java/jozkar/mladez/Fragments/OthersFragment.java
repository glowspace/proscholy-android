package jozkar.mladez.Fragments;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import jozkar.mladez.Adapters.MainRecyclerViewAdapter;
import jozkar.mladez.DataStructures.MainRowData;
import jozkar.mladez.R;

public class OthersFragment extends Fragment {

    public View view;

    public OthersFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        view = inflater.inflate(R.layout.fragment_number_table, container, false);

        ((Toolbar) view.findViewById(R.id.toolbar)).setTitle(R.string.others);

        List<MainRowData> rowListItem = getOtherList();


        RecyclerView.LayoutManager layoutManager;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            layoutManager = new GridLayoutManager(getContext(), 3);
        } else {
            if (getResources().getConfiguration().smallestScreenWidthDp >= 600) {
                layoutManager = new GridLayoutManager(getContext(), 2);
            } else {
                layoutManager = new LinearLayoutManager(getContext());
            }
        }

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(layoutManager);

        MainRecyclerViewAdapter adapter = new MainRecyclerViewAdapter(getActivity(), rowListItem);
        recyclerView.setAdapter(adapter);
        return view;
    }

    private List<MainRowData> getOtherList() {

        List<MainRowData> currentItem = new ArrayList<MainRowData>();

        currentItem.add(new MainRowData(1, getString(R.string.settings), R.mipmap.settings));
        currentItem.add(new MainRowData(4, getString(R.string.bookmarks), R.drawable.bookmark_gold));
        currentItem.add(new MainRowData(2, getString(R.string.info_project), R.mipmap.ic_launcher));
        currentItem.add(new MainRowData(3, getString(R.string.info_app), R.mipmap.info));


        return currentItem;
    }
}
