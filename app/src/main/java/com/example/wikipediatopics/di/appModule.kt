package com.example.wikipediatopics.di

import com.example.wikipediatopics.api.WikipediaApi
import com.example.wikipediatopics.page.PageRepository
import com.example.wikipediatopics.page.PageViewModel
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val appModule =
    module {
        single { createWikipediaApi() }

        single { PageRepository(get()) }

        viewModel { PageViewModel(get()) }
    }

private fun createWikipediaApi(): WikipediaApi {
    val moshi =
        Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()

    val retrofit =
        Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl("https://en.wikipedia.org/w/")
            .build()

    return retrofit.create(WikipediaApi::class.java)
}
