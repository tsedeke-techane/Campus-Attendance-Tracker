package com.attendance.attendancetracker.common.utils

import java.io.IOException

/**
 * Base class for custom API related exceptions.
 */
sealed class ApiException(message: String?, cause: Throwable? = null) : IOException(message, cause)

/**
 * Indicates that there was no internet connection or a network connectivity issue.
 */
class NoInternetException(message: String = "No internet connection") : ApiException(message)

/**
 * Indicates an HTTP error from the server (e.g., 4xx or 5xx status codes).
 * @param statusCode The HTTP status code.
 * @param statusMessage The HTTP status message (e.g., "Not Found", "Unauthorized").
 * @param serverErrorMessage An optional detailed error message from the server's response body.
 */
class HttpApiException(
    val statusCode: Int,
    val statusMessage: String?,
    val serverErrorMessage: String? = null,
    cause: Throwable? = null
) : ApiException("HTTP $statusCode ${statusMessage ?: ""}${serverErrorMessage?.let { ": $it" } ?: ""}", cause)

/**
 * Indicates an unexpected error during an API call, not covered by other specific exceptions.
 */
class UnknownApiException(message: String = "An unknown API error occurred", cause: Throwable? = null) : ApiException(message, cause)

/**
 * Indicates an error when parsing the response from the server.
 */
class ResponseParsingException(message: String = "Error parsing server response", cause: Throwable? = null) : ApiException(message, cause)
