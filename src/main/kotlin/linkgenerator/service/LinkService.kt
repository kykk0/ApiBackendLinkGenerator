package org.example.linkgenerator.service

import org.springframework.stereotype.Service

@Service
class LinkService {
    fun generateLink(paymentId: String, osType: String, isWebView: Boolean): String {
        return when {
            osType.equals("iOS", ignoreCase = true) -> {
                "ur-ios://tpay/$paymentId"
            }

            osType.equals("Android", ignoreCase = true) -> {
                "tinkoffbank://Main/tpay/$paymentId"
            }

            else -> {
                "https://tpay-web.com/pay/$paymentId"
            }
        }
    }
}