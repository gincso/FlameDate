package com.flamedate.app.ui.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.flamedate.app.domain.model.Message
import com.flamedate.app.domain.repository.DatingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ChatUiState(
    val messages: List<Message> = emptyList(),
    val isLoading: Boolean = true
)

class ChatViewModel(
    private val matchId: String,
    private val repository: DatingRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    init {
        loadMessages()
    }

    private fun loadMessages() {
        viewModelScope.launch {
            repository.markMessagesRead(matchId)
            repository.getMessages(matchId).collect { messages ->
                _uiState.value = ChatUiState(
                    messages = messages,
                    isLoading = false
                )
            }
        }
    }

    fun sendMessage(text: String) {
        viewModelScope.launch {
            repository.sendMessage(matchId, text)
        }
    }

    class Factory(
        private val matchId: String,
        private val repository: DatingRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ChatViewModel(matchId, repository) as T
        }
    }
}
