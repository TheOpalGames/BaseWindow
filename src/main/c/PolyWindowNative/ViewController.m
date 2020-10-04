#import "jni-interface-objc.h"
#import "ViewController.h"

@interface ViewController()

@property(nonatomic) CVDisplayLinkRef vsyncTimer;
@property(nonatomic) NSTimer *constantRefreshTimer;

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
    [self enableVsync];
}

-(void) enableVsync {
    CVDisplayLinkRef timer;
    
    if (CVDisplayLinkCreateWithCGDisplay(CGMainDisplayID(), &timer)) { // error
        // TODO: error handling
    }
    
    self.vsyncTimer = timer;
    CVDisplayLinkSetOutputCallback(timer, drawNextFrame, (__bridge void *) self);
    CVDisplayLinkStart(timer);
}

-(void) enableConstantRefresh {
    [NSThread detachNewThreadSelector:@selector(constantRefreshThread:) toTarget:self withObject:nil];
}

-(void) constantRefreshThread:(id) unused {
    self.constantRefreshTimer = [NSTimer scheduledTimerWithTimeInterval:0.166666666666666666666666 target:self selector:@selector(constantRefreshFrame:) userInfo:nil repeats:true];
}

-(void) constantRefreshFrame:(id) unused {
    [self drawNextFrame];
}

-(void) disableTimers {
    if (self.vsyncTimer != nil) {
        CVDisplayLinkStop(self.vsyncTimer);
        self.vsyncTimer = nil;
    }
    
    if (self.constantRefreshTimer != nil) {
        [self.constantRefreshTimer invalidate];
        self.constantRefreshTimer = nil;
    }
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
    [renderEncoder setVertexBuffer:self.ctx.uniformBuffer offset:0 atIndex:1];
    
    callbackFunction(self.ctx.cppCtx, "drawFrame");
    
    [renderEncoder endEncoding];
    
    [commands presentDrawable:drawable];
    [commands commit];
}

-(void) dealloc {
    [self disableTimers];
}

-(void) keyDown:(NSEvent *) event {
    unichar c = [[event charactersIgnoringModifiers] characterAtIndex:0];
    charEvent(self.ctx.cppCtx, "keyDown", c);
}

-(void) keyUp:(NSEvent *) event {
    unichar c = [[event charactersIgnoringModifiers] characterAtIndex:0];
    charEvent(self.ctx.cppCtx, "keyUp", c);
}

-(void) mouseDown:(NSEvent *)event {
    callbackFunction(self.ctx.cppCtx, "mouseDown");
}

-(void) mouseUp:(NSEvent *)event {
    callbackFunction(self.ctx.cppCtx, "mouseUp");
}

-(void) mouseMoved:(NSEvent *)event {
    mouseMove(self.ctx.cppCtx, event.deltaX, event.deltaY);
}

@end
