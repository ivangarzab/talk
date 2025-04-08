package com.ivangarzab.data.course

import com.ivangarzab.data.util.JsonLoader
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

/**
 * The purpose of this class is to test [CourseRemoteDataSource].
 *
 * THIS CLASS WAS GENERATED USING AI (Firebender plugin using: Claude-3.7 Sonnet)
 */
@OptIn(ExperimentalCoroutinesApi::class)
class CourseRemoteDataSourceTest {

    // Mock dependencies
    private val jsonLoader = mockk<JsonLoader>()
    
    // Class under test
    private lateinit var courseRemoteDataSource: CourseRemoteDataSource

    @Before
    fun setup() {
        courseRemoteDataSource = CourseRemoteDataSource(jsonLoader)
    }

    @Test
    fun `getCourse returns properly parsed course from JSON`() = runTest {
        // Given
        val mockJsonData = """
            {
              "id": "test-course-1",
              "info": {
                "title": "Test Course",
                "thumbnailImageUrl": "https://example.com/thumbnail.jpg",
                "backgroundImageUrl": "https://example.com/background.jpg",
                "subtitle": "A course for testing"
              },
              "units": [
                {
                  "id": "unit-1",
                  "title": "Unit 1",
                  "days": [
                    {
                      "id": "day-1",
                      "title": "Day 1",
                      "thumbnailImageUrl": "https://example.com/day1.jpg",
                      "subtitle": "First day"
                    }
                  ]
                }
              ]
            }
        """.trimIndent()
        
        coEvery { jsonLoader.loadJsonFromResources("course.json") } returns mockJsonData
        
        // When
        val result = courseRemoteDataSource.getCourse()
        
        // Then
        assertNotNull(result)
        assertEquals("test-course-1", result.id)
        
        // Verify info
        assertEquals("Test Course", result.info.title)
        assertEquals("https://example.com/thumbnail.jpg", result.info.thumbnailImageUrl)
        assertEquals("https://example.com/background.jpg", result.info.backgroundImageUrl)
        assertEquals("A course for testing", result.info.subtitle)
        
        // Verify units
        assertEquals(1, result.units.size)
        assertEquals("unit-1", result.units[0].id)
        assertEquals("Unit 1", result.units[0].title)
        
        // Verify days
        assertEquals(1, result.units[0].days.size)
        assertEquals("day-1", result.units[0].days[0].id)
        assertEquals("Day 1", result.units[0].days[0].title)
        assertEquals("https://example.com/day1.jpg", result.units[0].days[0].thumbnailImageUrl)
        assertEquals("First day", result.units[0].days[0].subtitle)
        
        // Verify the correct file was loaded
        coVerify { jsonLoader.loadJsonFromResources("course.json") }
    }
    
    @Test
    fun `getCourse handles empty JSON correctly`() = runTest {
        // Given
        val emptyJsonData = "{}"
        coEvery { jsonLoader.loadJsonFromResources("course.json") } returns emptyJsonData

        // When
        val result = courseRemoteDataSource.getCourse()

        // Then
        assertNotNull(result)
        // Check if fields are null or default values instead of accessing properties that might be null
        assertNull(result.id)
        assertNull(result.info)
        assertNull(result.units)
    }
}