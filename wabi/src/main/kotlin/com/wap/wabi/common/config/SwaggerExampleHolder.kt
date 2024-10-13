package com.wap.wabi.common.config

import io.swagger.v3.oas.models.examples.Example

data class SwaggerExampleHolder(
    val holder: Example,
    val name: String,
    val code: String,
)
