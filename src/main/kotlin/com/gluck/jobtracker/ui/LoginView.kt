package com.gluck.jobtracker.ui

import com.vaadin.flow.component.UI
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.html.Span
import com.vaadin.flow.component.login.LoginI18n
import com.vaadin.flow.component.login.LoginOverlay
import com.vaadin.flow.component.orderedlayout.FlexComponent
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
        val i18n = LoginI18n.createDefault()

        i18n.apply {
            form.apply {
                username = "Username"
                password = "Password"
                submit = "Sign In"
                forgotPassword = "Lost access?"
            }

            errorMessage.apply {
                title = "Access Denied"
                message = "Check your Caps Lock or password"
            }
        }

        val backButton = Button("<-- Go Back")
        backButton.apply {
            addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_CONTRAST)
            addClickListener {
                loginOverlay.isOpened = false
                UI.getCurrent().navigate("")
            }
        }

        val footerText = Span("Don't have an account? Too bad, it's invite only")
        footerText.apply {
            style.set("font-size", "var(--lumo-font-size-xs)")
            style.set("color", "var(--lumo-secondary-text-color)")
        }
        val footerLayout = VerticalLayout(footerText, backButton)
        footerLayout.apply {
            alignItems = FlexComponent.Alignment.CENTER
            isPadding = false
        }

        loginOverlay.apply {
            setTitle("Job Application Tracker")
            description = "Please log in to manage your applications."
            setI18n(i18n)
            footer.add(footerLayout)
            isForgotPasswordButtonVisible = false
            isOpened = true
            action = "login"
        }
    }

}