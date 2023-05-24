package engine

import engine.components.Transform
import glm_.quat.Quat
import glm_.vec3.Vec3

/**
 * Сущность - базовый объект игрового мира. Сами они ничего не делают, но могут иметь компоненты,
 * с полезной функциональностью.
 *
 * Сущности являются конкретными объектами в игровом мире, например шар, текст, что-то еще.
 * Сущности без компонентов могут быть использованы как пустые контейнеры для дочерних сущностей.
 *
 * Любая сущность обязана быть размещена в сцене, и иметь компонент [Transform].
 * Он проставляется автоматически при инициализации сущности.
 *
 * Пользователь сущности может добавлять к ней компоненты, которые реализуют
 * какую-то функциональность. Например, компонент [engine.components.Renderer]
 * отрисовывает сущность в игровом мире как трехмерный объект.
 *
 * У сущности может быть родительская сущность. В этом случае все преобразования
 * [Transform] дочерних сущностей будут относительно родительской.
 * Например, если у родителя `transform.position = Vec3(1f, 2f, 3f)` и `transform.scale = 2f`,
 * а у дочерней сущности `transform.position = Vec3(1f, 0f, 0f)`, то в игровом мире дочерняя
 * сущность будет находиться в точке `(3f, 2f, 3f)` и иметь размер `2f`.
 *
 * Сущности могут иметь дочерние сущности, которые наследуют [Transform] родителя.
 */
class Entity(val scene: Scene, val parent: Entity?) {
    /**
     * Дочерние сущности. Работа с этим списком должна происходить через методы
     * [Scene.create] и [Entity.destroy]. Ручное изменение не предполагается,
     * чтение допустимо.
     */
    val children = mutableListOf<Entity>()

    /**
     * Компоненты сущности. Работа с этим списком должна происходить через метод
     * [Entity.addComponent]. Ручное модифицирование не предполагается,
     * чтение скорее всего не нужно, есть метод [Entity.query].
     */
    val components = mutableListOf<Component>()

    /**
     * Так как у сущности обязательно должен быть компонент [Transform], то
     * ссылка на него хранится в отдельной переменной и всегда доступна.
     */
    val transform = addComponent(Transform(this, Vec3(), Quat(), 1.0f))

    /**
     * Получить компонент сущности по типу, или `null`, если такого компонента нет.
     */
    inline fun <reified T : Component> query(): T? {
        for (component in components) {
            if (component is T) return component
        }
        return null
    }

    /**
     * Добавить компонент к сущности.
     * Вызывает [Component.onCreate] у компонента автоматически.
     */
    fun <T : Component> addComponent(component: T): T {
        components.add(component)
        component.onCreate()
        return component
    }

    /**
     * Вызывается в [Scene.tick] для всех сущностей, обходя иерархию снизу вверх.
     *
     * Просто вызывает [Component.onTick] у всех компонентов сущности, затем рекурсивно
     * вызывает [tick] у всех дочерних сущностей.
     */
    fun tick() {
        for (component in components) component.onTick()
        for (child in children) child.tick()
    }

    /**
     * Удалить сущность из сцены, вызывая [Component.onDestroy] у всех компонентов.
     *
     * Если у сущности есть дочерние сущности, то они также будут удалены.
     */
    fun destroy() {
        for (component in components) component.onDestroy()
        while (children.isNotEmpty()) children[0].destroy()
        parent?.children?.remove(this)
        scene.entities.remove(this)
    }
}