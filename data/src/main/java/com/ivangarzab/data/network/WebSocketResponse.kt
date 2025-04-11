package com.ivangarzab.data.network

import com.google.gson.annotations.SerializedName

/**
 * The purpose of this file is to hold all of the data classes that are required
 * to handle the web socket responses inside the :data module.
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

enum class WebSocketResponseType {
    /**
     * Response type indicating that the recording session has started.
     */
    @SerializedName("asrMetadata")
    METADATA,

    /**
     * Response type containing the transcription result of the audio recording.
     */
    @SerializedName("asrResult")
    RESULT,

    /**
     * Response type indicating that the recording session has been finished and closed.
     */
    @SerializedName("asrClosed")
    CLOSED
}
