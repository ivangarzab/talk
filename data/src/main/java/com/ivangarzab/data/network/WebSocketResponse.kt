package com.ivangarzab.data.network

/**
 *
 */
data class WebSocketResponse(
    val id: String,
    val type: WebSocketResponseType,
    val text: String,
    val isFinal: Boolean,
    val segments: List<WebSocketResponseSegment>,
    val processedAudioDurationMs: Int
)

data class WebSocketResponseSegment(
    val text: String,
    val confidence: Double
)

enum class WebSocketResponseType(type: String) {
    METADATA("asrMetadata"),
    RESULT("asrResult"),
    CLOSED("asrClosed")
}
