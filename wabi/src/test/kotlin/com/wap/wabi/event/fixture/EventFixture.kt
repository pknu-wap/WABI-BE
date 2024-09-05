package com.wap.wabi.event.fixture

import com.wap.wabi.common.Reflection
import com.wap.wabi.event.entity.Event

object EventFixture {
    fun createEvent(name: String): Event {
        return Event.builder()
            .adminId(1)
            .name(name)
            .startAt(null)
            .endAt(null)
            .eventStudentMaxCount(0)
            .build()
    }

    fun createEvent(name: String, id: Long): Event {
        val event = createEvent(name)
        return Reflection.makeIdChangedClone(Event::class.java, event, id)
    }
}