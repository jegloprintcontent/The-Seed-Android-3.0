package org.ileewe.theseedjcli.utils

data class Resource<T>(val status: Status?, val data: T?, val message: String?) {

    companion object {
        /**
         * Creates [Resource] object with `SUCCESS` status and [data].
         */
        fun <T> success(data: T): Resource<T>? {
            return Resource<T>(Status.SUCCESS, data, null)
        }

        /**
         * Creates [Resource] object with `ERROR` status and [message]
         */
        fun <T> error(
            message: String,
            data: T?
        ): Resource<T>? {
            return Resource<T>(Status.ERROR, data, message)
        }

        /**
         * Creates [Resource] object with `LOADING` status to notify the UI to show loading.
         */
        fun <T> loading(data: T?): Resource<T>? {
            return Resource<T>(Status.LOADING, data, null)
        }

    }

    enum class Status {
        SUCCESS, ERROR, LOADING
    }
}
