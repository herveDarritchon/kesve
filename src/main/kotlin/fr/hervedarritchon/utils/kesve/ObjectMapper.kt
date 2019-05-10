package fr.hervedarritchon.utils.kesve

import fr.hervedarritchon.utils.kesve.models.exceptions.NoSourceSetException
import java.net.URL

class ObjectMapper internal constructor(
    private val source: String,
    private val sep: Char = ','
) {
    fun toList(): List<String> {
        if (source.isBlank()) return emptyList()
        return source.split(sep).toList()
    }
}

class ObjectMapperBuilderFinal internal constructor(private val source: String) {
    fun build(): ObjectMapper {
        return ObjectMapper(source)
    }
}

class ObjectMapperBuilder {

    companion object {
        fun from(source: String): ObjectMapperBuilderFinal {
            return ObjectMapperBuilderFinal(source)
        }

        fun from(resource: URL?): ObjectMapperBuilderFinal {
            val content: String = resource?.readText() ?: throw NoSourceSetException("File not found or content empty")
            return ObjectMapperBuilderFinal(content)
        }
    }
}

