package com.ivangarzab.data.network

import android.annotation.SuppressLint
import com.google.gson.Gson
import com.ivangarzab.data.BuildConfig
import com.ivangarzab.data.audio.AudioChunk
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
 * TODO:
 */
class WebSocketRepository {

    // In theory, this client should be created with a [Network] object from a [NetworkCallback].
    private val okHttpClient: OkHttpClient

    private val webSocketRequest: Request

    private var webSocket: WebSocket? = null

    private val _webSocketResponses: MutableStateFlow<List<WebSocketResponse>> = MutableStateFlow(emptyList())

    private val webSocketListener: WebSocketListener = object : WebSocketListener() {
        override fun onOpen(webSocket: WebSocket, response: okhttp3.Response) {
            super.onOpen(webSocket, response)
            Timber.d("Web socket connection established successfully")
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            super.onMessage(webSocket, text)
            Timber.i("Received message from web socket: $text")
            onWebSocketRawResponseReceived(text)
        }

        override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
            super.onMessage(webSocket, bytes)
            Timber.v("Received byte message from web socket: $bytes")
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            super.onClosing(webSocket, code, reason)
            Timber.d("Web socket connection is closing with code: $code and reason: $reason")
            webSocket.close(1000, null)
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            super.onClosed(webSocket, code, reason)
            Timber.d("Web socket connection closed with code: $code and reason: $reason")
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: okhttp3.Response?) {
            super.onFailure(webSocket, t, response)
                Timber.w(t, "Failed to open web socket with error: ${t.cause} and response: $response")
        }
    }

    init {
        okHttpClient = OkHttpClient.Builder().apply {
            pingInterval(10, TimeUnit.SECONDS)
        }.build()
        webSocketRequest = Request.Builder().apply {
            url(WEB_SOCKET_URL)
            header(HEADER_ACCESS_TOKEN, BuildConfig.WS_ACCESS_TOKEN)
            header(HEADER_CLIENT_INFO, FIELD_CLIENT_INFO)
        }.build()
        openWebSocket()
    }

    private fun openWebSocket() {
        Timber.v("Attempting to open web socket for url: $WEB_SOCKET_URL")
        webSocket = okHttpClient.newWebSocket(webSocketRequest, webSocketListener)
    }

    /**
     * Decipher the raw [WebSocketResponse] from the provided text using [Gson],
     * and update the internal [_webSocketResponses] state with the parsed response.
     */
    private fun onWebSocketRawResponseReceived(text: String) {
        val response = Gson().fromJson(text, WebSocketResponse::class.java)
        Timber.d("Updating internal state with new web socket response: $response")
        _webSocketResponses.value += response
    }

    /**
     * TODO:
     */
    fun listenForWebSocketResponses(): StateFlow<List<WebSocketResponse>> {
        return _webSocketResponses.asStateFlow()
    }

    /**
     * TODO:
     */
    @SuppressLint("DefaultLocale")
    suspend fun sendStartStreamingMessage(learningLocale: String = "en-US", inputSampleRate: Int = 16000) {
        webSocket?.let {
            it.send(String.format(START_MESSAGE, learningLocale, inputSampleRate))
            Timber.d("Start message sent to web socket with learning locale: $learningLocale and input sample rate: $inputSampleRate")
        } ?: Timber.w("Unable to send start streaming message as web socket is null")
    }

    /**
     * TODO:
     */
    suspend fun sendAudioDataChunkMessage(audioChunk: AudioChunk) {
        webSocket?.let { ws ->
            ws.send(String.format(AUDIO_CHUNK_MESSAGE, audioChunk.chunk, audioChunk.isFinal))
            Timber.d("Audio data chunk message sent to web socket with chunk length: ${audioChunk.chunk.length} and isFinal: ${audioChunk.isFinal}")
            if (audioChunk.isFinal) {
                Timber.d("Final audio chunk sent to web socket")
//                ws.close(1000, "We're done streaming") TODO: Close once we get the 'closing' message
            }
        } ?: Timber.w("Unable to send audio data chunk message as web socket is null")
    }

    suspend fun sendStopStreamingMessage() {
        sendAudioDataChunkMessage(AudioChunk(type = "asrStream", chunk = "", isFinal = true))
    }

    companion object {
        private const val WEB_SOCKET_URL = "wss://speak-api--feature-mobile-websocket-interview.preview.usespeak.dev/v2/ws"

        private const val HEADER_ACCESS_TOKEN = "x-access-token"

        private const val HEADER_CLIENT_INFO = "x-client-info"
        private const val FIELD_CLIENT_INFO = "Speak Interview Test"

        private const val START_MESSAGE = "{\"type\":\"asrStart\", \"learningLocale\":\"%s\", \"metadata\":{\"deviceAudio\":{\"inputSampleRate\":%d}}}"
        private const val AUDIO_CHUNK_MESSAGE = "{\"type\":\"asrStream\", \"chunk\":\"%s\", \"isFinal\":%b}"
    }
}