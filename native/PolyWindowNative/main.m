#include "main-objc.h"

@implementation PolyWindowContext

-(id)init {
    self = [super init];
    
    self.device = MTLCreateSystemDefaultDevice();
    self.library = [self.device newDefaultLibrary];
    self.commands = [self.device newCommandQueue];
    
    return self;
}

@end

PolyWindowContext *init() {
    return [[PolyWindowContext alloc] init];
}

void draw(PolyWindowContext *ctx, int mode, float vertexData[]) {
    id<MTLCommandBuffer> buf = [ctx.commands commandBuffer];
    // more TODO
}
