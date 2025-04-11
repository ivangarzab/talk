package com.ivangarzab.record.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ivangarzab.resources.R
import com.ivangarzab.resources.ui.theme.selectedBlue

/**
 * The purpose of this composable function is to hold a circular button
 * for the [com.ivangarzab.record.RecordScreen].
 */
@Composable
fun RecordScreenButton(
    modifier: Modifier = Modifier,
    onRecordClick: () -> Unit
) {
    Box(modifier = modifier) {
        Button(
            modifier = Modifier
                .size(96.dp),
            onClick = { onRecordClick() },
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.background,
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
fun RecordScreenButtonPreview() {
    RecordScreenButton { }
}