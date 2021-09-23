package br.com.zupacademy.adriano.shared.handlers




import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.grpc.protobuf.StatusProto
import java.lang.Exception

interface ExceptionHandler<E: Exception> {
    fun handle(e: E): StatusWithDetails

    fun supports(e: Exception): Boolean

    data class StatusWithDetails(val status: Status, val metadata: io.grpc.Metadata){

        fun asRuntimeException(): StatusRuntimeException {
            return status.asRuntimeException(metadata)
        }

    }
}