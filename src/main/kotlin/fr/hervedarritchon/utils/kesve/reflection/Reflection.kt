package fr.hervedarritchon.utils.kesve.reflection

import kotlin.reflect.KClass

/**
 * Created by Hervé Darritchon on 2019-05-12.
 *
 */

class A

class B(val a: A)

class C(val a: A, val b: B)

class NoUsableConstructor : Error()

inline fun <reified T> makeInstance(parameters: List<String> = listOf()): T {
    return makeInstance(T::class, parameters) as T
}

fun makeInstance(clazz: KClass<*>, parameters: List<String> = listOf()): Any? {


    if (parameters.isNotEmpty()) {
        val elt = parameters[0]
        return when {
            elt.checkBoolean() -> elt.toBoolean()
            elt.checkInt() -> elt.toInt()
            elt.checkFloat() -> elt.toFloat()
            else -> elt
        }
    }

    val constructor = clazz.constructors
        .minBy { it.parameters.size } ?: throw NoUsableConstructor()

    val arguments = constructor.parameters
        .map {
            it.type.classifier as KClass<*>
        }
        .map {
            makeRandomInstance(it)
        }
        .toTypedArray()

    return constructor.call(*arguments)
}

fun <T> isType(objectToCast: Any?, implementationInterface: Class<T>): Boolean {
    if (objectToCast == null) {
        return false
    }

    return try {
        implementationInterface.cast(objectToCast)
        true
    } catch (e: ClassCastException) {
        false
    }
}

private fun String.checkBoolean(): Boolean {
    val regex = Regex(pattern = "^(?i)(true|false)\$")

    return matches(regex)

}

private fun String.checkInt(): Boolean {
    val regex = Regex(pattern = "^[-+]?\\d*$")

    return matches(regex)
}

private fun String.checkFloat(): Boolean {
    val regex = Regex(pattern = "^[-+]?\\d*\\.\\d*$")

    return matches(regex)
}

inline fun <reified T> makeRandomInstance(): T {
    return makeRandomInstance(T::class) as T
}

inline fun <reified T> universalMakeRandomInstance(): T {
    return T::class
        .constructors
        .first {
            it.parameters
                .isEmpty()
        }
        .call()
}

fun makeRandomInstance(clazz: KClass<*>): Any? {
    val constructor = clazz.constructors
        .minBy { it.parameters.size } ?: throw NoUsableConstructor()

    val arguments = constructor.parameters
        .map { it.type.classifier as KClass<*> }
        .map { makeRandomInstance(it) }
        .toTypedArray()

    return constructor.call(*arguments)
}