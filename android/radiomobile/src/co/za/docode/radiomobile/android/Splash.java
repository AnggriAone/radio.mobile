package co.za.docode.radiomobile.android;

import co.za.docode.radiomobile.android.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class Splash extends Activity {

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.splash);

		new Handler().postDelayed(new Runnable(){
			public void run() {
				Intent mainIntent = new Intent(Splash.this,VibeMedia.class);
				Splash.this.startActivity(mainIntent);
				Splash.this.finish();
			}
		}, 2000);
	}
}