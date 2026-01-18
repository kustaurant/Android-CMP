package com.kus.kustaurant.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter

@Composable
expect fun homeTabPainter(): Painter
@Composable
expect fun drawTabPainter(): Painter
@Composable
expect fun tierTabPainter(): Painter
@Composable
expect fun communityTabPainter(): Painter
@Composable
expect fun myTabPainter(): Painter