package jozkar.mladez;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by Jozkar on 4.4.2016.
 */
public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.splash);

        final boolean error = getIntent().getBooleanExtra("null",false);
        if(error){
            TextView name = (TextView)findViewById(R.id.textView);
            name.setText(R.string.reload);
        }
        PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putInt("lastResult", 0).apply();
        Thread timerThread = new Thread(){
            public void run(){
                try{
                    super.run();
                    if(error) {
                        sleep(3500);
                    }else{
                        sleep(1500);
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }finally {
                    Intent i = new Intent(Splash.this, MainScreen.class);
                    startActivity(i);
                    finish();
                }
            }
        };
        timerThread.start();
    }
}
