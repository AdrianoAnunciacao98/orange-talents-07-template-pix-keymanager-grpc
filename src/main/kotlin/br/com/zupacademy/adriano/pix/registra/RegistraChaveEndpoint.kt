package br.com.zupacademy.adriano.pix.registra

import br.com.zupacademy.adriano.PixKeymanagerRegistraGrpcServiceGrpc
import br.com.zupacademy.adriano.RegistraChavePixRequest
import br.com.zupacademy.adriano.RegistraChavePixResponse
import io.grpc.stub.ClientResponseObserver
import io.grpc.stub.StreamObserver
import jakarta.inject.Inject
import jakarta.inject.Singleton
import toModel



@Singleton
class RegistraChaveEndpoint(@Inject private val service: NovaChavePixService,)
    : PixKeymanagerRegistraGrpcServiceGrpc.PixKeymanagerRegistraGrpcServiceImplBase(){
    override fun registra(
        request: RegistraChavePixRequest,
        responseObserver: StreamObserver<RegistraChavePixResponse>
    ) {
        val novaChave = request.toModel()
        val chaveCriada = service.registra(novaChave)

        responseObserver.onNext(RegistraChavePixResponse.newBuilder()
            .setClientId(chaveCriada.clienteId.toString())
            .setPixId(chaveCriada.id.toString())
            .build())
    }
}

