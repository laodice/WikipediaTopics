package com.example.wikipediatopics.api

import com.squareup.moshi.JsonDataException
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.net.HttpURLConnection

class WikipediaApiTests {
    private val mockWebServer = MockWebServer()
    private lateinit var api: WikipediaApi

    @Before
    fun setup() {
        mockWebServer.start()

        api =
            Retrofit.Builder()
                .baseUrl(mockWebServer.url("/"))
                .addConverterFactory(
                    MoshiConverterFactory.create(
                        Moshi.Builder()
                            .addLast(KotlinJsonAdapterFactory())
                            .build(),
                    ),
                )
                .build()
                .create(WikipediaApi::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `Should return a wikipedia result`() =
        runBlocking {
            val mockResponse =
                MockResponse()
                    .setResponseCode(HttpURLConnection.HTTP_OK)
                    .setBody(
                        """
                        {
                            "parse": {
                                "title": "Page Title",
                                "pageid": "1234",
                                "text": {
                                    "*": "html code"
                                }
                            }
                        }
                        """.trimIndent(),
                    )
            mockWebServer.enqueue(mockResponse)

            val response = api.getPage("topic")
            assertThat(response.parse.title, `is`("Page Title"))
            assertThat(response.parse.pageId, `is`(1234))
            assertThat(response.parse.text.htmlDump, `is`("html code"))
        }

    @Test(expected = HttpException::class)
    fun `Should throw a HttpException when the page is not found`() {
        runBlocking {
            val mockResponse =
                MockResponse()
                    .setResponseCode(HttpURLConnection.HTTP_NOT_FOUND)
                    .setBody("")
            mockWebServer.enqueue(mockResponse)

            api.getPage("topic")
        }
    }

    @Test(expected = JsonDataException::class)
    fun `Should throw JsonDataException when the response is missing a required value`() {
        runBlocking {
            val mockResponse =
                MockResponse()
                    .setResponseCode(HttpURLConnection.HTTP_OK)
                    .setBody(
                        """
                        {
                           "parse": {
                               "title": "Page Title",
                               "text": {
                                   "*": "html code"
                               }
                           }
                        }
                        """.trimIndent(),
                    )
            mockWebServer.enqueue(mockResponse)
            api.getPage("topic")
        }
    }

    @Test(expected = JsonDataException::class)
    fun `Should throw JsonDataException when the data model variable does not match the json name`() {
        runBlocking {
            val mockResponse =
                MockResponse()
                    .setResponseCode(HttpURLConnection.HTTP_OK)
                    .setBody(
                        """
                        {
                           "parse": {
                               "title": "Page Title",
                               "titleId": "1234",
                               "text": {
                                   "*": "html code"
                               }
                           }
                        }
                        """.trimIndent(),
                    )
            mockWebServer.enqueue(mockResponse)
            api.getPage("topic")
        }
    }
}
