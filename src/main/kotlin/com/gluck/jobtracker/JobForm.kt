package com.gluck.jobtracker

import com.vaadin.flow.component.Component
import com.vaadin.flow.component.ComponentEvent
import com.vaadin.flow.component.ComponentEventListener
import com.vaadin.flow.component.Key
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.combobox.ComboBox
import com.vaadin.flow.component.formlayout.FormLayout
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.binder.BeanValidationBinder
import com.vaadin.flow.data.binder.ValidationException
import com.vaadin.flow.shared.Registration

class JobForm: FormLayout() {

    private val position = TextField("Position")
    private val companyName = TextField("Company Name")
    private val status: ComboBox<Status> = ComboBox("Application status")

    private val saveButton = Button(Icon(VaadinIcon.SAFE))
    private val deleteButton = Button(Icon(VaadinIcon.TRASH))
    private val cancelButton = Button(Icon(VaadinIcon.CLOSE))

    private var job = JobApplication()
    val binder: BeanValidationBinder<JobApplication> = BeanValidationBinder(JobApplication::class.java)

    init {
        status.setItems(Status.entries)

        binder.bindInstanceFields(this)

        add(
            position,
            companyName,
            status,
            createButtonsLayout()
        )
    }

    fun setJob(job: JobApplication?) {
        this.job = job ?: JobApplication()
        binder.readBean(this.job)
    }

    private fun createButtonsLayout(): Component {
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY)
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR)
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY)

        saveButton.addClickShortcut(Key.ENTER)
        cancelButton.addClickShortcut(Key.ESCAPE)

        saveButton.addClickListener { validateAndSave() }
        deleteButton.addClickListener { fireEvent(DeleteEvent(this, job))}
        cancelButton.addClickListener { fireEvent(CancelEvent(this))}

        return HorizontalLayout(saveButton, deleteButton, cancelButton)

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
            fireEvent(SaveEvent(this, job))
        } catch (e: ValidationException) {
            e.printStackTrace()
        }

    }

}

abstract class JobFormEvent(source: JobForm): ComponentEvent<JobForm>(source, false)

class SaveEvent(source: JobForm, val job: JobApplication): JobFormEvent(source)

class DeleteEvent(source: JobForm, val job: JobApplication): JobFormEvent(source)

class CancelEvent(source: JobForm): JobFormEvent(source)