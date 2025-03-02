package tech.oklocation.jar.assignment.data.remote.model

data class OnboardingResponse(
    val success: Boolean,
    val data: OnboardingDataContainer
)

data class OnboardingDataContainer(
    val onboardingData: OnboardingData
)

data class OnboardingData(
    val toolBarText: String,
    val introTitle: String,
    val introSubtitle: String,
    val educationCardList: List<EducationCard>,
    val saveButtonCta: SaveButtonCta,
    val ctaLottie: String,
    val screenType: String,
    val cohort: String,
    val combination: String,
    val collapseCardTiltInterval: Int,
    val collapseExpandIntroInterval: Int,
    val bottomToCenterTranslationInterval: Int,
    val expandCardStayInterval: Int,
    val seenCount: Int
)

data class EducationCard(
    val image: String,
    val collapsedStateText: String,
    val expandStateText: String,
    val backGroundColor: String,
    val strokeStartColor: String,
    val strokeEndColor: String,
    val startGradient: String,
    val endGradient: String
)

data class SaveButtonCta(
    val text: String,
    val deeplink: String?,
    val backgroundColor: String,
    val textColor: String,
    val strokeColor: String,
    val icon: String?,
    val order: String?
)