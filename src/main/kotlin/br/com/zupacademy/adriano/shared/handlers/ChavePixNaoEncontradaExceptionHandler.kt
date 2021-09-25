package br.com.zupacademy.adriano.shared.handlers

import io.grpc.Status
import jakarta.inject.Singleton
import java.lang.Exception

@Singleton
class ChavePixNaoEncontradaExceptionHandler: ExceptionHandler<ChavePixNaoEncontradaException> {

    override fun handle(e: ChavePixNaoEncontradaException): ExceptionHandler.StatusWithDetails{
        return ExceptionHandler.StatusWithDetails(Status.NOT_FOUND
            .withDescription(e.message)
            .withDescription("") ) 
    }

    override fun supports(e: Exception): Boolean {
        return e is ChavePixNaoEncontradaException
    }
}