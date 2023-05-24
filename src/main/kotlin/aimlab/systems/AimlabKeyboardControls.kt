package aimlab.systems

import aimlab.Settings
import engine.Scene
import engine.systems.KeyboardControls
import graphics.Key
import graphics.KeyButtonStatus

class AimlabKeyboardControls(scene: Scene) : KeyboardControls(scene) {
    fun IncreaseSensitivity() {
        if (input.getKeyStatus(Key.LCTRL) == KeyButtonStatus.DOWN &&
            input.getKeyStatus(Key.LSHIFT) == KeyButtonStatus.UP &&
            input.getKeyStatus(Key.PLUS) == KeyButtonStatus.PRESS
        ) {
            Settings.increaseSensitivity()
        }
    }

    fun DecreaseSensitivity() {
        if (input.getKeyStatus(Key.LCTRL) == KeyButtonStatus.DOWN &&
            input.getKeyStatus(Key.LSHIFT) == KeyButtonStatus.UP &&
            input.getKeyStatus(Key.MINUS) == KeyButtonStatus.PRESS
        ) {
            Settings.decreaseSensitivity()
        }
    }

    fun IncreaseSensitivityFast() {
        if (input.getKeyStatus(Key.LCTRL) == KeyButtonStatus.DOWN &&
            input.getKeyStatus(Key.LSHIFT) == KeyButtonStatus.DOWN &&
            input.getKeyStatus(Key.PLUS) == KeyButtonStatus.PRESS
        ) {
            Settings.increaseSensitivityFast()
        }
    }

    fun DecreaseSensitivityFast() {
        if (input.getKeyStatus(Key.LCTRL) == KeyButtonStatus.DOWN &&
            input.getKeyStatus(Key.LSHIFT) == KeyButtonStatus.DOWN &&
            input.getKeyStatus(Key.MINUS) == KeyButtonStatus.PRESS
        ) {
            Settings.decreaseSensitivityFast()
        }
    }

    override fun beforeTick() {
        super.beforeTick()
        IncreaseSensitivity()
        DecreaseSensitivity()
        IncreaseSensitivityFast()
        DecreaseSensitivityFast()
    }
}