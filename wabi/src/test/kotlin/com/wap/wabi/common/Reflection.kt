package com.wap.wabi.common

import java.lang.reflect.Constructor
import java.lang.reflect.Field

object Reflection {
    fun <T> makeIdChangedClone(clazz: Class<T>, entity: Any, id: Long): T {
        val constructor: Constructor<T> = clazz.getDeclaredConstructor()
        constructor.isAccessible = true
        val clone = constructor.newInstance()

        val fields: Array<Field> = clazz.declaredFields
        for (field in fields) {
            field.isAccessible = true
            field.set(clone, field.get(entity))
        }

        val idField: Field = clazz.getDeclaredField("id")
        idField.isAccessible = true
        idField.set(clone, id)

        return clone
    }
}