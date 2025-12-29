package com.gluck.jobtracker.ui

import com.gluck.jobtracker.model.JobApplicationRequest
import com.gluck.jobtracker.model.Status
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.ComponentEventListener
import com.vaadin.flow.component.Key
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.combobox.ComboBox
import com.vaadin.flow.component.datepicker.DatePicker
import com.vaadin.flow.component.formlayout.FormLayout
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.binder.BeanValidationBinder
import com.vaadin.flow.data.binder.ValidationException
import com.vaadin.flow.shared.Registration
import java.time.LocalDate
import java.time.ZoneId

class JobForm: FormLayout() {

    private val position = TextField("Position")
    private val companyName = TextField("Company Name")
    private val status: ComboBox<Status> = ComboBox("Application status")
    private val dateApplied = DatePicker("Application date")

    private val saveButton = Button(Icon(VaadinIcon.SAFE))
    private val deleteButton = Button(Icon(VaadinIcon.TRASH))
    private val cancelButton = Button(Icon(VaadinIcon.CLOSE))

    private var job = JobApplicationRequest()
    private var currentJobId: Long? = null
    val binder: BeanValidationBinder<JobApplicationRequest> = BeanValidationBinder(JobApplicationRequest::class.java)

    init {
        status.setItems(Status.entries)

        binder.bindInstanceFields(this)

        add(
            position,
            companyName,
            status,
            setDatePicker(),
            createButtonsLayout()
        )
    }

    fun setJob(dto: JobApplicationRequest?, id: Long? = null) {
        this.job = dto ?: JobApplicationRequest()
        this.currentJobId = id
        binder.readBean(this.job)
        deleteButton.isVisible = (id != null)
    }

    private fun createButtonsLayout(): Component {
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY)
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR)
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY)

        saveButton.addClickShortcut(Key.ENTER)
        cancelButton.addClickShortcut(Key.ESCAPE)

        saveButton.addClickListener { validateAndSave() }
        deleteButton.addClickListener { currentJobId?.let { fireEvent(DeleteEvent(this, it))}}
        cancelButton.addClickListener { fireEvent(CancelEvent(this))}

        return HorizontalLayout(saveButton, deleteButton, cancelButton)

    }

    fun setDatePicker(): Component {
        val now = LocalDate.now(ZoneId.systemDefault())
        dateApplied.apply {
            isAutoOpen = true
            isRequiredIndicatorVisible = true
            min = now.minusDays(60)
            max = now
            helperText = "Must be within 60 days from today"
            isWeekNumbersVisible = true
            placeholder = "DD.MM.YYYY"
            i18n = DatePicker.DatePickerI18n()
                .setFirstDayOfWeek(1)
                .setBadInputErrorMessage("Invalid date format")
                .setRequiredErrorMessage("Field is required")
                .setMinErrorMessage("Too early, choose another date")
                .setMaxErrorMessage("Too late, choose another date")
                .setDateFormat("dd.MM.yyyy")
        }
        return HorizontalLayout(dateApplied)
    }

    fun addSaveListener(listener: ComponentEventListener<SaveEvent>): Registration {
        return addListener(SaveEvent::class.java, listener)
    }

    fun addDeleteListener(listener: ComponentEventListener<DeleteEvent>): Registration {
        return addListener(DeleteEvent::class.java, listener)
    }

    fun addCancelButton(listener: ComponentEventListener<CancelEvent>): Registration {
        return addListener(CancelEvent::class.java, listener)
    }

    private fun validateAndSave() {
        try {
            binder.writeBean(job)
            fireEvent(SaveEvent(this, job, currentJobId))
        } catch (e: ValidationException) {
            e.printStackTrace()
        }

    }

}

abstract class JobFormEvent(source: JobForm): com.vaadin.flow.component.ComponentEvent<JobForm>(source, false)

class SaveEvent(source: JobForm, val job: JobApplicationRequest, val jobId: Long?): JobFormEvent(source)

class DeleteEvent(source: JobForm, val jobId: Long?): JobFormEvent(source)

class CancelEvent(source: JobForm): JobFormEvent(source)