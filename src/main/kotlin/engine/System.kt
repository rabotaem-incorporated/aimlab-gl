package engine

abstract class System(val scene: Scene) {
    open fun onStart() {}
    open fun onStop() {}
    open fun beforeTick() {}
    open fun afterTick() {}
}
