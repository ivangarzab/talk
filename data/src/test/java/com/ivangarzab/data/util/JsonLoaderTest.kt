package com.ivangarzab.data.util

import android.content.Context
import android.content.res.AssetManager
import io.mockk.*
import junit.framework.TestCase.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.ByteArrayInputStream
import java.io.IOException

/**
 * The purpose of this class is to test [JsonLoader].
 *
 * THIS CLASS WAS GENERATED USING AI (Firebender plugin using: Claude-3.7 Sonnet)
 */
class JsonLoaderTest {

    // Mock dependencies
    private val context = mockk<Context>()
    private val assetManager = mockk<AssetManager>()
    
    // Class under test
    private lateinit var jsonLoader: JsonLoader

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        // Set up context to return the mock AssetManager
        every { context.assets } returns assetManager
        
        // Initialize the class under test
        jsonLoader = JsonLoader(context)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `loadJsonFromResources returns correct JSON string when file exists`() {
        // Given
        val fileName = "test.json"
        val jsonContent = """{"key": "value"}"""
        val inputStream = ByteArrayInputStream(jsonContent.toByteArray())
        
        every { assetManager.open(fileName) } returns inputStream
        
        // When
        val result = jsonLoader.loadJsonFromResources(fileName)
        
        // Then
        assertEquals(jsonContent, result)
        verify { assetManager.open(fileName) }
    }

    @Test
    fun `loadJsonFromResources returns empty string when file doesn't exist`() {
        // Given
        val fileName = "non_existent.json"
        
        every { assetManager.open(fileName) } throws IOException("File not found")

        // When
        val result = jsonLoader.loadJsonFromResources(fileName)
        
        // Then
        assertEquals("", result)
        verify { assetManager.open(fileName) }
    }
    
    @Test
    fun `loadJsonFromResources handles empty file correctly`() {
        // Given
        val fileName = "empty.json"
        val inputStream = ByteArrayInputStream("".toByteArray())
        
        every { assetManager.open(fileName) } returns inputStream
        
        // When
        val result = jsonLoader.loadJsonFromResources(fileName)
        
        // Then
        assertEquals("", result)
        verify { assetManager.open(fileName) }
    }
    
    @Test
    fun `loadJsonFromResources handles special characters in JSON correctly`() {
        // Given
        val fileName = "special_chars.json"
        val jsonContent = """{"key": "value with \"quotes\" and special chars: \n\t\r"}"""
        val inputStream = ByteArrayInputStream(jsonContent.toByteArray())
        
        every { assetManager.open(fileName) } returns inputStream
        
        // When
        val result = jsonLoader.loadJsonFromResources(fileName)
        
        // Then
        assertEquals(jsonContent, result)
        verify { assetManager.open(fileName) }
    }
}