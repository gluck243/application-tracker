package com.gluck.jobtracker.ui

import com.gluck.base.ui.MainLayout
import com.gluck.jobtracker.service.JobService
import com.gluck.jobtracker.model.JobApplicationRequest
import com.gluck.jobtracker.model.JobApplicationResponse
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.dialog.Dialog
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.html.H1
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.renderer.LocalDateRenderer
import com.vaadin.flow.data.value.ValueChangeMode
import com.vaadin.flow.router.Menu
import com.vaadin.flow.router.Route

@Route(value = "", layout = MainLayout::class)
@Menu(title = "Applications", order = 1.0, icon = "vaadin:dashboard")
class JobListView(private val service: JobService): VerticalLayout() {

    private val grid = Grid(JobApplicationResponse::class.java)
    private val jobForm = JobForm()
    private val dialog = Dialog("Job Application Information")
    val filterField = TextField()


    init {
        addClassName("list-view")
        setSizeFull()
        configureGrid()
        configureDialog()
        configureJobForm()
        add(
            H1("My Job Applications"),
            getToolbar(),
            getFilterField(),
            grid
        )
        updateList()
    }

    private fun configureGrid() {
        grid.setColumns("position", "companyName", "status")
        grid.addColumn(LocalDateRenderer(JobApplicationResponse::dateApplied, "dd.MM.yyyy")).setHeader("Date Applied")
        grid.columns.forEach { it.isAutoWidth = true }
        grid.addItemDoubleClickListener { event ->
            editJob(event.item)
        }
    }

    private fun editJob(dto: JobApplicationResponse) {
        val requestDto = service.findJob(dto.id)
        jobForm.setJob(requestDto, dto.id)
        dialog.open()

    }

    private fun closeEditor() {
        jobForm.setJob(null)
        dialog.close()
    }

    private fun configureJobForm() {
        jobForm.addSaveListener { event ->
            if (event.jobId == null)
                service.saveJob(event.job)
            else
                service.updateJob(event.jobId, event.job)
            updateList()
            closeEditor()
        }

        jobForm.addDeleteListener { event ->
            if (event.jobId != null) {
                service.deleteJob(event.jobId)
                updateList()
                closeEditor()
            }
        }

        jobForm.addCancelButton {
            closeEditor()
        }
    }

    private fun updateList() {
        if (filterField.value.isNullOrEmpty())
            grid.setItems(service.getAllJobs())
        else
            grid.setItems(service.findJobsByCompanyName(filterField.value))
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
        jobForm.setJob(JobApplicationRequest())
        dialog.open()
    }

    private fun getFilterField(): Component {
        filterField.valueChangeMode = ValueChangeMode.LAZY
        filterField.addValueChangeListener {
            updateList()
        }
        return HorizontalLayout(filterField)
    }

}