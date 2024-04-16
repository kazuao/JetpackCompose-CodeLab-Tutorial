package com.example.scarletexample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.scarletexample.ui.theme.ScarletExampleTheme
import com.tinder.scarlet.Scarlet
import com.tinder.scarlet.WebSocket
import com.tinder.scarlet.lifecycle.android.AndroidLifecycle
import com.tinder.scarlet.websocket.okhttp.newWebSocketFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient

class MainActivity : ComponentActivity() {

    private fun subscribe() {
        val service = createWebSocketService()

        CoroutineScope(Dispatchers.IO).launch {
            service.observeWebSocketEvent().collect { event ->
                when (event) {
                    is WebSocket.Event.OnConnectionOpened<*> -> {
                        service.sendMessage(Message(text = "Hello"))
                    }

                    is WebSocket.Event.OnMessageReceived -> {
                        println("Received: ${event.message}")
                    }
                }
            }
        }
    }

    private fun createWebSocketService(): WebSocketService {
        val okHttpClient = OkHttpClient.Builder()
            .build()

        val scarletInstance = Scarlet.Builder()
            .webSocketFactory(okHttpClient.newWebSocketFactory("wss://example.com/api"))
            .addMessageAdapterFactory(KotlinSerializationAdapter.Factory())
            .lifecycle(AndroidLifecycle.ofApplicationForeground(application))
            .build()

        return scarletInstance.create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ScarletExampleTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Button(onClick = { subscribe() }) {
                        Text(text = "subscribe")
                    }
                }
            }
        }
    }
}
