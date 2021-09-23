package br.com.zupacademy.adriano.pix.registra

import io.micronaut.data.annotation.Embeddable
import javax.persistence.Embedded
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.validation.constraints.NotNull

    @Embeddable
    class ContaAssociada(
        @field:NotNull
        @Enumerated(EnumType.STRING)
        val tipoConta: TipoConta,
        @Embedded
        val instituicao: Instituicao,
        val agencia: String,
        val numero: String,
        @Embedded
        val titular: Titular
    ) {
}