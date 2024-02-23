package de.mm20.launcher2.preferences.ktx

import de.mm20.launcher2.preferences.LegacySettings
import scheme.Scheme

fun Scheme.toSettingsColorsScheme(): LegacySettings.AppearanceSettings.CustomColors.Scheme {
    val scheme = this
    return LegacySettings.AppearanceSettings.CustomColors.Scheme.newBuilder()
        .setPrimary(scheme.primary)
        .setSurfaceTint(scheme.primary)
        .setOnPrimary(scheme.onPrimary)
        .setPrimaryContainer(scheme.primaryContainer)
        .setOnPrimaryContainer(scheme.onPrimaryContainer)
        .setSecondary(scheme.secondary)
        .setOnSecondary(scheme.onSecondary)
        .setSecondaryContainer(scheme.secondaryContainer)
        .setOnSecondaryContainer(scheme.onSecondaryContainer)
        .setTertiary(scheme.tertiary)
        .setOnTertiary(scheme.onTertiary)
        .setTertiaryContainer(scheme.tertiaryContainer)
        .setOnTertiaryContainer(scheme.onTertiaryContainer)
        .setError(scheme.error)
        .setOnError(scheme.onError)
        .setErrorContainer(scheme.errorContainer)
        .setOnErrorContainer(scheme.onErrorContainer)
        .setBackground(scheme.background)
        .setOnBackground(scheme.onBackground)
        .setSurface(scheme.surface)
        .setOnSurface(scheme.onSurface)
        .setSurfaceVariant(scheme.surfaceVariant)
        .setOnSurfaceVariant(scheme.onSurfaceVariant)
        .setOutline(scheme.outline)
        .setOutlineVariant(scheme.outlineVariant)
        .setInverseSurface(scheme.inverseSurface)
        .setInverseOnSurface(scheme.inverseOnSurface)
        .setInversePrimary(scheme.inversePrimary)
        .setScrim(scheme.scrim)
        .setSurfaceDim(scheme.surfaceDim)
        .setSurfaceBright(scheme.surfaceBright)
        .setSurfaceContainerLowest(scheme.surfaceContainerLowest)
        .setSurfaceContainerLow(scheme.surfaceContainerLow)
        .setSurfaceContainer(scheme.surfaceContainer)
        .setSurfaceContainerHigh(scheme.surfaceContainerHigh)
        .setSurfaceContainerHighest(scheme.surfaceContainerHighest)
        .build()
}