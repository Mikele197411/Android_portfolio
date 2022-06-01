package com.mshilkov.cool_timer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private SeekBar seekBar;
    private TextView textView;
    private Boolean isTimerOn=false;
    private Button button;
    private CountDownTimer countDownTimer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        seekBar=findViewById(R.id.seekBar);
        textView=findViewById(R.id.textView);
        button=findViewById(R.id.button);
        seekBar.setMax(600);
        seekBar.setProgress(60);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                String result=texViewLabel(progress);
                textView.setText(result);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void onStartTimer(View view) {

        if(!isTimerOn)
        {
            button.setText("Stop");
            seekBar.setEnabled(false);
            isTimerOn=true;
            countDownTimer= new CountDownTimer(seekBar.getProgress()*1000, 1000) {
                @Override
                public void onTick(long l) {
                    int progress=(int)l/1000;
                    String result=texViewLabel(progress);
                    textView.setText(result);
                }

                @Override
                public void onFinish() {
                    SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    if(sharedPreferences.getBoolean("enable_sound", true))
                    {
                        MediaPlayer mediaPlayer=MediaPlayer.create(getApplicationContext(),R.raw.bell_sound);
                        mediaPlayer.start();
                        resestTimer();
                    }
                    else
                    {
                        resestTimer();
                    }

                }
            };
            countDownTimer.start();



        }
        else
        {
            resestTimer();

        }


    }

    private String texViewLabel(int progress)
    {
        int minutes=progress/60;
        int seconds=progress-(minutes*60);
        String minutesString="";
        String secondsString="";
        if(minutes<10)
        {
            minutesString="0"+minutes;
        }
        else
        {
            minutesString=String.valueOf(minutes);
        }
        if(seconds<10)
        {
            secondsString="0"+seconds;
        }
        else
        {
            secondsString=String.valueOf(seconds);
        }
        return (minutesString+":"+secondsString);
    }

    private void resestTimer()
    {
        countDownTimer.cancel();
        button.setText("Start");
        seekBar.setEnabled(true);
        textView.setText("00:60");
        seekBar.setProgress(60);
        isTimerOn=false;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.action_settings)
        {
            Intent openSettings=new Intent(this, SettingsActivity.class);
            startActivity(openSettings);
            return  true;
        }
        else if(id==R.id.action_about)
        {
            Intent openAbout=new Intent(this, AboutActivity.class);
            startActivity(openAbout);
            return  true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.timer_menu, menu);
        return true;
    }
}