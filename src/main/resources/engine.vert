#version 420 core

layout (location = 0) in vec3 aPos;
layout (location = 1) in vec3 aNormal;
layout (location = 2) in vec2 aTexCoord;

// uniform int mode;

uniform mat4 projectionView;
uniform mat4 transform;

out vec2 TexCoord;

void main() {
    gl_Position = projectionView * transform * vec4(aPos, 1.0);
    TexCoord = aTexCoord;
}
