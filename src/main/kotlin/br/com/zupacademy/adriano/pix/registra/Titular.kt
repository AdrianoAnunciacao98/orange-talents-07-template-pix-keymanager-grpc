package br.com.zupacademy.adriano.pix.registra

import io.micronaut.data.annotation.Embeddable

@Embeddable
class Titular(val nomeTitular: String, val cpfTitular: String) {
}