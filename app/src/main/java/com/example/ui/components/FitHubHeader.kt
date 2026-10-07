package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.InterFontFamily
import com.example.ui.theme.TextMain
import com.example.ui.theme.TextMuted

/**
 * FitHubHeader matching HTML .header:
 * - padding: 48px 20px 16px; (handled via top padding / status bars)
 * - .header-left: gap: 12px;
 * - .logo-small:
 *   - font-size: 10px; font-weight: 700; line-height: 1.2; text-align: center;
 *   - background: #ffffff; padding: 6px; border-radius: 12px;
 *   - box-shadow: 0 4px 12px rgba(0,0,0,0.04); border: 0.5px solid rgba(0, 0, 0, 0.08);
 *   - span: font-weight: 400; color: var(--text-muted);
 * - .header-title-container:
 *   - h1: font-size: 26px; font-weight: 700; color: var(--text-main); letter-spacing: -0.5px; margin-bottom: 2px;
 *   - p: font-size: 13px; color: var(--text-muted); letter-spacing: -0.2px;
 * - .profile-icon:
 *   - width: 42px; height: 42px; background: #ffffff; border-radius: 50%;
 *   - font-size: 18px; color: var(--text-main);
 *   - box-shadow: 0 4px 12px rgba(0,0,0,0.05); border: 0.5px solid rgba(0, 0, 0, 0.08);
 */
@Composable
fun FitHubHeader(
    onProfileClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp)
            .testTag("fithub_header"),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // .header-left: display: flex; align-items: center; gap: 12px;
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // .logo-small
            Box(
                modifier = Modifier
                    .shadow(
                        elevation = 4.dp,
                        shape = RoundedCornerShape(12.dp),
                        ambientColor = Color(0x0A000000),
                        spotColor = Color(0x0F000000)
                    )
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White)
                    .border(0.5.dp, Color(0x14000000), RoundedCornerShape(12.dp))
                    .padding(horizontal = 7.dp, vertical = 6.dp)
                    .testTag("header_logo_badge"),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "FitHub",
                        fontFamily = InterFontFamily,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextMain,
                        lineHeight = 12.sp
                    )
                    Text(
                        text = "fitness",
                        fontFamily = InterFontFamily,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Normal,
                        color = TextMuted,
                        lineHeight = 11.sp
                    )
                }
            }

            // .header-title-container
            Column {
                Text(
                    text = "FitHub",
                    fontFamily = InterFontFamily,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = (-0.5).sp,
                    color = TextMain
                )
                Text(
                    text = "Your fitness, all in one place.",
                    fontFamily = InterFontFamily,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Normal,
                    letterSpacing = (-0.2).sp,
                    color = TextMuted
                )
            }
        }

        // .profile-icon: 42px x 42px
        Box(
            modifier = Modifier
                .size(42.dp)
                .shadow(
                    elevation = 4.dp,
                    shape = CircleShape,
                    ambientColor = Color(0x0D000000),
                    spotColor = Color(0x12000000)
                )
                .clip(CircleShape)
                .background(Color.White)
                .border(0.5.dp, Color(0x14000000), CircleShape)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = ripple(bounded = true, color = TextMain.copy(alpha = 0.15f)),
                    onClick = onProfileClick
                )
                .testTag("profile_button"),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Rounded.Person,
                contentDescription = "User Profile",
                tint = TextMain,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}
