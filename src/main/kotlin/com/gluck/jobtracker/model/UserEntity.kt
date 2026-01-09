package com.gluck.jobtracker.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.validation.constraints.NotBlank
import java.util.UUID

@Entity
@Table(name = "users")
class UserEntity(
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null,

    @Column(unique = true, nullable = false) @NotBlank(message = "Username cannot be blank")
    var username: String,

    @Column(nullable = false) @NotBlank(message = "Password cannot be blank")
    var password: String,

    @Column(nullable = false)
    var role: String = "ROLE_GUEST"
)