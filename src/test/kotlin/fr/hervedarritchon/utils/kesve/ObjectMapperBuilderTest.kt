package fr.hervedarritchon.utils.kesve

import io.kotlintest.matchers.types.shouldBeTypeOf
import io.kotlintest.shouldBe
import io.kotlintest.specs.ShouldSpec

class ObjectMapperBuilderTest : ShouldSpec({
    should("return the length of the string") {
        "sammy".length shouldBe 5
        "".length shouldBe 0
    }
    "toto" {
        should("create an csv object mapper") {
            ObjectMapperBuilder().build().shouldBeTypeOf<ObjectMapper>()
        }
    }
})