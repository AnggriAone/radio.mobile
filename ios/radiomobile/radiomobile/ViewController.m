//
//  ViewController.m
//  radiomobile
//
//  Created by bryan allott on 2012/09/14.
//

#import "ViewController.h"
#import <AVFoundation/AVFoundation.h>
#include <AudioToolbox/AudioToolbox.h>
#import <QuartzCore/CoreAnimation.h>
#import <MediaPlayer/MediaPlayer.h>
#import <CFNetwork/CFNetwork.h>

//@interface ViewController ()

//@end

@implementation ViewController

@synthesize theAudio, theItem, url;

- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view, typically from a nib.
    volumeSlider.backgroundColor = [UIColor clearColor];
    MPVolumeView *volumeView = [[MPVolumeView alloc] initWithFrame:volumeSlider.bounds];
	[volumeSlider addSubview:volumeView];
	[volumeView sizeToFit];
	
	[self setButtonImageNamed:@"playbutton.png"];
    _playing = NO;
    
    
    AudioSessionInitialize(NULL, NULL, NULL, NULL);
    UInt32 sessionCategory = kAudioSessionCategory_MediaPlayback;
    OSStatus err = AudioSessionSetProperty(kAudioSessionProperty_AudioCategory,
                                           sizeof(sessionCategory),
                                           &sessionCategory);
    AudioSessionSetActive(TRUE);
    if (err) {
        NSLog(@"AudioSessionSetProperty kAudioSessionProperty_AudioCategory failed: %d", err);
    }
    /*
     UInt32 audioRouteOverride = kAudioSessionOverrideAudioRoute_Speaker;
     AudioSessionSetProperty (kAudioSessionProperty_OverrideAudioRoute,sizeof (audioRouteOverride),&audioRouteOverride);
     */
}

- (void)viewDidUnload
{
    [super viewDidUnload];
    // Release any retained subviews of the main view.
}
- (void)finalize
{
    if(theAudio && _playing) {
        [theAudio stop];
    }
    if(theItem) {
        [theItem removeObserver:self forKeyPath:@"status"];
    }
    [super finalize];
}
- (IBAction)playPause:(id)sender
{
    if (!_playing)
	{
		if(theAudio) {
            [theAudio play];
            _playing = YES;
            [self setButtonImageNamed:@"stopbutton.png"];
        } else {
            url = [[NSURL alloc] initWithString:@"http://cdn-routable.core25-40.ndstream.net:8080/;listen.mp3"];
            theItem = [AVPlayerItem playerItemWithURL:url];
            
            [theItem addObserver:self forKeyPath:@"status" options:0 context:nil];
            theAudio = [AVPlayer playerWithPlayerItem:theItem];
            //theAudio.delegate = self;
            
            [self setButtonImageNamed:@"loadingbutton.png"];
        }
	}
	else
	{
        [theAudio pause];
		_playing = NO;
        [self setButtonImageNamed:@"playbutton.png"];
	}
}
- (IBAction)showInfo:(id)sender
{
    [[UIApplication sharedApplication] openURL:[NSURL URLWithString: @"http://bryanallott.net/"]];
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    return (interfaceOrientation != UIInterfaceOrientationPortraitUpsideDown);
}

//streamer helpers
- (void)setButtonImageNamed:(NSString *)imageName
{
	currentImageName = imageName;
	UIImage *image = [UIImage imageNamed:imageName];
	
	[button.layer removeAllAnimations];
	[button setImage:image forState:0];
    
	if ([imageName isEqual:@"loadingbutton.png"]) {
		[self spinButton];
	}
}
- (void)animationDidStop:(CAAnimation *)theAnimation finished:(BOOL)finished
{
	if (finished)
	{
		[self spinButton];
	}
}
- (void)spinButton
{
	[CATransaction begin];
	[CATransaction setValue:(id)kCFBooleanTrue forKey:kCATransactionDisableActions];
	CGRect frame = [button frame];
	button.layer.anchorPoint = CGPointMake(0.5, 0.5);
	button.layer.position = CGPointMake(frame.origin.x + 0.5 * frame.size.width, frame.origin.y + 0.5 * frame.size.height);
	[CATransaction commit];
    
	[CATransaction begin];
	[CATransaction setValue:(id)kCFBooleanFalse forKey:kCATransactionDisableActions];
	[CATransaction setValue:[NSNumber numberWithFloat:2.0] forKey:kCATransactionAnimationDuration];
    
	CABasicAnimation *animation;
	animation = [CABasicAnimation animationWithKeyPath:@"transform.rotation.z"];
	animation.fromValue = [NSNumber numberWithFloat:0.0];
	animation.toValue = [NSNumber numberWithFloat:2 * M_PI];
	animation.timingFunction = [CAMediaTimingFunction functionWithName: kCAMediaTimingFunctionLinear];
	animation.delegate = self;
	[button.layer addAnimation:animation forKey:@"rotationAnimation"];
    
	[CATransaction commit];
}
- (void)observeValueForKeyPath:(NSString *)keyPath ofObject:(id)object
                        change:(NSDictionary *)change context:(void *)context {
    
    if (object == theItem && [keyPath isEqualToString:@"status"]) {
        if(theItem.status == AVPlayerStatusReadyToPlay) {
            [theAudio play];
            _playing = YES;
            [self setButtonImageNamed:@"stopbutton.png"];
            [theItem removeObserver:self forKeyPath:@"status"];
        }
        else if(theItem.status == AVPlayerStatusFailed) {
            UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"radiomobile" message:theItem.error.description delegate:self cancelButtonTitle:@"Ok" otherButtonTitles:nil];
            [alert show];
            [self setButtonImageNamed:@"playbutton.png"];
            _playing = NO;
        }
        else if(theItem.status == AVPlayerStatusUnknown) {
            NSLog(@"unknown");
        }
    }
}
@end
