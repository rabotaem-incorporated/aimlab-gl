package graphics

import glm_.vec2.Vec2
import glm_.vec3.Vec3
import java.nio.ByteBuffer

data class Layout(val fragments: List<Fragment>) {
    enum class Fragment {
        Float3,
        Float2,
        Int1,
        Int3,
        ;

        fun width(): Int = when (this) {
            Float3 -> 3 * 4
            Float2 -> 2 * 4
            Int1 -> 1 * 4
            Int3 -> 3 * 4
        }
    }

    val width: Int = fragments.sumOf { it.width() }

    val offsets: List<Int> = fragments.runningFold(0) { offset, fragment ->
        offset + fragment.width()
    }

    companion object {
        fun of(vararg fragments: Fragment): Layout = Layout(fragments.toList())

        /*
        fun layoutOf(
            struct: KClass<*>,
        ): Layout {
            assert(struct.isData) { "Struct must be a data class" }

            val locationCount = struct.declaredMemberProperties.count()
            val layoutFragments = MutableList<Fragment?>(locationCount) { null }

            struct.declaredMemberProperties.map { prop ->
                val location = prop.annotations.firstOrNull { it is Location } as Location? ?:
                    throw Error("Property ${prop.name} is missing @Location annotation")

                layoutFragments[location.location] = when (prop.returnType.classifier) {
                    Int::class -> Fragment.Int1
                    Float3::class -> Fragment.Float3
                    else -> throw Error("Unsupported type: ${prop.returnType.classifier}")
                }
            }

            return Layout(layoutFragments.requireNoNulls())
        }
         */
    }
}

/*
@Target(AnnotationTarget.PROPERTY)
annotation class Location(val location: Int)
*/

fun ByteBuffer.putFloat3(float3: Vec3) {
    putFloat(float3.x)
    putFloat(float3.y)
    putFloat(float3.z)
}

fun ByteBuffer.putFloat2(float2: Vec2) {
    putFloat(float2.x)
    putFloat(float2.y)
}

fun ByteBuffer.putInt3(int: Int3) {
    putInt(int.x)
    putInt(int.y)
    putInt(int.z)
}
