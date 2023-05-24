package engine.components

import engine.Component
import engine.Entity
import engine.Material
import engine.systems.Light
import engine.systems.RenderPipeline
import engine.systems.SceneCamera
import graphics.ShaderProgram
import graphics.Vao

/**
 * Общий компонент для отрисовки объекта.
 *
 * Позиция и прочие свойства объекта в глобальных координатах вычисляется по матрице трансформации,
 * из [Transform].
 */
class Renderer(
    entity: Entity,
    private val material: Material,
    private var model: Vao,
) : Component(entity) {
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
