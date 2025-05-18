package com.example.bit_chronicles_1

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ChatMessages(messages: List<Message>) {
    Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        messages.forEach { message ->
            Text(
                text = (if (message.isFromUser) "You: " else "Bot: ") + message.text,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }
    }
}
