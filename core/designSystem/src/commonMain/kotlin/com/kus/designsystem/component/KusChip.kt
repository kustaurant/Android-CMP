package com.kus.designsystem.component
  
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kus.designsystem.util.noRippleClickable
import org.jetbrains.compose.ui.tooling.preview.Preview

val Signature1 = Color(0xFF43AB38)
private val UnselectedStroke = Color(0xFFAAAAAA)

@Composable
fun KusChip(
    text: String,
    onClick: () -> Unit,
    selected: Boolean,
    isSelectable: Boolean,
    modifier: Modifier = Modifier
) {
    val strokeColor = if (selected) Signature1 else UnselectedStroke
    val bgColor = if (selected) Signature1.copy(alpha = 0.2f) else Color.Transparent

    Surface(
        modifier = modifier.then(
            if (isSelectable) Modifier.noRippleClickable(onClick) else Modifier
        ),
        shape = RoundedCornerShape(16.dp),
        color = bgColor,
        border = BorderStroke(1.dp, strokeColor)
    ) {
        Box(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                color = strokeColor,
                fontSize = 14.sp
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewKusChip() {
    var isSelected1 by remember { mutableStateOf(true) }
    var isSelected2 by remember { mutableStateOf(true) }

    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        KusChip(
            text = "Selectable",
            selected = isSelected1,
            isSelectable = true,
            onClick = { isSelected1 = !isSelected1 }
        )

        KusChip(
            text = "Locked (selected but not clickable)",
            selected = isSelected2,
            isSelectable = false,
            onClick = { isSelected2 = !isSelected2 } // 호출 안 됨
        )
    }
}