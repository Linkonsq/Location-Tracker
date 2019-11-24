package linkon.siddique.smartprofile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Intent intent;
    private AudioManager myAudioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        intent = new Intent(this, MyService.class);

        ///////////////////////////
        myAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        Button high = (Button) findViewById(R.id.button5);
        high.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                Toast.makeText(MainActivity.this, "Now in normal mode", Toast.LENGTH_SHORT).show();
            }
        });

        Button silent = (Button) findViewById(R.id.button6);
        silent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myAudioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                //myAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                Toast.makeText(MainActivity.this, "Now in silent mode", Toast.LENGTH_SHORT).show();
            }
        });
        //////////////////////////////////
    }

    public void onClickStart(View view) {
        startService(intent);
    }

    public void onClickStop(View view) {
        stopService(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}