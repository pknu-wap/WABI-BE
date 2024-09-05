package com.wap.wabi.band.fixture

import com.wap.wabi.band.entity.Band
import com.wap.wabi.common.Reflection

object BandFixture {
    fun createBand(name: String): Band {
        return Band.builder()
            .adminId(1)
            .bandName(name)
            .build()
    }

    fun createBand(name: String, id: Long): Band {
        val band = createBand(name)
        return Reflection.makeIdChangedClone(Band::class.java, band, id)
    }
}