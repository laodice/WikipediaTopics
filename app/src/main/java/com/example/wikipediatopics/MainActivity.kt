package com.example.wikipediatopics

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.wikipediatopics.api.WikipediaApi
import com.example.wikipediatopics.page.PageRepository
import com.example.wikipediatopics.page.PageViewModel
import com.example.wikipediatopics.ui.theme.WikipediaTopicsTheme
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class MainActivity : ComponentActivity() {
    private val moshi =
        Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()

    private val retrofit =
        Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl("https://en.wikipedia.org/w/")
            .build()

    private val api = retrofit.create(WikipediaApi::class.java)
    private val repository = PageRepository(api)
    private val viewModel = PageViewModel(repository)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.searchTopic("Cumae")

        setContent {
            WikipediaTopicsTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    Greeting("Android")
                }
            }
        }
    }
}

@Composable
fun Greeting(
    name: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = "Hello $name!",
        modifier = modifier,
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WikipediaTopicsTheme {
        Greeting("Android")
    }
}
