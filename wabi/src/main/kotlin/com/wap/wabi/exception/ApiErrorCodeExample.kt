package com.wap.wabi.exception

import kotlin.reflect.KClass

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class ApiErrorCodeExample(val value: KClass<out BaseErrorCode>)
