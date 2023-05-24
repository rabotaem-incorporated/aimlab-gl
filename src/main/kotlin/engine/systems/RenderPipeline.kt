package engine.systems

import aimlab.systems.UiManager
import engine.Scene
import engine.System
import engine.components.Renderer
import engine.components.UiRenderer

/**
 * Графический конвейер, который рендерит все объекты в сцене.
 *
 * По сути просто хранит ссылки на все рендереры и вызывает их методы [Renderer.render], в нужном порядке.
 *
 * Если объект надо нарисовать, каждый тик необходимо вызвать [query] из компонента [Renderer] (или аналога).
 *
 * Перед тиком список рендереров очищается.
 */
class RenderPipeline(scene: Scene) : System(scene) {
    private val toRender = mutableListOf<Renderer>()
    private val uiToRender = mutableListOf<UiRenderer>()

    override fun beforeTick() {
        toRender.clear()
        uiToRender.clear()
    }

    fun query(renderer: Renderer) {
        toRender.add(renderer)
    }

    fun query(renderer: UiRenderer) {
        uiToRender.add(renderer)
    }

    override fun afterTick() {
        game.glfwContext.apply {
            val shaderProgram = currentShaderProgram!!
            shaderProgram.enableDepthTest()

            for (renderer in toRender) {
                renderer.render(shaderProgram)
            }

            shaderProgram.disableDepthTest()

            for (renderer in uiToRender) {
                renderer.render(shaderProgram, UiManager.uiProjectionViewMatrix(game.glfwContext))
            }
        }
    }
}
