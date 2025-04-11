package com.ivangarzab.websocket.models

/**
 * The purpose of this file is to hold all of the data classes that are required
 * for the audio directory inside the :data module.
 */
data class AudioChunk(
    val type: String,
    val chunk: String,
    val isFinal: Boolean
)
