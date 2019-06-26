package fr.hervedarritchon.utils.kesve

import fr.hervedarritchon.utils.kesve.models.Elt
import fr.hervedarritchon.utils.kesve.models.exceptions.NoSourceSetException
import io.kotlintest.matchers.types.shouldBeTypeOf
import io.kotlintest.shouldBe
import io.kotlintest.shouldThrow
import io.kotlintest.specs.ShouldSpec

class ObjectMapperTest : ShouldSpec({
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
        should("create an Elt object if the file contains some elts separated by ','") {
            shouldThrow<NoSourceSetException> {
                ObjectMapperBuilder
                    .from(this.javaClass.getResource("test-file-empty.csv"))
                    .build()
                    .toObject<Elt>() shouldBe Elt("elt1", "elt2")
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
        should("return list of (elt1,elt2) if the content is elt1,elt2") {
            ObjectMapperBuilder
                .from("elt1,elt2")
                .build()
                .toList() shouldBe listOf("elt1", "elt2")
        }
        should("return list of (elt1,elt2) if the content is header1,header2\nelt1,elt2") {
            ObjectMapperBuilder
                .from("header1,header2\nelt1,elt2")
                .withHeader()
                .build()
                .toList() shouldBe listOf("elt1", "elt2")
        }
    }

    "mapper.toObject<T>() from a String" {
        should("return an instance of Elt if the content is elt1,elt2") {
            ObjectMapperBuilder
                .from("elt1,elt2")
                .build()
                .toObject<Elt>() shouldBe Elt("elt1", "elt2")
        }
    }
})