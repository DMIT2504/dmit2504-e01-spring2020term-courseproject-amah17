package ca.nait.audioeditor;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Button playBtn;
    private SeekBar positionBar;
    private SeekBar volumeBar;
    private TextView elapsedTimeText;
    private TextView remainingTimeText;
    private MediaPlayer mMediaPlayer;

    private float speed = 1.0f;
    private float pitch = 1.0f;
    private int totalTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playBtn = findViewById(R.id.playBtn);
        elapsedTimeText = findViewById(R.id.elapsedTimeLabel);
        remainingTimeText = findViewById(R.id.remainingTimeLabel);

        mMediaPlayer = MediaPlayer.create(this, R.raw.audio);
        mMediaPlayer.setLooping(true);
        mMediaPlayer.seekTo(0);;
        mMediaPlayer.setVolume(0.5f, 0.5f);
        mMediaPlayer.setPlaybackParams(mMediaPlayer.getPlaybackParams().setSpeed(speed));
        mMediaPlayer.setPlaybackParams(mMediaPlayer.getPlaybackParams().setPitch(pitch));

        totalTime = mMediaPlayer.getDuration();

        //position bar java code
        positionBar = findViewById(R.id.positionBar);
        positionBar.setMax(totalTime);
        positionBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        mMediaPlayer.seekTo(progress);
                        positionBar.setProgress(progress);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                }
        );

        //Volume bar java code
        volumeBar = findViewById(R.id.volumeBar);
        volumeBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        float volumeNum = progress / 100f;
                        mMediaPlayer.setVolume(volumeNum, volumeNum);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                }
        );

        //Thead to update the positionBar and timelabel
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mMediaPlayer != null){
                    try {
                        Message msg = new Message();
                        msg.what = mMediaPlayer.getCurrentPosition();
                        handler.sendMessage(msg);
                        Thread.sleep(1000);
                    }
                    catch (InterruptedException e){}
                }
            }
        }).start();

    }

    public void playBtnClick(View view) {
        if (!mMediaPlayer.isPlaying()){
            // Stop it
            mMediaPlayer.start();
            playBtn.setBackgroundResource(R.drawable.stop);
        }
        else {
            mMediaPlayer.pause();
            playBtn.setBackgroundResource(R.drawable.play);
        }
    }

    @Override
    protected void onStart(){
        super.onStart();
        mMediaPlayer.pause();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        mMediaPlayer.release();
    }

    public void speedDecreaseBtn(View view) {
    }

    public void speedResetBtn(View view) {
    }

    public void speedIncreaseBtn(View view) {
    }

    public void pitchDecreaseBtn(View view) {
    }

    public void pitchResetBtn(View view) {
    }

    public void pitchIncreaseBtn(View view) {
    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int currentPosition = msg.what;
            // Update positionBar.
            positionBar.setProgress(currentPosition);

            // Update Labels.
            String elapsedTime = createTimeLabel(currentPosition);
            elapsedTimeText.setText(elapsedTime);

            String remainingTime = createTimeLabel(totalTime-currentPosition);
            remainingTimeText.setText("- " + remainingTime);
        }
    };

    public String createTimeLabel(int time) {
        String timeLabel = "";
        int min = time / 1000 / 60;
        int sec = time / 1000 % 60;

        timeLabel = min + ":";
        if (sec < 10) timeLabel += "0";
        timeLabel += sec;

        return timeLabel;
    }
}