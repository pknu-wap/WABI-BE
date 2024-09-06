package com.wap.wabi.event.fixture

import com.wap.wabi.band.entity.Band
import com.wap.wabi.event.entity.Event
import com.wap.wabi.event.entity.EventBand

object EventBandFixture {
    fun createEventBnd(event: Event, band: Band): EventBand {
        return EventBand.builder()
            .event(event)
            .band(band)
            .build()
    }
}