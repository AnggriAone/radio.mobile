package co.za.docode.radiomobile.android;

import co.za.docode.radiomobile.android.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;

public class VibeMedia extends Activity {
	private ImageButton start;
	private MediaPlayer mp = null;
	private StreamProxy proxy = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		setContentView(R.layout.mediavibe);
		setTitle(R.string.app_name);
		
		initControls();
		Toast.makeText(getApplicationContext(), R.string.click_play_to_start, 2000).show();
	}
	@Override
	public void onConfigurationChanged (Configuration newConfig) {
	  super.onConfigurationChanged(newConfig);
	  setContentView(R.layout.mediavibe);
	  setButton();
	}
	@Override
	public void onDestroy() {
		Stop();
		super.onDestroy();
	}
	private void initControls() {
		start = (ImageButton)findViewById(R.id.start);
		start.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Toggle();
			}
		});

	}
	private void setButton() {
		start = (ImageButton)findViewById(R.id.start);
		if((null != mp) && (mp.isPlaying())) {
			start.setImageResource(R.drawable.button_pause);
		} else {
			start.setImageResource(R.drawable.button_play);
		}
	}
	private void Toggle() {
		if(null == mp){
			
			Start();
			
			if((null != mp) && (mp.isPlaying())) {
				start = (ImageButton)findViewById(R.id.start);
				start.setImageResource(R.drawable.button_pause);
			} else {
				Toast.makeText(getApplicationContext(), R.string.cnx_issues, 2000).show();
			}
		} else {
			if(mp.isPlaying()) {
				Pause();
				start = (ImageButton)findViewById(R.id.start);
				start.setImageResource(R.drawable.button_play);
			} else {
				Resume();
				start = (ImageButton)findViewById(R.id.start);
				start.setImageResource(R.drawable.button_pause);
			}
		}
	}
	private boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		return (netInfo != null && netInfo.isConnectedOrConnecting());
	}
	private void Start() {
		if(!isOnline()) {
			new AlertDialog.Builder(this)
			.setMessage(R.string.no_internet)
			.setCancelable(false)
			.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					//do nothing
				}
			})
			.show();
			return;
		}
		synchronized (this) {
			mp = new MediaPlayer();
			try {
				String url = "http://cdn-routable.core25-40.ndstream.net:8080/;"; 
				String playUrl = url;
				int sdkVersion = 0;
				try {
					sdkVersion = Integer.parseInt(Build.VERSION.SDK);
				} catch (NumberFormatException e) {
				}
				if (sdkVersion < 8) {
					if (proxy == null) {
						proxy = new StreamProxy();
						proxy.init();
						proxy.start();
					}
					String proxyUrl = String.format("http://127.0.0.1:%d/%s", proxy.getPort(), url);
					playUrl = proxyUrl;
				}
				mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
				mp.setDataSource(getApplicationContext(), Uri.parse(playUrl));
			} catch (Exception e) {
				mp = null;
			}

			if(null != mp) {
				try {
					mp.prepareAsync();
				} catch (Exception e) {
					mp = null;
				}
			}
			if(null != mp) {
				int retryCount = 0;
				do {
					int waitingCount = 0;
					do {
						mp.start();
						Thread.yield();
						try {
							Thread.sleep(80);
						} catch (InterruptedException e) {}
					} while(!mp.isPlaying() && waitingCount++ < 20);
					
				} while(!mp.isPlaying() && (retryCount++ < 3));
			}
			if(!mp.isPlaying()) {
				mp.release();
				mp = null;
			}
		}
	}
	private void Resume() {
		if (mp == null){
			Start();
		} else {
			if(!mp.isPlaying()) {
				mp.start();
			}
		}
	}
	private void Pause() {
		if (mp != null){
			if(mp.isPlaying()) {
				mp.pause();
			}
		}
	}
	private void Stop(){
		synchronized(this) {
			if (proxy != null) {
				proxy.stop();
				proxy = null;
			}
			if (mp != null){
				if(mp.isPlaying()) {
					mp.stop();
				}
				mp.release();
				mp = null;
			}
		}
	}
}
