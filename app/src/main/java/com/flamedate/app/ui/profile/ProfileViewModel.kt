package com.flamedate.app.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.flamedate.app.domain.model.UserProfile
import com.flamedate.app.domain.repository.DatingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ProfileUiState(
    val profile: UserProfile? = null,
    val isLoading: Boolean = true
)

class ProfileViewModel(
    private val repository: DatingRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadProfile()
    }

    private fun loadProfile() {
        viewModelScope.launch {
            repository.getProfile("current_user").collect { profile ->
                _uiState.value = ProfileUiState(
                    profile = profile ?: UserProfile(
                        id = "current_user",
                        name = "You",
                        age = 25,
                        bio = "",
                        photos = emptyList(),
                        interests = emptyList(),
                        distance = 0.0,
                        occupation = "",
                        school = "",
                        isVerified = false
                    ),
                    isLoading = false
                )
            }
        }
    }

    fun updateBio(bio: String) {
        _uiState.value.profile?.let { profile ->
            val updated = profile.copy(bio = bio)
            viewModelScope.launch {
                repository.updateProfile(updated)
            }
            _uiState.value = _uiState.value.copy(profile = updated)
        }
    }

    class Factory(private val repository: DatingRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ProfileViewModel(repository) as T
        }
    }
}
