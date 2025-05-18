package com.example.bit_chronicles_1

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BakingViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<UiState>(UiState.Initial)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val messages = mutableListOf<Message>()

    private val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = BuildConfig.API_KEY
    )

    fun sendPrompt(prompt: String) {
        messages.add(Message(prompt, isFromUser = true))
        _uiState.value = UiState.Loading

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val fullPrompt = buildString {
                    messages.forEach { message ->
                        if (message.isFromUser) {
                            append("User: ${message.text}\n")
                        } else {
                            append("Bot: ${message.text}\n")
                        }
                    }
                    append("Bot:")
                }

                val response = generativeModel.generateContent(content { text(fullPrompt) })
                val botReply = response.text?.trim() ?: "No response"

                messages.add(Message(botReply, isFromUser = false))

                _uiState.value = UiState.Success(messages.toList())
            } catch (e: Exception) {
                _uiState.value = UiState.Error(
                    errorMessage = e.localizedMessage ?: "Unknown error",
                    messages = messages.toList()
                )
            }
        }
    }

}

