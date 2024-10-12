package com.wap.wabi.common.config

import com.wap.wabi.exception.ApiErrorCodeExample
import com.wap.wabi.exception.BaseErrorCode
import com.wap.wabi.exception.payload.response.ErrorReason
import com.wap.wabi.exception.payload.response.ErrorResponse
import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.Operation
import io.swagger.v3.oas.models.examples.Example
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.media.Content
import io.swagger.v3.oas.models.media.MediaType
import io.swagger.v3.oas.models.responses.ApiResponse
import io.swagger.v3.oas.models.responses.ApiResponses
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import io.swagger.v3.oas.models.servers.Server
import org.springdoc.core.customizers.OperationCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import kotlin.reflect.KClass


@Configuration
class SwaggerConfig {
    @Bean
    fun UMCstudyAPI(): OpenAPI {
        val info = Info()
            .title("WABI API")
            .description("WABI API 명세서입니다.")
            .version("1.0.0")
        val jwtSchemeName = "JWT TOKEN"
        // API 요청헤더에 인증정보 포함
        val securityRequirement = SecurityRequirement().addList(jwtSchemeName)
        // SecuritySchemes 등록
        val components = Components()
            .addSecuritySchemes(
                jwtSchemeName, SecurityScheme()
                    .name(jwtSchemeName)
                    .type(SecurityScheme.Type.HTTP) // HTTP 방식
                    .scheme("bearer")
                    .bearerFormat("JWT")
            )
        return OpenAPI()
            .addServersItem(Server().url("/"))
            .info(info)
            .addSecurityItem(securityRequirement)
            .components(components)
    }
    @Bean
    fun customize(): OperationCustomizer {
        return OperationCustomizer { operation, handlerMethod ->
            val apiErrorCodeExample = handlerMethod.getMethodAnnotation(ApiErrorCodeExample::class.java)
            // ApiErrorCodeExample 애노테이션이 있는 메소드에 적용
            if (apiErrorCodeExample != null) {
                generateErrorCodeResponseExample(operation, apiErrorCodeExample.value)
            }
            operation
        }
    }

    private fun generateErrorCodeResponseExample(operation: Operation, type: KClass<out BaseErrorCode>) {
        val responses = operation.responses
        // 해당 이넘에 선언된 에러코드들의 목록을 가져옵니다.
        val errorCodes: Array<out BaseErrorCode>? = type.java.enumConstants
        // 400, 401, 404 등 에러코드의 상태코드들로 리스트로 모읍니다.
        // 400 같은 상태코드에 여러 에러코드들이 있을 수 있습니다.
        val statusWithExampleHolders: Map<Int, List<ExampleHolder>> = errorCodes?.map { baseErrorCode ->
            try {
                val errorReason = baseErrorCode.getErrorReason()
                ExampleHolder(
                    holder = getSwaggerExample("test", errorReason),
                    code = errorReason?.status?.value().toString(),
                    name = errorReason?.code ?: "Unknown"
                )
            } catch (e: NoSuchFieldException) {
                throw RuntimeException(e)
            }
        }?.groupBy { it.code.toInt() } ?: emptyMap()

        // response 객체들을 responses에 넣습니다.
        addExamplesToResponses(responses, statusWithExampleHolders)
    }

    private fun getSwaggerExample(value: String, errorReason: ErrorReason): Example {
        val errorResponse: ErrorResponse = ErrorResponse(errorReason, "요청시 패스정보입니다.")
        val example = Example()
        example.description(value)
        example.setValue(errorResponse)
        return example
    }

    private fun addExamplesToResponses(
        responses: ApiResponses,
        statusWithExampleHolders: Map<Int, List<ExampleHolder>>
    ) {
        statusWithExampleHolders.forEach { (status, exampleHolders) ->
            val content = Content()
            val mediaType = MediaType()

            // 상태 코드마다 ApiResponse를 생성합니다.
            val apiResponse = ApiResponse()

            // List<ExampleHolder>를 순회하며, mediaType 객체에 예시값을 추가합니다.
            exampleHolders.forEach { exampleHolder ->
                mediaType.addExamples(exampleHolder.name, exampleHolder.holder)
            }

            // ApiResponse의 content에 mediaType을 추가합니다.
            content.addMediaType("application/json", mediaType)
            apiResponse.content = content

            // 상태코드를 key 값으로 responses에 추가합니다.
            responses.addApiResponse(status.toString(), apiResponse)
        }
    }

}
