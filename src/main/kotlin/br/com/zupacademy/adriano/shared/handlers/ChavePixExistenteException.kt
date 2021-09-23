package br.com.zupacademy.adriano.shared.handlers

class ChavePixExistenteException: Exception() {
    override val message: String?
    get() = "Já existe essa chave cadastrada"
}