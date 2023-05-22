package graphics

import glm_.vec2.Vec2
import org.lwjgl.glfw.GLFW

/**
 * Система ввода. Поддерживает позицию мыши, нажатые кнопки мыши, движение мыши из кадра в кадр, нажатые клавиши.
 *
 * @param context Контекст окна. Через него происходит взаимодействие с GLFW.
 */
class Input(private val context: GlfwContext) {
    private var lastMousePosition = Vec2(0.0f, 0.0f)
    private var keyStatuses : MutableMap<Key, KeyButtonStatus> = mutableMapOf()
    private var mouseButtonStatuses : MutableMap<MouseButton, KeyButtonStatus> = mutableMapOf()

    init {
        for (key in Key.values()) {
            keyStatuses[key] = KeyButtonStatus.UP
        }

        for (button in MouseButton.values()) {
            mouseButtonStatuses[button] = KeyButtonStatus.UP
        }
    }

    /**
     * Вектор, показывающий смещение мыши в пикселях за последний кадр.
     */
    var mouseDelta = Vec2(0.0f, 0.0f)
        private set

    /**
     * Возвращает текущую позицию мыши в окне в пикселях.
     */
    val mousePosition: Vec2
        get() {
            val x = DoubleArray(1)
            val y = DoubleArray(1)
            GLFW.glfwGetCursorPos(context.handle, x, y)
            return Vec2(x[0].toFloat(), y[0].toFloat())
        }

    /**
     * Устанавливает позицию мыши в окне в пикселях.
     */
    fun setMousePosition(xy: Vec2) {
        lastMousePosition = xy
        GLFW.glfwSetCursorPos(context.handle, xy.x.toDouble(), xy.y.toDouble())
    }

    fun isKeyPressed(key: Key): Boolean {
        return GLFW.glfwGetKey(context.handle, key.code) == GLFW.GLFW_PRESS
    }

    fun isMouseButtonPressed(button: MouseButton): Boolean {
        return GLFW.glfwGetMouseButton(context.handle, button.code) == GLFW.GLFW_PRESS
    }

    fun getMouseButtonStatus(button: MouseButton) = mouseButtonStatuses[button]!!

    fun getKeyStatus(key: Key) = keyStatuses[key]

    /**
     * Обновляет состояние клавиш и мыши.
     *
     * Должен вызываться в начале каждого кадра, каждый кадр.
     */
    fun tick() {
        val mousePosition = mousePosition
        mouseDelta = mousePosition - lastMousePosition
        lastMousePosition = mousePosition
        for (key in Key.values()) {
            if (isKeyPressed(key)) {
                keyStatuses[key] = if (keyStatuses[key] == KeyButtonStatus.UP) KeyButtonStatus.PRESS
                else KeyButtonStatus.DOWN
            } else {
                keyStatuses[key] = if (keyStatuses[key] == KeyButtonStatus.DOWN) KeyButtonStatus.RELEASE
                else KeyButtonStatus.UP
            }
        }
        for (button in MouseButton.values()) {
            if (isMouseButtonPressed(button)) {
                mouseButtonStatuses[button] = if (mouseButtonStatuses[button] == KeyButtonStatus.UP) KeyButtonStatus.PRESS
                else KeyButtonStatus.DOWN
            } else {
                mouseButtonStatuses[button] = if (mouseButtonStatuses[button] == KeyButtonStatus.DOWN) KeyButtonStatus.RELEASE
                else KeyButtonStatus.UP
            }
        }
    }
}

/**
 * Коды клавиш, которые отслеживает система ввода.
 */
enum class Key(val code: Int) {
    SPACE(GLFW.GLFW_KEY_SPACE),
    ESCAPE(GLFW.GLFW_KEY_ESCAPE),
    W(GLFW.GLFW_KEY_W),
    A(GLFW.GLFW_KEY_A),
    S(GLFW.GLFW_KEY_S),
    D(GLFW.GLFW_KEY_D),
    LCTRL(GLFW.GLFW_KEY_LEFT_CONTROL),
    LSHIFT(GLFW.GLFW_KEY_LEFT_SHIFT),
    PLUS(GLFW.GLFW_KEY_EQUAL),
    MINUS(GLFW.GLFW_KEY_MINUS),
    F11(GLFW.GLFW_KEY_F11),
}

/**
 * Коды кнопок мыши, которые отслеживает система ввода.
 */
enum class MouseButton(val code: Int) {
    LEFT(GLFW.GLFW_MOUSE_BUTTON_LEFT),
    RIGHT(GLFW.GLFW_MOUSE_BUTTON_RIGHT),
    ;
}

/**
 * Статус клавиши.
 *
 * @property UP Если в этот и в предыдущий кадр клавиша не зажата
 * @property DOWN Если в этот и в предыдущий кадр клавиша зажата
 * @property PRESS Если в этот кадр клавиша зажата, а в предыдущем нет
 * @property RELEASE Если в этот кадр клавиша не зажата, а в предыдущем была
 */
enum class KeyButtonStatus {
    UP,
    DOWN,
    PRESS,
    RELEASE,
}
