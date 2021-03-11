package org.fairventures.treeo.services

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.fairventures.treeo.MockWebSeverFileReader
import org.fairventures.treeo.models.NewRegisteredUser
import org.fairventures.treeo.models.RegisterUser
import org.json.JSONObject
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.net.HttpURLConnection

class RequestManagerTest {
    private val mockSever = MockWebServer()
    lateinit var apiService: ApiService

    @Before
    fun setUp() {
        mockSever.start()
        apiService = ApiService.create()
    }

    @After
    fun tearDown() {
        mockSever.shutdown()
    }

    private fun `parse mocked JSON response`(mockResponse: String): JSONObject {
        return JSONObject(mockResponse)
    }

    @Test
    fun ` test create user endpoint success`() {
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(MockWebSeverFileReader("create_user_success_response.json").content)

        mockSever.enqueue(response)
        val user = RegisterUser(
            "first",
            "last",
            "secret1234",
            "test@gmail.com",
            "country",
            "username",
            "0759111222"
        )
        val actualResponse = apiService.createUser(user)
        val result: NewRegisteredUser? = null
        actualResponse to result
        print(result)
    }

}