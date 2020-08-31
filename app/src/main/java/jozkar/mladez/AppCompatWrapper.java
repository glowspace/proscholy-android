package jozkar.mladez;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import static jozkar.mladez.App.SP;

public class AppCompatWrapper extends AppCompatActivity {

    public static boolean night, serif, note, acord, mute, sleep, lock, remote, silenceRequest, initialized, fullscreen, fullscreenSong;
    public static AudioManager amanager;
    public static int old = -1, oldA = -1, version, lastResult;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SP.edit().putBoolean("myActivity", true).apply();
        night = SP.getBoolean("night", false);
        serif = SP.getBoolean("serif", false);
        acord = SP.getBoolean("acord", false);
        note = SP.getBoolean("note", true);
        initialized = SP.getBoolean("initialized", false);
        mute = SP.getBoolean("mute", false);
        sleep = SP.getBoolean("sleep", false);
        lock = SP.getBoolean("lock", false);
        remote = SP.getBoolean("remote", false);
        silenceRequest = SP.getBoolean("silenceRequest", false);
        version = SP.getInt("oldVersion", 0);
        fullscreen = SP.getBoolean("fullscreen", false);
        fullscreenSong = SP.getBoolean("fullscreenSong", true);
        old = SP.getInt("oldAudio", -1);
        oldA = SP.getInt("oldAlarm",-1);

        if(fullscreen) {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        if (night) {
            setTheme(R.style.Dark);
        } else {
            setTheme(R.style.Light);
        }

        if(sleep){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }

        amanager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
    }

    public void setSound(){
        if(mute) {
            NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !nm.isNotificationPolicyAccessGranted()) {
                Log.d("SOUND", "doesn't have a permission");
                if (!silenceRequest) {
                    Intent nmIntent = new Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
                    SP.edit().putBoolean("silenceRequest", true).apply();
                    silenceRequest = true;
                    startActivity(nmIntent);
                } else {
                    SP.edit().putBoolean("mute", false).apply();
                    SP.edit().putBoolean("silenceRequest", false).apply();
                    silenceRequest = false;
                    mute = false;
                    Snackbar snack = Snackbar.make(findViewById(android.R.id.content), R.string.volume_unchanged, Snackbar.LENGTH_LONG).setDuration(Snackbar.LENGTH_LONG);
                    snack.getView().setBackgroundColor(getResources().getColor(R.color.red));
                    snack.show();
                }
            } else {
                if (old == -1) {
                    old = amanager.getRingerMode();
                    oldA = amanager.getStreamVolume(AudioManager.STREAM_ALARM);
                    SP.edit().putInt("oldAudio", old).apply();
                    SP.edit().putInt("oldAlarm", oldA).apply();
                }
                amanager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                amanager.setStreamVolume(AudioManager.STREAM_ALARM, 0, 0);
            }

        }
    }

    @Override
    protected void onResume() {
        SP.edit().putBoolean("myActivity", true).apply();
        Log.d("ACTIVITY", "RESUME");
        setSound();
        super.onResume();
    }

    @Override
    protected void onStop(){
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            if(pm != null && !pm.isInteractive()){
                SP.edit().putBoolean("myActivity", true).apply();
            }
        } else {
            if(pm != null && !pm.isScreenOn()){
                SP.edit().putBoolean("myActivity", true).apply();
            }
        }

        if (old != -1 && !SP.getBoolean("myActivity", false)) {
            amanager.setRingerMode(old);
            amanager.setStreamVolume(AudioManager.STREAM_ALARM, oldA, 0);
            old = -1;
            oldA = -1;
            SP.edit().putInt("oldAudio", -1).putInt("oldAlarm", -1).apply();
            Log.d("SOUND", "RESTORED");
        }

        SP.edit().putBoolean("myActivity", false).apply();

        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart(){
        super.onRestart();
    }

    @Override
    protected void onPause() {
        if(SP.getBoolean("myActivity", false)){
            SP.edit().putBoolean("myActivity", false).apply();
        }
        super.onPause();
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        finish();
    }
}
