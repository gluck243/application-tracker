package com.gluck.jobtracker

import com.gluck.jobtracker.controllers.JobController
import com.gluck.jobtracker.model.JobApplicationRequest
import com.gluck.jobtracker.model.JobApplicationResponse
import com.gluck.jobtracker.model.Status
import com.gluck.jobtracker.service.JobService
import org.hamcrest.Matchers.containsString
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import tools.jackson.databind.ObjectMapper
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import java.time.LocalDate

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

        val mocks = getMockJobs()

        whenever(service.getAllJobs()).thenReturn(mocks)

        mockMvc.get("/api/jobs") {
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            jsonPath("$.length()") {value(2)}
            jsonPath("$[0].id") {value(4)}
            jsonPath("$[0].position") {value("Software Developer")}
            jsonPath("$[1].dateApplied") {value("15.08.2025")}
            jsonPath("$[1].companyName") {value("Google")}
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

        mockMvc.post("/api/job") {
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

        mockMvc.post("/api/job") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(invalidRequest)
        }.andExpect {
            status { isBadRequest() }
            content { string(containsString("position")) }
            content { string(containsString("companyName")) }
        }

        verify(service, never()).saveJob(any())

    }

    private fun getMockJobs(): List<JobApplicationResponse> {
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