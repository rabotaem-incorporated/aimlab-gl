package engine

import glm_.vec3.Vec3
import graphics.ShaderProgram
import graphics.Texture

abstract class Material {
    abstract fun bind(shaderProgram: ShaderProgram)
}

class SolidColorMaterial(private val color: Vec3) : Material() {
    override fun bind(shaderProgram: ShaderProgram) {
        shaderProgram.setUniform("solidColor", color)
        shaderProgram.setUniform("mode", 0)
    }
}

class TexturedMaterial(private val texture: Texture) : Material() {
    override fun bind(shaderProgram: ShaderProgram) {
        shaderProgram.setUniform("mode", 1)
        texture.bind()
    }
}

class LitMaterial(
    private val color: Vec3,
    private val ambient: Vec3,
    private val diffuse: Vec3,
    private val specular: Vec3,
    private val shininess: Float,
) : Material() {
    override fun bind(shaderProgram: ShaderProgram) {
        shaderProgram.setUniform("ambientLight", ambient)
        shaderProgram.setUniform("diffuseLight", diffuse)
        shaderProgram.setUniform("specularLight", specular)
        shaderProgram.setUniform("shininess", shininess)

        shaderProgram.setUniform("solidColor", color)
        shaderProgram.setUniform("mode", 2)
    }
}

class LitTexturedMaterial(
    private val texture: Texture,
    private val ambient: Vec3,
    private val diffuse: Vec3,
    private val specular: Vec3,
    private val shininess: Float,
) : Material() {
    override fun bind(shaderProgram: ShaderProgram) {
        shaderProgram.setUniform("ambientLight", ambient)
        shaderProgram.setUniform("diffuseLight", diffuse)
        shaderProgram.setUniform("specularLight", specular)
        shaderProgram.setUniform("shininess", shininess)

        shaderProgram.setUniform("mode", 3)
        texture.bind()
    }
}
