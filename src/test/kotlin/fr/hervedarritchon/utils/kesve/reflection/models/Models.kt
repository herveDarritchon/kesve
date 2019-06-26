package fr.hervedarritchon.utils.kesve.reflection.models

/**
 * Created by Hervé Darritchon on 2019-06-25.
 *
 */
class A

class B(val a: A)

class C(val a: A, val b: B)

data class ComposedFloatString(val f: Float, val s: String)