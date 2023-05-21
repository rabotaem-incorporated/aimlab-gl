package engine.components

import engine.Component
import engine.Entity
import engine.Material
import engine.Scene
import engine.systems.Light
import engine.systems.RenderPipeline
import engine.systems.SceneCamera
import glm_.mat4x4.Mat4
import graphics.ShaderProgram
import graphics.Vao

class Renderer(
    entity: Entity,
    scene: Scene,
    val material: Material,
    var model: Vao,
) : Component(entity, scene) {
    private lateinit var pipeline: RenderPipeline
    private lateinit var camera: SceneCamera
    private lateinit var light: Light

    override fun onCreate() {
        pipeline = scene.query()
        camera = scene.query()
        light = scene.query()
    }

    override fun onTick() {
        pipeline.query(this)
    }

    fun render(shaderProgram: ShaderProgram) {
        material.bind(shaderProgram)
        shaderProgram.setUniform("transform", entity.transform.modelToWorldMatrix)
        shaderProgram.setUniform("projectionView", camera.inner.projectionViewMatrix)
        shaderProgram.setUniform("lightDir", light.direction)
        shaderProgram.setUniform("viewPos", camera.inner.position)
        model.drawAll()
    }
}
