package engine.systems

import engine.Scene
import engine.System
import glm_.vec3.Vec3

class Light(scene: Scene, val direction: Vec3) : System(scene)
