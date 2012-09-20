package co.za.bryanallott.radiomobile.bb.lib.ui;

import net.rim.device.api.system.EncodedImage;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;

public class PlayPauseField extends Field {
	private EncodedImage _buttonPause;
	private EncodedImage _buttonPlay;
	private EncodedImage _defaultButton;
	private int _fieldWidth, _fieldHeight;
	private boolean _isPlaying = false;
	public PlayPauseField() {
		super(Field.FOCUSABLE | Field.FIELD_HCENTER | Field.FIELD_VCENTER);
		_buttonPlay = EncodedImage.getEncodedImageResource("media-playback-start.png");
		_buttonPause = EncodedImage.getEncodedImageResource("media-playback-pause.png");
		_defaultButton = _buttonPlay;
		_fieldWidth = _defaultButton.getScaledWidth();
		_fieldHeight = _defaultButton.getScaledHeight();
	}
	protected void paint(Graphics graphics) {
		if(null != _defaultButton) {
			final int imgHeight = _defaultButton.getScaledHeight();
			final int imgWidth = _defaultButton.getScaledWidth();
			graphics.drawImage((_fieldWidth - imgWidth)>>1
								, (_fieldHeight - imgHeight)>>1
								, imgWidth
								, imgHeight, _defaultButton, 0, 0, 0);
		}
	}
	public boolean isPlaying() {
		return _isPlaying;
	}
	protected void setReadyToPlay() {
		_defaultButton = _buttonPlay;
		_isPlaying = false;
		this.invalidate();
	}
	protected void setReadyToPause() {
		_defaultButton = _buttonPause;
		_isPlaying = true;
		this.invalidate();
	}
	protected void layout(int width, int height) {
		setExtent(_fieldWidth, _fieldHeight);
	}
	public int getPreferredWidth() {
		return _fieldWidth;
	}
	public int getPreferredHeight() {
		return _fieldHeight;
	}
	protected void drawFocus(Graphics graphics, boolean on) {
		graphics.setBackgroundColor(Color.WHITESMOKE);
		graphics.clear();
		graphics.setGlobalAlpha(130);
		super.drawFocus(graphics, on);
	}
}