package com.gluck.jobtracker

import com.gluck.base.ui.MainLayout
import com.vaadin.flow.component.html.H1
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.Route

@Route(value = "", layout = MainLayout::class)
class JobListView: VerticalLayout() {

    init {
        add(H1("Hello from Kotlin View!"))
    }

}