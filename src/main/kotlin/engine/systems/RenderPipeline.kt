package engine.systems

import engine.Scene
import engine.System
import engine.components.Renderer

class RenderPipeline(scene: Scene) : System(scene) {
    private val toRender = mutableListOf<Renderer>()

    override fun beforeTick() {
        toRender.clear()
    }

    fun query(renderer: Renderer) {
        toRender.add(renderer)
    }

    override fun afterTick() {
        scene.tickContext?.draw {
            for (renderer in toRender) {
                renderer.render(this)
            }
        }
    }
}
