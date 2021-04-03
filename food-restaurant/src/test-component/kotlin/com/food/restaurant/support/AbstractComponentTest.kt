package com.food.restaurant.support

import io.restassured.response.ValidatableResponse
import org.apache.commons.io.IOUtils
import org.json.JSONException
import org.json.JSONObject
import org.springframework.util.ResourceUtils
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream
import java.nio.charset.StandardCharsets

abstract class AbstractComponentTest {

    protected open fun getJsonStringExpectedFromClasspath(pathJsonExpected: String): String {
        return try {
            val file: File = ResourceUtils.getFile("classpath:${pathJsonExpected}")
            val `is`: InputStream = FileInputStream(file)
            IOUtils.toString(`is`, StandardCharsets.UTF_8)
        } catch (e: IOException) {
            e.printStackTrace()
            throw RuntimeException("Unable to parse file [${pathJsonExpected}] from classpath", e)
        }
    }

    protected open fun getJsonObjectExpectedFromClasspath(pathJsonExpected: String): JSONObject {
        return try {
            val jsonTxt: String = this.getJsonStringExpectedFromClasspath(pathJsonExpected)
            JSONObject(jsonTxt)
        } catch (e: JSONException) {
            e.printStackTrace()
            throw RuntimeException("Unable to parse file [${pathJsonExpected}] from classpath", e)
        }
    }

    protected open fun getJsonStringActual(json: ValidatableResponse?): String {
        return json?.extract()?.asString() ?: ""
    }

    protected open fun getJsonObjectActual(json: ValidatableResponse?): JSONObject {
        return try {
            JSONObject(this.getJsonStringActual(json))
        } catch (e: JSONException) {
            e.printStackTrace()
            throw RuntimeException("Unable get json actual from rest assured response", e)
        }
    }
}
