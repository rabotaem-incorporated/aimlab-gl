#version 420 core

layout (location = 0) in vec3 aPos;
layout (location = 1) in vec3 aNormal;
layout (location = 2) in vec2 aTexCoord;

// uniform int mode;

uniform mat4 projectionView;
uniform mat4 transform;

uniform vec3 lightDir;
uniform vec3 viewPos;

out vec2 TexCoord;
out vec3 Normal;
out vec3 FragPos;

void main() {
    gl_Position = projectionView * transform * vec4(aPos, 1.0);
    TexCoord = aTexCoord;
    FragPos = vec3(transform * vec4(aPos, 1.0));
    Normal = mat3(transpose(inverse(transform))) * aNormal;
}
