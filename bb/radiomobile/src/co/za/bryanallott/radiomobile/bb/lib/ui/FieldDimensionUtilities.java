package co.za.bryanallott.radiomobile.bb.lib.ui;

import net.rim.device.api.ui.decor.Border;
import net.rim.device.api.ui.Field;

public class FieldDimensionUtilities {
	private FieldDimensionUtilities() { }

	public static int getBorderWidth(Field field ) {
		int width = 0;

		Border border = field.getBorder();
		if(null != border) {
			width = border.getLeft() + border.getRight();
		}
		return width;
	}
	public static int getBorderHeight( Field field ) {
		int height = 0;

		Border border = field.getBorder();
		if(null != border) {
			height = border.getTop() + border.getBottom();
		}
		return height;
	}
	public static int getBorderAndPaddingWidth(Field field) {
		int width = 0;
		width = field.getPaddingLeft() + field.getPaddingRight();
		Border border = field.getBorder();
		if( border != null ) {
			width += border.getLeft() + border.getRight();
		}
		return width;
	}
	public static int getBorderAndPaddingHeight(Field field) {
		int height = 0;
		height = field.getPaddingTop() + field.getPaddingBottom();
		Border border = field.getBorder();
		if( border != null ) {
			height += border.getTop() + border.getBottom();
		}
		return height;
	}
}
