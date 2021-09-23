package br.com.zupacademy.adriano.pix.integration.itau

import br.com.zupacademy.adriano.TipoDeConta
import br.com.zupacademy.adriano.pix.registra.*

data class DadosDaContaResponse(
    val tipo: TipoDeConta,
    val instituicao: InstituicaoRequest,
    val agencia: String,
    val numero: String,
    val titular: TitularRequest
) {
    fun toModel(): ContaAssociada {
        return ContaAssociada(
            TipoConta.valueOf(tipo.name), instituicao.toModel(), agencia, numero, titular.toModel()
        )
    }
}

data class TitularResponse(val nome: String, val cpf: String)

data class InstituicaoResponse(val nome: String, val ispb: String)