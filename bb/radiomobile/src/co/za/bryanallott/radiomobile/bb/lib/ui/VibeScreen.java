package co.za.bryanallott.radiomobile.bb.lib.ui;

import co.za.bryanallott.radiomobile.bb.AboutScreen;

import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Menu;
import net.rim.device.api.ui.container.MainScreen;

public class VibeScreen extends MainScreen {
	protected VibeScreen(long style) {
		super(style);
	}
	protected VibeScreen() {
		super(NO_VERTICAL_SCROLL);
	}
	protected void makeMenu(Menu menu, int instance) {
		menu.add(_aboutMenu);
		menu.add(MenuItem.separator(_aboutMenu.getOrdinal() + 1));
		
		super.makeMenu(menu,instance);
	}
	private MenuItem _aboutMenu = new MenuItem("About",150,10) {
		public void run() {
			UiApplication.getUiApplication().pushScreen(new AboutScreen());
		} 
	};
}
