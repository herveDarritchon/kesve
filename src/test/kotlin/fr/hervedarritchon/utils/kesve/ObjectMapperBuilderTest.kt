package fr.hervedarritchon.utils.kesve

import fr.hervedarritchon.utils.kesve.models.exceptions.NoSourceSetException
import io.kotlintest.matchers.types.shouldBeTypeOf
import io.kotlintest.shouldBe
import io.kotlintest.shouldThrow
import io.kotlintest.specs.ShouldSpec

class ObjectMapperBuilderTest : ShouldSpec({
    "ObjectMapperBuilder from String" {
        should("create a mapper to marshall and unmarshall some CSV data") {
            ObjectMapperBuilder
                .from("")
                .build().shouldBeTypeOf<ObjectMapper>()
        }
    }
    "ObjectMapperBuilder from File" {
        should("create a mapper to marshall and unmarshall some CSV data") {
            shouldThrow<NoSourceSetException> {
                ObjectMapperBuilder
                    .from(this.javaClass.getResource("test-file-empty.csv"))
                    .build()
            }
        }
    }
    "mapper.toList() from a string" {
        should("return an Empty list if the string is empty") {
            ObjectMapperBuilder
                .from("")
                .build()
                .toList() shouldBe emptyList()
        }
        should("return an Empty list if the string is blanck") {
            ObjectMapperBuilder
                .from(" ")
                .build()
                .toList() shouldBe emptyList()
        }
        should("return list of (elt1,elt2) if the string is elt1,elt2") {
            ObjectMapperBuilder
                .from("elt1,elt2")
                .build()
                .toList() shouldBe listOf("elt1","elt2")
        }

    }

}) {

}