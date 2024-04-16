package com.example.scarletexample

import com.tinder.scarlet.Message
import com.tinder.scarlet.WebSocket
import com.tinder.scarlet.ws.Receive
import com.tinder.scarlet.ws.Send
import kotlinx.coroutines.flow.Flow

interface WebSocketService {
    @Receive
    fun observeWebSocketEvent(): Flow<WebSocket.Event>

    @Send
    fun sendMessage(message: com.example.scarletexample.Message)

    @Receive
    fun observeMessage(): Flow<Message>
}
