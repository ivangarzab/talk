package com.ivangarzab.data.audio

import io.mockk.MockKAnnotations
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

/**
 * The purpose of this class is to test [AudioChunksLocalDataSource].
 *
 * THIS CLASS WAS GENERATED USING AI (Firebender plugin using: Claude-3.7 Sonnet)
 */
class AudioChunksLocalDataSourceTest {
    // Class under test
    private lateinit var audioChunksLocalDataSource: AudioChunksLocalDataSource

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        audioChunksLocalDataSource = AudioChunksLocalDataSource()
    }

    @Test
    fun `getAudioChunks returns empty list`() = runBlocking {
        // When
        val result = audioChunksLocalDataSource.getAudioChunks()

        // Then
        assertEquals(emptyList<AudioChunk>(), result)
    }
}