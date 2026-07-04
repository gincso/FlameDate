package com.flamedate.app.ui.discover

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.flamedate.app.domain.model.UserProfile
import com.flamedate.app.ui.components.SwipeableCard
import com.flamedate.app.ui.theme.FlameRed
import com.flamedate.app.ui.theme.LikeGreen
import com.flamedate.app.ui.theme.NopeRed
import com.flamedate.app.ui.theme.SuperLikeBlue

@Composable
fun DiscoverScreen(
    viewModel: DiscoverViewModel = viewModel(factory = DiscoverViewModel.Factory(
        (androidx.compose.ui.platform.LocalContext.current.applicationContext as com.flamedate.app.FlameDateApp).repository
    ))
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (uiState.isLoading) {
            CircularProgressIndicator(color = FlameRed)
        } else if (uiState.profiles.isEmpty()) {
            NoMoreProfilesContent()
        } else {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    val currentProfile = uiState.profiles.lastOrNull()
                    if (currentProfile != null) {
                        SwipeableCard(
                            profile = currentProfile,
                            onSwipeLeft = { viewModel.onSwipeLeft(currentProfile) },
                            onSwipeRight = { viewModel.onSwipeRight(currentProfile) },
                            onSwipeUp = { viewModel.onSwipeUp(currentProfile) }
                        )
                    }
                }

                ActionButtons(
                    onNope = {
                        uiState.profiles.lastOrNull()?.let { viewModel.onSwipeLeft(it) }
                    },
                    onSuperLike = {
                        uiState.profiles.lastOrNull()?.let { viewModel.onSwipeUp(it) }
                    },
                    onLike = {
                        uiState.profiles.lastOrNull()?.let { viewModel.onSwipeRight(it) }
                    },
                    modifier = Modifier.padding(bottom = 24.dp)
                )
            }
        }
    }
}

@Composable
private fun NoMoreProfilesContent() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "No more profiles",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Check back later for new people nearby",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
        )
    }
}

@Composable
private fun ActionButtons(
    onNope: () -> Unit,
    onSuperLike: () -> Unit,
    onLike: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(color = NopeRed.copy(alpha = 0.1f), shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            IconButton(onClick = onNope) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Nope",
                    tint = NopeRed,
                    modifier = Modifier.size(32.dp)
                )
            }
        }

        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(color = SuperLikeBlue.copy(alpha = 0.1f), shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            IconButton(onClick = onSuperLike) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Super Like",
                    tint = SuperLikeBlue,
                    modifier = Modifier.size(32.dp)
                )
            }
        }

        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(color = LikeGreen.copy(alpha = 0.1f), shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            IconButton(onClick = onLike) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Like",
                    tint = LikeGreen,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}

