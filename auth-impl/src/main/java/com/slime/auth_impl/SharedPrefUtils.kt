/*
 * Copyright (C) 2022, Kasem S.M
 * All rights reserved.
 */
package com.slime.auth_impl

import android.content.SharedPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion

/**
 * Inspired from: https://gist.github.com/ologe/eaa1dea1f94cdda1a39adcaf3886658a
 */

fun SharedPreferences.observe(key: String, defValue: String?): Flow<String?> {
    val flow = MutableStateFlow(getString(key, defValue))

    val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, _ ->
        flow.value = getString(key, defValue)
    }

    try {
        registerOnSharedPreferenceChangeListener(listener)
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return flow
        .onCompletion { unregisterOnSharedPreferenceChangeListener(listener) }
        .catch { it.printStackTrace() }
}
