package com.ivangarzab.data.course

import com.ivangarzab.data.audio.AudioChunksLocalDataSource
import com.ivangarzab.data.network.NetworkRepository
import io.mockk.*
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * The purpose of this class is to test [CourseRepository].
 *
 * THIS CLASS WAS GENERATED USING AI (Firebender plugin using: Claude-3.7 Sonnet)
 */
@OptIn(ExperimentalCoroutinesApi::class)
class CourseRepositoryTest {
    
    // Mock dependencies
    private val remoteDataSource = mockk<CourseRemoteDataSource>()
    private val localDataSource = mockk<CourseLocalDataSource>()
    private val networkRepository = mockk<NetworkRepository>()
    
    // Test coroutine scope
    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)
    
    // Network availability flow
    private val networkAvailabilityFlow = MutableStateFlow(false)
    
    // Sample course data
    private val remoteCourse = Course(
        id = "remote-1",
        info = Info(
            title = "Remote Course",
            thumbnailImageUrl = "https://example.com/remote-thumbnail.jpg",
            backgroundImageUrl = "https://example.com/remote-background.jpg",
            subtitle = "Learn remotely"
        ),
        units = listOf(
            Unit(
                id = "remote-unit-1",
                title = "Remote Unit 1",
                days = listOf(
                    Day(
                        id = "remote-day-1",
                        title = "Remote Day 1",
                        thumbnailImageUrl = "https://example.com/remote-day-thumbnail.jpg",
                        subtitle = "First remote day"
                    )
                )
            )
        )
    )
    
    private val localCourse = Course(
        id = "local-1",
        info = Info(
            title = "Local Course",
            thumbnailImageUrl = "https://example.com/local-thumbnail.jpg",
            backgroundImageUrl = "https://example.com/local-background.jpg",
            subtitle = "Learn locally"
        ),
        units = listOf(
            Unit(
                id = "local-unit-1",
                title = "Local Unit 1",
                days = listOf(
                    Day(
                        id = "local-day-1",
                        title = "Local Day 1",
                        thumbnailImageUrl = "https://example.com/local-day-thumbnail.jpg",
                        subtitle = "First local day"
                    )
                )
            )
        )
    )

    private val emptyCourse = Course(
        id = "",
        info = Info(
            title = "",
            thumbnailImageUrl = "",
            backgroundImageUrl = "",
            subtitle = ""
        ),
        units = listOf()
    )
    
    // Class under test
    private lateinit var courseRepository: CourseRepository
    
    @Before
    fun setup() {
        // Configure mocks
        coEvery { networkRepository.listenForNetworkAvailability() } returns networkAvailabilityFlow
        coEvery { remoteDataSource.getCourse() } returns remoteCourse
        coEvery { localDataSource.getCourse() } returns localCourse
        
        // Initialize the repository (this will trigger the network collection)
        courseRepository = CourseRepository(
            remoteDataSource = remoteDataSource,
            localDataSource = localDataSource,
            networkRepository = networkRepository,
            coroutineScope = testScope
        )
    }
    
    @After
    fun tearDown() {
        clearAllMocks()
    }
    
    @Test
    fun `repository loads local data when network is unavailable on init`() = runTest {
        // Given
        // Network is initially unavailable (set in the networkAvailabilityFlow)
        
        // When
        testScope.advanceUntilIdle() // Let coroutines complete
        
        // Then
        val courseData = courseRepository.listenForCourseData().first()
        assertEquals(localCourse, courseData)
        coVerify(exactly = 0) { remoteDataSource.getCourse() }
        coVerify(exactly = 1) { localDataSource.getCourse() }
    }
    
    @Test
    fun `repository loads remote data when network becomes available`() = runTest {
        // Given
        testScope.advanceUntilIdle() // Process initial state
        
        // When
        networkAvailabilityFlow.value = true
        testScope.advanceUntilIdle() // Let coroutines complete
        
        // Then
        val courseData = courseRepository.listenForCourseData().first()
        assertEquals(remoteCourse, courseData)
        coVerify(exactly = 1) { remoteDataSource.getCourse() }
    }
    
    @Test
    fun `repository loads local data when network becomes unavailable`() = runTest {
        // Given
        networkAvailabilityFlow.value = true
        testScope.advanceUntilIdle() // Process initial state
        
        // When
        networkAvailabilityFlow.value = false
        testScope.advanceUntilIdle() // Let coroutines complete
        
        // Then
        val courseData = courseRepository.listenForCourseData().first()
        assertEquals(localCourse, courseData)
        coVerify(exactly = 1) { remoteDataSource.getCourse() }
        coVerify(exactly = 1) { localDataSource.getCourse() }
    }
    
    @Test
    fun `repository handles null course data from remote source`() = runTest {
        // Given
        coEvery { remoteDataSource.getCourse() } returns emptyCourse
        networkAvailabilityFlow.value = true
        
        // When
        testScope.advanceUntilIdle() // Let coroutines complete
        
        // Then
        val courseData = courseRepository.listenForCourseData().first()
        assertEquals(emptyCourse, courseData)
    }
    
    @Test
    fun `repository handles null course data from local source`() = runTest {
        // Given
        coEvery { localDataSource.getCourse() } returns emptyCourse
        
        // When
        testScope.advanceUntilIdle() // Let coroutines complete
        
        // Then
        val courseData = courseRepository.listenForCourseData().first()
        assertEquals(emptyCourse, courseData)
    }
    
    @Test
    fun `multiple network changes trigger correct data source calls`() = runTest {
        // Given initial state (network unavailable)
        testScope.advanceUntilIdle()
        
        // When changing network state multiple times
        networkAvailabilityFlow.value = true  // Switch to remote
        testScope.advanceUntilIdle()
        networkAvailabilityFlow.value = false // Switch to local
        testScope.advanceUntilIdle()
        networkAvailabilityFlow.value = true  // Switch to remote again
        testScope.advanceUntilIdle()
        
        // Then
        val courseData = courseRepository.listenForCourseData().first()
        assertEquals(remoteCourse, courseData)
        coVerify(exactly = 2) { remoteDataSource.getCourse() }
        coVerify(exactly = 2) { localDataSource.getCourse() }
    }
}