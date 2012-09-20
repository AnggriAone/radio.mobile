package co.za.bryanallott.radiomobile.bb.lib.ui;

import net.rim.device.api.ui.Manager;

public class HorizontalSpacer extends Manager {
	private final int _managerWidth, _managerHeight;
	public HorizontalSpacer(final int spaceWidth, final int height, final long style) {
		super(Manager.NO_HORIZONTAL_SCROLL | Manager.NO_VERTICAL_SCROLL | Manager.NON_FOCUSABLE | style);
		_managerWidth = spaceWidth;
		_managerHeight = height;
	}
	public HorizontalSpacer(final int spaceWidth, final int height) {
		super(Manager.NO_HORIZONTAL_SCROLL | Manager.NO_VERTICAL_SCROLL | Manager.NON_FOCUSABLE);
		_managerWidth = spaceWidth;
		_managerHeight = height;
	}
	protected void sublayout(int width, int height) {
		setExtent(_managerWidth, _managerHeight);
	}
	public int getPreferredWidth() {
		return _managerWidth;
	}
	public int getPreferredHeight() {
		return _managerHeight;
	}
}