package com.ivangarzab.data.network

import android.annotation.SuppressLint
import com.google.gson.Gson
import com.ivangarzab.data.BuildConfig
import com.ivangarzab.websocket.repositories.WebSocketRepository
import com.ivangarzab.websocket.models.AudioChunk
import com.ivangarzab.websocket.models.WebSocketResponse
import com.ivangarzab.websocket.models.WebSocketResponseType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import timber.log.Timber
import java.util.concurrent.TimeUnit

/**
 * The purpose of this repository is to handle the [WebSocket] connection that is used to
 * send and receive data from the backend server, regarding the record feature.
 */
class WebSocketRepositoryImpl : WebSocketRepository {

    // In theory, this client should be created with a [Network] object from a [NetworkCallback].
    private val okHttpClient: OkHttpClient

    private val webSocketRequest: Request

    private var webSocket: WebSocket? = null

    private val _webSocketResponses: MutableStateFlow<List<WebSocketResponse>> = MutableStateFlow(emptyList())

    private val webSocketListener: WebSocketListener = object : WebSocketListener() {
        override fun onOpen(webSocket: WebSocket, response: okhttp3.Response) {
            super.onOpen(webSocket, response)
            Timber.v("Web socket connection established successfully")
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            super.onMessage(webSocket, text)
            Timber.v("Received message from web socket: $text")
            onWebSocketRawResponseReceived(text)
        }

        override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
            super.onMessage(webSocket, bytes)
            Timber.v("Received byte message from web socket: $bytes")
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            super.onClosing(webSocket, code, reason)
            Timber.v("Web socket connection is closing with code: $code and reason: $reason")
            webSocket.close(1000, reason)
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            super.onClosed(webSocket, code, reason)
            Timber.v("Web socket connection closed with code: $code and reason: $reason")
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: okhttp3.Response?) {
            super.onFailure(webSocket, t, response)
            Timber.w(t, "Failed to open web socket with error: ${t.cause} and response: $response")
        }
    }

    init {
        okHttpClient = OkHttpClient.Builder().apply {
            pingInterval(PING_INTERVAL_MIN, TimeUnit.SECONDS)
        }.build()
        webSocketRequest = Request.Builder().apply {
            url(WEB_SOCKET_URL)
            header(HEADER_ACCESS_TOKEN, BuildConfig.WS_ACCESS_TOKEN)
            header(HEADER_CLIENT_INFO, FIELD_CLIENT_INFO)
        }.build()
    }

    /**
     * Decipher the raw [WebSocketResponse] from the provided text
     * using [Gson], and update the internal [_webSocketResponses] state with the parsed response.
     */
    private fun onWebSocketRawResponseReceived(text: String) {
        val response = Gson().fromJson(text, WebSocketResponse::class.java)
        handleWebSocketResponse(response)
    }

    private fun handleWebSocketResponse(response: WebSocketResponse) {
        when (response.type) {
            WebSocketResponseType.METADATA -> "START"
            WebSocketResponseType.RESULT -> "RESULT"
            WebSocketResponseType.CLOSED -> {
                // Close the web socket to release all resources
                webSocket?.close(1000, "Web socket connection closed after end of stream.")
                "CLOSED"
            }
        }.let { type: String ->
            Timber.v("Received $type response from web socket")
        }
        _webSocketResponses.value += response
    }

    /**
     * Consume a [StateFlow] of a list of [WebSocketResponse].
     */
    override fun listenForWebSocketResponses(): StateFlow<List<WebSocketResponse>> {
        return _webSocketResponses.asStateFlow()
    }

    /**
     * Open a [WebSocket] for the [WEB_SOCKET_URL], using the predefined [okHttpClient]
     * and [webSocketRequest] to establish a connection.
     */
    override suspend fun openWebSocket() {
        Timber.v("Attempting to open web socket for url: $WEB_SOCKET_URL")
        webSocket = okHttpClient.newWebSocket(webSocketRequest, webSocketListener)
    }

    /**
     * Send a message to the web socket to indicate the backend that we want to star recording.
     */
    @SuppressLint("DefaultLocale")
    override suspend fun sendStartStreamingMessage(learningLocale: String, inputSampleRate: Int) {
        webSocket?.let { ws ->
            ws.send(String.format(START_MESSAGE, learningLocale, inputSampleRate))
            Timber.v("Start message sent to web socket with learning locale: $learningLocale and input sample rate: $inputSampleRate")
        } ?: Timber.w("Unable to send start streaming message as web socket is null")
    }

    /**
     * Send an [AudioChunk] to the web socket for backend processing.
     */
    override suspend fun sendAudioDataChunkMessage(audioChunk: AudioChunk) {
        webSocket?.let { ws ->
            ws.send(String.format(AUDIO_CHUNK_MESSAGE, audioChunk.chunk, audioChunk.isFinal))
            if (audioChunk.isFinal) {
                Timber.v("Final audio chunk sent to web socket")
            }
        } ?: Timber.w("Unable to send audio data chunk message as web socket is null")
    }

    companion object {
        private const val PING_INTERVAL_MIN: Long = 10

        private const val WEB_SOCKET_URL = "wss://speak-api--feature-mobile-websocket-interview.preview.usespeak.dev/v2/ws"

        private const val HEADER_ACCESS_TOKEN = "x-access-token"

        private const val HEADER_CLIENT_INFO = "x-client-info"
        private const val FIELD_CLIENT_INFO = "Speak Interview Test"

        private const val START_MESSAGE = "{\"type\":\"asrStart\", \"learningLocale\":\"%s\", \"metadata\":{\"deviceAudio\":{\"inputSampleRate\":%d}}}"
        private const val AUDIO_CHUNK_MESSAGE = "{\"type\":\"asrStream\", \"chunk\":\"%s\", \"isFinal\":%b}"
    }
}