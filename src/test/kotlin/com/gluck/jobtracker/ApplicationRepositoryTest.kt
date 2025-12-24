package com.gluck.jobtracker


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
        val record1 = JobApplication(position = "Backend Dev", companyName = "Abc Ltd")
        val record2 = JobApplication(position = "Programmer", companyName = "Google", status = Status.INTERVIEWING)
        val record3 = JobApplication(position = "Code Pro", companyName = "Bongo", status = Status.APPLIED)

        repository.save(record1)
        repository.save(record2)
        repository.save(record3)

        val filterResult = repository.filterByCompany("goo")

        assertEquals(filterResult.size, 1)
        assertEquals(filterResult[0].companyName, "Google")
    }

}