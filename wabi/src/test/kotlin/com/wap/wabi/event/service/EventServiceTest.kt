package com.wap.wabi.event.service

import com.wap.wabi.band.fixture.BandFixture
import com.wap.wabi.band.repository.BandRepository
import com.wap.wabi.event.fixture.EventBandFixture
import com.wap.wabi.event.fixture.EventFixture
import com.wap.wabi.event.payload.request.EventCreateRequest
import com.wap.wabi.event.payload.request.EventUpdateRequest
import com.wap.wabi.event.payload.response.EventData
import com.wap.wabi.event.repository.EventBandRepository
import com.wap.wabi.event.repository.EventRepository
import jakarta.transaction.Transactional
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import java.time.LocalDateTime
import java.util.Optional

@Transactional
@SpringBootTest
@SuppressWarnings("NonAsciiCharacters")
class EventServiceTest {
    @Autowired
    private lateinit var eventService: EventService

    @MockBean
    private lateinit var eventRepository: EventRepository

    @MockBean
    private lateinit var eventBandRepository: EventBandRepository

    @MockBean
    private lateinit var bandRepository: BandRepository

    @Test
    fun 이벤트를_생성한다() {
        //Given
        val adminId: Long = 1L
        val eventName = "Event 1"
        val startAt = LocalDateTime.now()
        val endAt = LocalDateTime.now().plusDays(1)
        val eventStudentMaxCount = 80
        val bandIds = listOf(1L, 2L, 3L)
        val eventCreateRequest = EventCreateRequest(
            eventName = eventName,
            startAt = startAt,
            endAt = endAt,
            eventStudentMaxCount = eventStudentMaxCount,
            bandIds = bandIds
        )

        val savedEvent = EventFixture.createEvent(id = 1, name = eventName)

        val band1 = BandFixture.createBand(id = 1, name = "Band 1")
        val band2 = BandFixture.createBand(id = 2, name = "Band 2")
        val band3 = BandFixture.createBand(id = 3, name = "Band 3")

        `when`(eventRepository.save(any())).thenReturn(savedEvent)
        `when`(bandRepository.findAllById(eventCreateRequest.bandIds)).thenReturn(listOf(band1, band2, band3))

        //When
        val result = eventService.createEvent(adminId = adminId, eventCreateRequest = eventCreateRequest)

        //Then
        assertThat(result.id).isEqualTo(savedEvent.id)
    }

    @Test
    fun 이벤트를_수정한다() {
        //Given
        val adminId: Long = 1L
        val eventId: Long = 1L
        val originalEventName = "Event 1"
        val newEventName = "Event 2"
        val startAt = LocalDateTime.now()
        val endAt = LocalDateTime.now().plusDays(1)
        val eventStudentMaxCount = 80
        val eventUpdateRequest = EventUpdateRequest(
            eventId = eventId,
            eventName = newEventName,
            startAt = startAt,
            endAt = endAt,
            eventStudentMaxCount = eventStudentMaxCount,
        )

        val savedEvent = EventFixture.createEvent(id = 1L, name = originalEventName)
        val updatedEvent = EventFixture.createEvent(id = 1L, name = newEventName)

        `when`(eventRepository.findById(any())).thenReturn(Optional.of(savedEvent))

        //When
        val result = eventService.updateEvent(adminId = adminId, eventUpdateRequest = eventUpdateRequest)

        //Then
        assertThat(result.name).isEqualTo(updatedEvent.name)
    }

    @Test
    fun 이벤트를_단일조회_한다() {
        //Given
        val adminId = 1L
        val eventId = 1L
        val eventName = "Event 1"
        val event = EventFixture.createEvent(id = eventId, name = eventName)
        val band1 = BandFixture.createBand(id = 1, name = "Band 1")
        val band2 = BandFixture.createBand(id = 2, name = "Band 2")
        val band3 = BandFixture.createBand(id = 3, name = "Band 3")

        val eventBand1 = EventBandFixture.createEventBnd(event, band1)
        val eventBand2 = EventBandFixture.createEventBnd(event, band2)
        val eventBand3 = EventBandFixture.createEventBnd(event, band3)
        val eventBands = listOf(eventBand1, eventBand2, eventBand3)

        `when`(eventRepository.findById(any())).thenReturn(Optional.of(event))
        `when`(eventBandRepository.findAllByEvent(any())).thenReturn(eventBands)

        val expected = EventData.of(event, eventBands)

        //When
        val result = eventService.getEvent(adminId = adminId, eventId = eventId)

        //Then
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun 이벤트를_목록으로_조회_한다() {
        //Given
        val adminId = 1L
        val event1 = EventFixture.createEvent(id = 1, name = "Event 1")
        val event2 = EventFixture.createEvent(id = 2, name = "Event 2")
        val band1 = BandFixture.createBand(id = 1, name = "Band 1")
        val band2 = BandFixture.createBand(id = 2, name = "Band 2")
        val band3 = BandFixture.createBand(id = 3, name = "Band 3")

        val eventBand1 = EventBandFixture.createEventBnd(event1, band1)
        val eventBand2 = EventBandFixture.createEventBnd(event1, band2)
        val eventBand3 = EventBandFixture.createEventBnd(event2, band2)
        val eventBand4 = EventBandFixture.createEventBnd(event2, band3)

        val eventData1 = EventData.of(event1, listOf(eventBand1, eventBand2))
        val eventData2 = EventData.of(event2, listOf(eventBand3, eventBand4))

        `when`(eventRepository.findAllByAdminId(any())).thenReturn(listOf(event1, event2))
        `when`(eventBandRepository.findAllByEvent(event1)).thenReturn(listOf(eventBand1, eventBand2))
        `when`(eventBandRepository.findAllByEvent(event2)).thenReturn(listOf(eventBand3, eventBand4))

        //When
        val result = eventService.getEvents(adminId)

        //Then
        assertThat(result).isEqualTo(listOf(eventData1, eventData2))
    }

    @Test
    fun 이벤트를_삭제한다() {
        //Given
        val adminId = 1L
        val event = EventFixture.createEvent(id = 1L, name = "Event 1")

        `when`(eventRepository.findById(any())).thenReturn(Optional.of(event))

        //When
        eventService.deleteEvent(adminId = adminId, eventId = event.id)

        //Then
        verify(eventRepository, times(1)).delete(event)
    }
}
