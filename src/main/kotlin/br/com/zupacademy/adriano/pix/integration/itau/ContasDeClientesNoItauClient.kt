package br.com.zupacademy.adriano.pix.integration.itau

import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.QueryValue
import io.micronaut.http.client.annotation.Client
import java.net.http.HttpResponse

@Client(value = "http://localhost:9091/api/v1/clientes")
interface ContasDeClientesNoItauClient {
    @Get("/api/v1/clientes/{clienteId}/contas{?tipo}")
    fun buscaContaPorTipo(@PathVariable clienteId: String, @QueryValue tipo: String): HttpResponse<DadosDaContaResponse>
}