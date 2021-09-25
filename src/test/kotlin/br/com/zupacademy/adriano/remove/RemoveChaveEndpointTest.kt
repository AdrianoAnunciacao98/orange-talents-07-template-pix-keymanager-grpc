package br.com.zupacademy.adriano.remove

import br.com.zupacademy.adriano.PixKeymanagerRemoveGrpcServiceGrpc
import br.com.zupacademy.adriano.RemoveChavePixRequest
import br.com.zupacademy.adriano.TipoDeChave
import br.com.zupacademy.adriano.pix.registra.ChavePixRepository
import br.com.zupacademy.adriano.pix.remove.ChavePix
import io.grpc.ManagedChannel
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.grpc.stub.AbstractBlockingStub
import io.micronaut.context.annotation.Bean
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import io.micronaut.grpc.server.GrpcServerChannel
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

@MicronautTest(transactional = false)
internal class RemoveChaveEndpointTest(val repository: ChavePixRepository, val grpcClient: PixKeymanagerRemoveGrpcServiceGrpc.PixKeymanagerRemoveGrpcServiceBlockingStub) {
    lateinit var CHAVE_EXISTENTE: ChavePix

    @BeforeEach
    fun setup(){
        CHAVE_EXISTENTE = repository.save(chave(
            tipo = TipoDeChave.EMAIL,
            chave = "rponte@gmail.com",
            clienteId = UUID.randomUUID()
        ))
    }
    @AfterEach
    fun cleanUp(){
        repository.deleteAll()
    }
    @Test
    fun `deve remover chave pix existente`(){
        val response = grpcClient.remove(RemoveChavePixRequest.newBuilder()
            .setPixId(CHAVE_EXISTENTE.id.toString())
            .setClienteId(CHAVE_EXISTENTE.clienteId.toString())
            .build()
        )
        assertEquals(CHAVE_EXISTENTE.id.toString(), response.pixId)
        assertEquals(CHAVE_EXISTENTE.clienteId.toString(), response.clienteId)
    }
    @Test
    fun `nao deve remover chave pix quando chave inexistente`(){
        val pixIdNaoExistente = UUID.randomUUID().toString()
        val thrown = assertThrows<StatusRuntimeException> {
            grpcClient.remove(RemoveChavePixRequest.newBuilder()
                .setPixId(pixIdNaoExistente)
                .setClienteId(CHAVE_EXISTENTE.clienteId.toString())
                .build()
            )
        }
        with(thrown){
            assertEquals(Status.NOT_FOUND, status.code)

        }
    }

    @Test
    fun `nao deve remover chave pix quando chave existente mas pertence a outro cliente `(){
        val outroClienteId = UUID.randomUUID().toString()

        val thrown = assertThrows<StatusRuntimeException> {
            grpcClient.remove(RemoveChavePixRequest.newBuilder()
                .setPixId(CHAVE_EXISTENTE.id.toString())
                .setClienteId(outroClienteId)
                .build()
            )
        }
        with(thrown){
            assertEquals(Status.NOT_FOUND, status.code)
            assertEquals("Chave pix nao encontrada ou nao pertence ao cliente", status.code)
        }
    }

    @Factory
    class Clients {
        @Bean
        fun blockingStub(@GrpcChannel(GrpcServerChannel.NAME) channel: ManagedChannel): PixKeymanagerRemoveGrpcServiceGrpc.PixKeymanagerRemoveGrpcServiceBlockingStub{
            return PixKeymanagerRemoveGrpcServiceGrpc.newBlockingStub(channel)
        }

    }
}