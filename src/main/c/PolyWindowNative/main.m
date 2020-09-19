#include "main-objc.h"

@implementation PolyWindowContext

-(id)init:(void *)cppCtx {
    self = [super init];
    self.cppCtx = cppCtx;
    
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
    self.metalLayer.frame = [self.appDelegate getMainView].frame;
    [[self.appDelegate getMainView].layer addSublayer:self.metalLayer];
    
    MTLRenderPipelineDescriptor *desc = [[MTLRenderPipelineDescriptor alloc] init];
    [desc setVertexFunction:[self.library newFunctionWithName:@"vertex_function"]];
    [desc setFragmentFunction:[self.library newFunctionWithName:@"fragment_function"]];
    desc.colorAttachments[0].pixelFormat = MTLPixelFormatBGRA8Unorm;
    self.pipelineState = [self.device newRenderPipelineStateWithDescriptor:desc error:nil];
}

@end

//void init() {
//    currentCtx = [[NSMutableDictionary alloc] init];
//}

static void unused(char a, ...) {
    // NOOP, just to get rid of compiler warnings.
}

void newCtx(void *cppCtx) {
    PolyWindowContext *ctx = [[PolyWindowContext alloc] init:cppCtx];
    unused(0, ctx);
}

void draw(PolyWindowContext *ctx, int primitive, int nVertices, float vertexData[]) {
    id<MTLBuffer> vertexBuffer = [ctx.device newBufferWithBytes:vertexData length:nVertices options:MTLResourceOptionCPUCacheModeDefault];
    
    [ctx.renderEncoder setRenderPipelineState:ctx.pipelineState];
    [ctx.renderEncoder setVertexBuffer:vertexBuffer offset:0 atIndex:0];
    [ctx.renderEncoder drawPrimitives:primitive vertexStart:0 vertexCount:nVertices];
}
