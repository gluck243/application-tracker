package com.gluck.jobtracker

import com.gluck.jobtracker.controllers.JobController
import com.gluck.jobtracker.exception.NoSuchJobFoundException
import com.gluck.jobtracker.model.JobApplicationRequest
import com.gluck.jobtracker.model.JobApplicationResponse
import com.gluck.jobtracker.model.Status
import com.gluck.jobtracker.service.JobService
import org.hamcrest.Matchers.containsString
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.check
import org.mockito.kotlin.eq
import org.mockito.kotlin.never
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import tools.jackson.databind.ObjectMapper
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put
import java.time.LocalDate
import kotlin.test.assertEquals

@WebMvcTest(JobController::class)
class JobControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockitoBean
    private lateinit var service: JobService

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Test
    fun `GET all job applications should return 200 OK`() {

        val mocks = getMockResponses()

        val pageOfMocks = PageImpl(mocks)

        whenever(service.getJobs(any(), org.mockito.kotlin.anyOrNull())).thenReturn(pageOfMocks)

        mockMvc.get("/api/jobs") {
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }

            jsonPath("$.content.length()") { value(2) }
            jsonPath("$.content[0].id") { value(4) }
            jsonPath("$.content[0].position") { value("Software Developer") }
            jsonPath("$.content[1].dateApplied") { value("15.08.2025") }
            jsonPath("$.content[1].companyName") { value("Google") }
            jsonPath("$.totalElements") { value(2) }
        }.andDo { print() }

    }

    @Test
    fun `POST create new job should return 201 CREATED and ID`() {

        val request = JobApplicationRequest(
            "Programmer",
            "Google",
            Status.INTERVIEWING,
            LocalDate.of(2025, 8, 15),
            null
        )

        whenever(service.saveJob(any())).thenReturn(6L)

        mockMvc.post("/api/jobs") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isCreated() }
            header { string("Location", containsString("/6")) }
        }.andDo { print() }

    }

    @Test
    fun `POST create job should return 400 BAD REQUEST on invalid input`() {

        val invalidRequest = JobApplicationRequest(
            "",
            "",
            Status.INTERVIEWING,
            LocalDate.of(2025, 8, 15),
            null
        )

        mockMvc.post("/api/jobs") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(invalidRequest)
        }.andExpect {
            status { isBadRequest() }
            content { string(containsString("position")) }
            content { string(containsString("companyName")) }
        }

        verify(service, never()).saveJob(any())

    }

    @Test
    fun `GET job by id should return 200 OK`() {

        val response = getMockResponses()[0]

        whenever(service.findJobById(response.id)).thenReturn(response)

        mockMvc.get("/api/jobs/${response.id}") {
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            jsonPath("$.id") { value(response.id) }
            jsonPath("$.companyName") { value(response.companyName) }
        }.andDo { print() }

    }

    @Test
    fun `GET job by id should return 404 when job does not exist`() {

        val invalidId = 99L
        val errorMsg = "No matching job found by $invalidId"

        whenever(service.findJobById(invalidId))
            .thenThrow(NoSuchJobFoundException(errorMsg))

        mockMvc.get("/api/jobs/${invalidId}") {
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isNotFound() }
            jsonPath("$.error") { value(errorMsg) }
        }

    }

    @Test
    fun `PUT job by id should update existing job and return it`() {

        val request = JobApplicationRequest(
            "Software Developer",
            "ABC Ltd.",
            Status.WISH_LIST,
            LocalDate.of(2025, 11, 15),
            null
        )
        val response = getMockResponses()[0]

        whenever(service.updateJobById(eq(response.id), any())).thenReturn(response)

        mockMvc.put("/api/jobs/${response.id}") {
            accept = MediaType.APPLICATION_JSON
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            jsonPath("$.position") { value(response.position) }
            jsonPath("$.companyName") { value(response.companyName) }
            jsonPath("$.status") { value(response.status.toString()) }
        }.andDo { print() }

        verify(service).updateJobById(eq(response.id), check { serviceArg ->
            assertEquals(request.companyName, serviceArg.companyName)
            assertEquals(request.position, serviceArg.position)
        })

    }

    @Test
    fun `PUT job by id should return 404 when job does not exist`() {

        val invalidId = 99L
        val errorMsg = "No matching job found by $invalidId"
        val request = JobApplicationRequest(
            "Software Developer",
            "ABC Ltd.",
            Status.WISH_LIST,
            LocalDate.of(2025, 11, 15),
            null
        )

        whenever(service.updateJobById(eq(invalidId), any()))
            .thenThrow(NoSuchJobFoundException(errorMsg))

        mockMvc.put("/api/jobs/${invalidId}") {
            accept = MediaType.APPLICATION_JSON
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isNotFound() }
            jsonPath("$.error") { value(errorMsg) }
        }

    }

    @Test
    fun `DELETE job by id should delete job and return NO CONTENT`() {
        val response = getMockResponses()[0]

        mockMvc.delete("/api/jobs/${response.id}") {
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isNoContent() }
        }.andDo { print() }

        verify(service, times(1)).deleteJob(response.id)
    }

    @Test
    fun `DELETE job by id should return 404 when job does not exist`() {

        val invalidId = 99L
        val errorMsg = "No matching job found by $invalidId"

        whenever(service.deleteJob(invalidId))
            .thenThrow(NoSuchJobFoundException(errorMsg))

        mockMvc.delete("/api/jobs/${invalidId}") {
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isNotFound() }
            jsonPath("$.error") { value(errorMsg) }
        }

    }

    private fun getMockResponses(): List<JobApplicationResponse> {
        val mockJob1 = JobApplicationResponse(4L,
            "Software Developer",
            "ABC Ltd.",
            Status.WISH_LIST,
            LocalDate.of(2025, 11, 15),
            null
        )
        val mockJob2 = JobApplicationResponse(7L,
            position = "Programmer",
            companyName = "Google",
            status = Status.INTERVIEWING,
            LocalDate.of(2025, 8, 15),
            null
        )

        return listOf(mockJob1, mockJob2)
    }

}