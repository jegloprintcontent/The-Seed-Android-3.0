package org.ileewe.theseedjcli.requests.response

import retrofit2.Response

sealed class ApiResponse<out T> {
    data class Success<out T>(val data: T) : ApiResponse<T>()
    data class Error(val exception: Throwable) : ApiResponse<Nothing>()
    object Loading : ApiResponse<Nothing>() // Represents the loading state

    companion object {
        // Creates an ApiResponse from a Retrofit Response
        fun <T> create(response: Response<T>): ApiResponse<T> {
            return if (response.isSuccessful) {
                response.body()?.let {
                    Success(it)
                } ?: Error(Throwable("Response body is null"))
            } else {
                Error(Throwable(response.message()))
            }
        }

        // Creates an ApiResponse from an exception
        fun <T> create(error: Throwable): ApiResponse<T> {
            return Error(error)
        }

        // Represents the loading state
        fun <T> loading(): ApiResponse<T> {
            return Loading
        }
    }
}
