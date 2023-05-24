package engine

import glm_.vec3.Vec3
import graphics.ShaderProgram
import graphics.Texture

/**
 * Материалы описывают внешний вид объектов при рендеринге.
 *
 * При реализации требуется правильно расставить uniform-переменные в шейдере.
 *
 * @see [engine.components.Renderer]
 * @see [engine.systems.RenderPipeline]
 */
abstract class Material {
    /**
     * Привязывает параметры материала к шейдерной программе через uniform-переменные.
     */
    abstract fun bind(shaderProgram: ShaderProgram)
}

/**
 * Материал, закрашивающий объект одним цветом. Во фрагметном шейдере используется как `mode = 0`.
 */
class SolidColorMaterial(private val color: Vec3) : Material() {
    override fun bind(shaderProgram: ShaderProgram) {
        shaderProgram.setUniform("solidColor", color)
        shaderProgram.setUniform("mode", 0)
    }
}

/**
 * Материал с заданной текстурой, не освещаемый. Во фрагментном шейдере используется как `mode = 1`.
 */
class TexturedMaterial(private val texture: Texture) : Material() {
    override fun bind(shaderProgram: ShaderProgram) {
        shaderProgram.setUniform("mode", 1)
        texture.bind()
    }
}

/**
 * Материал, освещаемый по модели Фонга одним цветом.
 * Направление света задается системой [engine.systems.Light].
 *
 * Во фрагментном шейдере используется как `mode = 2`.
 */
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

/**
 * Материал, освещаемый по модели Фонга с заданной текстурой.
 * Направление света задается системой [engine.systems.Light].
 *
 * Во фрагментном шейдере используется как `mode = 3`.
 */
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
