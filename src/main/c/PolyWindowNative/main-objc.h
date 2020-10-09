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
@property(nonatomic) ViewController *viewController;
@property(nonatomic) void *cppCtx;
@property(nonatomic) id<MTLRenderPipelineState> pipelineState;
@property(nonatomic) id<MTLRenderCommandEncoder> renderEncoder;

@property(nonatomic) id<MTLBuffer> uniformBuffer;
@property(nonatomic) bool hasTransformations;

@property(nonatomic) bool showCursor;
@property(nonatomic) bool vsync;

-(id) init:(void *)cppCtx;
-(void) postInit;

@end


//void init(void);
void newCtx(void *);
void draw(PolyWindowContext *ctx, int primitive, int nVertices, float vertexData[]);
void replaceMatrices(PolyWindowContext *ctx, int nMatrices, double **matrices);

#endif /* main_h */
