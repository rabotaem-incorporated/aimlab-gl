package engine.components

import engine.Component
import engine.Entity
import engine.Scene
import engine.systems.SceneCamera
import engine.systems.RenderPipeline
import glm_.vec3.Vec3
import graphics.ShaderProgram
import graphics.Texture
import graphics.Vao

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

class Renderer(
    entity: Entity,
    scene: Scene,
    val material: Material,
    val model: Vao,
) : Component(entity, scene) {
    private lateinit var pipeline: RenderPipeline
    private lateinit var camera: SceneCamera

    override fun onCreate() {
        pipeline = scene.query<RenderPipeline>()
        camera = scene.query<SceneCamera>()
    }

    override fun onTick() {
        pipeline.query(this)
    }

    fun render(shaderProgram: ShaderProgram) {
        material.bind(shaderProgram)
        shaderProgram.setUniform("transform", entity.transform.modelToWorldMatrix)
        shaderProgram.setUniform("projectionView", camera.inner.projectionViewMatrix)
        model.drawAll()
    }
}
