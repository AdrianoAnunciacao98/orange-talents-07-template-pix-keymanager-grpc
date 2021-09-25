package br.com.zupacademy.adriano.pix.remove

import br.com.zupacademy.adriano.PixKeymanagerRemoveGrpcServiceGrpc
import br.com.zupacademy.adriano.RemoveChavePixRequest
import br.com.zupacademy.adriano.RemoveChavePixResponse
import io.grpc.stub.ClientResponseObserver
import io.grpc.stub.StreamObserver
import jakarta.inject.Inject
import jakarta.inject.Singleton

@Singleton
class RemoveChaveEndpoint(@Inject private val service: RemoveChaveService,):
PixKeymanagerRemoveGrpcServiceGrpc.PixKeymanagerRemoveGrpcServiceImplBase()
{
override fun remove(
    request: RemoveChavePixRequest,
    responseObserver: StreamObserver<RemoveChavePixResponse>,
) {
    service.remove(clienteId = request.clienteId, pixId = request.pixId)

    responseObserver.onNext(
        RemoveChavePixResponse.newBuilder()
        .setClienteId(request.clienteId)
        .setPixId(request.pixId)
        .build()

    )
}

}