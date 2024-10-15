package com.example.wikipediatopics.api

interface ApiResult<T>

data class Success<T>(val value: T) : ApiResult<T>

data class Failure<T>(val error: Throwable?, val msg: String?) : ApiResult<T>
