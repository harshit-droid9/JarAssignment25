package tech.oklocation.jar.assignment.presentation.onboarding

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import tech.oklocation.jar.assignment.R
import tech.oklocation.jar.assignment.data.remote.model.EducationCard
import tech.oklocation.jar.assignment.data.remote.model.OnboardingData
import tech.oklocation.jar.assignment.data.remote.model.SaveButtonCta


@Composable
fun OnboardingScreen(
    paddingValues: PaddingValues,
    onboardingData: OnboardingData,
) {
    var showCta by remember { mutableStateOf(false) }
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(Modifier.fillMaxSize()) {
            OnboardingCarousel(
                screens = onboardingData.educationCardList,
                onFinished = {
                    showCta = true
                },
            )
            PrimaryCtaWithLottie(
                modifier = Modifier.align(Alignment.BottomCenter),
                cta = onboardingData.saveButtonCta,
                lottieAnimation = onboardingData.ctaLottie,
                onCtaClicked = {},
                isAnimationStarted = showCta
            )
        }
    }
}

@Composable
fun OnboardingCarousel(
    screens: List<EducationCard>,
    onFinished: () -> Unit,
    autoScrollDelayMs: Long = 1000,
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
                ),
                startY = 0f,
                endY = screenHeightPx,
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
    screen: EducationCard,
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
fun CollapsedCardContent(screen: EducationCard) {
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
fun ExpandedCardContent(screen: EducationCard) {
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

@Composable
fun PrimaryCtaWithLottie(
    modifier: Modifier = Modifier,
    cta: SaveButtonCta,
    lottieAnimation: String,
    onCtaClicked: () -> Unit,
    isAnimationStarted: Boolean,
) {
    if (!isAnimationStarted) return

    val startOffsetDp = 200.dp
    val endOffsetDp = 0.dp

    val slideAnim = remember {
        Animatable(initialValue = startOffsetDp.value)
    }

    val fadeAnim = remember {
        Animatable(initialValue = 0f)
    }

    val composition by rememberLottieComposition(
        LottieCompositionSpec.Url(lottieAnimation)
    )

    LaunchedEffect(isAnimationStarted) {
        slideAnim.animateTo(
            targetValue = endOffsetDp.value,
            animationSpec = tween(
                durationMillis = 800,
                easing = FastOutSlowInEasing
            )
        )
        fadeAnim.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 600,
                easing = LinearEasing
            )
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 24.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Box(
            modifier = modifier
                .offset(y = slideAnim.value.dp)
                .alpha(fadeAnim.value)
                .background(color = parseColor(cta.backgroundColor), CircleShape)
                .border(1.dp, parseColor(cta.strokeColor), CircleShape)
                .clickable { onCtaClicked() }
        ) {
            Row(
                modifier = Modifier.padding(vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Spacer(modifier.width(16.dp))
                if (cta.icon != null) {
                    AsyncImage(
                        model = rememberAsyncImagePainter(cta.icon),
                        contentDescription = "cta_icon"
                    )
                }
                Text(
                    text = cta.text,
                    style = MaterialTheme.typography.headlineLarge.copy(
                        color = Color.White
                    )
                )
                LottieAnimation(
                    composition = composition,
                    modifier = Modifier.size(44.dp),
                    iterations = LottieConstants.IterateForever
                )
                Spacer(modifier.width(16.dp))
            }
        }
    }
}

fun parseColor(hex: String): Color {
    val colorString = if (hex.startsWith("#")) hex else "#$hex"
    return Color(android.graphics.Color.parseColor(colorString))
}

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
    cardState: CardState,
    topOffsetPx: Float,
    collapsedHeightPx: Float,
    durationMillis: Int = 600,
) {
    val animSpec = tween<Float>(durationMillis, easing = FastOutSlowInEasing)

    cardState.yOffset.animateTo(topOffsetPx, animSpec)
    cardState.height.animateTo(collapsedHeightPx, animSpec)

}