package org.example.linkgenerator.controller

import org.example.linkgenerator.service.LinkService
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/p")
class LinkController(
    private val linkService: LinkService
) {

    @GetMapping("/{linkId}")
    fun handleRedirect(
        @PathVariable linkId: String,
        @RequestHeader("User-Agent", required = false) userAgent: String?
    ): ResponseEntity<String> {
        val paymentInfo = linkService.getPaymentInfo(linkId)

        return when {
            userAgent?.contains("iPhone") == true || userAgent?.contains("iPad") == true -> {
                ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, "text/html; charset=utf-8")
                    .body(generateIOSRedirect(paymentInfo.paymentId))
            }
            userAgent?.contains("Android") == true -> {
                ResponseEntity.status(302)
                    .header(HttpHeaders.LOCATION, generateAndroidIntent(paymentInfo.paymentId))
                    .build()
            }
            else -> {
                ResponseEntity.status(302)
                    .header(HttpHeaders.LOCATION, "https://tpay-web.com/pay/${paymentInfo.paymentId}")
                    .build()
            }
        }
    }

    private fun generateIOSRedirect(paymentId: String) = """
        <html>
            <script>
                window.location = 'bank100000000004://tpay/$paymentId';
                setTimeout(function() {
                    window.location = 'tinkoffbank://tpay/$paymentId';
                }, 1000);
                setTimeout(function() {
                    window.location = 'https://apps.apple.com/ru/app/id00000000';
                }, 2000);
            </script>
        </html>
    """.trimIndent()

    private fun generateAndroidIntent(paymentId: String) = """
        intent://tpay/$paymentId#Intent;
            scheme=tinkoffbank;
            package=ru.tinkoff.android;
            end
    """.trimIndent().replace("\n", "")
}
