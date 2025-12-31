package com.gluck.jobtracker.exception

class NoSuchJobFoundException(override val message: String = "No matching job found!"): RuntimeException()