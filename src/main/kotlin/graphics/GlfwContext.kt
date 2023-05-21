package graphics

import glm_.vec4.Vec4
import org.lwjgl.glfw.GLFW
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11

data class WindowSettings(
    val width: Int = 800,
    val height: Int = 600,
    val title: String = "Window",
)

data class GlfwSettings(
    val version: Pair<Int, Int> = 4 to 2,
)

class GlfwContext(windowSettings: WindowSettings, glfwSettings: GlfwSettings) {
    val handle: Long

    private var shaderProgram: ShaderProgram? = null
        set(value) {
            value?.use()
            field = value
        }

    var windowWidth = windowSettings.width
        private set

    var windowHeight = windowSettings.height
        private set

    val input = Input(this)
    val time = Time()

    init {
        assert(GLFW.glfwInit()) { "Failed to initialize GLFW" }
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, glfwSettings.version.first)
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, glfwSettings.version.second)
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE)

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

        GLFW.glfwSetFramebufferSizeCallback(handle) { _, width, height ->
            windowWidth = width
            windowHeight = height
            GL11.glViewport(0, 0, width, height)
        }

        GL11.glEnable(GL11.GL_DEPTH_TEST)
        // GL11.glEnable(GL11.GL_CULL_FACE)
        // GL11.glCullFace(GL11.GL_BACK)
    }

    fun compileShaderProgram(shaderProgramBuilder: ShaderProgramBuilder.() -> Unit): ShaderProgram {
        return ShaderProgramBuilder().apply(shaderProgramBuilder).build()
    }

    inline fun mainLoop(loop: FrameContext.() -> Unit) {
        while (!GLFW.glfwWindowShouldClose(handle)) {
            FrameContext().loop()

            GLFW.glfwSwapBuffers(handle)
            GLFW.glfwPollEvents()

            time.tick()
            input.tick()
        }
    }

    fun vao(): Vao {
        return Vao()
    }

    fun terminate() {
        GLFW.glfwTerminate()
    }

    fun close() {
        GLFW.glfwSetWindowShouldClose(handle, true)
    }
}

class FrameContext {
    fun clear(color: Vec4) {
        GL11.glClearColor(color.r, color.g, color.b, color.a)
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT or GL11.GL_DEPTH_BUFFER_BIT)
    }
}
