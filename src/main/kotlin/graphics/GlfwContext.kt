package graphics

import glm_.vec4.Vec4
import org.lwjgl.glfw.GLFW
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL13

/**
 * Настройки окна. Конкретно сейчас используются только для создания окна.
 */
data class WindowSettings(
    val width: Int = 800,
    val height: Int = 600,
    val xpos: Int = 200,
    val ypos: Int = 200,
    val title: String = "Window",
)

/**
 * Настройки GLFW. Конкретно сейчас используются только для создания окна.
 */
data class GlfwSettings(
    /** Версия OpenGL, пара (Major, Minor) */
    val version: Pair<Int, Int> = 4 to 2,
)

/**
 * Общий контекст для работы с OpenGL и GLFW. Это основной класс в этом слое.
 * Предполагается, что это основная поверхность соприкоснования со внешним миром.
 *
 * В констукторе создаётся окно GLFW и контекст OpenGL, подключаются функции OpenGL,
 * некоторые настройки OpenGL, например включение мультисэмплинга.
 *
 * @param windowSettings настройки окна
 */
class GlfwContext(windowSettings: WindowSettings, glfwSettings: GlfwSettings) {
    /**
     * Хэндл окна GLFW, возвращается функцией [GLFW.glfwCreateWindow].
     */
    val handle: Long

    /**
     * Счётчик кадров, увеличивается на 1 каждый кадр.
     */
    var frameCounter: Long = 0

    var windowWidth = windowSettings.width
        private set

    var windowHeight = windowSettings.height
        private set

    var savedWindowWidth = windowSettings.width
        private set

    var savedWindowHeight = windowSettings.height
        private set

    var windowPositionX = windowSettings.xpos
        private set

    var savedWindowPositionX = windowSettings.xpos
        private set

    var windowPositionY = windowSettings.ypos
        private set

    var savedWindowPositionY = windowSettings.ypos
        private set

    val input = Input(this)
    val time = Time()

    var currentShaderProgram: ShaderProgram? = null

    init {
        assert(GLFW.glfwInit()) { "Failed to initialize GLFW" }
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, glfwSettings.version.first)
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, glfwSettings.version.second)
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE)
        GLFW.glfwWindowHint(GLFW.GLFW_SAMPLES, 4)

        handle = GLFW.glfwCreateWindow(
            windowSettings.width,
            windowSettings.height,
            windowSettings.title,
            0,
            0,
        )

        if (handle == 0L) {
            GLFW.glfwTerminate()
            throw Error("Failed to create GLFW window :(")
        }

        GLFW.glfwMakeContextCurrent(handle)
        GL.createCapabilities()

        GL11.glViewport(0, 0, windowSettings.width, windowSettings.height)

        GLFW.glfwSetWindowPos(handle, windowPositionX, windowPositionY)

        GLFW.glfwSetFramebufferSizeCallback(handle) { _, width, height ->
            windowWidth = width
            windowHeight = height
            GL11.glViewport(0, 0, width, height)
        }

        GLFW.glfwSetWindowPosCallback(handle) {_, xpos, ypos ->
            windowPositionX = xpos
            windowPositionY = ypos
        }

        GL11.glEnable(GL13.GL_MULTISAMPLE)
    }

    /**
     * Создаёт шейдерную программу.
     *
     * @param shaderProgramBuilder функция, которая создаёт шейдерную программу, используя [ShaderProgramBuilder].
     */
    fun compileShaderProgram(shaderProgramBuilder: ShaderProgramBuilder.() -> Unit): ShaderProgram {
        return ShaderProgramBuilder().apply(shaderProgramBuilder).build(this)
    }

    inline fun mainLoop(loop: FrameContext.() -> Unit) {
        while (!GLFW.glfwWindowShouldClose(handle)) {
            FrameContext().loop()

            GLFW.glfwSwapBuffers(handle)
            GLFW.glfwPollEvents()

            time.tick()
            input.tick()
            frameCounter += 1
        }
    }

    fun vao(managed: Boolean = true): Vao {
        return Vao(managed)
    }

    fun terminate() {
        GLFW.glfwTerminate()
    }

    fun close() {
        GLFW.glfwSetWindowShouldClose(handle, true)
    }

    var cursorHidden: Boolean = false
        set(value) {
            GLFW.glfwSetInputMode(handle, GLFW.GLFW_CURSOR, if (value) GLFW.GLFW_CURSOR_HIDDEN else GLFW.GLFW_CURSOR_NORMAL)
            field = value
        }

    var fullscreen: Boolean = false
        set(value) {
            if (value) {
                val monitor = GLFW.glfwGetPrimaryMonitor()
                val mode = GLFW.glfwGetVideoMode(monitor)
                savedWindowWidth = windowWidth
                savedWindowHeight = windowHeight
                savedWindowPositionX = windowPositionX
                savedWindowPositionY = windowPositionY
                GLFW.glfwSetWindowMonitor(handle, monitor, 0, 0, mode!!.width(), mode.height(), mode.refreshRate())
            } else {
                GLFW.glfwSetWindowMonitor(handle, 0, savedWindowPositionX, savedWindowPositionY, savedWindowWidth, savedWindowHeight, GLFW.GLFW_DONT_CARE)
            }
            field = value
        }
}

class FrameContext {
    fun clear(color: Vec4) {
        GL11.glClearColor(color.r, color.g, color.b, color.a)
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT or GL11.GL_DEPTH_BUFFER_BIT)
    }
}
