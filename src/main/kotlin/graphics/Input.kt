package graphics

import glm_.vec2.Vec2
import org.lwjgl.glfw.GLFW

class Input(private val context: GlfwContext) {
    private var lastMousePosition = Vec2(0.0f, 0.0f)

    var mouseDelta = Vec2(0.0f, 0.0f)
        private set

    fun isKeyPressed(key: InputKey): Boolean {
        return GLFW.glfwGetKey(context.handle, key.code) == GLFW.GLFW_PRESS
    }

    val mousePosition: Vec2
        get() {
            val x = DoubleArray(1)
            val y = DoubleArray(1)
            GLFW.glfwGetCursorPos(context.handle, x, y)
            return Vec2(x[0].toFloat(), y[0].toFloat())
        }

    fun setMousePosition(xy: Vec2) {
        lastMousePosition = xy
        GLFW.glfwSetCursorPos(context.handle, xy.x.toDouble(), xy.y.toDouble())
    }

    fun isMousePressed(button: MouseButton): Boolean {
        return GLFW.glfwGetMouseButton(context.handle, button.code) == GLFW.GLFW_PRESS
    }

    fun tick() {
        val mousePosition = mousePosition
        mouseDelta = mousePosition - lastMousePosition
        lastMousePosition = mousePosition
    }
}

enum class InputKey(val code: Int) {
    SPACE(GLFW.GLFW_KEY_SPACE),
    ESCAPE(GLFW.GLFW_KEY_ESCAPE),
    W(GLFW.GLFW_KEY_W),
    A(GLFW.GLFW_KEY_A),
    S(GLFW.GLFW_KEY_S),
    D(GLFW.GLFW_KEY_D),
    LCTRL(GLFW.GLFW_KEY_LEFT_CONTROL),
    PLUS(GLFW.GLFW_KEY_EQUAL),
    MINUS(GLFW.GLFW_KEY_MINUS)
    ;
}

enum class MouseButton(val code: Int) {
    LEFT(GLFW.GLFW_MOUSE_BUTTON_LEFT),
    RIGHT(GLFW.GLFW_MOUSE_BUTTON_RIGHT),
    ;
}
