package tech.oklocation.jar.assignment.composable

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import tech.oklocation.jar.assignment.R
import tech.oklocation.jar.assignment.ui.theme.White

data class OnboardingScreen(
    val image: String,
    val collapsedStateText: String,
    val expandStateText: String,
    val backGroundColor: String,
    val strokeStartColor: String,
    val strokeEndColor: String,
    val startGradient: String,
    val endGradient: String
)

class CardState(
    val yOffset: Animatable<Float, *>,
    val height: Animatable<Float, *>,
    val alpha: Animatable<Float, *>,
)

suspend fun animateCardBottomToCenter(
    cardState: CardState, snappingPoint: Float, expandedHeightPx: Float, durationMillis: Int = 800
) {
    val animSpec = tween<Float>(durationMillis, easing = FastOutSlowInEasing)

    cardState.yOffset.animateTo(snappingPoint, animSpec)
    cardState.height.animateTo(expandedHeightPx, animSpec)

}

suspend fun animateCardCenterToTop(
    cardState: CardState, topOffsetPx: Float, collapsedHeightPx: Float, durationMillis: Int = 600
) {
    val animSpec = tween<Float>(durationMillis, easing = FastOutSlowInEasing)

    cardState.yOffset.animateTo(topOffsetPx, animSpec)
    cardState.height.animateTo(collapsedHeightPx, animSpec)

}

@Composable
fun OnboardingCarousel(
    screens: List<OnboardingScreen>, onFinished: () -> Unit = {}, autoScrollDelayMs: Long = 2000
) {
    val config = LocalConfiguration.current
    val density = LocalDensity.current

    val screenHeightDp = config.screenHeightDp.dp
    val screenHeightPx = with(density) { screenHeightDp.toPx() }

    val expandedHeightPx = screenHeightPx * 0.5f
    val collapsedHeightPx = with(density) { 60.dp.toPx() }

    val gapPx = with(density) { 16.dp.toPx() }
    fun topOffsetForIndex(index: Int) = index * (collapsedHeightPx + gapPx)

    var currentIndex by remember { mutableIntStateOf(0) }
    val coroutineScope = rememberCoroutineScope()

    val snappingPoints = remember {
        List(screens.size) {
            when (it) {
                0 -> screenHeightPx * 0.15f
                1 -> screenHeightPx * 0.7f
                2 -> screenHeightPx * 0.7f
                else -> screenHeightPx * 0.5f
            }
        }
    }
    val cardStates = remember {
        List(screens.size) { i ->
            CardState(
                yOffset = Animatable(screenHeightPx),
                height = Animatable(expandedHeightPx),
                alpha = Animatable(if (i == 0) 1f else 0f),
            )
        }
    }

    LaunchedEffect(currentIndex) {
        if (currentIndex in screens.indices) {
            val currentCard = cardStates[currentIndex]

            animateCardBottomToCenter(
                cardState = currentCard,
                snappingPoint = snappingPoints[currentIndex],
                expandedHeightPx = expandedHeightPx,
                durationMillis = 800
            )

            delay(autoScrollDelayMs)

            // 2) Animate center → top (shrink)
            if (currentIndex < screens.size - 1) {
                val nextCard = cardStates[currentIndex + 1]

                // Place next card at bottom so it’s ready
                nextCard.yOffset.snapTo(screenHeightPx)
                nextCard.height.snapTo(expandedHeightPx)
                nextCard.alpha.snapTo(1f)

                coroutineScope.launch {
                    animateCardCenterToTop(
                        cardState = currentCard,
                        topOffsetPx = topOffsetForIndex(currentIndex),
                        collapsedHeightPx = collapsedHeightPx,
                        durationMillis = 600
                    )
                }

                delay(600)
                currentIndex++
            } else {
                // Last card
                animateCardCenterToTop(
                    cardState = currentCard,
                    topOffsetPx = topOffsetForIndex(currentIndex),
                    collapsedHeightPx = expandedHeightPx,
                    durationMillis = 600
                )
                delay(autoScrollDelayMs)
                onFinished()
            }
        }
    }

    val backgroundGradient = remember(currentIndex) {
        List(screens.size) {
            val card = screens[currentIndex]
            Brush.verticalGradient(
                colors = listOf(
                    parseColor(card.startGradient),
                    parseColor(card.endGradient).copy(alpha = 0.32f)
                ), startY = 0f, endY = screenHeightPx
            )
        }
    }

    val cardBackgroundColor = remember(currentIndex) { screens[currentIndex].backGroundColor }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundGradient[currentIndex])
    ) {
        HeaderSection()

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 70.dp, start = 16.dp, end = 16.dp)
        ) {
            screens.indices.forEach { index ->
                val cardState = cardStates[index]
                val yOffset = cardState.yOffset.value
                val alpha = cardState.alpha.value

                OnboardingCard(
                    screenHeightPx = screenHeightPx,
                    screen = screens[index],
                    cardState = cardState,
                    backGroundColor = cardBackgroundColor,
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .graphicsLayer { translationY = yOffset }
                        .alpha(alpha)
                        .zIndex(index.toFloat()),
                )
            }
        }

    }
}

@Composable
fun OnboardingCard(
    screen: OnboardingScreen,
    modifier: Modifier = Modifier,
    screenHeightPx: Float,
    cardState: CardState,
    backGroundColor: String
) {
    val density = LocalDensity.current
    val heightDp = with(density) { cardState.height.value.toDp() }

    val backgroundColor = parseColor(backGroundColor).copy(alpha = 0.32f)
    val strokeGradient = Brush.verticalGradient(
        colors = listOf(
            parseColor(screen.strokeStartColor), parseColor(screen.strokeEndColor)
        ), startY = 0f, endY = screenHeightPx
    )
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(heightDp)
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .border(1.dp, strokeGradient, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        if (heightDp > 100.dp) {
            ExpandedCardContent(screen)
        } else {
            CollapsedCardContent(screen)
        }
    }
}


@Composable
fun HeaderSection() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, start = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            modifier = Modifier,
            painter = painterResource(R.drawable.ic_back_button),
            contentDescription = "back button",
            tint = Color.White
        )

        Text(
            text = stringResource(R.string.onboarding),
            style = MaterialTheme.typography.headlineMedium.copy(
                color = MaterialTheme.colorScheme.onPrimary
            ),
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}

@Composable
fun CollapsedCardContent(screen: OnboardingScreen) {
    Row(
        verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(RoundedCornerShape(20.dp)),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = screen.image,
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                contentScale = ContentScale.Crop
            )
        }

        Text(
            text = screen.collapsedStateText,
            style = MaterialTheme.typography.headlineLarge.copy(
                color = MaterialTheme.colorScheme.onPrimary
            ),
            modifier = Modifier.padding(start = 12.dp)
        )

        Box(
            modifier = Modifier.weight(1f), contentAlignment = Alignment.CenterEnd
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_down_arrow),
                contentDescription = "down_arrow",
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Composable
fun ExpandedCardContent(screen: OnboardingScreen) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(), contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = screen.image,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                contentScale = ContentScale.FillBounds
            )
        }

        // Text
        Text(
            text = screen.expandStateText,
            style = MaterialTheme.typography.headlineSmall.copy(
                color = MaterialTheme.colorScheme.onPrimary
            ),
            modifier = Modifier.padding(bottom = 24.dp, top = 8.dp)
        )
    }
}


fun parseColor(hex: String): Color {
    val colorString = if (hex.startsWith("#")) hex else "#$hex"
    return Color(android.graphics.Color.parseColor(colorString))
}

@Composable
fun OnboardingScreen(paddingValues: PaddingValues) {
    val onboardingScreens = listOf(
        OnboardingScreen(
            image = "https://cdn.myjar.app/Homefeed/engagement_card/G2.png",
            collapsedStateText = "Buy gold anytime, anywhere",
            expandStateText = "Get better returns with gold!",
            backGroundColor = "992D7E",
            strokeStartColor = "#33FFFFFF",
            strokeEndColor = "#CCFFFFFF",
            startGradient = "#713A65",
            endGradient = "#713A65"
        ), OnboardingScreen(
            image = "https://cdn.myjar.app/Homefeed/engagement_card/G1.png",
            collapsedStateText = "Save extra cash, get returns",
            expandStateText = "Gold's growth is unstoppable!",
            backGroundColor = "197A41",
            strokeStartColor = "#33FFFFFF",
            strokeEndColor = "#CCFFFFFF",
            startGradient = "#286642",
            endGradient = "#286642"
        ), OnboardingScreen(
            image = "https://cdn.myjar.app/Homefeed/engagement_card/G3_1.PNG",
            collapsedStateText = "ZERO platform or convenience fees",
            expandStateText = "NO HIDDEN FEES",
            backGroundColor = "722ed1",
            strokeStartColor = "#33FFFFFF",
            strokeEndColor = "#CCFFFFFF",
            startGradient = "#722ed1",
            endGradient = "#722ed1"
        )
    )

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        color = MaterialTheme.colorScheme.background
    ) {
        OnboardingCarousel(screens = onboardingScreens, onFinished = {
            // Navigate to next screen or handle completion
        })
    }
}