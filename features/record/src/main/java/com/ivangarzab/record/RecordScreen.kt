package com.ivangarzab.record

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import com.ivangarzab.resources.ui.theme.selectedBlue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ivangarzab.resources.ui.theme.TalkTheme
import com.ivangarzab.resources.R as R

/**
 * TODO:
 */
@Composable
fun RecordScreen(
    modifier: Modifier = Modifier,
    onRecordClick: () -> Unit
) {
    TalkTheme {
        Box(
            modifier = modifier
                .fillMaxSize()
                //TODO: The Material colors don't seem to be working!
                .background(color = Color(0xFFD6D6D6))
        ) {
            Box(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 36.dp)
                    .fillMaxSize()
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(16.dp)
                    )
            ) {
                Text(
                    modifier = Modifier
                        .padding(top = 128.dp)
                        .fillMaxWidth()
                        .align(Alignment.TopCenter),
                    text = "THIS IS A VERY LONG AND ACCURATE TEST",
                    style = MaterialTheme.typography.displayLarge,
                    textAlign = TextAlign.Center
                )
                RecordScreenButton(
                    modifier = Modifier
                        .padding(bottom = 32.dp)
                        .align(Alignment.BottomCenter),
                    onRecordClick = onRecordClick
                )
            }
        }
    }
}

@Composable
fun RecordScreenButton(
    modifier: Modifier,
    onRecordClick: () -> Unit
) {
    Box(modifier = modifier) {
        Button(
            modifier = Modifier
                .size(96.dp),
            onClick = { onRecordClick() },
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFD6D6D6),
                contentColor = selectedBlue
            )
        ) {
            Image(
                modifier = Modifier
                    .size(64.dp),
                contentDescription = stringResource(R.string.description_record_button),
                painter = painterResource(R.drawable.ic_arrow_up),
                colorFilter = ColorFilter.tint(color = selectedBlue)
            )
        }
    }
}

@Preview
@Composable
fun RecordScreenPreview() {
    RecordScreen(
        onRecordClick = { }
    )
}