package engine.systems

import engine.Scene
import engine.System
import engine.components.Button
import glm_.glm
import glm_.mat4x4.Mat4
import glm_.vec2.Vec2
import glm_.vec3.Vec3
import glm_.vec3.swizzle.xz
import glm_.vec4.Vec4
import glm_.vec4.swizzle.xy
import graphics.GlfwContext
import graphics.MouseButton

class UiManager(scene: Scene) : System(scene) {
    private val buttons = mutableListOf<Button?>()
    private lateinit var camera: Camera2d

    override fun onStart() {
        camera = scene.query()
    }

    override fun afterTick() {
        for (button in buttons) {
            if (button == null) continue

            val mousePos = scene.glfwContext.input.mousePosition
            val windowSize = Vec2(scene.glfwContext.windowWidth, scene.glfwContext.windowHeight)
            val mouseNdc = (mousePos / windowSize) * 2.0f - 1.0f
            val transformMatrix = uiProjectionViewMatrix(scene.glfwContext)

            val mouseUiWorld = glm.inverse(transformMatrix) * Vec4(mouseNdc.x, 0.0f, mouseNdc.y, 1.0f)
            val mouseUiWorldXZ = mouseUiWorld.xy
            mouseUiWorldXZ.y *= -1.0f

            val buttonMinXZ = button.boundingBox.min.xz
            val buttonMaxXZ = button.boundingBox.max.xz

            if (mouseUiWorldXZ.x >= buttonMinXZ.x && mouseUiWorldXZ.x <= buttonMaxXZ.x &&
                mouseUiWorldXZ.y >= buttonMinXZ.y && mouseUiWorldXZ.y <= buttonMaxXZ.y) {
                if (scene.glfwContext.input.isMousePressed(MouseButton.LEFT)) {
                    button.state = Button.State.PRESSED
                } else {
                    if (button.state == Button.State.PRESSED) {
                        button.click()
                    } else {
                        button.state = Button.State.HIGHLIGHTED
                    }
                }
            } else {
                button.state = Button.State.DEFAULT
            }
        }
    }

    data class ButtonHandle(val id: Int)

    fun register(button: Button): ButtonHandle {
        val handle = ButtonHandle(buttons.size)
        buttons.add(button)
        return handle
    }

    fun unregister(handle: ButtonHandle) {
        buttons[handle.id] = null
    }

    companion object {
        fun uiProjectionViewMatrix(glfwContext: GlfwContext): Mat4 {
            val aspectRatio = glfwContext.windowWidth.toFloat() / glfwContext.windowHeight.toFloat()
            return glm.ortho(
                -aspectRatio,
                aspectRatio,
                -1.0f,
                1.0f,
                -1.0f,
                1.0f,
            ) * glm.lookAt(Vec3(0.0f, 0.0f, 0.0f), Vec3(0.0f, 1.0f, 0.0), Vec3(0.0f, 0.0f, 1.0))
        }

    }
}
