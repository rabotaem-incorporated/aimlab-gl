package aimlab.systems

import engine.systems.KeyboardControls
import engine.Scene
import graphics.InputKey
import aimlab.Settings

class AimlabKeyboardControls(scene : Scene) : KeyboardControls(scene) {
    private fun IncreaseSensitivity() {
        if (scene.tickContext!!.input.isKeyPressed(InputKey.LCTRL) && scene.tickContext!!.input.isKeyPressed(InputKey.PLUS)) {
            Settings.increaseSensitivity()
        }
    }

    private fun DecreaseSensitivity() {
        if (scene.tickContext!!.input.isKeyPressed(InputKey.LCTRL) && scene.tickContext!!.input.isKeyPressed(InputKey.MINUS)) {
            Settings.decreaseSensitivity()
        }
    }

    override fun beforeTick() {
        super.beforeTick()
        IncreaseSensitivity()
        DecreaseSensitivity()
    }
}