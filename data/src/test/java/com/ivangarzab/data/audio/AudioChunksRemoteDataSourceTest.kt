package com.ivangarzab.data.audio

import com.ivangarzab.data.util.JsonLoader
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

/**
 * The purpose of this class is to test [AudioChunksRemoteDataSource].
 *
 * THIS CLASS WAS GENERATED USING AI (Firebender plugin using: Claude-3.7 Sonnet)
 */
class AudioChunksRemoteDataSourceTest {

    // Mock dependencies
    private val jsonLoader = mockk<JsonLoader>()

    // Class under test
    private lateinit var audioChunksRemoteDataSource: AudioChunksRemoteDataSource

    @Before
    fun setup() {
        audioChunksRemoteDataSource = AudioChunksRemoteDataSource(jsonLoader)
    }

    @Test
    fun `getAudioChunks returns correct list when json is loaded`() = runBlocking {
        // Given
        val mockJsonData = """
            [
                {
                    "type": "interim",
                    "chunk": "Hello world",
                    "isFinal": false
                },
                {
                    "type": "final",
                    "chunk": "Testing is important",
                    "isFinal": true
                }
            ]
        """.trimIndent()

        coEvery { jsonLoader.loadJsonFromResources("asr-stream-audio-chunks.json") } returns mockJsonData

        // When
        val result = audioChunksRemoteDataSource.getAudioChunks()

        // Then
        assertEquals(2, result.size)

        assertEquals("interim", result[0].type)
        assertEquals("Hello world", result[0].chunk)
        assertEquals(false, result[0].isFinal)

        assertEquals("final", result[1].type)
        assertEquals("Testing is important", result[1].chunk)
        assertEquals(true, result[1].isFinal)
    }

    @Test
    fun `getAudioChunks returns empty list when json is empty`() = runBlocking {
        // Given
        val emptyJsonData = "[]"
        coEvery { jsonLoader.loadJsonFromResources("asr-stream-audio-chunks.json") } returns emptyJsonData

        // When
        val result = audioChunksRemoteDataSource.getAudioChunks()

        // Then
        assertEquals(0, result.size)
    }
}
