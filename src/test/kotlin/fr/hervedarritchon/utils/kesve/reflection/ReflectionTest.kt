package fr.hervedarritchon.utils.kesve.reflection

import io.kotlintest.matchers.string.shouldContain
import io.kotlintest.matchers.types.shouldBeTypeOf
import io.kotlintest.specs.ShouldSpec

/**
 * Created by Hervé Darritchon on 2019-05-12.
 *
 */
class ReflectionTest : ShouldSpec(
    {
        "Object with no parameter" {
            should("Creates single instance using an empty constructor on the JVM") {
                val a: A = makeRandomInstance()
                a.shouldBeTypeOf<A>()

                a.toString() shouldContain "A@"
            }

            should("Creates single instance using an empty constructor everywhere") {
                val a: A = universalMakeRandomInstance()
                a.shouldBeTypeOf<A>()

                a.toString() shouldContain "A@"
            }

        }
        "Object with parameters" {
            should("Creates single instance using constructor with one parameter on the JVM") {
                val b: B = makeRandomInstance()
                b.shouldBeTypeOf<B>()

                b.toString() shouldContain "B@"

                b.a.shouldBeTypeOf<A>()
            }
            should("Creates single instance using constructor with two parameters on the JVM") {
                val c: C = makeRandomInstance()
                c.shouldBeTypeOf<C>()

                c.toString() shouldContain "C@"
                c.a.shouldBeTypeOf<A>()
                c.b.shouldBeTypeOf<B>()
            }


        }

    }
)