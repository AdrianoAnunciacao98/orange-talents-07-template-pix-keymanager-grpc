package br.com.zupacademy.adriano.remove

import br.com.zupacademy.adriano.TipoDeChave
import org.hibernate.validator.internal.util.Contracts.assertTrue
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class TipoDeChaveTest {

    @Nested
    inner class ALEATORIA {

        @Test
        fun `deve ser valido quando chave aleatoria for nula ou vazia `(){
            with(TipoDeChave.ALEATORIA){
                assertTrue(valida(null), "")
                assertTrue(valida(""),"")
            }
        }

        @Test
        fun `nao deve ser valido quando chave aleatoria possuir um valor`(){
            with(TipoDeChave.ALEATORIA){
                assertFalse(valida("um valor qualquer"))
            }
        }

    }

    @Nested
    inner class CPF {
        @Test
        fun `deve ser valido quando cpf for um numero valido `(){
            with(TipoDeChave.CPF){
                assertTrue(valida("35060731332"), "sqsq")
            }
        }
        @Test
        fun `nao deve ser valido quando cpf for um numero invalido`(){
            with(TipoDeChave.CPF){
                assertFalse(valida("35060731331"))
            }
        }
        @Test
        fun `nao deve ser valido quando cpf nao for informado`(){
            with(TipoDeChave.CPF){
                assertFalse(valida(null))
                assertFalse(valida(""))
            }
        }
    }

    @Nested
    inner class CELULAR {
        @Test
        fun `deve ser valido quando celular for um numero valido`(){
            with(TipoDeChave.CELULAR){
                assertTrue(valida("5511987654321"), "")
            }
        }
        @Test
        fun `nao deve ser valido quando celular for um numero invalido`(){
            with(TipoDeChave.CELULAR){
                assertFalse(valida("11987654321"))
                assertFalse(valida("+55a11987654321"))
            }
        }
    }

    @Nested
    inner class EMAIL{
        @Test
        fun `deve ser valido quando email for endereco valido`(){
            with(TipoDeChave.EMAIL){
                assertTrue(valida("zup.edu@zup.com.br"),"")
            }
        }
        @Test
        fun `nao deve ser valido quando email estiver em formato invalido`(){
            with(TipoDeChave.EMAIL){
                assertFalse(valida("zup.eduzup.com"))
                assertFalse(valida("zup.edu@zupcom."))
            }
        }
    }

     fun valida(chave: String?): Boolean
}