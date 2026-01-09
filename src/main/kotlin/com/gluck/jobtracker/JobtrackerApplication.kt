package com.gluck.jobtracker

import com.vaadin.flow.component.dependency.StyleSheet
import com.vaadin.flow.theme.lumo.Lumo
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
@StyleSheet(Lumo.STYLESHEET)
@StyleSheet(Lumo.UTILITY_STYLESHEET)
@StyleSheet("styles.css")
class JobtrackerApplication

    fun main(args: Array<String>) {
        runApplication<JobtrackerApplication>(*args)
    }

