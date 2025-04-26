package org.example.linkgenerator.dto

data class PaymentInfoResponse(
    val paymentId: String,
    val amount: Long,
    val currency: String,
    val merchantName: String,
    val description: String
)
