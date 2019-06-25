package fr.hervedarritchon.utils.kesve.reflection

import kotlin.reflect.KClass

/**
 * Created by Hervé Darritchon on 2019-05-12.
 *
 */
class NoUsableConstructor : Error()

inline fun <reified T> makeInstance(parameters: List<String> = listOf()): T {
    return makeInstance(T::class, parameters) as T
}

fun makeInstance(clazz: KClass<*>, listOfParameters: List<String> = listOf()): Any? {

    val parameters = if (listOfParameters.isNotEmpty()) {
        listOfParameters.map {
            it.castStringTo()
        }
    } else listOf()

    val primitive = makePrimitiveOrNull(clazz, parameters.firstOrNull())
    if (primitive != null) {
        return primitive
    }

    val constructors = clazz.constructors
        .sortedBy { it.parameters.size }

    for (constructor in constructors) {
        try {
            val arguments = constructor.parameters
                .map { it.type.classifier as KClass<*> }
                .mapIndexed { index, kClass ->
                    makeInstance(
                        kClass,
                        listOf(listOfParameters[index])
                    )
                }
                .toTypedArray()

            return constructor.call(*arguments)
        } catch (e: Throwable) {
            // no-op. We catch any possible error here that might occur during class creation
        }
    }

    throw NoUsableConstructor()
}

private fun String.castStringTo(): Any {
    return when {
        checkBoolean() -> toBoolean()
        checkInt() -> toInt()
        checkFloat() -> toFloat()
        this.length == 1 -> single()
        else -> this
    }
}

private fun makePrimitiveOrNull(clazz: KClass<*>, first: Any?): Any? =
    when (clazz) {
        Int::class -> first as Int
        Long::class -> first as Long
        Double::class -> first as Double
        Float::class -> first as Float
        Char::class -> first as Char
        Boolean::class -> first as Boolean
        String::class -> first as String
        else -> null
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