#import "jni-interface-objc.h"
#import "ViewController.h"

@interface ViewController()

@property(nonatomic) CVDisplayLinkRef timer;

//-(CVReturn) drawFrame:(CVDisplayLinkRef) timer (const CVTimeStamp *) inNow, (const CVTimeStamp *) inOutputTime, (CVOptionFlags) flagsIn, (CVOptionFlags *) flagsOut;

-(void) drawNextFrame;

@end

CVReturn drawNextFrame(CVDisplayLinkRef displayLink, const CVTimeStamp *inNow, const CVTimeStamp *inOutputTime, CVOptionFlags flagsIn, CVOptionFlags *flagsOut, void *displayLinkContext) {
    
    [((__bridge ViewController *) displayLinkContext) drawNextFrame];
    return kCVReturnSuccess;
}

@implementation ViewController


-(void) viewDidLoad {
    [super viewDidLoad];
    
    // self.timer = [CADisplayLink displayLinkWithTarget:self selector:@selector(drawFrame)];
    
    CVDisplayLinkRef timer;
    
    if (CVDisplayLinkCreateWithCGDisplay(CGMainDisplayID(), &timer)) { // error
        // TODO: error handling
    }
    
    self.timer = timer;
    CVDisplayLinkSetOutputCallback(timer, drawNextFrame, (__bridge void *) self);
    CVDisplayLinkStart(self.timer);
}

-(void) drawNextFrame {
    drawFrame(self.ctx.env);
}

-(void) dealloc {
    CVDisplayLinkStop(self.timer);
}

@end
