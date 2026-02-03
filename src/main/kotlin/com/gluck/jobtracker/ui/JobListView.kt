package com.gluck.jobtracker.ui

import com.gluck.base.ui.MainLayout
import com.gluck.jobtracker.service.JobService
import com.gluck.jobtracker.model.JobApplicationRequest
import com.gluck.jobtracker.model.JobApplicationResponse
import com.gluck.jobtracker.model.JobFilter
import com.gluck.jobtracker.model.Status
import com.gluck.jobtracker.service.SecurityService
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.dialog.Dialog
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.html.H2
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.select.Select
import com.vaadin.flow.component.textfield.NumberField
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider
import com.vaadin.flow.data.provider.DataProvider
import com.vaadin.flow.data.provider.Query
import com.vaadin.flow.data.renderer.LocalDateRenderer
import com.vaadin.flow.data.value.ValueChangeMode
import com.vaadin.flow.router.Menu
import com.vaadin.flow.router.Route
import com.vaadin.flow.server.auth.AnonymousAllowed
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort

@Route(value = "", layout = MainLayout::class)
@AnonymousAllowed
@Menu(title = "Applications", order = 1.0, icon = "vaadin:dashboard")
class JobListView(private val service: JobService, private val securityService: SecurityService): VerticalLayout() {

    private val grid = Grid(JobApplicationResponse::class.java)
    private val jobForm = JobForm()
    private val dialog = Dialog("Job Application Information")
    private val addJobButton = Button("Add Job")
    private val searchField = TextField()
    private val searchSelector = Select<String>()
    private val login = Button("Login")
    private val logout = Button("Logout")
    private lateinit var dataProvider: ConfigurableFilterDataProvider<JobApplicationResponse, Void, JobFilter>
    private val counterSelector = Select<String>()
    private val counter = NumberField()

    init {
        addClassName("list-view")
        setSizeFull()
        configureGrid()
        configureDialog()
        configureJobForm()
        add(
            getToolbar(),
            getSearchToolbar(),
            grid
        )
    }

    private fun configureGrid() {

        val baseProvider = DataProvider.fromFilteringCallbacks(
            { query: Query<JobApplicationResponse, JobFilter> ->
                val offset = query.offset
                val limit = query.limit

                val pageable = PageRequest.of(
                    offset / limit,
                    limit,
                    Sort.by("dateApplied").descending()
                )

                val filter = query.filter.orElse(JobFilter())

                service.getJobs(pageable, filter.searchTerm, filter.searchBy ?: "Company").content.stream()
            },
            { query: Query<JobApplicationResponse, JobFilter> ->
                val filter = query.filter.orElse(JobFilter())
                service.countJobs(filter.searchTerm, filter.searchBy ?: "Company").toInt()
            }
        )

        dataProvider = baseProvider.withConfigurableFilter()
        grid.setItems(dataProvider)
        grid.apply {
            setColumns("position", "companyName", "status")
            addColumn(LocalDateRenderer({ it.dateApplied() }, "dd.MM.yyyy")).setHeader("Date Applied")
            addColumn("description")
            columns.forEach { it.isAutoWidth = true }
        }

        if (securityService.getAuthenticatedUser().isPresent) {
            grid.addItemDoubleClickListener { event ->
                editJob(event.item)
            }
        }

    }

    private fun getSearchToolbar(): Component {

        searchSelector.apply {
            label = "Search by"
            setItems("Company", "Position", "Description")
            value = "Company"
            addValueChangeListener { refreshGrid() }
        }

        searchField.apply {
            placeholder = "Filter..."
            isClearButtonVisible = true
            valueChangeMode = ValueChangeMode.LAZY
            addValueChangeListener { refreshGrid() }
        }

        counterSelector.apply {
            label = "Count by"
            setItems("Total", "Rejected", "Ghosted", "Interviewing", "Offer")
            value = "Total"
            style.set("margin-left", "auto")
            addValueChangeListener { changeCounter() }
        }

        counter.apply {
            isReadOnly = true
            style.set("margin-left", "auto")
            updateCounter()
        }

        val rightToolbar = HorizontalLayout(counterSelector, counter)
        rightToolbar.apply {
            defaultVerticalComponentAlignment = FlexComponent.Alignment.BASELINE
            style.set("margin-left", "auto")
        }

        val mainToolbar = HorizontalLayout(searchSelector, searchField, rightToolbar)
        mainToolbar.apply {
            setWidthFull()
            defaultVerticalComponentAlignment = FlexComponent.Alignment.BASELINE
        }

        return mainToolbar

    }

    private fun refreshGrid() {
        val filterObj = JobFilter().apply {
            searchTerm = searchField.value ?: ""
            searchBy = searchSelector.value ?: "Company"
        }
        dataProvider.setFilter(filterObj)
    }

    private fun editJob(dto: JobApplicationResponse) {
        val requestDto = service.findJobForEditing(dto.id())
        jobForm.setJob(requestDto, dto.id())
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
                service.updateJobById(event.jobId, event.job)
            dataProvider.refreshAll()
            updateCounter()
            closeEditor()
        }

        jobForm.addDeleteListener { event ->
            if (event.jobId != null) {
                service.deleteJob(event.jobId)
                dataProvider.refreshAll()
                updateCounter()
                closeEditor()
            }
        }

        jobForm.addCancelButton {
            closeEditor()
        }
    }

    private fun configureDialog() {
        dialog.add(jobForm)
    }

    private fun getToolbar(): Component {
        val toolbar = HorizontalLayout()
        toolbar.apply {
            setWidthFull()
            addClassName("toolbar")
            defaultVerticalComponentAlignment = FlexComponent.Alignment.CENTER
            justifyContentMode = FlexComponent.JustifyContentMode.BETWEEN
        }

        val title = H2("Job Tracker")
        title.style.set("margin", "0")

        val actions = HorizontalLayout()

        if (securityService.getAuthenticatedUser().isPresent) {
            logout.addClickListener { securityService.logout() }
            addJobButton.addClickListener { addJob() }
            actions.add(addJobButton, logout)
        }

        else {
            login.addClickListener {
                UI.getCurrent().navigate(LoginView::class.java)
            }
            actions.add(login)
        }

        toolbar.add(title, actions)

        toolbar.style.set("background-color", "#f5f5f5")
        toolbar.style.set("padding", "10px 20px")

        return toolbar
    }

    private fun addJob() {
        grid.asSingleSelect().clear()
        jobForm.setJob(JobApplicationRequest())
        dialog.open()
    }

    private fun updateCounter() {
        counter.value = service.countJobs(null, null).toDouble()
    }

    private fun changeCounter() {
        when (counterSelector.value) {
            "Rejected" -> counter.value = service.countByStatus(Status.REJECTED).toDouble()
            "Ghosted" -> counter.value = service.countByStatus(Status.GHOSTED).toDouble()
            "Interviewing" -> counter.value = service.countByStatus(Status.INTERVIEWING).toDouble()
            "Offer" -> counter.value = service.countByStatus(Status.OFFER).toDouble()
            else -> updateCounter()
        }
    }

}