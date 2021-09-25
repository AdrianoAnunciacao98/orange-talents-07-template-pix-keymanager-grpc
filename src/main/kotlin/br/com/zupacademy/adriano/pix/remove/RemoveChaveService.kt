package br.com.zupacademy.adriano.pix.remove

import br.com.zupacademy.adriano.pix.registra.ChavePixRepository
import br.com.zupacademy.adriano.shared.handlers.ChavePixExistenteException
import br.com.zupacademy.adriano.shared.validation.ValidUUID
import jakarta.inject.Inject
import java.util.*
import javax.persistence.Id
import javax.transaction.Transactional
import javax.validation.Valid
import javax.validation.constraints.NotBlank

class RemoveChaveService(@Inject val repository: ChavePixRepository) {

    @Transactional
    fun remove(
        @NotBlank @ValidUUID(message = "cliente ID com formato invalido") clienteId: String?,
        @NotBlank @ValidUUID (message = "pix ID com formato invalido") pixId: String?,
    ) {
        val uuidPixId = UUID.fromString(pixId)
        val uuidClienteId = UUID.fromString(clienteId)

        val chave = repository.findByAndClienteId(uuidPixId, uuidClienteId)
            .orElseThrow{
                ChavePixNaoEncontradaException()
                repository.delete(uuidPixId)
            }
    }
}