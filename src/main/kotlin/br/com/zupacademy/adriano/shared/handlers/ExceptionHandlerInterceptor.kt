package br.com.zupacademy.adriano.shared.handlers


import io.grpc.BindableService
import io.micronaut.aop.ConstructorInvocationContext
import io.micronaut.aop.MethodInterceptor
import io.micronaut.aop.MethodInvocationContext
import jakarta.inject.Inject
import jakarta.inject.Singleton
import org.slf4j.LoggerFactory
import java.lang.Exception

@Singleton
class ExceptionHandlerInterceptor(@Inject private val resolver: ExceptionHandlerResolver ) : MethodInterceptor<BindableService,Any>{
    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun intercept(context: MethodInvocationContext<BindableService,Any?>): Any? {
        try{
            return context.proceed()
        } catch(e: Exception){
            logger.error("Exception '${e.javaClass.name}' while processing the call: ${context.targetMethod}", e)
            val handler = resolver.resolve(e)
            val status = handler.handle(e)
            return null
        }
    }


}