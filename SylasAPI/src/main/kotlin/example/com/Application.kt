package example.com

import com.fasterxml.jackson.databind.SerializationFeature
import example.com.plugins.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*

fun main() {
    embeddedServer(Netty, port = 4444, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    install(ContentNegotiation){
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
        }
    }
    configureRouting()
}
