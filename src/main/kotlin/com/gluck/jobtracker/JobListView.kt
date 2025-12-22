package com.gluck.jobtracker

import com.gluck.base.ui.MainLayout
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.html.H1
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.binder.Binder
import com.vaadin.flow.router.Menu
import com.vaadin.flow.router.Route

@Route(value = "", layout = MainLayout::class)
@Menu(title = "Applications", order = 1.0, icon = "vaadin:dashboard")
class JobListView(private val  repository: ApplicationRepository): VerticalLayout() {

    private val grid = Grid(JobApplication::class.java)
    private val jobForm = JobForm()

    private val binder: Binder<JobApplication> = Binder()
    private val positionField = TextField()
    private val companyNameField = TextField()
    private val statusField = TextField()

    init {
        addClassName("list-view")
        setSizeFull()
        configureGrid()
        add(H1("Job Applications"), jobForm, grid)
        updateList()
    }

    private fun configureGrid() {
        grid.setColumns("position", "companyName", "status")
        grid.columns.forEach { it.isAutoWidth = true }
    }

    private fun updateList() {
        grid.setItems(repository.findAll())
    }

}