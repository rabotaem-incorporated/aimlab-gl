package engine.systems

import engine.Scene
import engine.System
import glm_.vec3.Vec3

/**
 * Просто система-контейнер, которая показывает [engine.components.Renderer], откуда светит свет.
 */
class Light(scene: Scene, val direction: Vec3) : System(scene)
