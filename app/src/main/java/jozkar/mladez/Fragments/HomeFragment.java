package jozkar.mladez.Fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.view.ActionMode;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.futuremind.recyclerviewfastscroll.FastScroller;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import jozkar.mladez.Adapters.RecyclerViewAdapter;
import jozkar.mladez.App;
import jozkar.mladez.BottomSheet.FilterBottomSheet;
import jozkar.mladez.DataStructures.Record;
import jozkar.mladez.DataStructures.RowData;
import jozkar.mladez.DataStructures.SongBook;
import jozkar.mladez.R;

import static android.view.View.GONE;

/**
 * Created by Jozkar on 29.11.2015.
 */
public class HomeFragment extends Fragment {

    private static View view;
    RecyclerView.LayoutManager layoutManager;
    public static SearchView search;
    static ActionMode am;
    static ActionModeCallback amc;

    public static RecyclerView recyclerView;
    public static RecyclerViewAdapter adapter;
    public static List<RowData> rowListItem, searchList;
    public static Context context;
    public static FastScroller fastScroller;
    public static Activity activity;
    public static AppCompatDelegate acd;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
        activity = getActivity();
        acd = AppCompatDelegate.create(activity,null);
        amc = new ActionModeCallback();
        view = inflater.inflate(R.layout.fragment_home, container, false);

        if(App.tags.noChecked > 0){
            view.findViewById(R.id.filterWrapper).setVisibility(View.VISIBLE);
            ChipGroup cg = view.findViewById(R.id.filterList);
            for(String s : App.tags.getFilter()){
                Chip c = (Chip) inflater.inflate(R.layout.chip_close, cg, false);
                c.setHint(s);
                c.setText(App.tags.getById(s).getName());
                c.setCloseIconVisible(true);
                c.setClickable(false);
                c.setOnCloseIconClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cg.removeView(v);
                        App.tags.getById(((Chip) v).getHint().toString()).setChecked(false);
                        refreshSearch();
                        if (App.tags.noChecked == 0){
                            view.findViewById(R.id.filterWrapper).setVisibility(GONE);
                        }
                    }
                });
                cg.addView(c);
            }
        } else {
            view.findViewById(R.id.filterWrapper).setVisibility(GONE);
        }
        view.findViewById(R.id.filter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FilterBottomSheet fbs = new FilterBottomSheet();
                fbs.show(getParentFragmentManager(), "filter");
            }
        });

        search = (SearchView) view.findViewById(R.id.search);
        search.setSubmitButtonEnabled(true);
        search.clearFocus();
        search.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                view.findViewById(R.id.logo).setVisibility(View.VISIBLE);
                return false;
            }
        });
        search.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.findViewById(R.id.logo).setVisibility(GONE);
            }
        });
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if(s == null || s.isEmpty()) {
                    adapter = new RecyclerViewAdapter(context, rowListItem, false,true);
                    recyclerView.setAdapter(adapter);
                    fastScroller.setVisibility(View.VISIBLE);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if(s.isEmpty()){
                    if(App.tags.noChecked == 0) {
                        adapter = new RecyclerViewAdapter(context, rowListItem, false, true);
                        recyclerView.setAdapter(adapter);
                        fastScroller.setVisibility(View.VISIBLE);
                    } else {
                        searchList = getSearch(s);
                        adapter = new RecyclerViewAdapter(context, searchList, false, true);
                        recyclerView.setAdapter(adapter);
                        fastScroller.setVisibility(GONE);
                    }
                } else {
                    searchList = getSearch(s);
                    adapter = new RecyclerViewAdapter(context, searchList, false, true);
                    recyclerView.setAdapter(adapter);
                    fastScroller.setVisibility(GONE);
                }
                return false;
            }
        });

        view.findViewById(R.id.logo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search.setIconified(false);
            }
        });
        //search.setOnKeyListener(this);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setTitle("");
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        fastScroller = (FastScroller) view.findViewById(R.id.fastscroll);
        rowListItem = getAlphabeticalOrder();
        layoutManager = new LinearLayoutManager(context);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(layoutManager);
        if(App.tags.noChecked > 0){
            searchList = getSearch(search.getQuery().toString());
            adapter = new RecyclerViewAdapter(context, searchList, false, true);
        } else {
            adapter = new RecyclerViewAdapter(context, rowListItem, false, true);
        }
        recyclerView.setAdapter(adapter);
        fastScroller.setRecyclerView(recyclerView);
        return view;

    }

    public static void refreshSearch(){
        if(App.tags.noChecked == 0){
            adapter = new RecyclerViewAdapter(context, rowListItem, false, true);
            fastScroller.setVisibility(View.VISIBLE);
        } else {
            view.findViewById(R.id.filterWrapper).setVisibility(View.VISIBLE);
            ChipGroup cg = view.findViewById(R.id.filterList);
            cg.removeAllViews();
            for(String s : App.tags.getFilter()){
                Chip c = (Chip) LayoutInflater.from(context).inflate(R.layout.chip_close, cg, false);
                c.setHint(s);
                c.setText(App.tags.getById(s).getName());
                c.setCloseIconVisible(true);
                c.setClickable(false);
                c.setOnCloseIconClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cg.removeView(v);
                        App.tags.getById(((Chip) v).getHint().toString()).setChecked(false);
                        refreshSearch();
                        if (App.tags.noChecked == 0){
                            view.findViewById(R.id.filterWrapper).setVisibility(GONE);
                        }
                    }
                });
                cg.addView(c);

            }
            searchList = getSearch(search.getQuery().toString());
            if (searchList.size() == rowListItem.size()) {
                adapter = new RecyclerViewAdapter(context, rowListItem, false, true);
                fastScroller.setVisibility(View.VISIBLE);
            } else {
                adapter = new RecyclerViewAdapter(context, searchList, false, true);
                fastScroller.setVisibility(GONE);
            }
        }
        recyclerView.setAdapter(adapter);

    }

    public ArrayList<RowData> getAlphabeticalOrder(){
        App.songs.sorting(App.byName);
        ArrayList<RowData> r = new ArrayList<>();
        for(Record rec : App.songs.db){
            r.add(new RowData(rec.getId(), rec.getName(), "", rec.getId()));
        }
        return r;
    }

    @Override
    public void onStop() {
        super.onStop();
        if(am != null){
            am.finish();
            am = null;
        }
    }

    public static ArrayList<RowData> getSearch(String search){
        App.songs.sorting(App.byName);
        ArrayList<RowData> rNumber = new ArrayList<>(), rStartName = new ArrayList<>(), rName = new ArrayList<>(), rContent = new ArrayList<>();
        ArrayList<String> filter = App.tags.getFilter();

        try{

            String normSearch = Normalizer.normalize(search, Normalizer.Form.NFD);
            normSearch = App.pattern.matcher(normSearch).replaceAll("");


            for(Record rec : App.songs.db){
                boolean set = false;
                StringBuilder sb;
                for(SongBook s : rec.getSongbooks()){
                    sb = new StringBuilder();
                    sb.append(App.songbooks.getById(s.getId()).getShortcut());
                    sb.append(s.getNumber());

                    String nums = Normalizer.normalize(sb.toString(), Normalizer.Form.NFD);
                    nums = App.pattern.matcher(nums).replaceAll("");

                    if(!set && nums.matches("(?i:.*" + normSearch + ".*)")){
                        if(filter.size() == 0) {
                            rNumber.add(new RowData(rec.getId(), rec.getName(), normSearch, rec.getId(), true));
                            set = true;
                        } else {
                            if(rec.containsAll(filter)){
                                rNumber.add(new RowData(rec.getId(), rec.getName(), normSearch, rec.getId(), true));
                                set = true;
                            }
                        }
                    }
                }

                if(!set){
                    sb = new StringBuilder();
                    sb.append(rec.getId());

                    String nums = Normalizer.normalize(sb.toString(), Normalizer.Form.NFD);
                    nums = App.pattern.matcher(nums).replaceAll("");

                    if(nums.matches("(?i:.*" + normSearch + ".*)")){
                        if(filter.size() == 0) {
                            rNumber.add(new RowData(sb.toString(), rec.getName(), normSearch, rec.getId(), true));
                            set = true;
                        } else {
                            if(rec.containsAll(filter)){
                                rNumber.add(new RowData(sb.toString(), rec.getName(), normSearch, rec.getId(), true));
                                set = true;
                            }
                        }
                    }
                }

                if(!set) {
                    if (rec.getSearchName().matches("(?i:" + normSearch + ".*)")) {
                        if(filter.size() == 0) {
                            rStartName.add(new RowData(rec.getId(), rec.getName(), normSearch, rec.getId(), true));
                            set = true;
                        } else {
                            if(rec.containsAll(filter)){
                                rStartName.add(new RowData(rec.getId(), rec.getName(), normSearch, rec.getId(), true));
                                set = true;
                            }
                        }
                    }
                }

                if(!set) {
                    if (rec.getSearchName().matches("(?i:.*" + normSearch + ".*)")) {
                        if(filter.size() == 0) {
                            rName.add(new RowData(rec.getId(), rec.getName(), normSearch, rec.getId(), true));
                            set = true;
                        } else {
                            if(rec.containsAll(filter)){
                                rName.add(new RowData(rec.getId(), rec.getName(), normSearch, rec.getId(), true));
                                set = true;
                            }
                        }
                    }
                }

                if(!set) {
                    if (rec.getSearch().matches("(?i:.*" + normSearch + ".*)")) {
                        if(filter.size() == 0){
                            rContent.add(new RowData(rec.getId(), rec.getName(), normSearch, rec.getId(), true));
                        } else {
                            if(rec.containsAll(filter)){
                                rContent.add(new RowData(rec.getId(), rec.getName(), normSearch, rec.getId(), true));
                            }
                        }
                    }
                }
            }
        }catch(Exception e){
            return null;
        }

        Collections.sort(rNumber, CompareByNumber);
        rNumber.addAll(rStartName);
        rNumber.addAll(rName);
        rNumber.addAll(rContent);

        return rNumber;
    }

    public static Comparator<RowData> CompareByNumber = new Comparator<RowData>() {

        public int compare(RowData r1, RowData r2) {
            String number1 = r1.getNumber().replaceAll("\\D","");
            String number2 = r2.getNumber().replaceAll("\\D","");

            int num1 = number1.isEmpty()?0:Integer.parseInt(number1),
                    num2 = number2.isEmpty()?0:Integer.parseInt(number2);

            //ascending order
            return num1-num2;

            //descending order
            //return number2.compareTo(number1);
        }
    };



    public static void enableActionMode(int position) {
        if (am == null) {
            am = acd.startSupportActionMode(amc);
        }

        toggleSelection(position);
    }

    public static void toggleSelection(int position) {
        adapter.toggleSelection(position);
        int count = adapter.getSelectedItemCount();

        if (count == 0) {
            am.finish();
        } else {
            if(count == 1) {
                am.setTitle(count + " píseň");
            }else if (count > 1 && count < 5) {
                am.setTitle(count + " písně");
            } else if (count > 4) {
                am.setTitle(count + " písní");
            }
            am.invalidate();
        }
    }

    public static void toggleSelection(int position, boolean value) {
        adapter.setSelection(position, value);
        int count = adapter.getSelectedItemCount();

        if (count == 0) {
            am.finish();
        } else {
            if(count == 1) {
                am.setTitle(count + " píseň");
            }else if (count > 1 && count < 5) {
                am.setTitle(count + " písně");
            } else if (count > 4) {
                am.setTitle(count + " písní");
            }
            am.invalidate();
        }
    }

    private class ActionModeCallback implements ActionMode.Callback {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.menu_action_mode, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_all:
                    boolean set;
                    if(adapter.getSelectedItemCount() < adapter.getItemCount()){
                        set = true;
                    } else {
                        set = false;
                    }
                    for(int i = 0; i < adapter.getItemCount(); i++){
                        toggleSelection(i, set);
                    }
                    return false;
                case R.id.action_favorite:
                    boolean noFavorite = true;
                    ArrayList<RowData> fav = new ArrayList<>();
                    for(int i = 0; i < adapter.itemList.size(); i++){
                        if(adapter.isSelected(i) && App.songs.checkFavorite(adapter.itemList.get(i).getId())){
                            noFavorite = false;
                            fav.add(adapter.itemList.get(i));
                        }
                    }

                    if(!noFavorite){
                        for(RowData r: fav){
                            App.songs.unSetFavorite(r.getId());
                        }
                    } else {
                        for(int i = 0; i < adapter.itemList.size(); i++){
                            if(adapter.isSelected(i)){
                                App.songs.setFavorite(adapter.itemList.get(i).getId());
                            }
                        }
                    }
                    adapter.notifyDataSetChanged();
                    return false;

                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            am = null;
            adapter.cleanSelectedIndex();
        }
    }
}