package linkon.siddique.smartprofile;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class MyService extends Service {
    private AudioManager myAudioManager;
    //private int currentVolume, maxVolume;

    @Override
    public void onCreate() {
        super.onCreate();
        myAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        //maxVolume = myAudioManager.getStreamMaxVolume(AudioManager.STREAM_RING);
        Toast.makeText(this, "Created", Toast.LENGTH_SHORT).show();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Started", Toast.LENGTH_SHORT).show();
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Destroyed", Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }

    public void Normal() {
        myAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        Toast.makeText(this, "Now in normal mode", Toast.LENGTH_SHORT).show();

        /*currentVolume = myAudioManager.getStreamVolume(AudioManager.STREAM_RING);
        for (int i = currentVolume; i < maxVolume; i++) {
            //myAudioManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_VIBRATE);
            myAudioManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
        }*/
    }

    public void Silent() {
        myAudioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
        //myAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
        Toast.makeText(this, "Now in silent mode", Toast.LENGTH_SHORT).show();

        /*currentVolume = myAudioManager.getStreamVolume(AudioManager.STREAM_RING);
        for (int i = currentVolume; i > 0; i--) {
            //myAudioManager.adjustVolume(AudioManager.ADJUST_LOWER, AudioManager.FLAG_VIBRATE);
            myAudioManager.adjustVolume(AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
        }*/
    }
}
