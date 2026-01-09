package com.gluck.jobtracker.ui

import com.vaadin.flow.component.login.LoginOverlay
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.BeforeEnterEvent
import com.vaadin.flow.router.BeforeEnterObserver
import com.vaadin.flow.router.Route
import com.vaadin.flow.server.auth.AnonymousAllowed

@Route("login")
@AnonymousAllowed
class LoginView: VerticalLayout(), BeforeEnterObserver {

    val loginOverlay = LoginOverlay()

    override fun beforeEnter(event: BeforeEnterEvent) {
        val params = event.location.queryParameters.parameters
        if (params.containsKey("error"))
            loginOverlay.isError = true
    }

    init {
        loginOverlay.apply {
            isOpened = true
            action = "login"
            setTitle("Job Application Tracker")
            description = "Log in to edit records"
        }
    }

}