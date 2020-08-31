package jozkar.mladez;

import android.content.Context;
import android.preference.PreferenceManager;
import android.util.TypedValue;
import android.view.ScaleGestureDetector;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Jozkar on 17.11.2015.
 */
public class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
    float scale, def = 17.0f;
    TextView tv;
    Context context;
    Toast toast;

    public ScaleListener(TextView view,Context c){
        tv = view;
        context = c;
        toast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {

        scale = PreferenceManager.getDefaultSharedPreferences(context).getFloat("textScale", 17.0f);

        scale *= detector.getScaleFactor();
        scale = Math.max(8.5f, Math.min(scale, 51.0f));

        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, scale);

        float pomer = (scale / def) * 100;
        PreferenceManager.getDefaultSharedPreferences(context).edit().putFloat("textScale", scale).putInt("textSizePercent", (int) pomer).apply();

        toast.setText("Velikost písma je " + (int) pomer + " %");
        toast.show();

        return true;
    }
}