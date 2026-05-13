package com.nallanudi.ui.theme

import androidx.compose.ui.graphics.Color

val Blue50 = Color(0xFFEFF6FF)
val Blue100 = Color(0xFFDBEAFE)
val Blue600 = Color(0xFF2563EB)
val Blue700 = Color(0xFF1D4ED8)
val Blue900 = Color(0xFF1E3A8A)

val Teal50 = Color(0xFFF0FDFA)
val Teal100 = Color(0xFFCCFBF1)
val Teal600 = Color(0xFF0D9488)
val Teal700 = Color(0xFF0F766E)

val Amber50 = Color(0xFFFFFBEB)
val Amber100 = Color(0xFFFEF3C7)
val Amber600 = Color(0xFFD97706)
val Amber700 = Color(0xFFB45309)

val Green50 = Color(0xFFF0FDF4)
val Green100 = Color(0xFFDCFCE7)
val Green600 = Color(0xFF16A34A)
val Green700 = Color(0xFF15803D)

val Red50 = Color(0xFFFEF2F2)
val Red100 = Color(0xFFFEE2E2)
val Red600 = Color(0xFFDC2626)

val Slate50 = Color(0xFFF8FAFC)
val Slate100 = Color(0xFFF1F5F9)
val Slate200 = Color(0xFFE2E8F0)
val Slate300 = Color(0xFFCBD5E1)
val Slate600 = Color(0xFF475569)
val Slate700 = Color(0xFF334155)
val Slate800 = Color(0xFF1E293B)
val Slate900 = Color(0xFF0F172A)

val ScienceColor = Blue600
val ScienceLight = Blue100
val MathColor = Color(0xFF7C3AED)
val MathLight = Color(0xFFEDE9FE)
val CommerceColor = Green600
val CommerceLight = Green100

val White = Color(0xFFFFFFFF)

fun subjectColor(subject: String): Color = when (subject) {
    "Science" -> ScienceColor
    "Mathematics" -> MathColor
    "Commerce" -> CommerceColor
    else -> Blue600
}

fun subjectLightColor(subject: String): Color = when (subject) {
    "Science" -> ScienceLight
    "Mathematics" -> MathLight
    "Commerce" -> CommerceLight
    else -> Blue100
}
