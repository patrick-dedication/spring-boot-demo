package com.example.demo

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.util.StopWatch

@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class LogExecutionTime

private val LOGGER = LoggerFactory.getLogger(LoggingAspect::class.java)

@Aspect
@Component
class LoggingAspect {

    @Around("@annotation(com.example.demo.LogExecutionTime)")
    fun logExecutionTime(proceedingJoinPoint: ProceedingJoinPoint): Any? {
        val methodSignature = proceedingJoinPoint.signature as MethodSignature
        val className = methodSignature.declaringType.simpleName
        val methodName = methodSignature.name

        val stopWatch = StopWatch("$className->$methodName")

        LOGGER.info("'${stopWatch.id}': Starting execution")
        stopWatch.start(methodName)

        return proceedingJoinPoint.proceed().also {
            stopWatch.stop()
            LOGGER.info("'${stopWatch.id}': running time = ${stopWatch.totalTimeMillis} ms")
        }
    }

}
