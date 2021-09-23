package br.com.zupacademy.adriano.pix.registra

import br.com.zupacademy.adriano.pix.integration.itau.ContasDeClientesNoItauClient
import br.com.zupacademy.adriano.pix.remove.ChavePix
import br.com.zupacademy.adriano.shared.handlers.ChavePixExistenteException
import io.micronaut.data.annotation.Repository
import io.micronaut.validation.Validated
import jakarta.inject.Inject
import jakarta.inject.Singleton
import org.slf4j.LoggerFactory
import java.lang.IllegalStateException
import javax.transaction.Transactional
import javax.validation.Valid

@Validated
@Singleton
class NovaChavePixService(
    @Inject val repository: ChavePixRepository,
    @Inject val itauClient: ContasDeClientesNoItauClient,)
 {
    private val LOGGER = LoggerFactory.getLogger(this::class.java)

     @Transactional
     fun registra(@Valid novaChave: NovaChavePix): ChavePix {
         if(repository.existsByChave(novaChave.chave))
             throw ChavePixExistenteException()

         val response = itauClient.buscaContaPorTipo(novaChave.clientId!!, novaChave.tipoDeConta!!.name)
         val conta = response.body()?.toModel()?: throw IllegalStateException("Cliente não encontrado no Itau")

         val chave = novaChave.toModel(conta)
         repository.save(chave)

         return chave
     }

}