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
    id<CAMetalDrawable> drawable = [self.ctx.metalLayer nextDrawable];
    
    MTLRenderPassDescriptor *desc = [MTLRenderPassDescriptor renderPassDescriptor];
    desc.colorAttachments[0].texture = drawable.texture;
    desc.colorAttachments[0].loadAction = MTLLoadActionClear;
    desc.colorAttachments[0].clearColor = MTLClearColorMake(1, 1, 1, 1);
    desc.colorAttachments[0].storeAction = MTLStoreActionStore;
    
    id<MTLCommandBuffer> commands = [self.ctx.commands commandBuffer];
    id<MTLRenderCommandEncoder> renderEncoder = [commands renderCommandEncoderWithDescriptor:desc];
    self.ctx.renderEncoder = renderEncoder;
    
    callbackFunction(self.ctx.cppCtx, "drawFrame");
    
    [renderEncoder endEncoding];
    
    [commands presentDrawable:drawable];
    [commands commit];
}

-(void) dealloc {
    CVDisplayLinkStop(self.timer);
}

@end
