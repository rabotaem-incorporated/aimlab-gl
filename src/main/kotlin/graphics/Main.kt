package graphics

import glm_.glm
import glm_.mat4x4.Mat4
import glm_.vec2.Vec2
import glm_.vec3.Vec3
import glm_.vec4.Vec4


fun main() = NativeAllocatorContext.new {
    GlfwContext(
        WindowSettings(),
        GlfwSettings(),
    ).apply {
        val shaderProgram = compileShaderProgram {
            vertex("/sample.vert")
            fragment("/sample.frag")
            uniform("projection")
            uniform("model")
            uniform("view")
        }

        val vertexLayout = Layout.of(Layout.Fragment.Float3, Layout.Fragment.Float3, Layout.Fragment.Float2)

        val texture2 = Texture("/pepega.jpg")
        val suzanne = Mesh.loadObj("/suzanne.obj").vao(this)

        val vao = vao()
        vao.withBind {
            val vertices = allocStructs(vertexLayout, 8).fill {
                put(Vec3(+1.0f, +1.0f, +1.0f), Vec3(0.0f, 0.0f, 1.0f), Vec2(0.0f, 0.0f))
                put(Vec3(+1.0f, +1.0f, -1.0f), Vec3(0.0f, 0.0f, 1.0f), Vec2(0.0f, 1.0f))
                put(Vec3(+1.0f, -1.0f, +1.0f), Vec3(0.0f, 0.0f, 1.0f), Vec2(1.0f, 0.0f))
                put(Vec3(+1.0f, -1.0f, -1.0f), Vec3(0.0f, 0.0f, 1.0f), Vec2(1.0f, 1.0f))
                put(Vec3(-1.0f, +1.0f, +1.0f), Vec3(0.0f, 0.0f, 1.0f), Vec2(0.0f, 1.0f))
                put(Vec3(-1.0f, +1.0f, -1.0f), Vec3(0.0f, 0.0f, 1.0f), Vec2(0.0f, 0.0f))
                put(Vec3(-1.0f, -1.0f, +1.0f), Vec3(0.0f, 0.0f, 1.0f), Vec2(1.0f, 1.0f))
                put(Vec3(-1.0f, -1.0f, -1.0f), Vec3(0.0f, 0.0f, 1.0f), Vec2(1.0f, 0.0f))
            }

            val indices = allocStructs(Layout.of(Layout.Fragment.Int3), 12).fill {
                put(Int3(0, 1, 2))
                put(Int3(2, 1, 3))
                put(Int3(4, 5, 6))
                put(Int3(6, 5, 7))

                put(Int3(0, 1, 4))
                put(Int3(4, 1, 5))
                put(Int3(2, 3, 6))
                put(Int3(6, 3, 7))

                put(Int3(0, 2, 4))
                put(Int3(4, 2, 6))
                put(Int3(1, 3, 5))
                put(Int3(5, 3, 7))
            }

            vertexBuffer(
                Buffer.Usage.Static,
                vertices,
            )

            elementBuffer(
                Buffer.Usage.Static,
                indices,
            )
        }

        val camera = Camera(
            Vec3(0.0f, 0.0f, 3.0f),
            0.0f, 0.0f,
            60.0f,
            this,
        )

        mainLoop {
            clear(Vec4(0.2f, 0.3f, 0.3f, 1.0f))

            shaderProgram.using {
                texture2.bind()

                val speed = 10.0f
                if (input.isKeyPressed(InputKey.ESCAPE)) close()
                if (input.isKeyPressed(InputKey.W)) camera.position.plusAssign(camera.direction * speed * time.delta)
                if (input.isKeyPressed(InputKey.S)) camera.position.plusAssign(-camera.direction * speed * time.delta)
                if (input.isKeyPressed(InputKey.D)) camera.position.plusAssign(camera.right * speed * time.delta)
                if (input.isKeyPressed(InputKey.A)) camera.position.plusAssign(-camera.right * speed * time.delta)

                camera.yaw += input.mouseDelta.x * 0.005f
                camera.pitch -= input.mouseDelta.y * 0.005f
                camera.pitch = glm.clamp(camera.pitch, -glm.radians(89.0f), glm.radians(89.0f))
                input.setMousePosition(Vec2(windowWidth / 2, windowHeight / 2))

                setUniform("projection", camera.projectionMatrix)
                setUniform("view", camera.viewMatrix)

                for (i in 0 until 10) {
                    for (j in 0 until 10) {
                        for (k in 0 until 10) {
                            val model = Mat4.identity
                            model.translateAssign(k.toFloat() * 3.0f, j.toFloat() * 3.0f, i.toFloat() * 3.0f)
                            model.rotateAssign((time.current + i + j) * 2.0f, Vec3(0.0f, 0.0f, 1.0f))
                            model.rotateAssign(time.current * 1.0f, Vec3(0.0f, 1.0f, 0.0f))
                            setUniform("model", model)
                            vao.drawAll()
                        }
                    }
                }

                setUniform("model", Mat4.identity.translate(0.0f, 0.0f, 10.0f))
                suzanne.drawAll()
                setUniform("model", Mat4.identity.translate(0.0f, 0.0f, -10.0f))
                suzanne.drawAll()
                // var model = Mat4.identity
                // model = glm.rotate(model, time.current * 2.0f, Vec3(0.0f, 0.0f, 1.0f))
                // model = glm.rotate(model, time.current * 1.0f, Vec3(0.0f, 1.0f, 0.0f))
                // setUniform("model", model)
                // vao.drawAll()
            }
        }

        terminate()
    }
}
