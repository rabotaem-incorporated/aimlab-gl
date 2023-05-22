package aimlab.systems

import engine.systems.KeyboardControls
import engine.Scene
import graphics.InputKey
import aimlab.Settings

class AimlabKeyboardControls(scene : Scene) : KeyboardControls(scene) {
    private fun IncreaseSensitivity() {
        if (scene.tickContext!!.input.isKeyPressed(InputKey.LCTRL) && scene.tickContext!!.input.isKeyPressed(InputKey.PLUS)) {
            if (Settings.sensitivity.x > 10) return
            Settings.sensitivity.plusAssign(Settings.dSensitivity)
        }
    }

    private fun DecreaseSensitivity() {
        if (scene.tickContext!!.input.isKeyPressed(InputKey.LCTRL) && scene.tickContext!!.input.isKeyPressed(InputKey.MINUS)) {
            if (Settings.sensitivity.x < 1e-6) return
            Settings.sensitivity.minusAssign(Settings.dSensitivity)
        }
    }

    override fun beforeTick() {
        super.beforeTick()
        IncreaseSensitivity()
        DecreaseSensitivity()
    }
}