package com.gluck.jobtracker


import com.gluck.jobtracker.repository.JobApplicationEntity
import com.gluck.jobtracker.model.Status
import com.gluck.jobtracker.repository.ApplicationRepository
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest
import org.springframework.data.domain.PageRequest
import kotlin.test.assertEquals

@DataJpaTest
class ApplicationRepositoryTest {

    @Autowired
    lateinit var repository: ApplicationRepository

    @Test
    fun `should filter companies by name`() {
        val record1 = JobApplicationEntity(0L, "Backend Dev", "Abc Ltd", Status.APPLIED, null, "")
        val record2 = JobApplicationEntity(0L, "Programmer", "Google", Status.INTERVIEWING, null, "")
        val record3 = JobApplicationEntity(0L, "Code Pro", "Bongo", Status.APPLIED, null, "")

        repository.saveAll(listOf(record1, record2, record3))

        val filterResult = repository.searchByCompany("goo", PageRequest.of(0, 10))

        assertEquals(1, filterResult.totalElements)

        assertEquals("Google", filterResult.content[0].companyName)
    }

}