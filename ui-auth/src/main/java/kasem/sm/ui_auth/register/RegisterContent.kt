/*
 * Copyright (C) 2021, Kasem S.M
 * All rights reserved.
 */
package kasem.sm.ui_auth.register

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kasem.sm.common_ui.SlimePrimaryButton
import kasem.sm.common_ui.SlimeScreenColumn
import kasem.sm.common_ui.SlimeTypography
import kasem.sm.common_ui.getFont
import kasem.sm.ui_auth.common.AuthViewState
import kasem.sm.ui_auth.common.PasswordField
import kasem.sm.ui_auth.common.UsernameField

/**
 * This composable maintains the entire screen for handling user registration.
 *
 * @param[viewState] The current state of the screen to render.
 * @param[onUsernameChanged] A callback invoked when the user enters their username.
 * @param[onPasswordChanged] A callback invoked when the user enters their password.
 * @param[onConfirmClicked] A callback invoked when the user clicks the register button.
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun RegisterContent(
    viewState: AuthViewState,
    onUsernameChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onConfirmClicked: () -> Unit,
    togglePasswordVisibility: (Boolean) -> Unit,
    toggleAccountDiscoverability: (Boolean) -> Unit,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        val keyboardController = LocalSoftwareKeyboardController.current
        val focusManager = LocalFocusManager.current

        SlimeScreenColumn(
            verticalArrangement = Arrangement.Bottom,
        ) {
            item {
                Text(
                    text = "Publish Your Passion",
                    color = MaterialTheme.colorScheme.primary,
                    style = getFont(SlimeTypography.SemiBold(24.sp, letterSpacing = 1.sp))
                )
                Text(
                    text = "Get Started",
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .padding(vertical = 15.dp),
                    style = getFont(SlimeTypography.Medium(16.sp))
                )
            }

            item {
                UsernameField(
                    text = viewState.username,
                    onUsernameChanged = onUsernameChanged,
                    enabled = !viewState.isLoading,
                    modifier = Modifier
                        .padding(vertical = 10.dp),
                    onNext = {

                        focusManager.moveFocus(FocusDirection.Down)
                    }
                )
            }

            item {
                PasswordField(
                    text = viewState.password,
                    onPasswordChanged = onPasswordChanged,
                    enabled = !viewState.isLoading,
                    passwordToggle = viewState.passwordVisibility,
                    onPasswordToggleClick = togglePasswordVisibility,
                    modifier = Modifier
                        .padding(vertical = 10.dp),
                )
            }

            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Checkbox(
                        checked = viewState.isAccountDiscoverable,
                        onCheckedChange = toggleAccountDiscoverability
                    )
                    Text(
                        text = "Discoverable to other users in explore section",
                        style = getFont(SlimeTypography.Medium(14.sp, letterSpacing = 1.sp)),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }

            item {
                SlimePrimaryButton(
                    text = "Confirm Registration",
                    onClick = {
                        onConfirmClicked()
                        keyboardController?.hide()
                    },
                    enabled = !viewState.isLoading,
                    isLoading = viewState.isLoading,
                    modifier = Modifier
                        .padding(vertical = 15.dp)
                )
            }
        }
    }
}
