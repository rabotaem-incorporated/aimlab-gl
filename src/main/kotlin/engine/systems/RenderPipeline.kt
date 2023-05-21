package engine.systems

import engine.Scene
import engine.System
import engine.components.Renderer
import engine.components.UiRenderer
import glm_.glm
import glm_.vec3.Vec3

class RenderPipeline(scene: Scene) : System(scene) {
    private val toRender = mutableListOf<Renderer>()
    private val uiToRender = mutableListOf<UiRenderer>()

    override fun beforeTick() {
        toRender.clear()
    }

    fun query(renderer: Renderer) {
        toRender.add(renderer)
    }

    fun query(renderer: UiRenderer) {
        uiToRender.add(renderer)
    }

    override fun afterTick() {
        scene.tickContext?.draw {
            enableDepthTest()

            for (renderer in toRender) {
                renderer.render(this)
            }

            disableDepthTest()

            val aspectRatio = scene.glfwContext.windowWidth.toFloat() / scene.glfwContext.windowHeight.toFloat()

            for (renderer in uiToRender) {
                renderer.render(this, UiManager.uiProjectionViewMatrix(scene.glfwContext))
            }
        }
    }
}
