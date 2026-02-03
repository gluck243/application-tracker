package com.gluck.jobtracker


import com.gluck.jobtracker.model.Status
import com.gluck.jobtracker.repository.ApplicationRepository
import com.gluck.jobtracker.repository.JobApplicationEntity
import org.junit.jupiter.api.Test
import org.mockito.kotlin.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest
import org.springframework.data.domain.PageRequest
import kotlin.test.assertEquals

@DataJpaTest
class ApplicationRepositoryTest {

    @Autowired
    lateinit var repository: ApplicationRepository

    @Test
    fun `should save and filter companies by name`() {

        repository.saveAll(getRecords())

        val filterResult = repository.searchByCompany("goo", PageRequest.of(0, 10))

        assertEquals(1, filterResult.totalElements)

        assertEquals("Google", filterResult.content[0].companyName)
    }

    @Test
    fun `should save and count total jobs as 3`() {

        repository.saveAll(getRecords())

        val allJobs = repository.count()

        assertEquals(3, allJobs)

    }

    @Test
    fun `should save and count applied jobs as 2 and interviewing as 1`() {

        repository.saveAll(getRecords())

        val applied = repository.countByStatusIs(Status.APPLIED)
        val interviewing = repository.countByStatusIs(Status.INTERVIEWING)

        assertEquals(2, applied)
        assertEquals(1, interviewing)

    }

    @Test
    fun `should save and filter companies by position`() {

        repository.saveAll(getRecords())

        val filterResult = repository.searchByPosition("pro", PageRequest.of(0, 10))

        assertEquals(2, filterResult.totalElements)

        assertEquals("Programmer", filterResult.content[0].position)
        assertEquals("Code Pro", filterResult.content[1].position)

    }

    @Test
    fun `should save and filter companies by description`() {

        repository.saveAll(getRecords())

        val filterResult = repository.searchByDescription("test", PageRequest.of(0, 10))

        assertEquals(3, filterResult.totalElements)

    }

    private fun getRecords(): List<JobApplicationEntity> {
        return listOf(
            JobApplicationEntity(
                0L,
                "Backend Dev",
                "Abc Ltd",
                Status.APPLIED, null,
                "Testing"
            ),
            JobApplicationEntity(
                0L,
                "Programmer",
                "Google",
                Status.INTERVIEWING,
                null,
                "I want to write a test"
            ),
            JobApplicationEntity(
                0L,
                "Code Pro",
                "Bongo",
                Status.APPLIED,
                null,
                "Test"
            )
        )
    }

}