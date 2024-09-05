package com.wap.wabi.event.service

import com.wap.wabi.band.fixture.BandFixture
import com.wap.wabi.band.repository.BandRepository
import com.wap.wabi.event.payload.request.EventCreateRequest
import com.wap.wabi.event.repository.EventRepository
import com.wap.wabi.event.repository.EventStudentRepository
import com.wap.wabi.student.repository.StudentRepository
import jakarta.transaction.Transactional
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
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
    private lateinit var bandRepository: BandRepository

    @Test
    fun 이벤트를_생성한다() {
        //Given
        val adminId: Long = 1L
        val eventCreateRequest = EventCreateRequest(
            eventName = "Event 1",
            startAt = LocalDateTime.now(),
            endAt = LocalDateTime.now().plusDays(1),
            eventStudentMaxCount = 80,
            bandIds = listOf(1, 2, 3)
        )

        val band1 = BandFixture.createBand("Band 1")
        val band2 = BandFixture.createBand("Band 2")
        val band3 = BandFixture.createBand("Band 3")

        `when`(eventRepository.save(eventCreateRequest.toEventEntity(adminId))).thenReturn(null)
        `when`(bandRepository.findAllById(eventCreateRequest.bandIds)).thenReturn(listOf(band1, band2, band3))

        //When
        val result = eventService.createEvent(adminId = adminId, eventCreateRequest = eventCreateRequest)

        //Then
        assertThat(result).isEqualTo(1)
    }
}