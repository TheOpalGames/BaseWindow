#include "main-objc.h"

#ifndef AAPLTextureIndexBaseColor
#define AAPLTextureIndexBaseColor 0
#endif

#include <stdarg.h>

typedef struct {
    bool skipTransformations;
} ShaderOptions;

@implementation PolyWindowContext

-(id)init:(void *)cppCtx {
    self = [super init];
    
    [self retain]; // mem addr stored in Java.
    self.cppCtx = cppCtx;
    
    return self;
}

-(void) postInit {
    [self.viewController setContext:self];
    
    self.device = MTLCreateSystemDefaultDevice();
    self.library = [self.device newDefaultLibrary];
    self.commands = [self.device newCommandQueue];
    
    self.metalLayer = [CAMetalLayer layer];
    self.metalLayer.device = self.device;
    self.metalLayer.pixelFormat = MTLPixelFormatBGRA8Unorm;
    self.metalLayer.frame = [[self.viewController view] frame];
    [[self.viewController view].layer addSublayer:self.metalLayer];
    
    self.uniformBuffer = [self.device newBufferWithLength:sizeof(Transformation) options:MTLResourceOptionCPUCacheModeDefault];
    
    setTransformation(self, false);
    
    self.optionsBuffer = [self.device newBufferWithLength:sizeof(ShaderOptions) options:MTLResourceOptionCPUCacheModeDefault];
    
    self.showCursor = true;
    self.vsync = true;
    
    MTLRenderPipelineDescriptor *desc = [[MTLRenderPipelineDescriptor alloc] init];
    [desc setVertexFunction:[self.library newFunctionWithName:@"vertex_main"]];
    [desc setFragmentFunction:[self.library newFunctionWithName:@"fragment_main"]];
    desc.colorAttachments[0].pixelFormat = MTLPixelFormatBGRA8Unorm;
    self.pipelineState = [self.device newRenderPipelineStateWithDescriptor:desc error:nil];
    
}

@end

//void init() {
//    currentCtx = [[NSMutableDictionary alloc] init];
//}

//static void unused(char a, ...) {
//    // NOOP, just to get rid of compiler warnings.
//}

void createApp(void *cppCtx) {
    PolyWindowContext *ctx = [[PolyWindowContext alloc] init:cppCtx];
    [[NSThread currentThread] threadDictionary][INIT_KEY] = ctx;
    
    const char *argv[0];
    NSApplicationMain(0, argv);
}

void draw(PolyWindowContext *ctx, int primitive, int nVertices, float vertexData[], bool skipTransformations) {
    id<MTLBuffer> vertexBuffer = [ctx.device newBufferWithBytes:vertexData length:nVertices options:MTLResourceOptionCPUCacheModeDefault];
    
    ShaderOptions options;
    options.skipTransformations = skipTransformations;
    memcpy([ctx.optionsBuffer contents], &options, sizeof(ShaderOptions));
    
    [ctx.renderEncoder setRenderPipelineState:ctx.pipelineState];
    [ctx.renderEncoder setVertexBuffer:vertexBuffer offset:0 atIndex:1];
    [ctx.renderEncoder drawPrimitives:primitive vertexStart:0 vertexCount:nVertices];
}

static matrix_float4x4 toMatrix(float *matrix) {
    matrix_float4x4 res = {
        .columns[0] = {matrix[ 0], matrix[ 1], matrix[ 2], matrix[ 3]},
        .columns[1] = {matrix[ 4], matrix[ 5], matrix[ 6], matrix[ 7]},
        .columns[2] = {matrix[ 8], matrix[ 9], matrix[10], matrix[11]},
        .columns[3] = {matrix[12], matrix[13], matrix[14], matrix[15]}
    };
    return res;
}

static matrix_float4x4 transform(int nMatrices, float **matrices) { // for hungry lumas only
    matrix_float4x4 finalMatrix = toMatrix(matrices[nMatrices-2]);
    
    for (int i = nMatrices-2; i >= 0; i++) {
        finalMatrix = simd_mul(toMatrix(matrices[i]), finalMatrix);
    }
    
    return finalMatrix;
}

static matrix_float4x4 multiplyMatrices(int nMatrices, float **matrices) {
    matrix_float4x4 finalMatrix = toMatrix(matrices[nMatrices-2]);
    
    for (int i = nMatrices-2; i >= 0; i++) {
        finalMatrix = simd_mul(toMatrix(matrices[i]), finalMatrix);
    }
    
    return finalMatrix;
}

float *transformVector(PolyWindowContext *ctx, float nonVector[4]) {
    simd_float4 vector = {
        nonVector[0], nonVector[1], nonVector[2], nonVector[3]
    };
    
    simd_float4 res = simd_mul(ctx.transformation->matrix, vector);
    
    float *nonVecRes = malloc(sizeof(float)*4);
    nonVecRes[0] = res[0];
    nonVecRes[1] = res[1];
    nonVecRes[2] = res[2];
    nonVecRes[3] = res[3];
    
    return nonVecRes;
}

void setTransformation(PolyWindowContext *ctx, bool hasTransformations, ...) {
    Transformation transformation;
    transformation.hasTransformations = hasTransformations;
    
    if (hasTransformations) {
        va_list args;
        va_start(args, hasTransformations);
        
        transformation.matrix = va_arg(args, matrix_float4x4);
        
        va_end(args);
    }
    
    memcpy([ctx.uniformBuffer contents], &transformation, sizeof(Transformation));
}

void replaceMatrices(PolyWindowContext *ctx, int nMatrices, float **matrices) {
    if (nMatrices == 0)
        return setTransformation(ctx, false);
    
    matrix_float4x4 theMatrix = multiplyMatrices(nMatrices, matrices);
    
    setTransformation(ctx, true, theMatrix);
}

void toggleShowCursor(PolyWindowContext *ctx) {
    if (ctx.showCursor)
        [NSCursor hide];
    else
        [NSCursor unhide];
    
    ctx.showCursor = !(ctx.showCursor);
}

void toggleVsync(PolyWindowContext *ctx) {
    [ctx.viewController disableTimers];
    
    if (ctx.vsync)
        [ctx.viewController enableVsync];
    else
        [ctx.viewController enableConstantRefresh];
    
    ctx.vsync = !(ctx.vsync);
}

Texture *loadTexture(PolyWindowContext *ctx, char *bytes, int width, int height) {
    MTLTextureDescriptor *desc = [[MTLTextureDescriptor alloc] init];
    desc.pixelFormat = MTLPixelFormatBGRA8Unorm;
    desc.width = width;
    desc.height = height;
    
    id<MTLTexture> texture = [ctx.device newTextureWithDescriptor:desc];
    
    Texture *textureData = malloc(sizeof(Texture));
    textureData->texture = texture;
    textureData->width = width;
    textureData->height = height;
    textureData->bytes = bytes;
    
    [textureData->texture retain]; // held by Java
    
    return textureData;
}

void setTexture(PolyWindowContext *ctx, Texture *texture, float originX, float originY, float sizeX, float sizeY) {
    if (texture != NULL) {
        MTLRegion region = {
            {originX, originY, 0},
            {texture->width, texture->height, 1}
        };
        
        [texture->texture replaceRegion:region mipmapLevel:0 withBytes:texture->bytes bytesPerRow:(4*texture->width)];
    }
    
    [ctx.renderEncoder setFragmentTexture:texture->texture atIndex:AAPLTextureIndexBaseColor];
}

char *getClipboard() {
    NSPasteboard *clipboard = [NSPasteboard generalPasteboard];
    NSData *data = [clipboard dataForType:NSPasteboardTypeString];
    return (char *) [data bytes];
}

void setClipboard(char *text) {
    NSPasteboard *clipboard = [NSPasteboard generalPasteboard];
    NSString *nsText = [[NSString alloc] initWithUTF8String:text];
    [clipboard setString:nsText forType:NSPasteboardTypeString];
}

PolyWindowContext *newWindow(PolyWindowContext *ctx, void *cppCtx) {
    ViewController *viewController = [[ViewController alloc] init];
    PolyWindowContext *newCtx = [[PolyWindowContext alloc] init:cppCtx];
    [viewController setContext:newCtx];
    
    [NSWindow windowWithContentViewController:viewController];
    [newCtx postInit];
    
    return newCtx;
}
