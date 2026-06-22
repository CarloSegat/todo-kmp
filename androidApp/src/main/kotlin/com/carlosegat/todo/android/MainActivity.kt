package com.carlosegat.todo.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.carlosegat.todo.android.ui.TodoNavHost
import com.carlosegat.todo.android.ui.theme.TodoAppTheme

class MainActivity : ComponentActivity() {
    // savedInstanceState: a Bundle the framework uses to restore transient UI state when the
    // Activity is destroyed and recreated (e.g. screen rotation).
    // It is null on a fresh launch, and non-null on recreation (it holds whatever was stashed
    // in onSaveInstanceState)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge() // vertical edges

        // setContent() { ... } also works
        // Kotlin has a rule so that empty () on trailing lambda can be omitted
        setContent {
            TodoAppTheme {
                TodoNavHost()
            }
        }
    }
}
