package com.flamedate.app.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.flamedate.app.domain.model.UserProfile
import com.flamedate.app.ui.theme.LikeGreen
import com.flamedate.app.ui.theme.NopeRed
import com.flamedate.app.ui.theme.SuperLikeBlue
import kotlinx.coroutines.launch
import kotlin.math.abs

@Composable
fun SwipeableCard(
    profile: UserProfile,
    onSwipeLeft: () -> Unit,
    onSwipeRight: () -> Unit,
    onSwipeUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    val offsetX = remember { Animatable(0f) }
    val offsetY = remember { Animatable(0f) }
    var showLike by remember { mutableStateOf(false) }
    var showNope by remember { mutableStateOf(false) }
    var showSuperLike by remember { mutableStateOf(false) }

    val swipeThreshold = 300f

    Box(
        modifier = modifier
            .offset(x = offsetX.value.dp, y = offsetY.value.dp)
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragEnd = {
                        if (offsetX.value > swipeThreshold) {
                            scope.launch {
                                offsetX.animateTo(
                                    targetValue = 2000f,
                                    animationSpec = tween(300, easing = FastOutSlowInEasing)
                                )
                                onSwipeRight()
                            }
                        } else if (offsetX.value < -swipeThreshold) {
                            scope.launch {
                                offsetX.animateTo(
                                    targetValue = -2000f,
                                    animationSpec = tween(300, easing = FastOutSlowInEasing)
                                )
                                onSwipeLeft()
                            }
                        } else if (offsetY.value < -swipeThreshold) {
                            scope.launch {
                                offsetY.animateTo(
                                    targetValue = -2000f,
                                    animationSpec = tween(300, easing = FastOutSlowInEasing)
                                )
                                onSwipeUp()
                            }
                        } else {
                            scope.launch {
                                offsetX.animateTo(0f, animationSpec = tween(300))
                                offsetY.animateTo(0f, animationSpec = tween(300))
                            }
                        }
                    },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        scope.launch {
                            offsetX.snapTo(offsetX.value + dragAmount.x)
                            offsetY.snapTo(offsetY.value + dragAmount.y)
                        }
                        showLike = offsetX.value > 80
                        showNope = offsetX.value < -80
                        showSuperLike = offsetY.value < -80
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Box {
                AsyncImage(
                    model = profile.photos.firstOrNull() ?: "",
                    contentDescription = profile.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .size(500.dp),
                    contentScale = ContentScale.Crop
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.7f)
                                ),
                                startY = 500f
                            )
                        )
                )

                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(16.dp)
                ) {
                    if (showNope) {
                        Text(
                            text = "NOPE",
                            color = NopeRed,
                            fontSize = 40.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .background(
                                    color = Color.White.copy(alpha = 0.8f),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                    if (showLike) {
                        Text(
                            text = "LIKE",
                            color = LikeGreen,
                            fontSize = 40.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .background(
                                    color = Color.White.copy(alpha = 0.8f),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                    if (showSuperLike && abs(offsetX.value) < 80) {
                        Text(
                            text = "SUPER",
                            color = SuperLikeBlue,
                            fontSize = 40.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .background(
                                    color = Color.White.copy(alpha = 0.8f),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                ) {
                    Text(
                        text = "${profile.name}, ${profile.age}",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                if (profile.isVerified) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(start = 16.dp, bottom = 50.dp)
                    ) {
                        Text(
                            text = "${profile.occupation}",
                            color = Color.White.copy(alpha = 0.9f),
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
    }
}
