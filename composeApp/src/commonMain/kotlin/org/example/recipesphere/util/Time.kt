package org.example.recipesphere.util

import kotlinx.datetime.Clock

fun nowEpochMs(): Long = Clock.System.now().toEpochMilliseconds()
