package org.example.linkgenerator.dto

import jakarta.validation.constraints.NotBlank

data class GenerateLinkRequest(
    @field:NotBlank val paymentId: String,
    @field:NotBlank val osType: String,
    val isWebView: Boolean,
)