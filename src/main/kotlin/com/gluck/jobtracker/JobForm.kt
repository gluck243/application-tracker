package com.gluck.jobtracker

import com.vaadin.flow.component.combobox.ComboBox
import com.vaadin.flow.component.formlayout.FormLayout
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.binder.BeanValidationBinder
import com.vaadin.flow.data.binder.Binder
import kotlin.reflect.full.instanceParameter

class JobForm: FormLayout() {

    private val position = TextField("Position")
    private val companyName = TextField("Company Name")
    private val statusField: ComboBox<Status> = ComboBox("Application status")

    init {
        statusField.setItems(Status.entries)
        val binder: BeanValidationBinder<JobApplication> = BeanValidationBinder(JobApplication::class.java)
        binder.bindInstanceFields(this)
        add(position)
        add(companyName)
        add(statusField)
    }

}