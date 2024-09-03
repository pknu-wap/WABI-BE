package com.wap.wabi.band.fixture

import com.wap.wabi.band.entity.Band

object BandFixture {
    fun createBand(id: Long): Band {
        return Band.builder()
            .setId(id)
            .setAdminId(1)
            .setBandName("TestBand")
            .build()
    }
}