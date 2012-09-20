package co.za.bryanallott.radiomobile.bb.lib.ui;

import co.za.bryanallott.radiomobile.bb.lib.ImageHelper;

import net.rim.device.api.system.EncodedImage;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;

public abstract class ImageField extends Field {
	private EncodedImage _defaultButton;
	private int _fieldWidth, _fieldHeight;
	public ImageField(String resourceName, int maxWidth) {
		super(Field.NON_FOCUSABLE | Field.FIELD_HCENTER | Field.FIELD_VCENTER);
		_defaultButton = ImageHelper.scaleImageToFitWidth(EncodedImage.getEncodedImageResource(resourceName)
				, maxWidth);
		_fieldWidth = _defaultButton.getScaledWidth();
		_fieldHeight = _defaultButton.getScaledHeight();
	}
	protected void paint(Graphics graphics) {
		graphics.drawImage(0
							, 0
							, _fieldWidth, _fieldHeight, _defaultButton, 0, 0, 0);
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
		// TODO Auto-generated method stub
		//super.drawFocus(graphics, on);
	}
	public void SetImage(String resourceName) {
		_defaultButton = EncodedImage.getEncodedImageResource(resourceName);
		_fieldWidth = _defaultButton.getScaledWidth();
		_fieldHeight = _defaultButton.getScaledHeight();
		super.invalidate();
	}
}