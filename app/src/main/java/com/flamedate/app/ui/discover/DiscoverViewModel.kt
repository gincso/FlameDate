package com.flamedate.app.ui.discover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.flamedate.app.domain.model.SwipeAction
import com.flamedate.app.domain.model.UserProfile
import com.flamedate.app.domain.repository.DatingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class DiscoverUiState(
    val profiles: List<UserProfile> = emptyList(),
    val isLoading: Boolean = true
)

class DiscoverViewModel(
    private val repository: DatingRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DiscoverUiState())
    val uiState: StateFlow<DiscoverUiState> = _uiState.asStateFlow()

    init {
        loadProfiles()
    }

    private fun loadProfiles() {
        viewModelScope.launch {
            repository.getDiscoverProfiles().collect { profiles ->
                _uiState.value = DiscoverUiState(
                    profiles = profiles,
                    isLoading = false
                )
            }
        }
    }

    fun onSwipeLeft(profile: UserProfile) {
        removeProfile(profile)
    }

    fun onSwipeRight(profile: UserProfile) {
        viewModelScope.launch {
            repository.swipe(profile.id, SwipeAction.LIKE)
        }
        removeProfile(profile)
    }

    fun onSwipeUp(profile: UserProfile) {
        viewModelScope.launch {
            repository.swipe(profile.id, SwipeAction.SUPER_LIKE)
        }
        removeProfile(profile)
    }

    private fun removeProfile(profile: UserProfile) {
        _uiState.value = _uiState.value.copy(
            profiles = _uiState.value.profiles.filter { it.id != profile.id }
        )
    }

    class Factory(private val repository: DatingRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return DiscoverViewModel(repository) as T
        }
    }
}
