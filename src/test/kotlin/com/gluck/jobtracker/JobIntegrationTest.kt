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
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put
import org.springframework.transaction.annotation.Transactional
import tools.jackson.databind.ObjectMapper
import java.time.LocalDate
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user

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
            jsonPath("$.content.length()") {value(2)}
            jsonPath("$.content[0].position") {value("Software Developer")}
            jsonPath("$.content[0].status") {value("WISH_LIST")}
            jsonPath("$.content[1].dateApplied") {value("15.08.2025")}
            jsonPath("$.content[1].companyName") {value("Google")}
        }

    }

    @Test
    // @WithMockUser(username = "admin", roles = ["ROLE_ADMIN"])
    fun `should create, get, update and delete a job application`() {

        val request = JobApplicationRequest(
            "Programmer",
            "Google",
            Status.INTERVIEWING,
            LocalDate.of(2025, 8, 15)
        )
        val updateRequest = JobApplicationRequest(
            "Super Programmer",
            "ABC Ltd.",
            Status.OFFER,
            LocalDate.of(2025, 8, 15),
            "DATA ADDED VIA TEST"
        )

        val postResult = mockMvc.post("/api/jobs") {
            with(user("admin").authorities(SimpleGrantedAuthority("ROLE_ADMIN")))
            with(csrf())
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isCreated() }
            header { string("Location", containsString("/api/jobs/")) }
        }.andReturn()

        val jobs = repository.findAll()
        assertEquals(1, jobs.size)
        val savedId = jobs[0].id

        val locationHeader = postResult.response.getHeader("Location")
        assertTrue(locationHeader!!.endsWith("/$savedId"))

        mockMvc.get("/api/jobs/$savedId") {
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            jsonPath("$.position") { value("Programmer") }
            jsonPath("$.companyName") { value("Google") }
        }

        mockMvc.put("/api/jobs/$savedId") {
            with(user("admin").authorities(SimpleGrantedAuthority("ROLE_ADMIN")))
            with(csrf())
            accept = MediaType.APPLICATION_JSON
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(updateRequest)
        }.andExpect {
            status { isOk() }
            jsonPath("$.position") { value("Super Programmer") }
            jsonPath("$.companyName") { value("ABC Ltd.") }
            jsonPath("$.status") { value(Status.OFFER.name) }
            jsonPath("$.description") { value("DATA ADDED VIA TEST").toString() }
        }

        mockMvc.delete("/api/jobs/$savedId") {
            with(user("admin").authorities(SimpleGrantedAuthority("ROLE_ADMIN")))
            with(csrf())
        }.andExpect {
            status { isNoContent() }
        }

        assertTrue(repository.findById(savedId).isEmpty)
        assertEquals(repository.count(), 0)

    }

}
