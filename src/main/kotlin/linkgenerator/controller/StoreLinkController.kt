package org.example.linkgenerator.controller

import jakarta.validation.Valid
import org.example.linkgenerator.dto.GenerateLinkRequest
import org.example.linkgenerator.service.LinkService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/links")
class StoreLinkController(
    private val linkService: LinkService
) {
    @PostMapping
    fun generateLink(@Valid @RequestBody request: GenerateLinkRequest): ResponseEntity<Map<String, String>> {
        val url = linkService.generateLink(request.paymentId, request.osType, request.isWebView)
        return ResponseEntity.status(201).body(mapOf("url" to url))
    }
}