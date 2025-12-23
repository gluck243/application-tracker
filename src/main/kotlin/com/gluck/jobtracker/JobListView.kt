package com.gluck.jobtracker

import com.gluck.base.ui.MainLayout
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.dialog.Dialog
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.html.H1
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.Menu
import com.vaadin.flow.router.Route

@Route(value = "", layout = MainLayout::class)
@Menu(title = "Applications", order = 1.0, icon = "vaadin:dashboard")
class JobListView(private val  repository: ApplicationRepository): VerticalLayout() {

    private val grid = Grid(JobApplication::class.java)
    private val jobForm = JobForm()
    private val dialog = Dialog("Job Application Information")

    init {
        addClassName("list-view")
        setSizeFull()
        configureGrid()
        configureDialog()
        configureJobForm()
        add(H1("Job Applications"), getToolbar(), grid)
        updateList()
    }

    private fun configureGrid() {
        grid.setColumns("position", "companyName", "status")
        grid.columns.forEach { it.isAutoWidth = true }
        grid.addItemDoubleClickListener { event ->
            editJob(event.item)
        }
    }

    private fun editJob(job: JobApplication) {
        if(job == null) {
            closeEditor()
        } else {
            jobForm.setJob(job)
            dialog.open()
        }
    }

    private fun closeEditor() {
        jobForm.setJob(null)
        dialog.close()
    }

    private fun updateList() {
        grid.setItems(repository.findAll())
    }

    private fun configureJobForm() {
        jobForm.addSaveListener { event ->
            repository.save(event.job)
            updateList()
            closeEditor()
        }

        jobForm.addDeleteListener { event ->
            repository.delete(event.job)
            updateList()
            closeEditor()
        }

        jobForm.addCancelButton {
            closeEditor()
        }
    }

    private fun configureDialog() {
        dialog.add(jobForm)
    }

    private fun getToolbar(): Component {
        val addJobButton = Button("Add Job")
        addJobButton.addClickListener {
            addJob()
        }
        return HorizontalLayout(addJobButton)
    }

    private fun addJob() {
        grid.asSingleSelect().clear()
        jobForm.setJob(JobApplication())
        dialog.open()
    }

}