package com.example.bit_chronicles_1

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun BakingScreen(
    bakingViewModel: BakingViewModel = viewModel()
) {
    val uiState by bakingViewModel.uiState.collectAsState()
    var prompt by remember { mutableStateOf(TextFieldValue("")) }
    val scrollState = rememberScrollState()
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(16.dp)
                .verticalScroll(scrollState)
        ) {
            when (uiState) {
                is UiState.Loading -> CircularProgressIndicator()
                is UiState.Success -> ChatMessages((uiState as UiState.Success).messages)
                is UiState.Error -> {
                    ChatMessages((uiState as UiState.Error).messages)
                    Text(
                        text = (uiState as UiState.Error).errorMessage,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
                else -> {
                    Text(text = "Write something to start the chat.")
                }
            }
        }

        TextField(
            value = prompt,
            onValueChange = {
                if (it.text.contains("\n")) {
                    val message = it.text.trim()
                    if (message.isNotEmpty()) {
                        bakingViewModel.sendPrompt(message)
                        prompt = TextFieldValue("")
                        keyboardController?.hide()
                    }
                } else {
                    prompt = it
                }
            },
            placeholder = { Text("Type your message...") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            singleLine = false
        )
    }
}
