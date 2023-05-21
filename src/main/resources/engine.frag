#version 420 core

in vec2 TexCoord;
in vec3 Normal;
in vec3 FragPos;

out vec4 FragColor;

uniform int mode;
uniform vec3 solidColor;
uniform sampler2D texture1;

uniform vec3 lightDir;
uniform vec3 viewPos;

uniform vec3 ambientLight;
uniform vec3 diffuseLight;
uniform vec3 specularLight;
uniform float shininess;

void main() {
    if (mode == 0) {
        FragColor = vec4(solidColor, 1.0);
    } else if (mode == 1) {
        FragColor = vec4(texture(texture1, TexCoord).xyz, 1.0);
    } else if (mode == 2) {
        vec3 normal = normalize(Normal);

        float diff = max(dot(normal, lightDir), 0.0);
        vec3 diffuse = diff * diffuseLight;

        vec3 viewDir = normalize(viewPos - FragPos);
        vec3 reflectDir = reflect(-lightDir, normal);
        float spec = pow(max(dot(viewDir, reflectDir), 0.0), shininess);
        vec3 specular = specularLight * spec;

        vec3 light = ambientLight + diffuse + specular;

        FragColor = vec4(solidColor * light, 1.0);
    } else if (mode == 3) {
        vec3 normal = normalize(Normal);

        float diff = max(dot(normal, lightDir), 0.0);
        vec3 diffuse = diff * diffuseLight;

        vec3 viewDir = normalize(viewPos - FragPos);
        vec3 reflectDir = reflect(-lightDir, normal);
        float spec = pow(max(dot(viewDir, reflectDir), 0.0), shininess);
        vec3 specular = specularLight * spec;

        vec3 light = ambientLight + diffuse + specular;

        FragColor = vec4(texture(texture1, TexCoord).xyz * light, 1.0);
    }
}
