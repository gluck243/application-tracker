package com.gluck.jobtracker

import com.gluck.jobtracker.model.JobApplicationEntity
import com.gluck.jobtracker.model.JobApplicationRequest
import com.gluck.jobtracker.model.Status
import com.gluck.jobtracker.repository.ApplicationRepository
import org.hamcrest.Matchers.containsString
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.transaction.annotation.Transactional
import tools.jackson.databind.ObjectMapper
import java.time.LocalDate
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class JobIntegrationTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var repository: ApplicationRepository

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @BeforeEach
    fun setup() {
        repository.deleteAll()
    }

    @Test
    fun `should get all job applications`() {

        repository.save(JobApplicationEntity(
            position = "Software Developer",
            companyName = "ABC Ltd.",
            status = Status.WISH_LIST,
            dateApplied = LocalDate.of(2025, 11, 15)
        ))
        repository.save(JobApplicationEntity(
            position = "Programmer",
            companyName = "Google",
            status = Status.INTERVIEWING,
            dateApplied = LocalDate.of(2025, 8, 15)
        ))

        mockMvc.get("/api/jobs") {
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            jsonPath("$.length()") {value(2)}
            jsonPath("$[0].position") {value("Software Developer")}
            jsonPath("$[0].status") {value("WISH_LIST")}
            jsonPath("$[1].dateApplied") {value("15.08.2025")}
            jsonPath("$[1].companyName") {value("Google")}
        }

    }

    @Test
    fun `should create job application`() {

        val request = JobApplicationRequest(
            "Programmer",
            "Google",
            Status.INTERVIEWING,
            LocalDate.of(2025, 8, 15),
        )

        val result = mockMvc.post("/api/jobs") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isCreated() }
            header { string("Location", containsString("/api/jobs/")) }
        }.andReturn()

        val jobs = repository.findAll()

        assertEquals(1, jobs.size)
        assertEquals("Programmer", jobs[0].position)
        assertEquals("Google", jobs[0].companyName)


        val locationHeader = result.response.getHeader("Location")
        assertTrue(locationHeader!!.endsWith("/" + jobs[0].id))

    }

//    @Test
//    fun `should delete user successfully`() {
//
//    }

}