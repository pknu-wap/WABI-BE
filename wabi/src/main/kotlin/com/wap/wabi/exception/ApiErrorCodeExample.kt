package com.wap.wabi.exception

import com.wap.wabi.exception.ErrorCode
import kotlin.reflect.KClass

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class ApiErrorCodeExample(val value: KClass<out BaseErrorCode>)
