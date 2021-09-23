package br.com.zupacademy.adriano.pix.registra

data class InstituicaoRequest(val nome: String, val ispb: String) {

    fun toModel(): Instituicao{
        return Instituicao(nome,ispb)
    }

}