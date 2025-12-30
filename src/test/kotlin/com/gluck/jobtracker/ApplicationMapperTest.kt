package com.gluck.jobtracker

import com.gluck.jobtracker.model.JobApplicationEntity
import com.gluck.jobtracker.model.JobApplicationRequest
import com.gluck.jobtracker.model.Status
import com.gluck.jobtracker.service.ApplicationMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate
import kotlin.test.assertEquals

class ApplicationMapperTest {

    private val mapper = ApplicationMapper()

    @Test
    fun `should map request to entity`() {

        val request = JobApplicationRequest(
            "Programmer",
            "Google",
            Status.INTERVIEWING,
            LocalDate.of(2025, 8, 15),
        )

        val entity = mapper.toEntity(request)

        assertThat(entity)
            .usingRecursiveComparison()
            .ignoringFields("id")
            .isEqualTo(request)

        assertEquals(0, entity.id)

    }

    @Test
    fun `should map entity to response`() {

        val entity = JobApplicationEntity(
            1L,
            "Programmer",
            "Google",
            Status.INTERVIEWING,
            LocalDate.of(2025, 8, 15),
        )

        val response = mapper.toResponse(entity)

        assertThat(response)
            .usingRecursiveComparison()
            .isEqualTo(entity)

    }

    @Test
    fun `should map entity to request`() {

        val entity = JobApplicationEntity(
            1L,
            "Programmer",
            "Google",
            Status.INTERVIEWING,
            LocalDate.of(2025, 8, 15),
        )

        val request = mapper.toRequest(entity)

        assertThat(request)
            .usingRecursiveComparison()
            .isEqualTo(entity)


    }

}