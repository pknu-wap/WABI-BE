package com.wap.wabi.event.service

import com.wap.wabi.band.fixture.BandFixture
import com.wap.wabi.band.fixture.BandStudentFixture
import com.wap.wabi.band.repository.BandRepository
import com.wap.wabi.band.repository.BandStudentRepository
import com.wap.wabi.common.TestConstants
import com.wap.wabi.event.entity.Enum.EventStudentStatus
import com.wap.wabi.event.fixture.EventBandFixture
import com.wap.wabi.event.fixture.EventFixture
import com.wap.wabi.event.fixture.EventStudentFixture
import com.wap.wabi.event.payload.request.CheckInRequest
import com.wap.wabi.event.payload.request.EventCreateRequest
import com.wap.wabi.event.payload.request.EventUpdateRequest
import com.wap.wabi.event.payload.response.EventData
import com.wap.wabi.event.repository.EventBandRepository
import com.wap.wabi.event.repository.EventRepository
import com.wap.wabi.event.repository.EventStudentRepository
import com.wap.wabi.student.fixture.StudentFixture
import com.wap.wabi.student.repository.StudentRepository
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
    private lateinit var studentRepository: StudentRepository

    @MockBean
    private lateinit var eventStudentRepository: EventStudentRepository

    @MockBean
    private lateinit var eventRepository: EventRepository

    @MockBean
    private lateinit var eventBandRepository: EventBandRepository

    @MockBean
    private lateinit var bandRepository: BandRepository

    @MockBean
    private lateinit var bandStudentRepository: BandStudentRepository

    @Test
    fun 이벤트를_생성한다() {
        //Given
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
        val result = eventService.createEvent(adminId = TestConstants.ADMIN_ID, eventCreateRequest = eventCreateRequest)

        //Then
        assertThat(result.id).isEqualTo(savedEvent.id)
    }

    @Test
    fun 특정_밴드에_속한_학생들을_이벤트에_참여시킨다() {
        //Given
        val event1 = EventFixture.createEvent("Event1")
        val band1 = BandFixture.createBand("Band1")
        val student1 = StudentFixture.createStudent("Student1")
        val student2 = StudentFixture.createStudent("Student2")
        val bandStudent1 = BandStudentFixture.createBandStudent(student = student1, band = band1)
        val bandStudent2 = BandStudentFixture.createBandStudent(student = student2, band = band1)

        `when`(bandStudentRepository.findAllByBand(any())).thenReturn(listOf(bandStudent1, bandStudent2))

        val expected = 2

        //When
        val result = eventService.saveEventStudentsFromBand(event = event1, band = band1)

        //Then
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun 이벤트를_수정한다() {
        //Given
        val eventId: Long = 1
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

        val savedEvent = EventFixture.createEvent(id = eventId, name = originalEventName)
        val updatedEvent = EventFixture.createEvent(id = eventId, name = newEventName)

        `when`(eventRepository.findById(any())).thenReturn(Optional.of(savedEvent))

        //When
        val result = eventService.updateEvent(adminId = TestConstants.ADMIN_ID, eventUpdateRequest = eventUpdateRequest)

        //Then
        assertThat(result.name).isEqualTo(updatedEvent.name)
    }

    @Test
    fun 이벤트를_단일조회_한다() {
        //Given
        val eventId = 1L
        val event = EventFixture.createEvent(id = eventId, name = "Event1")
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
        val result = eventService.getEvent(adminId = TestConstants.ADMIN_ID, eventId = eventId)

        //Then
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun 이벤트를_목록으로_조회_한다() {
        //Given
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
        val result = eventService.getEvents(TestConstants.ADMIN_ID)

        //Then
        assertThat(result).isEqualTo(listOf(eventData1, eventData2))
    }

    @Test
    fun 이벤트를_삭제한다() {
        //Given
        val eventId = 1L
        val event = EventFixture.createEvent(id = eventId, name = "Event 1")

        `when`(eventRepository.findById(any())).thenReturn(Optional.of(event))

        //When
        eventService.deleteEvent(adminId = TestConstants.ADMIN_ID, eventId = eventId)

        //Then
        verify(eventRepository, times(1)).delete(event)
    }

    @Test
    fun 이벤트에_체크인_한다() {
        //Given
        val checkInRequest = CheckInRequest(
            studentId = "201912050",
            eventId = 1
        )

        val event = EventFixture.createEvent(id = 1, name = "Event 1")
        val band = BandFixture.createBand(id = 1, name = "Band 1")
        val student = StudentFixture.createStudent(id = "201912050", name = "Student1")
        val eventStudent = EventStudentFixture.createEventStudent(id = 1, event = event, student = student, band = band)

        `when`(studentRepository.findById(any())).thenReturn(Optional.of(student))
        `when`(eventRepository.findById(any())).thenReturn(Optional.of(event))
        `when`(eventStudentRepository.findByStudentAndEvent(any(), any())).thenReturn(Optional.of(eventStudent))

        //When
        val result = eventService.checkIn(checkInRequest)

        //Then
        assertThat(result).isEqualTo(EventStudentStatus.CHECK_IN)
    }

    @Test
    fun 이벤트의_주최자인지_판단한다() {
        //Given
        val event1 = EventFixture.createEvent(id = 1, name = "Event1")

        //When
        val result = eventService.validateEventOwner(adminId = TestConstants.ADMIN_ID, event = event1)

        //Then
        assertThat(result).isTrue()
    }


}
