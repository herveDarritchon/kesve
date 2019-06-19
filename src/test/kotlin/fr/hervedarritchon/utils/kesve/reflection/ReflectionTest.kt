package fr.hervedarritchon.utils.kesve.reflection

import io.kotlintest.matchers.string.shouldContain
import io.kotlintest.matchers.types.shouldBeTypeOf
import io.kotlintest.shouldBe
import io.kotlintest.specs.ShouldSpec

/**
 * Created by Hervé Darritchon on 2019-05-22.
 *
 */
class ReflectionTest : ShouldSpec({

    "Object with no parameter" {
        should("Creates single instance using an empty constructor on the JVM") {
            val a: A = makeInstance()
            a.shouldBeTypeOf<A>()

            a.toString() shouldContain "A@"
        }
    }

    "Object with one parameter" {
        should("Creates single instance of String using the constructor and the value as parameter") {
            val a: String = makeInstance(listOf("Value"))
            a.shouldBeTypeOf<String>()

            a shouldContain "Value"
        }
        should("Creates single instance of Boolean using the constructor and the value as parameter") {
            val a: Boolean = makeInstance(listOf("true"))
            a.shouldBeTypeOf<Boolean>()

            a shouldBe true
        }
        should("Creates single instance of Int using the constructor and the value as parameter") {
            val a: Int = makeInstance(listOf("12"))
            a.shouldBeTypeOf<Int>()

            a shouldBe 12
        }
        should("Creates single instance of Float using the constructor and the value as parameter") {
            val a: Float= makeInstance(listOf("12.13"))
            a.shouldBeTypeOf<Float>()

            a shouldBe 12.13F
        }


    }
})