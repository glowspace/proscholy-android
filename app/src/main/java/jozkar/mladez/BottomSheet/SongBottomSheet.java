package jozkar.mladez.BottomSheet;

import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.HashMap;
import java.util.Map;

import jozkar.mladez.App;
import jozkar.mladez.DataStructures.Record;
import jozkar.mladez.DataStructures.SongBook;
import jozkar.mladez.Fragments.HomeFragment;
import jozkar.mladez.R;
import jozkar.mladez.Song;

import static jozkar.mladez.Song.rec;
import static jozkar.mladez.Song.songbook;
import static jozkar.mladez.Song.songlist;

public class SongBottomSheet extends BottomSheetDialogFragment {

    View v;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        v = inflater.inflate(R.layout.bottom_sheet_filter, container, false);
        LinearLayout layout = v.findViewById(R.id.content);
        float dpi = getResources().getDisplayMetrics().density;

        int [] bookmarks = new int[]{R.drawable.bookmark_gold,R.drawable.bookmark_red,R.drawable.bookmark_green,R.drawable.bookmark_blue,R.drawable.bookmark_purple,R.drawable.bookmark_gray};
        String[] colors = new String[]{"gold","red","green","blue","purple","gray"};
        Map<String, ChipGroup> groups = new HashMap<String, ChipGroup>();

        TextView tv = new TextView(getContext());
        tv.setText(getResources().getString(R.string.storeToBookmark));
        tv.setPadding((int)(25*dpi), (int)(10*dpi), (int)(25*dpi),0);
        layout.addView(tv);

        GridLayout gl = (GridLayout) inflater.inflate(R.layout.grid_layout, layout, false);
        gl.setColumnCount(7);
        layout.addView(gl);
        for(int i = 0; i<bookmarks.length; i++){
            final String currentColor = colors[i];
            ImageButton c = (ImageButton) inflater.inflate(R.layout.image_button, gl, false);
            c.setImageDrawable(getResources().getDrawable(bookmarks[i]));
            c.getBackground().setColorFilter(getResources().getColor(R.color.darkWhite), PorterDuff.Mode.OVERLAY);
            c.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String number = rec.getId(), book;
                    if(songbook == null || songbook.isEmpty()){
                        book = "Zp";
                    } else {
                        book = App.songbooks.getById(songbook).getShortcut();
                        Log.d("BOOK", book);
                        Record r = songlist.getById(number);
                        for(SongBook s : r.getSongbooks()){
                            if(s.getId().equals(songbook)){
                                number = s.getNumber();
                            }
                        }
                    }
                    App.SP.edit().putString(currentColor + "Bookmark", rec.getId()).putString(currentColor + "BookmarkSongNumber", number).putString(currentColor + "BookmarkSongbook", book).apply();
                    Song.updateBookmarks();
                    dismiss();
                }

            });
            gl.addView(c);
        }

        ImageButton c = (ImageButton) inflater.inflate(R.layout.image_button, gl, false);
        c.setImageDrawable(getResources().getDrawable(R.drawable.trash));
        c.getBackground().setColorFilter(getResources().getColor(R.color.darkWhite), PorterDuff.Mode.OVERLAY);
        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                App.SP.edit().putString("goldBookmark", "-1").putString("greenBookmark", "-1").putString("blueBookmark", "-1").putString("redBookmark", "-1").putString("purpleBookmark", "-1").putString("grayBookmark", "-1").apply();
                Song.updateBookmarks();
                dismiss();
            }

        });
        gl.addView(c);

        TextView tvSettings = new TextView(getContext());
        tvSettings.setText(getResources().getString(R.string.view_setting));
        tvSettings.setPadding((int)(25*dpi), (int)(10*dpi), (int)(25*dpi),0);
        layout.addView(tvSettings);

        GridLayout glSettings = (GridLayout) inflater.inflate(R.layout.grid_layout, layout, false);
        glSettings.setColumnCount(3);
        glSettings.setPadding((int)(25*dpi), (int)(10*dpi), (int)(25*dpi),(int)(10*dpi));
        layout.addView(glSettings);

        Switch night = (Switch) inflater.inflate(R.layout.switch_button, glSettings, false);
        night.setText(R.string.night);
        night.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                App.SP.edit().putBoolean("night", !Song.night).apply();
                dismiss();
                getActivity().recreate();
            }
        });
        if(Song.night){
            night.setChecked(true);
        }

        glSettings.addView(night);


        Switch acords = (Switch) inflater.inflate(R.layout.switch_button, glSettings, false);
        acords.setText(R.string.acords);
        acords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                App.SP.edit().putBoolean("acord", !Song.acord).apply();
                dismiss();
                getActivity().recreate();
            }
        });
        if(Song.acord){
            acords.setChecked(true);
        }

        glSettings.addView(acords);
        return v;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
    }
}
