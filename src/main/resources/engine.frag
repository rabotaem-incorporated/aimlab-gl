#version 420 core

in vec2 TexCoord;

out vec4 FragColor;

uniform int mode;
uniform vec3 solidColor;
uniform sampler2D texture1;

void main() {
    if (mode == 0) {
        FragColor = vec4(solidColor, 1.0);
    } else if (mode == 1) {
        FragColor = texture(texture1, TexCoord);
    }
}
