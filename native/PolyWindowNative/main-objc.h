#ifndef main_h
#define main_h

@import Metal;

@interface PolyWindowContext:NSObject {
    
}

@property(nonatomic) id<MTLDevice> device;
@property(nonatomic) id<MTLLibrary> library;
@property(nonatomic) id<MTLCommandQueue> commands;
@property(nonatomic) MTLPrimitiveType *primitives;

@end



PolyWindowContext *init(void);

#endif /* main_h */
