package br.com.zupacademy.adriano.shared.handlers


import com.google.rpc.Status
import io.grpc.Status.ALREADY_EXISTS

import jakarta.inject.Singleton
import java.lang.Exception

@Singleton
class ChavePixExistenteExceptionHandler: ExceptionHandler<ChavePixExistenteException> {
    override fun handle(e: ChavePixExistenteException): ExceptionHandler.StatusWithDetails {
    return ExceptionHandler.StatusWithDetails(
        Status.ALREADY_EXISTS
        .withDescription(e.message)
        .withCause(e))
    }

    override fun supports(e: Exception): Boolean {
       return e is ChavePixExistenteException
    }

}