#include "main-objc.h"

@implementation PolyWindowContext

-(id)init {
    self = [super init];
    
    [[NSThread currentThread] threadDictionary][INIT_KEY] = self;
    
    const char *argv[0];
    NSApplicationMain(0, argv);
    
    return self;
}

-(void) postInit {
    self.device = MTLCreateSystemDefaultDevice();
    self.library = [self.device newDefaultLibrary];
    self.commands = [self.device newCommandQueue];
    
    self.metalLayer = [CAMetalLayer layer];
    self.metalLayer.device = self.device;
    self.metalLayer.pixelFormat = MTLPixelFormatBGRA8Unorm;
}

@end

//void init() {
//    currentCtx = [[NSMutableDictionary alloc] init];
//}

static void unused(char a, ...) {
    // NOOP, just to get rid of compiler warnings.
}

void newCtx(void *env) {
    PolyWindowContext *ctx = [[PolyWindowContext alloc] init];
    unused(0, ctx);
}

void draw(PolyWindowContext *ctx, int primitive, float vertexData[]) {
    id<MTLCommandBuffer> buf = [ctx.commands commandBuffer];

    MTLRenderPassDescriptor *desc = [MTLRenderPassDescriptor renderPassDescriptor];
    desc.texture = buf.texture;
}
