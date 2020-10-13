#ifndef main_h
#define main_h

@import Metal;
@import QuartzCore;

#import <Cocoa/Cocoa.h>
#import "AppDelegate.h"

#define INIT_KEY @"polywindowEnvInit"

@interface PolyWindowContext:NSObject {
    
}

@property(nonatomic, strong) id<MTLDevice> device;
@property(nonatomic, strong) id<MTLLibrary> library;
@property(nonatomic, strong) id<MTLCommandQueue> commands;
@property(nonatomic, strong) CAMetalLayer *metalLayer;
@property(nonatomic, strong) AppDelegate *appDelegate;
@property(nonatomic, strong) ViewController *viewController;
@property(nonatomic) void *cppCtx;
@property(nonatomic, strong) id<MTLRenderPipelineState> pipelineState;
@property(nonatomic, strong) id<MTLRenderCommandEncoder> renderEncoder;

@property(nonatomic, strong) id<MTLBuffer> uniformBuffer;
@property(nonatomic) bool hasTransformations;

@property(nonatomic) bool showCursor;
@property(nonatomic) bool vsync;

-(id) init:(void *)cppCtx;
-(void) postInit;

@end

typedef struct {
    id<MTLTexture> texture;
    int width;
    int height;
    char *bytes;
} Texture;


//void init(void);
void createApp(void *);
void draw(PolyWindowContext *ctx, int primitive, int nVertices, float vertexData[]);
void replaceMatrices(PolyWindowContext *ctx, int nMatrices, double **matrices);

void toggleShowCursor(PolyWindowContext *ctx);
void toggleVsync(PolyWindowContext *ctx);

Texture *loadTexture(PolyWindowContext *ctx, char *bytes, int width, int height);
void setTexture(PolyWindowContext *ctx, Texture *texture, float originX, float originY, float sizeX, float sizeY);

char *getClipboard(void);
void setClipboard(char *clipboard);

PolyWindowContext *newWindow(PolyWindowContext *ctx, void *cppCtx);

#endif /* main_h */
