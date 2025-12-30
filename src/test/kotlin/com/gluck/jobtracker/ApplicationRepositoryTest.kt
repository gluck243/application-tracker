package com.gluck.jobtracker


import com.gluck.jobtracker.model.JobApplicationEntity
import com.gluck.jobtracker.model.Status
import com.gluck.jobtracker.repository.ApplicationRepository
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest
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

        repository.save(record1)
        repository.save(record2)
        repository.save(record3)

        val filterResult = repository.filterByCompany("goo")

        assertEquals(filterResult.size, 1)
        assertEquals(filterResult[0].companyName, "Google")
    }

}