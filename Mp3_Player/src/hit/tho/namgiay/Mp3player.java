package hit.tho.namgiay;
 
import hit.tho.namgiay.R;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
 
public class Mp3player extends Activity {
 
    private Button buttonPlayStop;
    private MediaPlayer mediaPlayer;
    private SeekBar seekBar;
    private CountDownTimer countDownTimer;
    private final Handler handler = new Handler();
 
    // Here i override onCreate method.
    //
    // setContentView() method set the layout that you will see then
    // the application will starts
    //
    // initViews() method i create to init views components.
    @Override
    public void onCreate(Bundle icicle) {
            super.onCreate(icicle);
            setContentView(R.layout.main);
            initViews();  
            
            
 
    }
    private void startCountDowntimer(){
    	int coutD =Integer.parseInt( txtPhut.getText().toString())*1000*60;
    	countDownTimer =   new CountDownTimer(coutD, 1000) {

            public void onTick(long millisUntilFinished) {
               
            }

            public void onFinish() {
            	pauseI=0;
            }
         };
         countDownTimer.start();
    }
    EditText txtPhut;
    
    // This method set the setOnClickListener and method for it (buttonClick())
    private void initViews() {
        buttonPlayStop = (Button) findViewById(R.id.ButtonPlayStop);
         txtPhut =(EditText) findViewById(R.id.txtPhut);
        buttonPlayStop.setOnClickListener(new OnClickListener() {@Override public void onClick(View v) {buttonClick();}});
 
       // mediaPlayer = MediaPlayer.create(this, R.raw.testsong_20_sec); 
        mediaPlayer = MediaPlayer.create(this, R.raw.thaynhuhittho5s); 
        seekBar = (SeekBar) findViewById(R.id.SeekBar01);
        seekBar.setMax(mediaPlayer.getDuration());
        seekBar.setOnTouchListener(new OnTouchListener() {@Override public boolean onTouch(View v, MotionEvent event) {
        	seekChange(v);
			return false; }
		});
 
    }
    
    public void startPlayProgressUpdater() {
    	if(pauseI==1)
    	seekBar.setProgress(mediaPlayer.getCurrentPosition());
    	
		if (mediaPlayer.isPlaying()) {
			Runnable notification = new Runnable() {
		        public void run() {
		        	if(pauseI==1)
		        	startPlayProgressUpdater();
				}
		    };
		    handler.postDelayed(notification,1000);
    	}else{
    		
    		//mediaPlayer.pause();
    		//buttonPlayStop.setText(getString(R.string.play_str));
    		
//    		seekBar.setProgress(0);
//    		mediaPlayer.start();
    		mediaPlayer.start();
            startPlayProgressUpdater(); 
    	}
    } 
 
    // This is event handler thumb moving event
    private void seekChange(View v){
    	if(mediaPlayer.isPlaying()){
	    	SeekBar sb = (SeekBar)v;
			mediaPlayer.seekTo(sb.getProgress());
		}
    }
 int pauseI =0;
    // This is event handler for buttonClick event
    private void buttonClick(){
        if (buttonPlayStop.getText() == getString(R.string.play_str)) {
            buttonPlayStop.setText(getString(R.string.pause_str));
            try{
            
            	pauseI =1;
            	mediaPlayer.start();
                startPlayProgressUpdater(); 
            	startCountDowntimer();
            }catch (IllegalStateException e) {
            	mediaPlayer.pause();
            }
        }else {
            buttonPlayStop.setText(getString(R.string.play_str));
            mediaPlayer.pause();
            pauseI =0;
        }
    }
}