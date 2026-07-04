package com.flamedate.app.ui.matches

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.flamedate.app.domain.model.Match
import com.flamedate.app.domain.repository.DatingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class MatchesUiState(
    val matches: List<Match> = emptyList(),
    val isLoading: Boolean = true
)

class MatchesViewModel(
    private val repository: DatingRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MatchesUiState())
    val uiState: StateFlow<MatchesUiState> = _uiState.asStateFlow()

    init {
        loadMatches()
    }

    private fun loadMatches() {
        viewModelScope.launch {
            repository.getMatches().collect { matches ->
                _uiState.value = MatchesUiState(
                    matches = matches,
                    isLoading = false
                )
            }
        }
    }

    fun clearNewFlag(matchId: String) {
        viewModelScope.launch {
            repository.clearNewMatchFlag(matchId)
        }
    }

    class Factory(private val repository: DatingRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MatchesViewModel(repository) as T
        }
    }
}
