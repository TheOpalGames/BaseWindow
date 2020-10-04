#import "AppDelegate.h"
#import "ViewController.h"
#import "main-objc.h"
#import "jni-interface-objc.h"

@interface AppDelegate ()

@property (weak) IBOutlet NSWindow *window;
@property (weak) IBOutlet NSView *mainView;
@property (weak) IBOutlet ViewController *viewController;

@property PolyWindowContext *ctx;

@end

@implementation AppDelegate

- (void) applicationDidFinishLaunching:(NSNotification *)aNotification {
    self.ctx = (PolyWindowContext *) [[NSThread currentThread] threadDictionary][INIT_KEY];
    [[NSThread currentThread] threadDictionary][INIT_KEY] = nil;
    
    self.ctx.appDelegate = self;
    [self.viewController setContext:(__bridge void *)(self.ctx)];
    
    [self.ctx postInit];
}


- (void)applicationWillTerminate:(NSNotification *)aNotification {
    callbackFunction(self.ctx.cppCtx, "appClosed");
}

- (NSWindow *) getWindow {
    return self.window;
}

- (NSView *) getMainView {
    return self.mainView;
}

-(NSViewController *) getViewController {
    return self.viewController;
}

@end
