package engine.systems

import engine.Scene
import engine.System
import graphics.InputKey

class ExitOnEscape(scene: Scene) : System(scene) {
    override fun beforeTick() {
        if (scene.tickContext!!.input.isKeyPressed(InputKey.ESCAPE)) {
            scene.tickContext!!.glfwContext.close()
        }
    }
}