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
    private var inputKeyStatuses : MutableMap<InputKey, InputKeyStatus> = mutableMapOf()

    init {
        for (key in InputKey.values()) {
            inputKeyStatuses[key] = InputKeyStatus.UP
        }
    }

    /**
     * Вектор, показывающий смещение мыши в пикселях за последний кадр.
     */
    var mouseDelta = Vec2(0.0f, 0.0f)
        private set

    /**
     * Проверяет, нажата ли клавиша.
     */
    fun isKeyPressed(key: InputKey): Boolean {
        return GLFW.glfwGetKey(context.handle, key.code) == GLFW.GLFW_PRESS
    }

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

    /**
     * Проверяет, нажата ли кнопка мыши.
     */
    fun isMousePressed(button: MouseButton): Boolean {
        return GLFW.glfwGetMouseButton(context.handle, button.code) == GLFW.GLFW_PRESS
    }

    /**
     * Возвращает статус клавиши.
     */
    fun getKeyStatus(key: InputKey) = inputKeyStatuses[key]

    /**
     * Обновляет состояние клавиш и мыши.
     *
     * Должен вызываться в начале каждого кадра, каждый кадр.
     */
    fun tick() {
        val mousePosition = mousePosition
        mouseDelta = mousePosition - lastMousePosition
        lastMousePosition = mousePosition
        for (key in InputKey.values()) {
            if (isKeyPressed(key)) {
                inputKeyStatuses[key] = if (inputKeyStatuses[key] == InputKeyStatus.UP) InputKeyStatus.PRESSED
                else InputKeyStatus.DOWN
            } else {
                inputKeyStatuses[key] = if (inputKeyStatuses[key] == InputKeyStatus.DOWN) InputKeyStatus.RELEASED
                else InputKeyStatus.UP
            }
        }
    }
}

/**
 * Коды клавиш, которые отслеживает система ввода.
 */
enum class InputKey(val code: Int) {
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
    F11(GLFW.GLFW_KEY_F11)
    ;
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
 * @property PRESSED Если в этот кадр клавиша зажата, а в предыдущем нет
 * @property RELEASED Если в этот кадр клавиша не зажата, а в предыдущем была
 */
enum class InputKeyStatus {
    UP,
    DOWN,
    PRESSED,
    RELEASED,
    ;
}
