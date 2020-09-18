#import <Cocoa/Cocoa.h>

@interface AppDelegate : NSObject <NSApplicationDelegate>

- (void) appplicationDidFinishLaunching:(NSNotification *)aNotification;
- (void) applicationWillTerminate:(NSNotification *)aNotification;

- (NSWindow *) getWindow;
- (NSView *) getMainView;

@end

