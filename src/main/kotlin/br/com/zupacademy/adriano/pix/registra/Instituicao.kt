package br.com.zupacademy.adriano.pix.registra

import javax.persistence.Embeddable
import javax.persistence.Embedded

@Embeddable
class Instituicao(val nome: String, val ispb: String) {
}