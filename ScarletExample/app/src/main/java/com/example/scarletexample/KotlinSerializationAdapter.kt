package com.example.scarletexample

import com.tinder.scarlet.Message
import com.tinder.scarlet.MessageAdapter
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import java.lang.reflect.Type

class KotlinSerializationAdapter<T>(
    private val json: Json,
    private val type: Type
) : MessageAdapter<T> {

    override fun fromMessage(message: Message): T {
        return json.decodeFromString(serializer(type.javaClass), (message as Message.Text).value)
    }

    override fun toMessage(data: T): Message {
        val jsonString = json.encodeToString(serializer(type.javaClass), data)
        return Message.Text(jsonString)
    }

    class Factory(private val json: Json = Json { ignoreUnknownKeys = true }) : MessageAdapter.Factory {
        override fun create(type: Type, annotations: Array<Annotation>): MessageAdapter<*> {
            return KotlinSerializationAdapter(json, type)
        }
    }
}