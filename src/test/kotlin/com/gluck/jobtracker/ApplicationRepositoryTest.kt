package com.gluck.jobtracker


import com.gluck.jobtracker.model.JobApplicationEntity
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
        val record1 = JobApplicationEntity(position = "Backend Dev", companyName = "Abc Ltd")
        val record2 = JobApplicationEntity(position = "Programmer", companyName = "Google", status = Status.INTERVIEWING)
        val record3 = JobApplicationEntity(position = "Code Pro", companyName = "Bongo", status = Status.APPLIED)

        repository.saveAll(listOf(record1, record2, record3))

        val filterResult = repository.searchByCompany("goo", PageRequest.of(0, 10))

        assertEquals(1, filterResult.totalElements)

        assertEquals("Google", filterResult.content[0].companyName)
    }

}