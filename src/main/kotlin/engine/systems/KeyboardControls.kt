package engine.systems

import engine.Scene
import engine.System
import graphics.Key
import graphics.KeyButtonStatus

/**
 * Общие настройки управления, полезные для любой игры.
 *
 * Можно расширять, добавляя свои методы.
 */
open class KeyboardControls(scene: Scene) : System(scene) {
    private fun exitOnEscape() {
        if (input.isKeyPressed(Key.ESCAPE)) {
            game.glfwContext.close()
        }
    }

    private fun toggleFullscreen() {
        if (input.getKeyStatus(Key.F11) == KeyButtonStatus.PRESS) {
            game.glfwContext.fullscreen = !game.glfwContext.fullscreen
        }
    }

    override fun beforeTick() {
        exitOnEscape()
        toggleFullscreen()
    }
}