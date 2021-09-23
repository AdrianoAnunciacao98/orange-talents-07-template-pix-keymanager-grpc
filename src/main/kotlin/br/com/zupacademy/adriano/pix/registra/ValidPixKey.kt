package br.com.zupacademy.adriano.pix.registra


import jakarta.inject.Singleton
import javax.lang.model.element.AnnotationValue
import javax.validation.*
import kotlin.reflect.KClass

@MustBeDocumented
@Target(AnnotationTarget.CLASS,AnnotationTarget.TYPE)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [ValidPixKeyValidator::class])
annotation class ValidPixKey(
    val message: String = "chave pix inválida (\${validatedValue.tipo})",
    val groups: Array<KClass<Any>> = [],
    val payload: Array<KClass<Payload>> = [],
)

@Singleton
class ValidPixKeyValidator: ConstraintValidator<ValidPixKey, NovaChavePix>{

    override fun isValid(value: NovaChavePix?,
                         annotationMetadata: AnnotationValue<ValidPixKey>, context: ConstraintValidatorContext,)
                         : Boolean {
        if(value?.tipo == null){
            return false
        }
        return value.tipo.valida(value.chave)
    }


}