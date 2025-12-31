package com.gluck.jobtracker

import com.gluck.jobtracker.exception.NoSuchJobFoundException
import com.gluck.jobtracker.model.JobApplicationEntity
import com.gluck.jobtracker.model.JobApplicationRequest
import com.gluck.jobtracker.model.JobApplicationResponse
import com.gluck.jobtracker.model.Status
import com.gluck.jobtracker.repository.ApplicationRepository
import com.gluck.jobtracker.service.ApplicationMapper
import com.gluck.jobtracker.service.JobService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.*
import java.time.LocalDate
import java.util.Optional
import kotlin.test.assertEquals

@ExtendWith(MockitoExtension::class)
class JobServiceTest {

    @Mock
    private lateinit var repository: ApplicationRepository

    @Mock
    private lateinit var mapper: ApplicationMapper

    @InjectMocks
    private lateinit var service: JobService

    @Test
    fun `should save a job application and return id 4`() {

        val mocks = getMockJobs()
        val request = JobApplicationRequest("Software Developer",
            "ABC Ltd.",
            Status.WISH_LIST,
            LocalDate.of(2025, 11, 15))
        val requestCaptor = argumentCaptor<JobApplicationRequest>()

        whenever(mapper.toEntity(request)).thenReturn(mocks[0])
        whenever(repository.save(mocks[0])).thenReturn(mocks[0])

        val saveId = service.saveJob(request)

        verify(repository).save(mocks[0])
        verify(mapper).toEntity(requestCaptor.capture())

        assertEquals("Software Developer", requestCaptor.firstValue.position)
        assertEquals(4L, saveId)

    }

    @Test
    fun `should get all jobs`() {

        val mocks = getMockJobs()
        val response1 = JobApplicationResponse(4L, "Software Developer", "ABC Ltd.", Status.WISH_LIST, LocalDate.of(2025, 11, 15), null)
        val response2 = JobApplicationResponse(7L, position = "Programmer", companyName = "Google", status = Status.INTERVIEWING, LocalDate.of(2025, 8, 15), null)

        whenever(repository.findAll()).thenReturn(mocks)
        whenever(mapper.toResponse(any())).thenReturn(response1).thenReturn(response2)

        val allJobs = service.getAllJobs()

        assertThat(allJobs).containsExactly(response1, response2)

        verify(repository).findAll()
        verify(mapper).toResponse(mocks[0])
        verify(mapper).toResponse(mocks[1])
    }

    @Test
    fun `should filter a company name starting with G`() {

        val existingJob = getMockJobs()[1]
        val response2 = JobApplicationResponse(7L,
            position = "Programmer",
            companyName = "Google",
            status = Status.INTERVIEWING,
            LocalDate.of(2025, 8, 15), null)
        val stringCaptor = argumentCaptor<String>()

        whenever(repository.filterByCompany("G")).thenReturn(listOf(existingJob))
        whenever(mapper.toResponse(existingJob)).thenReturn(response2)

        val filteredJobs = service.findJobsByCompanyName("G")

        verify(repository).filterByCompany(stringCaptor.capture())
        verify(repository, never()).findAll()

        assertEquals("Google", filteredJobs[0].companyName)
        assertEquals("G", stringCaptor.firstValue)

    }

    @Test
    fun `should update existing entity from request`() {

        val existingJob = getMockJobs()[0]
        val updateRequest = JobApplicationRequest(
            "Senior Full-stack Developer",
            "ABC Ltd.",
            Status.INTERVIEWING,
            LocalDate.of(2025, 11, 15)
        )

        whenever(repository.findById(4L)).thenReturn(Optional.of(existingJob))
        whenever(repository.save(existingJob)).thenReturn(existingJob)

        service.updateJob(4L, updateRequest)

        val inOrder = inOrder(repository)
        inOrder.verify(repository).findById(4L)
        inOrder.verify(repository).save(existingJob)

        assertEquals("Senior Full-stack Developer", existingJob.position)
        assertEquals(Status.INTERVIEWING, existingJob.status)

    }

    @Test
    fun `should throw exception when updating non-existent job`() {

        val updateRequest = JobApplicationRequest(
            "Senior Full-stack Developer",
            "ABC Ltd.",
            Status.INTERVIEWING,
            LocalDate.of(2025, 11, 15)
        )

        whenever(repository.findById(99L)).thenReturn(Optional.empty())

        assertThrows<NoSuchElementException> {
            service.updateJob(99L, updateRequest)
        }

        verify(repository, never()).save(any())

    }

    @Test
    fun `should delete job`() {

        val existingJob = getMockJobs()[0]

        whenever(repository.findById(4L)).thenReturn(Optional.of(existingJob))

        service.deleteJob(4L)

        val inOrder = inOrder(repository)
        inOrder.verify(repository).findById(4L)
        inOrder.verify(repository).deleteById(4L)

    }

    @Test
    fun `should throw exception when deleting non-existent job`() {

        whenever(repository.findById(99L)).thenReturn(Optional.empty())

        assertThrows<NoSuchJobFoundException> {
            service.deleteJob(99L)
        }

        verify(repository, never()).deleteById(any())

    }

    @Test
    fun `should find job for editing`() {

        val existingJob = getMockJobs()[0]
        val request = JobApplicationRequest(
            "Software Developer",
            "ABC Ltd.",
            Status.WISH_LIST,
            LocalDate.of(2025, 11, 15)
        )

        whenever(repository.findById(4L)).thenReturn(Optional.of(existingJob))
        whenever(mapper.toRequest(existingJob)).thenReturn(request)

        val foundJob = service.findJobForEditing(4L)

        assertThat(foundJob)
            .usingRecursiveComparison()
            .ignoringFields("id")
            .isEqualTo(request)

        verify(repository).findById(4L)
        verify(mapper).toRequest(existingJob)

    }

    @Test
    fun `should throw exception when finding job for editing`() {

        whenever(repository.findById(99L)).thenReturn(Optional.empty())

        assertThrows<NoSuchJobFoundException> {
            service.findJobForEditing(99L)
        }

        verify(mapper, never()).toRequest(any())

    }

    @Test
    fun `should find job by id`() {

        val existingJob = getMockJobs()[0]
        val response = JobApplicationResponse(
            4L,
            "Software Developer",
            "ABC Ltd.",
            Status.WISH_LIST,
            LocalDate.of(2025, 11, 15),
            ""
        )

        whenever(repository.findById(4L)).thenReturn(Optional.of(existingJob))
        whenever(mapper.toResponse(existingJob)).thenReturn(response)

        val foundJob = service.findJobById(4L)

        assertThat(foundJob)
            .usingRecursiveComparison()
            .ignoringFields("id")
            .isEqualTo(response)

        verify(repository).findById(4L)
        verify(mapper).toResponse(existingJob)

    }

    @Test
    fun `should throw exception when finding job by id`() {

        whenever(repository.findById(99L)).thenReturn(Optional.empty())

        assertThrows<NoSuchJobFoundException> {
            service.findJobById(99L)
        }

        verify(mapper, never()).toResponse(any())

    }

    private fun getMockJobs(): List<JobApplicationEntity> {
        val mockJob1 = JobApplicationEntity(4L,
            "Software Developer",
            "ABC Ltd.",
            Status.WISH_LIST,
            LocalDate.of(2025, 11, 15)
        )
        val mockJob2 = JobApplicationEntity(7L,
            position = "Programmer",
            companyName = "Google",
            status = Status.INTERVIEWING,
            LocalDate.of(2025, 8, 15)
        )

        return listOf(mockJob1, mockJob2)
    }

}