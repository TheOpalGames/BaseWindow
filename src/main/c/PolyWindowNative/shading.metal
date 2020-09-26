#include <metal_stdlib>
using namespace metal;


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

vertex VertexOut vertex_function(device VertexIn *vertices [[buffer(0)]], constant Transformation &transformation [[buffer(1)]], uint index [[vertex_id]]) {
    VertexOut ret;
    ret.position = transformation.matrix * vertices[index].position; // with GPU efficiency
    ret.color = half4(vertices[index].color);
    return ret;
}

fragment half4 fragment_function(VertexOut out [[stage_in]]) {
    return out.color;
}

