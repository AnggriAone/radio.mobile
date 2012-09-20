package co.za.bryanallott.radiomobile.bb;

import net.rim.device.api.ui.UiApplication;

public class RadioMobileApp extends UiApplication {

	public static void main(String[] args) {
		new RadioMobileApp().enterEventDispatcher();
	}
	public RadioMobileApp() {
		new SplashScreen(this, new PlayerScreen());
	}
}
