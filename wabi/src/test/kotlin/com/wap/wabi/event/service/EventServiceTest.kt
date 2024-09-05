package com.wap.wabi.event.service

import com.wap.wabi.band.entity.QBand.band
import com.wap.wabi.band.fixture.BandFixture
import com.wap.wabi.band.repository.BandRepository
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
import java.util.*

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
        val eventCreateRequest = EventCreateRequest.of(
            eventName = "test",
            eventStudentMaxCount = 80,
            bandIds = listOf(1, 2, 3)
        )

        val band1 = BandFixture.createBand("Test1")
        val band2 = BandFixture.createBand("Test2")
        val band3 = BandFixture.createBand("Test3")

        `when`(bandRepository.findAllById(eventCreateRequest.bandIds)).thenReturn(listOf(band1, band2, band3))

        //When
        val result: Long = eventService.createEvent(adminId, eventCreateRequest)

        //Then
        assertThat(result).isEqualTo(1L)
    }
}