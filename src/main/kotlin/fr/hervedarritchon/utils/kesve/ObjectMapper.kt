package fr.hervedarritchon.utils.kesve

import fr.hervedarritchon.utils.kesve.models.exceptions.NoSourceSetException
import java.net.URL

class ObjectMapper internal constructor(
    private val source: String,
    private val header: Boolean,
    private val sep: Char = ','
) {
    fun toList(): List<String> {
        if (source.isBlank()) return emptyList()
        if (header) return source.cleanHeader().split(sep).toList()
        return source.split(sep).toList()
    }

    fun <T> toObject(): T {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}

internal fun String.cleanHeader(): String {
    val indexOf = this.indexOf("\n")
    return this.substring(indexOf + 1)
}

class ObjectMapperBuilderFinal internal constructor(
    private val source: String,
    private val header: Boolean = false
) {

    fun build(): ObjectMapper {
        return ObjectMapper(source, header = header)
    }

    fun withHeader(): ObjectMapperBuilderFinal {
        return ObjectMapperBuilderFinal(this.source, true)
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

