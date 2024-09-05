package com.wap.wabi.event.service

import com.wap.wabi.band.fixture.BandFixture
import com.wap.wabi.band.repository.BandRepository
import com.wap.wabi.event.fixture.EventFixture
import com.wap.wabi.event.payload.request.EventCreateRequest
import com.wap.wabi.event.repository.EventBandRepository
import com.wap.wabi.event.repository.EventRepository
import com.wap.wabi.event.repository.EventStudentRepository
import com.wap.wabi.student.repository.StudentRepository
import jakarta.transaction.Transactional
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import java.time.LocalDateTime

@Transactional
@SpringBootTest
@SuppressWarnings("NonAsciiCharacters")
class EventServiceTest {
    @Autowired
    private lateinit var eventService: EventService

    @MockBean
    private lateinit var eventRepository: EventRepository

    @MockBean
    private lateinit var eventStudentRepository: EventStudentRepository

    @MockBean
    private lateinit var studentRepository: StudentRepository

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

        //When & Then
        assertDoesNotThrow {
            eventService.createEvent(adminId = adminId, eventCreateRequest = eventCreateRequest)
        }
    }
}