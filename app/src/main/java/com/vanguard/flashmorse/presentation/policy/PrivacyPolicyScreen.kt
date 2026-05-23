package com.vanguard.flashmorse.presentation.policy

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vanguard.flashmorse.R
import com.vanguard.flashmorse.ui.theme.appBodyText
import com.vanguard.flashmorse.ui.theme.appMutedText
import com.vanguard.flashmorse.ui.theme.appSecondaryText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivacyPolicyScreen(
    onBackClick: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    val uriHandler = LocalUriHandler.current
    val privacyUrl = stringResource(R.string.app_privacy_url)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.privacy_title), fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = stringResource(R.string.cd_back), tint = colorScheme.onBackground)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorScheme.background,
                    titleContentColor = colorScheme.onBackground
                )
            )
        },
        containerColor = colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                stringResource(R.string.privacy_security_title),
                style = MaterialTheme.typography.labelMedium,
                color = colorScheme.appSecondaryText,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(
                    containerColor = colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        stringResource(R.string.privacy_on_device_title),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        stringResource(R.string.privacy_on_device_body),
                        style = MaterialTheme.typography.bodyMedium,
                        color = colorScheme.appBodyText,
                        lineHeight = 20.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                stringResource(R.string.privacy_permissions_title),
                style = MaterialTheme.typography.labelMedium,
                color = colorScheme.appSecondaryText,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))

            PermissionInfoCard(
                title = stringResource(R.string.privacy_camera_permission_title),
                description = stringResource(R.string.privacy_camera_permission_body),
                cardBgColor = colorScheme.surface,
                titleColor = colorScheme.onSurface,
                descColor = colorScheme.appBodyText
            )

            Spacer(modifier = Modifier.height(8.dp))

            PermissionInfoCard(
                title = stringResource(R.string.privacy_audio_permission_title),
                description = stringResource(R.string.privacy_audio_permission_body),
                cardBgColor = colorScheme.surface,
                titleColor = colorScheme.onSurface,
                descColor = colorScheme.appBodyText
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                stringResource(R.string.privacy_contact_title),
                style = MaterialTheme.typography.labelMedium,
                color = colorScheme.appSecondaryText,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                stringResource(
                    R.string.privacy_contact_body,
                    stringResource(R.string.app_support_url)
                ),
                style = MaterialTheme.typography.bodySmall,
                color = colorScheme.appMutedText,
                lineHeight = 16.sp
            )
            Text(
                "Read Full Policy Online",
                modifier = Modifier
                    .padding(top = 8.dp)
                    .clickable {
                        uriHandler.openUri(privacyUrl)
                    },
                style = MaterialTheme.typography.bodySmall,
                color = colorScheme.primary,
                textDecoration = androidx.compose.ui.text.style.TextDecoration.Underline
            )
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun PermissionInfoCard(
    title: String,
    description: String,
    cardBgColor: Color,
    titleColor: Color,
    descColor: Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(
            containerColor = cardBgColor
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(title, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold, color = titleColor)
            Spacer(modifier = Modifier.height(4.dp))
            Text(description, style = MaterialTheme.typography.bodySmall, color = descColor, lineHeight = 18.sp)
        }
    }
}
