package co.za.bryanallott.radiomobile.bb;

import javax.microedition.media.Player;
import javax.microedition.media.PlayerListener;
import javax.microedition.media.control.VolumeControl;

import net.rim.device.api.system.KeyListener;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Keypad;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.Status;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.MainScreen;
import rimx.media.streaming.StreamingPlayer;
import rimx.media.streaming.StreamingPlayerListener;

import co.za.bryanallott.radiomobile.bb.lib.ui.CustomBackground;
import co.za.bryanallott.radiomobile.bb.lib.ui.HorizontalButtonFieldSet;
import co.za.bryanallott.radiomobile.bb.lib.ui.PlayPauseField;
import co.za.bryanallott.radiomobile.bb.lib.ui.VibeScreen;


public class PlayerScreen extends VibeScreen implements StreamingPlayerListener, KeyListener {

	private StreamingPlayer sp;	
	private VolumeControl volC;
	private PlayerScreen playerScreen;
	private long lastBufferUpdate = System.currentTimeMillis();

	private String url="http://cdn-routable.core25-40.ndstream.net:8080/;listen.mp3";
	private String contentType = "audio/mpeg";
	private int bitRate = 128000;
	private int initBuffer = (bitRate/8)*5;	
	private int restartThreshold = (bitRate/8)*3;
	private int bufferCapacity = 4194304;
	private int bufferLeakSize = bufferCapacity/3;
	private int connectionTimeout = 6000;
	private boolean eventLogEnabled = false;
	private boolean sdLogEnabled = false;
	private int logLevel = 0;

	private LabelField _statusField;
	private long bufferStartsAt = 0;
	private long len = 0;
	private long nowReading = 0;

	private PlayPauseField _playPause;

	public PlayerScreen() {
		super(MainScreen.NO_VERTICAL_SCROLL);

		CustomBackground vlm = new CustomBackground() {
			protected void sublayout(int width, int height) {
				//
				//        [>||]
				//      [  Text  ]
				Field statusField = getField(1);
				final int text_height = statusField.getPreferredHeight();
				final int text_y = height - (text_height+5);
				layoutChild(statusField, width, text_height);
				setPositionChild(statusField
						, 0
						, text_y);
				
				Field playPauseField = getField(0);
				final int button_height = playPauseField.getPreferredHeight();
				final int play_y = text_y - (button_height + 10);
				layoutChild(playPauseField, width, button_height);
				setPositionChild(playPauseField
						, 0
						, play_y);
				
				setExtent(width, height);
			}
		};

		
		HorizontalButtonFieldSet playpause = new HorizontalButtonFieldSet(HorizontalFieldManager.FIELD_HCENTER);
		_playPause = new PlayPauseField() {
			protected boolean navigationClick(int status, int time) {
				if(!this.isPlaying()) {
					if(resume()) {
						this.setReadyToPause();
					}
				} else {
					pause();
					this.setReadyToPlay();
					_statusField.setText("Click Play");
				}
				return true;
			}
		};
		playpause.add(_playPause);
		vlm.add(playpause);
		
		_statusField = new LabelField("Click Play", LabelField.FIELD_HCENTER) {
			protected void paint(Graphics graphics) {
				graphics.setColor(Color.BLACK);
				super.paint(graphics);
			}
		};
		vlm.add(_statusField);
		add(vlm);
		
		_playPause.setFocus();
		this.playerScreen = this;
	}
	
	public boolean isDirty() {
		return false;
	}
	private void play(){
		new Thread(){
			public void run(){
				try{
					if(sp==null){
						sp = new StreamingPlayer(url, contentType);						
						sp.setBufferCapacity(bufferCapacity);						
						sp.setInitialBuffer(initBuffer);
						sp.setRestartThreshold(restartThreshold);
						sp.setBufferLeakSize(bufferLeakSize);
						sp.setConnectionTimeout(connectionTimeout);
						sp.setLogLevel(logLevel);
						sp.enableLogging(eventLogEnabled, sdLogEnabled);					
						sp.addStreamingPlayerListener(playerScreen);
						sp.realize();
						volC = (VolumeControl)sp.getControl("VolumeControl");
						sp.start();
					} else{
						sp.stop();
						sp.close();
						sp = null;
						run();
					}

				} catch(Throwable t){
					//log(t.toString());
				}
			}
		}.start();
	}

	public void pause(){
		try{
			if(sp!=null){
				sp.stop();
			}
		} catch(Throwable t){

		}
	}

	public boolean resume(){
		try{
			if(sp!=null){
				if(sp.getState()!=Player.STARTED){
					sp.start();
				}
			} else {
				play();
			}
		} catch(Throwable t){
			return false;
		}
		return true;
	}

	/** StreamingPlayerListener Implementation */
	public void bufferStatusChanged(long bufferStartsAt, long len) {
		this.bufferStartsAt = bufferStartsAt;
		this.len = len;		

		boolean update = (System.currentTimeMillis()-lastBufferUpdate)>25;
		if(!update)
			return;
		lastBufferUpdate = System.currentTimeMillis();		
	}

	public void downloadStatusUpdated(final long totalDownloaded) {		

	}

	public void feedPaused(final long available) {
		UiApplication.getUiApplication().invokeLater(new Runnable(){
			public void run(){
				_statusField.setText("Buffering...");
			}
		});

		boolean update = (System.currentTimeMillis()-lastBufferUpdate)>25;
		if(!update)
			return;
		lastBufferUpdate = System.currentTimeMillis();
	}

	public void feedRestarted(final long available) {
		UiApplication.getUiApplication().invokeLater(new Runnable(){
			public void run(){
				_statusField.setText("Reading...");
			}
		});

		boolean update = (System.currentTimeMillis()-lastBufferUpdate)>25;
		if(!update)
			return;
		lastBufferUpdate = System.currentTimeMillis();		
	}

	public void initialBufferCompleted(final long available) {
		UiApplication.getUiApplication().invokeLater(new Runnable(){
			public void run(){
				_statusField.setText("Reading...");
			}
		});

		boolean update = (System.currentTimeMillis()-lastBufferUpdate)>25;
		if(!update)
			return;
		lastBufferUpdate = System.currentTimeMillis();		
	}

	public void playerUpdate(String event, Object eventData) {
		if(event.equals(PlayerListener.END_OF_MEDIA)){
			pause();
		} else if(event.equalsIgnoreCase(PlayerListener.ERROR)){
			Dialog.inform(event+ ": " + eventData);
		}
	}

	public byte[] preprocessData(byte[] bytes, int off, int len) {	
		return null;		
	}

	public void nowReading(long now) {
		this.nowReading = now;

		boolean update = (System.currentTimeMillis()-lastBufferUpdate)>25;
		if(!update)
			return;
		lastBufferUpdate = System.currentTimeMillis();		
	}

	public void nowPlaying(long now){
		boolean update = (System.currentTimeMillis()-lastBufferUpdate)>25;
		if(!update)
			return;
		lastBufferUpdate = System.currentTimeMillis();
	}
	public void contentLengthUpdated(long contentLength){
		UiApplication.getUiApplication().invokeLater(new Runnable(){
			public void run(){
				_statusField.setText("radiomobile");
			}
		});
	}	

	public void streamingError(final int code){
		UiApplication.getUiApplication().invokeLater(new Runnable(){
			public void run(){
				switch(code){
				case StreamingPlayerListener.ERROR_DOWNLOADING:
					Status.show("An error occured while streaming.");
					break;
				case StreamingPlayerListener.ERROR_SEEKING:
					Status.show("Error seeking.");
					break;
				case StreamingPlayerListener.ERROR_OPENING_CONNECTION:
					Status.show("Error opening connection.");
					break;
				case StreamingPlayerListener.ERROR_PLAYING_MEDIA:
					Status.show("Error playing media- connection has been closed.");
					break;
				} 

			}
		});
	}

	public boolean keyChar(char key, int status, int time) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean keyDown(int keycode, int time) {
		if(volC!=null){
			int key = Keypad.key(keycode);
			if(key==Keypad.KEY_VOLUME_UP)
				volC.setLevel(volC.getLevel()+10);
			else if(key==Keypad.KEY_VOLUME_DOWN)
				volC.setLevel(volC.getLevel()-10);
		}
		return false;
	}

	public boolean keyRepeat(int keycode, int time) {
		if(volC!=null){
			int key = Keypad.key(keycode);
			if(key==Keypad.KEY_VOLUME_UP)
				volC.setLevel(volC.getLevel()+10);
			else if(key==Keypad.KEY_VOLUME_DOWN)
				volC.setLevel(volC.getLevel()-10);
		}
		return false;		
	}

	public boolean keyStatus(int keycode, int time) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean keyUp(int keycode, int time) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean onClose() {
		if(sp!=null){
			try {
				sp.close();
			} catch (Throwable e) {				
				return super.onClose();
			}
		}	
		return super.onClose();
	}

	protected boolean onSavePrompt() {
		return true;
	}

}
