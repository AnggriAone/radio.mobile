//
//  ViewController.h
//  radiomobile
//
//  Created by bryan allott on 2012/09/14.
//

#import <UIKit/UIKit.h>

@class AVAudioPlayer;
@class AVPlayerItem;

@interface ViewController : UIViewController<UITabBarDelegate> {
    
    IBOutlet UIButton   *button;
    IBOutlet UIView     *volumeSlider;
    
    NSString            *currentImageName;
    Boolean             _playing;
    
    AVPlayerItem        *theItem;
    AVAudioPlayer       *theAudio;
    NSURL               *url;
}

- (IBAction)showInfo:(id)sender;
- (IBAction)playPause:(id)sender;

@property (nonatomic, retain) AVAudioPlayer	*theAudio;
@property (nonatomic, retain) AVPlayerItem	*theItem;
@property (nonatomic, retain) NSURL         *url;
@end
