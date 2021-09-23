package br.com.zupacademy.adriano.pix.registra

import br.com.zupacademy.adriano.pix.remove.ChavePix
import io.micronaut.data.jpa.repository.JpaRepository
import java.util.*

interface ChavePixRepository: JpaRepository<ChavePix, UUID> {
    fun existsByChave(chave: String?): Boolean
}