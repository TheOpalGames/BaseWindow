#ifndef main_h
#define main_h

@import Metal;
@import QuartzCore;

#import <Cocoa/Cocoa.h>
#import "AppDelegate.h"

#define INIT_KEY @"polywindowEnvInit"

@interface PolyWindowContext:NSObject {
    
}

@property(nonatomic) id<MTLDevice> device;
@property(nonatomic) id<MTLLibrary> library;
@property(nonatomic) id<MTLCommandQueue> commands;
@property(nonatomic) CAMetalLayer *metalLayer;
@property(nonatomic) AppDelegate *appDelegate;
@property(nonatomic) void *cppCtx;
@property(nonatomic) id<MTLRenderPipelineState> pipelineState;

-(id) init:(void *)env;
-(void) postInit;

@end


//void init(void);
void newCtx(void *);

#endif /* main_h */
