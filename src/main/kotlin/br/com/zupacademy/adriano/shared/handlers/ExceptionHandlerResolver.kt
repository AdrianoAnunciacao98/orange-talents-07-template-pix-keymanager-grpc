package br.com.zupacademy.adriano.shared.handlers

import io.micronaut.http.server.exceptions.ExceptionHandler
import jakarta.inject.Inject
import org.xml.sax.helpers.DefaultHandler
import java.lang.Exception
import java.lang.IllegalStateException

class ExceptionHandlerResolver(
    @Inject private val handlers: List<ExceptionHandler<Exception,Any>>,
) {
    private var defaultHandler: ExceptionHandler<Exception,Any> = DefaultExceptionHandler()

    constructor(handlers:List<ExceptionHandler<Exception,Any>>,defaultHandler: ExceptionHandler<Exception,Any>): this(handlers){
        this.defaultHandler = defaultHandler
    }
    fun resolve(e: Exception): ExceptionHandler<Exception,Any> {
        val foundHandlers = handlers.filter { h -> h.supports(e) }
        if(foundHandlers.size>1)
            throw IllegalStateException("Too many handlers '${e.javaClass.name}'")
        return foundHandlers.firstOrNull() ?: defaultHandler
    }
}