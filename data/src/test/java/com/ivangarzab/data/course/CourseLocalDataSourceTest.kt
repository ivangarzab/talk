package com.ivangarzab.data.course

import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * The purpose of this class is to test [CourseLocalDataSource].
 *
 * THIS CLASS WAS GENERATED USING AI (Firebender plugin using: Claude-3.7 Sonnet)
 */
@OptIn(ExperimentalCoroutinesApi::class)
class CourseLocalDataSourceTest {

    // Class under test
    private lateinit var courseLocalDataSource: CourseLocalDataSource

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        courseLocalDataSource = CourseLocalDataSource()
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `getCourse returns stubbed course with empty values`() = runTest {
        // When
        val result = courseLocalDataSource.getCourse()

        // Then
        assertNotNull(result)
        assertEquals("", result.id)
        assertEquals("", result.info.title)
        assertEquals("", result.info.thumbnailImageUrl)
        assertEquals("", result.info.backgroundImageUrl)
        assertEquals("", result.info.subtitle)
        assertEquals(0, result.units.size)
    }

    @Test
    fun `getCourse returns course with consistent structure`() = runTest {
        // When
        val result = courseLocalDataSource.getCourse()

        // Then
        // Verify structure is as expected
        assertNotNull(result)
        assertNotNull(result.info)
        assertNotNull(result.units)
    }
}
