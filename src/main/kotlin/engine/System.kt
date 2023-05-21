package engine

abstract class System(val scene: Scene) {
    open fun onStart() {}
    open fun beforeTick() {}
    open fun afterTick() {}
}
