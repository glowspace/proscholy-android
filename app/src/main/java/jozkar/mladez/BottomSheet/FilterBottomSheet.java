package jozkar.mladez.BottomSheet;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.HashMap;
import java.util.Map;

import jozkar.mladez.Adapters.RecyclerViewAdapter;
import jozkar.mladez.DataStructures.Record;
import jozkar.mladez.Fragments.HomeFragment;
import jozkar.mladez.App;
import jozkar.mladez.R;

public class FilterBottomSheet extends BottomSheetDialogFragment {

    View v;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        v = inflater.inflate(R.layout.bottom_sheet_filter, container, false);
        LinearLayout layout = v.findViewById(R.id.content);
        float dpi = getResources().getDisplayMetrics().density;

        String [] categories = App.tags.getCategories();
        Map<String, ChipGroup> groups = new HashMap<String, ChipGroup>();

        for(String cat : categories){
            ChipGroup cg = (ChipGroup) inflater.inflate(R.layout.chip_group, layout, false);
            TextView tv = new TextView(getContext());
            tv.setText(App.tags.getById(cat).getName());
            tv.setPadding((int)(25*dpi), (int)(10*dpi), (int)(25*dpi), (int)(8*dpi));
            layout.addView(tv);
            layout.addView(cg);
            Log.d("STORE", cat);
            groups.put(cat, cg);
        }

        for(Record r: App.tags.db){
            String parent = r.getParent();
            if(!parent.equals("")) {
                Log.d("GET", parent);
                Chip c = (Chip) inflater.inflate(R.layout.chip_choice, groups.get(parent), false);
                c.setText(r.getName());
                c.setHint(r.getId());
                c.setCheckable(true);
                if(r.getChecked()){
                    c.setChecked(true);
                }
                c.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        Log.d("CHECK", isChecked+" "+buttonView.getHint());
                        if(isChecked){
                            App.tags.getById(buttonView.getHint().toString()).setChecked(true);
                        } else {
                            App.tags.getById(buttonView.getHint().toString()).setChecked(false);
                        }
                    }
                });
                groups.get(parent).addView(c);
            }
        }
        return v;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        HomeFragment.refreshSearch();
    }
}
