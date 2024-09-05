package com.wap.wabi.band.fixture

import com.wap.wabi.band.entity.Band

object BandFixture {
    fun createBand(name: String): Band {
        return Band.builder()
            .adminId(1)
            .bandName(name)
            .build()
    }
}