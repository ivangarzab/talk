package com.ivangarzab.data.audio

import com.ivangarzab.data.network.NetworkRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

/**
 * The purpose of this class is to test [AudioChunksRepository].
 * 
 * THIS CLASS WAS GENERATED USING AI (Firebender plugin using: Claude-3.7 Sonnet)
 */
@ExperimentalCoroutinesApi
class AudioChunksRepositoryTest {

    // Mocks
    private val mockRemoteDataSource = mockk<AudioChunksRemoteDataSource>()
    private val mockLocalDataSource = mockk<AudioChunksLocalDataSource>()
    private val mockNetworkRepository = mockk<NetworkRepository>()

    // Test data
    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)
    private val networkAvailabilityFlow = MutableStateFlow(false)

    private val remoteAudioChunks = listOf(
        AudioChunk("asrStream", "ABCDEFGH123456", false),
        AudioChunk("asrStream", "IJKLMNOPQR7890", false)
    )

    private val localAudioChunks = listOf(
        AudioChunk("asrStream", "ABCDEFGH123456", false),
        AudioChunk("asrStream", "IJKLMNOPQR7890", false)
    )

    // System under test
    private lateinit var repository: AudioChunksRepository

    @Before
    fun setup() {
        runTest {
            // Setup mocks
            every { mockNetworkRepository.listenForNetworkAvailability() } returns networkAvailabilityFlow
            coEvery { mockRemoteDataSource.getAudioChunks() } returns remoteAudioChunks
            coEvery { mockLocalDataSource.getAudioChunks() } returns localAudioChunks

            // Initialize repository
            repository = AudioChunksRepository(
                remoteDataSource = mockRemoteDataSource,
                localDataSource = mockLocalDataSource,
                networkRepository = mockNetworkRepository,
                coroutineScope = testScope
            )
        }
    }

    @Test
    fun `when network is available, repository should use remote data source`() = runTest {
        // Given
        networkAvailabilityFlow.value = true
        testDispatcher.scheduler.advanceUntilIdle()

        // When
        val result = repository.listenForAudioChunks().first()

        // Then
        assertEquals(remoteAudioChunks, result)
        coVerify { mockRemoteDataSource.getAudioChunks() }
    }

    @Test
    fun `when network is not available, repository should use local data source`() = runTest {
        // Given
        networkAvailabilityFlow.value = false
        testDispatcher.scheduler.advanceUntilIdle()

        // When
        val result = repository.listenForAudioChunks().first()

        // Then
        assertEquals(localAudioChunks, result)
        coVerify { mockLocalDataSource.getAudioChunks() }
    }

    @Test
    fun `when network availability changes, repository should switch data sources`() = runTest {
        // Given initially offline
        networkAvailabilityFlow.value = false
        testDispatcher.scheduler.advanceUntilIdle()

        // When network becomes available
        networkAvailabilityFlow.value = true
        testDispatcher.scheduler.advanceUntilIdle()

        // Then repository should use remote data
        val result = repository.listenForAudioChunks().first()
        assertEquals(remoteAudioChunks, result)

        // When network becomes unavailable again
        networkAvailabilityFlow.value = false
        testDispatcher.scheduler.advanceUntilIdle()

        // Then repository should use local data
        val updatedResult = repository.listenForAudioChunks().first()
        assertEquals(localAudioChunks, updatedResult)
    }
}
