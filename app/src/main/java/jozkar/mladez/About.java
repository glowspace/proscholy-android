package jozkar.mladez;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

public class About extends AppCompatWrapper {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_about);
        int type = getIntent().getIntExtra("type", 0);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (type == 0) {
            toolbar.setTitle(R.string.info_project);
        } else {
            toolbar.setTitle(R.string.info_app);
        }
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView about = (TextView) findViewById(R.id.about);
        about.setMovementMethod(LinkMovementMethod.getInstance());
        if (type == 0) {
            about.setText(Html.fromHtml(getString(R.string.about_project)));
        } else {
            about.setText(Html.fromHtml(getString(R.string.about_app)));
        }
        if (serif) {
            about.setTypeface(Typeface.SERIF);
        }

    }
}