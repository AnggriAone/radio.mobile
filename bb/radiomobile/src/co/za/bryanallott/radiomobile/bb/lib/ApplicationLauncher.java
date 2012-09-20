package co.za.bryanallott.radiomobile.bb.lib;

import net.rim.blackberry.api.browser.Browser;
import net.rim.blackberry.api.browser.BrowserSession;

public class ApplicationLauncher {
	
	public static void navigateToSite(final String url) {
		BrowserSession session = Browser.getDefaultSession();
		session.displayPage(url);
	}
}
