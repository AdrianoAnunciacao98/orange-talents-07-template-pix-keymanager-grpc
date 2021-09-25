package br.com.zupacademy.adriano.shared.handlers

import java.lang.RuntimeException

class ChavePixNaoEncontradaException(message: String) : RuntimeException(message) {
}