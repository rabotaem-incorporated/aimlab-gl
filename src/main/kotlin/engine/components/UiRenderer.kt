package engine.components

import engine.*
import engine.systems.RenderPipeline
import glm_.mat4x4.Mat4
import glm_.vec3.Vec3
import graphics.ShaderProgram
import graphics.Vao
import kotlin.math.sin

open class UiRenderer(
    entity: Entity,
    scene: Scene,
    var model: Vao,
    var material: Material = SolidColorMaterial(Vec3(1.0f, 1.0f, 1.0f)),
) : Component(entity, scene) {
    private lateinit var renderPipeline: RenderPipeline

    override fun onCreate() {
        renderPipeline = scene.query()
    }

    override fun onTick() {
        renderPipeline.query(this)
    }

    fun render(shaderProgram: ShaderProgram, uiProjectionViewMatrix: Mat4) {
        material.bind(shaderProgram)
        shaderProgram.setUniform("transform", entity.transform.modelToWorldMatrix)
        shaderProgram.setUniform("projectionView", uiProjectionViewMatrix)
        model.drawAll()
    }
}