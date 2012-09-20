package co.za.bryanallott.radiomobile.bb.lib.ui;

import co.za.bryanallott.radiomobile.bb.lib.ImageHelper;

import net.rim.device.api.system.Display;
import net.rim.device.api.system.EncodedImage;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.container.VerticalFieldManager;

public class CustomBackground extends VerticalFieldManager {
	private final EncodedImage _background;
	private final int _imageWidth;
	private final int _imageHeight;
	public CustomBackground() {
		super(VerticalFieldManager.USE_ALL_HEIGHT | VerticalFieldManager.USE_ALL_HEIGHT);
		EncodedImage tmp = EncodedImage.getEncodedImageResource("splash.png");
        _background = ImageHelper.scaleImageToFitWidth(tmp, Display.getWidth());
        _imageWidth = _background.getScaledWidth();
		_imageHeight = _background.getScaledHeight();
	}
	public void paint(Graphics graphics) {
		graphics.drawImage(0, 0, _imageWidth, _imageHeight, _background, 0, 0, 0);
		super.paint(graphics);
	}
	public int getPreferredWidth() {
		return _imageWidth;
	}
	public int getPreferredHeight() {
		return _imageHeight;
	}
}
