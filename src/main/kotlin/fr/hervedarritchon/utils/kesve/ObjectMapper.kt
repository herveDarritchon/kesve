package fr.hervedarritchon.utils.kesve

import fr.hervedarritchon.utils.kesve.models.exceptions.NoSourceSetException
import fr.hervedarritchon.utils.kesve.reflection.makeInstance
import java.net.URL

class ObjectMapper internal constructor(
    val source: String,
    private val header: Boolean,
    val sep: Char = ','
) {
    fun toList(): List<String> {
        if (source.isBlank()) return emptyList()
        if (header) return source.cleanHeader().split(sep).toList()
        return source.split(sep).toList()
    }

    inline fun <reified T> toObject(): T {
        val list = source.split(sep).toList()
        return makeInstance(parameters = list)
    }

}

internal fun String.cleanHeader(): String {
    val indexOf = this.indexOf("\n")
    return this.substring(indexOf + 1)
}

class ObjectMapperBuilderFinal internal constructor(
    private val source: String,
    private val header: Boolean = false,
    private val separator: Char = ','
) {

    fun build(): ObjectMapper {
        return ObjectMapper(source = source, header = header, sep = separator)
    }

    fun withHeader(): ObjectMapperBuilderFinal {
        return ObjectMapperBuilderFinal(source = source, header = true)
    }

    fun withSeparator(sep: Char): ObjectMapperBuilderFinal {
        return ObjectMapperBuilderFinal(source = source, separator = sep)
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

