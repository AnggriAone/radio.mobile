package co.za.bryanallott.radiomobile.bb.lib;

import net.rim.device.api.math.Fixed32;
import net.rim.device.api.system.EncodedImage;

public class ImageHelper {
	public static EncodedImage scaleImageToFitHeightNoPreserve(final EncodedImage source, final int requiredHeight) {
        int currentHeightFixed32 = Fixed32.toFP(source.getHeight());
    	int requiredHeightFixed32 = Fixed32.toFP(requiredHeight);
    	
    	int currentWidthFixed32 = Fixed32.toFP(source.getWidth());
    	int requiredWidthFixed32 = currentWidthFixed32;
    	int scaleXFixed32 = Fixed32.div(currentWidthFixed32, requiredWidthFixed32);
    	
    	int scaleYFixed32 = Fixed32.div(currentHeightFixed32, requiredHeightFixed32);
		return source.scaleImage32(scaleXFixed32, scaleYFixed32);
	}
	
	public static EncodedImage scaleImageToFitHeight(final EncodedImage source, final int requiredHeight) {
        int currentHeightFixed32 = Fixed32.toFP(source.getHeight());
    	int requiredHeightFixed32 = Fixed32.toFP(requiredHeight);
    	int scaleYFixed32 = Fixed32.div(currentHeightFixed32, requiredHeightFixed32);
		return source.scaleImage32(scaleYFixed32, scaleYFixed32);
	}
	public static EncodedImage scaleImageToFitWidth(final EncodedImage source, final int requiredWidth) {
		int currentWidthFixed32 = Fixed32.toFP(source.getWidth());
        int requiredWidthFixed32 = Fixed32.toFP(requiredWidth);
        int scaleXFixed32 = Fixed32.div(currentWidthFixed32, requiredWidthFixed32);
		return source.scaleImage32(scaleXFixed32, scaleXFixed32);
	}
	public static EncodedImage scaleImage(final EncodedImage source, final int requiredWidth, final int requiredHeight) {
		int currentWidthFixed32 = Fixed32.toFP(source.getWidth());
        int requiredWidthFixed32 = Fixed32.toFP(requiredWidth);
        int scaleXFixed32 = Fixed32.div(currentWidthFixed32, requiredWidthFixed32);
        
        int scaleYFixed32 = 0;
        if(-1 == requiredHeight) {
        	scaleYFixed32 = scaleXFixed32;
        } else {
        	int currentHeightFixed32 = Fixed32.toFP(source.getHeight());
        	int requiredHeightFixed32 = Fixed32.toFP(requiredHeight);
        	scaleYFixed32 = Fixed32.div(currentHeightFixed32, requiredHeightFixed32);
        }
		return source.scaleImage32(scaleXFixed32, scaleYFixed32);
	}
}
