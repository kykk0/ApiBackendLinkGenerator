package org.example.linkgenerator.service

import org.apache.commons.codec.digest.HmacUtils
import org.example.linkgenerator.dto.PaymentInfoResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.time.Duration
import java.util.*

@Service
class LinkService(
    private val redisTemplate: StringRedisTemplate,
    @Value("\${app.secret-key}") private val secretKey: String,
    @Value("\${app.domain}") private val domain: String
) {
    companion object {
        private const val LINK_TTL_MINUTES = 10L
    }

    fun validateSignature(paymentId: String, osType: String, isWebView: Boolean, signature: String): Boolean {
        val data = "$paymentId|$osType|$isWebView"
        return HmacUtils.hmacSha256Hex(secretKey, data) == signature
    }

    fun generateMaskedUrl(paymentId: String, osType: String, isWebView: Boolean): String {
        val linkId = UUID.randomUUID().toString()
        redisTemplate.opsForValue().set(
            "link:$linkId",
            "$paymentId|$osType|$isWebView",
            Duration.ofMinutes(LINK_TTL_MINUTES)
        )
        return "$domain/api/p/$linkId"
    }

    fun getPaymentInfo(linkId: String): PaymentInfoResponse {
        val storedData = redisTemplate.opsForValue().get("link:$linkId")
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Link expired or not found")


        val (paymentId, osType, isWebView) = storedData.split("|")

        // Здесь пока мок данные
        return PaymentInfoResponse(
            paymentId = paymentId,
            amount = 10000,
            currency = "RUB",
            merchantName = "T-Pay",
            description = "Оплата заказа"
        )
    }
}