package br.com.zupacademy.adriano.registra

import br.com.zupacademy.adriano.PixKeymanagerRegistraGrpcServiceGrpc
import br.com.zupacademy.adriano.RegistraChavePixRequest
import br.com.zupacademy.adriano.TipoDeChave
import br.com.zupacademy.adriano.TipoDeConta
import br.com.zupacademy.adriano.pix.integration.itau.ContasDeClientesNoItauClient
import br.com.zupacademy.adriano.pix.integration.itau.DadosDaContaResponse
import br.com.zupacademy.adriano.pix.integration.itau.InstituicaoResponse
import br.com.zupacademy.adriano.pix.integration.itau.TitularResponse
import br.com.zupacademy.adriano.pix.registra.ChavePixRepository
import br.com.zupacademy.adriano.pix.registra.ContaAssociada
import br.com.zupacademy.adriano.pix.registra.InstituicaoRequest
import br.com.zupacademy.adriano.pix.registra.TitularRequest
import io.grpc.ManagedChannel
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.grpc.stub.AbstractBlockingStub
import io.micronaut.context.annotation.Bean
import io.micronaut.context.annotation.Factory
import io.micronaut.data.jpa.repository.JpaRepository
import io.micronaut.grpc.annotation.GrpcChannel
import io.micronaut.grpc.annotation.GrpcService
import io.micronaut.grpc.server.GrpcServerChannel
import io.micronaut.http.HttpResponse
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import reactor.core.publisher.Mono.`when`
import org.mockito.Mockito


import java.util.*

@MicronautTest(transactional = false)
internal class RegistraChaveEndpointTest(val repository: ChavePixRepository, val grpcClient: PixKeymanagerRegistraGrpcServiceGrpc.PixKeymanagerRegistraGrpcServiceBlockingStub) {


    @Inject
    lateinit var itauClient: ContasDeClientesNoItauClient

    companion object{
        val CLIENTE_ID = UUID.randomUUID()
    }

    @BeforeEach
    fun setup(){
        repository.deleteAll()
    }

    @Test
    fun `deve registrar nova chave pix`(){
        Mockito.`when`(itauClient.buscaContaPorTipo(clienteId = CLIENTE_ID.toString(), tipo = "CONTA_CORRENTE"))
            .thenReturn(HttpResponse.ok(dadosDaContaResponse()))
        val response = grpcClient.registra(RegistraChavePixRequest.newBuilder()
            .setClientId(CLIENTE_ID.toString())
            .setTipoDeChave(TipoDeChave.EMAIL)
            .setChave("rponte@gmail.com")
            .setTipoDeConta(TipoDeConta.CONTA_CORRENTE)
            .build()
        )
        with(response){
            assertEquals(CLIENTE_ID.toString(),clientId)
            assertNotNull(pixId)
        }
    }

    @Test
    fun `nao deve registrar chave pix quando chave existente`(){
        repository.save(chave(
            tipo = br.com.zupacademy.adriano.pix.registra.TipoDeChave.CPF,
            chave = "6365750325",
            clienteId = CLIENTE_ID
        ))
        val thrown = assertThrows<StatusRuntimeException> {
            grpcClient.registra(RegistraChavePixRequest.newBuilder()
                .setClientId(CLIENTE_ID.toString())
                .setTipoDeChave(TipoDeChave.CPF)
                .setChave("6365750325")
                .setTipoDeConta(TipoDeConta.CONTA_CORRENTE)
                .build()
            )
        }
        with(thrown){
            assertEquals(Status.ALREADY_EXISTS.code, status.code)
            assertEquals("Chave Pix '63657520325' existente", status.description)
        }
    }

    @Test
    fun `nao deve registrar chave pix quando nao encontrar dados da conta cliente`(){
        Mockito.`when`(itauClient.buscaContaPorTipo(clienteId = CLIENTE_ID.toString(),tipo = "CONTA_CORRENTE"))
            .thenReturn(HttpResponse.notFound())

    val thrown = assertThrows<StatusRuntimeException> {
        grpcClient.registra(RegistraChavePixRequest.newBuilder()
            .setClientId(CLIENTE_ID.toString())
            .setTipoDeChave(TipoDeChave.EMAIL)
            .setChave("rponte@gmail.com")
            .setTipoDeConta(TipoDeConta.CONTA_CORRENTE)
            .build()
        )
    }
    with(thrown){
        assertEquals(Status.FAILED_PRECONDITION.code, status.code )
        assertEquals("Cliente não encontrado no Itau", status.description)
    }

    @Test
    fun `nao deve registrar chave pix quando parametros forem invalidos `(){
        val thrown = assertThrows<StatusRuntimeException>() {
            grpcClient.registra(RegistraChavePixRequest.newBuilder().build())
        }
        with(thrown){
            assertEquals(Status.INVALID_ARGUMENT.code, status.code)
            assertEquals("Dados Invalidos", status.description)
        }
    }

    @MockBean(ContasDeClientesNoItauClient:: class)
    fun itauClient(): ContasDeClientesNoItauClient?{
        return Mockito.mock(ContasDeClientesNoItauClient::class.java)
    }

    @Factory
    class Clients {
        @Bean
        fun blockingStub(@GrpcChannel(GrpcServerChannel.NAME)channel: ManagedChannel): PixKeymanagerRegistraGrpcServiceGrpc.PixKeymanagerRegistraGrpcServiceBlockingStub{
            return PixKeymanagerRegistraGrpcServiceGrpc.newBlockingStub(channel)
        }

    }

}
        private fun dadosDaContaResponse(): DadosDaContaResponse{
            return DadosDaContaResponse(
                tipo = TipoDeConta.CONTA_CORRENTE,
                instituicao = InstituicaoRequest("UNIBANCO ITAU SA", "232"),
                agencia = "1218",
                numero = "291900",
                titular = TitularRequest("Rafael Ponte", "343443")
            ) }
        }

