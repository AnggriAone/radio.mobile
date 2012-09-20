package co.za.bryanallott.radiomobile.bb;

import co.za.bryanallott.radiomobile.bb.lib.ui.VibeScreen;

import net.rim.blackberry.api.browser.Browser;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.RichTextField;
import net.rim.device.api.ui.container.VerticalFieldManager;


public class AboutScreen extends VibeScreen {

	private static class WhiteRichText extends RichTextField {
		public WhiteRichText(String text) {
			super(text, RichTextField.USE_ALL_WIDTH);
		}
		protected void paint(Graphics graphics) {
			graphics.setColor(Color.WHITESMOKE);
			super.paint(graphics);
		}
	}
	private static class WhiteLabel extends LabelField {
		public WhiteLabel(String text, long style) {
			super(text, style);
		}
		protected void paint(Graphics graphics) {
			graphics.setColor(Color.WHITE);
			super.paint(graphics);
		}
	}
	public AboutScreen() {
		super(VERTICAL_SCROLL);
		VerticalFieldManager custom = new VerticalFieldManager(USE_ALL_HEIGHT | USE_ALL_WIDTH) {
			protected void paintBackground(Graphics graphics) {
				graphics.setBackgroundColor(Color.RED);
				graphics.clear();
			}
		};
		Font defaultFont = Font.getDefault();
		WhiteRichText title = new WhiteRichText("Welcome to radiomobile Blackberry app!");
		title.setFont(defaultFont.derive(Font.BOLD, defaultFont.getHeight()-2));
		custom.add(title);
		
		custom.add(new WhiteLabel("\nVisit bryanallott.net", Field.FOCUSABLE) {
			protected boolean navigationClick(int status, int time) {
				Browser.getDefaultSession().displayPage("http://bryanallott.net/");
				return true;
			}
		});

		WhiteRichText disclaimer = new WhiteRichText("\nPlease note that although low on consumption, this app may incur data charges and you are advised to have a data bundle in place with your network.\n");
		disclaimer.setFont(defaultFont.derive(Font.ITALIC, defaultFont.getHeight()-2));
		custom.add(disclaimer);
		add(custom);
	}
}
