package fr.hervedarritchon.utils.kesve.reflection

import kotlin.reflect.KClass

/**
 * Created by Hervé Darritchon on 2019-05-12.
 *
 */

class A

class B(val a: A)

class C(val a: A, val b: B)

class NoUsableConstructor: Error()

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