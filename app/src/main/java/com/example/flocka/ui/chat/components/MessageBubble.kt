package com.example.flocka.ui.chat.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.example.flocka.ui.chat.domain.Message
import com.example.flocka.ui.chat.domain.privateChat

@Composable
fun MessageBubble(message: Message) {
    val bubbleColor = if (message.isMine) Color(0xFFECECEC) else Color.White
    val horizontalPadding = if (message.isMine) PaddingValues(start = 48.dp, end = 16.dp) else PaddingValues(start = 16.dp, end = 48.dp)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = if (message.isMine) Arrangement.End else Arrangement.Start
    ) {
        Column(
            modifier = Modifier
                .padding(horizontalPadding)
                .background(color = bubbleColor, shape = TailBubbleShape(isMine = message.isMine))
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Text(
                text = message.content,
                color = Color.Black,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = message.time,
                color = Color.Gray,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.align(Alignment.End)
            )
        }
    }
}

class TailBubbleShape(private val isMine: Boolean) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        return with(density) {
            val cornerRadius = 20.dp.toPx()
            val tailWidth = 6.dp.toPx()
            val tailHeight = 6.dp.toPx()
            val tailOffset = 10.dp.toPx()
            val tailBackOffset = 16.dp.toPx()

            val path = Path().apply {
                if (isMine) {
                    // Tail di kanan
                    moveTo(0f, cornerRadius)
                    arcTo(Rect(0f, 0f, cornerRadius * 2, cornerRadius * 2), 180f, 90f, false)
                    lineTo(size.width - cornerRadius, 0f)
                    arcTo(Rect(size.width - 2 * cornerRadius, 0f, size.width, 2 * cornerRadius), 270f, 90f, false)
                    lineTo(size.width, size.height - cornerRadius)
                    arcTo(Rect(size.width - 2 * cornerRadius, size.height - 2 * cornerRadius, size.width, size.height), 0f, 90f, false)

                    // Tail kanan
                    lineTo(size.width - tailOffset, size.height)
                    lineTo(size.width + tailWidth, size.height + tailHeight)
                    lineTo(size.width - tailBackOffset, size.height)

                    lineTo(cornerRadius, size.height)
                    arcTo(Rect(0f, size.height - 2 * cornerRadius, 2 * cornerRadius, size.height), 90f, 90f, false)
                    close()
                } else {
                    // Tail di kiri
                    moveTo(tailOffset, size.height)
                    lineTo(-tailWidth, size.height + tailHeight)
                    lineTo(tailBackOffset, size.height)

                    lineTo(cornerRadius, size.height)
                    arcTo(Rect(0f, size.height - 2 * cornerRadius, 2 * cornerRadius, size.height), 90f, 90f, false)
                    lineTo(0f, cornerRadius)
                    arcTo(Rect(0f, 0f, cornerRadius * 2, cornerRadius * 2), 180f, 90f, false)
                    lineTo(size.width - cornerRadius, 0f)
                    arcTo(Rect(size.width - 2 * cornerRadius, 0f, size.width, 2 * cornerRadius), 270f, 90f, false)
                    lineTo(size.width, size.height - cornerRadius)
                    arcTo(Rect(size.width - 2 * cornerRadius, size.height - 2 * cornerRadius, size.width, size.height), 0f, 90f, false)
                    close()
                }
            }

            Outline.Generic(path)
        }
    }
}


@Preview
@Composable
fun MessageBubblePreview() {
    MessageBubble(message = privateChat.messages.first())
}
