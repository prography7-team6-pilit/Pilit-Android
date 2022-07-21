package com.prography.pilit.data.datasource.remote

import kotlinx.coroutines.CoroutineDispatcher
import retrofit2.HttpException
import java.io.IOException

/**
 * {description}
 *
 * @author capri
 * @since 2022/07/21
 */

suspend fun <T> wrapToResource(dispatcher: CoroutineDispatcher, apiCall: suspend () -> T): Resource<T> {
    return try {
        Resource.Success(apiCall())
    } catch (throwable: Throwable) {
        when(throwable) {
            is IOException -> Resource.Error(throwable.message ?: "ERROR")
            is HttpException -> {
                val code = throwable.code()
                Resource.Error(code.toString())
            }
            else -> {
                Resource.Error(throwable.message ?: "ERROR")
            }
        }
    }
}