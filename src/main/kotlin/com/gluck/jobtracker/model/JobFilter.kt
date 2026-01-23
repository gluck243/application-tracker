package com.gluck.jobtracker.model

data class JobFilter(
    var searchTerm: String? = null,
    var searchBy: String = "Company"
)