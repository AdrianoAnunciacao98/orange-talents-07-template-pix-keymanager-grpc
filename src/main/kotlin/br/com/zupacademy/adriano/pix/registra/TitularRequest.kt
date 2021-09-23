package br.com.zupacademy.adriano.pix.registra

data class TitularRequest(val id: String, val nome: String, val cpf: String) {
    fun toModel(): Titular{
        return Titular(nome, cpf)
    }
}