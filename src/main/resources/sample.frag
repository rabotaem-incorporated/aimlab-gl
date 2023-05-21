#version 420 core

out vec4 FragColor;
// in vec3 vertexColor;
in vec2 TexCoord;

// uniform vec3 colorShift;

uniform sampler2D aTexture;

void main()
{
    // FragColor = mix(texture(texture1, TexCoord), texture(texture2, TexCoord), 0.2);
    FragColor = texture(aTexture, TexCoord);
}
