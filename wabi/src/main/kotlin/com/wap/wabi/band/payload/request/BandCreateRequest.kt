package com.wap.wabi.band.payload.request

import com.wap.wabi.band.entity.Band

data class BandCreateRequest(
    val bandName: String
) {
    fun toBand(adminId: Long): Band {
        return Band.builder()
            .adminId(adminId)
            .bandName(bandName)
            .build()
    }
}
