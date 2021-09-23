import br.com.zupacademy.adriano.RegistraChavePixRequest
import br.com.zupacademy.adriano.TipoDeChave
import br.com.zupacademy.adriano.TipoDeConta
import br.com.zupacademy.adriano.pix.registra.NovaChavePix

fun RegistraChavePixRequest.toModel(): NovaChavePix {
    return NovaChavePix(
        clientId = clientId,
        tipo = when(tipoDeChave){
            TipoDeChave.UNKNOWN_TIPO_CHAVE -> null
            else -> TipoDeChave.valueOf(tipoDeChave.name)
        },
        chave = chave,
        tipoDeConta = when(tipoDeConta){
            TipoDeConta.UNKNOWN_TIPO_CONTA -> null
            else -> TipoDeConta.valueOf(tipoDeConta.name)
        }
    )
}