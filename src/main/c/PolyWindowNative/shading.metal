#include <metal_stdlib>
#include <metal_texture>
#include <metal_common>

//#ifndef NULL
//#define NULL 0
//#endif
//
//#define isNull(thing) (long(thing) == NULL)

#ifndef AAPLTextureIndexBaseColor
#define AAPLTextureIndexBaseColor 0
#endif

using namespace metal;

constexpr sampler textureSampler(mag_filter::linear, min_filter::linear);

typedef struct {
    float4x4 matrix;
} Transformation;

typedef struct {
    float4 position;
    float4 color;
} VertexIn;

typedef struct {
    float4 position [[position]];
    half4 color;
} VertexOut;

vertex VertexOut vertex_main(device VertexIn *vertices [[buffer(1)]], constant Transformation &transformation [[buffer(0)]], uint index [[vertex_id]]) {
    VertexOut ret;
    ret.position = transformation.matrix * vertices[index].position; // with GPU efficiency
    ret.color = half4(vertices[index].color);
    return ret;
}

fragment half4 fragment_main(VertexOut out [[stage_in]], texture2d<half> texture [[texture(AAPLTextureIndexBaseColor)]]) {
    if (is_null_texture(texture))
        return out.color;
    
    return texture.sample(textureSampler, float2(out.position.x, out.position.y));
}
