package org.example.recipesphere.util

import kotlin.random.Random

fun newId(prefix: String = "r"): String {
    val rnd = Random.nextLong().toString(36)
    val ts  = nowEpochMs().toString(36)
    return "$prefix-$ts-$rnd".replace('-', 'x')
}
