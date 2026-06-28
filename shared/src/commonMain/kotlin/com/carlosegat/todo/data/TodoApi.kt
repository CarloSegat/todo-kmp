package com.carlosegat.todo.data

import com.carlosegat.todo.model.Todo
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json


class TodoApi(
    private val baseUrl: String = "http://10.0.2.2:8000",
    private val client: HttpClient = HttpClient {
        // ContentNegotiation: the Ktor plugin that auto-converts request/response bodies based on
        // the Content-Type header. Registering json(...) means setBody(dto) is serialized to JSON
        // and body<Dto>() deserializes the JSON response — no manual encode/decode calls.
        install(ContentNegotiation) {
            // ignoreUnknownKeys = true: don't fail if the server sends fields our DTO doesn't declare.
            // encodeDefaults = false: a property equal to its default value is left out of the JSON.
            // This is what makes TodoUpdateDto sparse — null (=default) fields are omitted on PUT.
            // (false is already the library default; set explicitly here to make the behaviour obvious.)
            json(Json { ignoreUnknownKeys = true; isLenient = true; encodeDefaults = false })
        }
    },
) {
    suspend fun fetchAll(): List<Todo> {
        val res = client.get("$baseUrl/todos")
        if (res.status != HttpStatusCode.OK) error("fetchAll failed: ${res.status}")
        return res.body<List<TodoDto>>().map { it.toDomain() }
    }

    suspend fun create(todo: Todo): Todo {
        val res = client.post("$baseUrl/todos") {
            contentType(ContentType.Application.Json)
            setBody(todo.toCreateDto())
        }
        if (res.status != HttpStatusCode.Created) error("create failed: ${res.status}")
        return res.body<TodoDto>().toDomain()
    }

    // Mirrors the BE's GET /todos/{id}; unused for now (matches Flutter's _fetchById), kept for
    // contract completeness. Returns null on 404 instead of throwing.
    suspend fun getById(id: String): Todo? {
        val res = client.get("$baseUrl/todos/$id")
        return when (res.status) {
            HttpStatusCode.OK -> res.body<TodoDto>().toDomain()
            HttpStatusCode.NotFound -> null
            else -> error("getById failed: ${res.status}")
        }
    }

    suspend fun update(id: String, patch: TodoUpdateDto): Todo {
        val res = client.put("$baseUrl/todos/$id") {
            contentType(ContentType.Application.Json)
            setBody(patch)
        }
        if (res.status != HttpStatusCode.OK) error("update failed: ${res.status}")
        return res.body<TodoDto>().toDomain()
    }

    suspend fun delete(id: String) {
        // BE returns 204 No Content with an empty body — do not attempt to parse it.
        val res = client.delete("$baseUrl/todos/$id")
        if (res.status != HttpStatusCode.NoContent) error("delete failed: ${res.status}")
    }
}
