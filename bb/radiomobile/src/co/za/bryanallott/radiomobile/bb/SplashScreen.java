package co.za.bryanallott.radiomobile.bb;

import java.util.Timer;
import java.util.TimerTask;

import co.za.bryanallott.radiomobile.bb.lib.ImageHelper;


import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Characters;
import net.rim.device.api.system.Display;
import net.rim.device.api.system.EncodedImage;
import net.rim.device.api.system.KeyListener;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;

public class SplashScreen extends MainScreen {
	private MainScreen next;
	private UiApplication application;
	private Timer timer = new Timer();
	private final Bitmap _myImage;
	public SplashScreen(UiApplication ui, MainScreen next) {
		super(Field.USE_ALL_HEIGHT | Field.USE_ALL_WIDTH | Field.FIELD_HCENTER | Field.FIELD_VCENTER | NO_VERTICAL_SCROLL | NO_HORIZONTAL_SCROLL);
		this.application = ui;
		this.next = next;

		_myImage = ImageHelper.scaleImageToFitWidth(EncodedImage.getEncodedImageResource("splash.png")
													, Display.getWidth()).getBitmap();
		add(new VerticalFieldManager(USE_ALL_WIDTH | USE_ALL_HEIGHT){
            public void paint(Graphics graphics) {
                graphics.drawBitmap(0
                					, 0
                					, _myImage.getWidth()
                					, _myImage.getHeight()
                					, _myImage, 0, 0);
                super.paint(graphics);
            }            
        });
        
		SplashScreenListener listener = new SplashScreenListener(this);
		this.addKeyListener(listener);
		timer.schedule(new CountDown(), 1500);
		application.pushScreen(this);
	}
	protected void paintBackground(Graphics graphics) {
		graphics.setBackgroundColor(Color.WHITE);
		graphics.clear();
	}
	public void dismiss() {
		timer.cancel();
		application.popScreen(this);
		application.pushScreen(next);
	}
	private class CountDown extends TimerTask {
		public void run() {
			DismissThread dThread = new DismissThread();
			application.invokeLater(dThread);
		}
	}
	private class DismissThread implements Runnable {
		public void run() {
			dismiss();
		}
	}
	protected boolean navigationClick(int status, int time) {
		dismiss();
		return true;
	}
	protected boolean navigationUnclick(int status, int time) {
		return false;
	}
	protected boolean navigationMovement(int dx, int dy, int status, int time) {
		return false;
	}
	private static class SplashScreenListener implements KeyListener {
		private SplashScreen screen;
		public boolean keyChar(char key, int status, int time) {
			//intercept the ESC and MENU key - exit the splash screen
			boolean retval = false;
			switch (key) {
			case Characters.CONTROL_MENU:
			case Characters.ESCAPE:
				screen.dismiss();
				retval = true;
				break;
			}
			return retval;
		}
		public boolean keyDown(int keycode, int time) {
			return false;
		}
		public boolean keyRepeat(int keycode, int time) {
			return false;
		}
		public boolean keyStatus(int keycode, int time) {
			return false;
		}
		public boolean keyUp(int keycode, int time) {
			return false;
		}
		public SplashScreenListener(SplashScreen splash) {
			screen = splash;
		}
	}
}
