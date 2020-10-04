#include "main-objc.h"

@import simd;

typedef struct {
    matrix_float4x4 matrix;
} Transformation;

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
    
    self.uniformBuffer = [self.device newBufferWithLength:sizeof(Transformation) options:MTLResourceOptionCPUCacheModeDefault];
    self.hasTransformations = false;
    
    self.showCursor = true;
    self.vsync = true;
    
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
    [ctx.renderEncoder setVertexBuffer:vertexBuffer offset:0 atIndex:1];
    [ctx.renderEncoder drawPrimitives:primitive vertexStart:0 vertexCount:nVertices];
}

static matrix_float4x4 toMatrix(double *matrix) {
    matrix_float4x4 res = {
        .columns[0] = {matrix[ 0], matrix[ 1], matrix[ 2], matrix[ 3]},
        .columns[1] = {matrix[ 4], matrix[ 5], matrix[ 6], matrix[ 7]},
        .columns[2] = {matrix[ 8], matrix[ 9], matrix[10], matrix[11]},
        .columns[3] = {matrix[12], matrix[13], matrix[14], matrix[15]}
    };
    return res;
}

static matrix_float4x4 multiplyMatrices(int nMatrices, double **matrices) {
    matrix_float4x4 finalMatrix = toMatrix(matrices[nMatrices-2]);
    
    for (int i = nMatrices-2; i >= 0; i++) {
        finalMatrix = simd_mul(toMatrix(matrices[i]), finalMatrix);
    }
    
    return finalMatrix;
}

void replaceMatrices(PolyWindowContext *ctx, int nMatrices, double **matrices) {
    if (nMatrices == 0) {
        ctx.hasTransformations = false;
        return;
    }
    
    matrix_float4x4 theMatrix = multiplyMatrices(nMatrices, matrices);
    
    Transformation transformation;
    transformation.matrix = theMatrix;
    memcpy([ctx.uniformBuffer contents], &transformation, sizeof(Transformation));
    
    ctx.hasTransformations = true;
}

void toggleShowCursor(PolyWindowContext *ctx) {
    if (ctx.showCursor)
        [NSCursor hide];
    else
        [NSCursor unhide];
    
    ctx.showCursor = !(ctx.showCursor);
}

void toggleVsync(PolyWindowContext *ctx) {
    [[ctx.appDelegate getViewController] disableTimers];
    
    if (ctx.vsync)
        [[ctx.appDelegate getViewController] enableVsync];
    else
        [[ctx.appDelegate getViewController] enableConstantRefresh];
}
