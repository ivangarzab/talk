package com.ivangarzab.course.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil3.compose.AsyncImage
import com.ivangarzab.data.course.Day
import com.ivangarzab.resources.R
import com.ivangarzab.resources.ui.theme.selectedBlue

/**
 * The purpose of this composable function is to display a single [Day] item
 * in the [CourseScreenUnitList].
 */
@Composable
fun CourseScreenUnitListDayItem(
    modifier: Modifier = Modifier,
    index: Int,
    day: Day,
    selected: Boolean = false,
    onUnitDayClick: (Day) -> Unit
) {
    val color = when (selected) {
        true -> selectedBlue
        false -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .size(125.dp)
            .clickable { onUnitDayClick(day) }
    ) {
        val lineHeight = 40.dp
        // Left side
        ConstraintLayout(
            modifier = Modifier
                .align(Alignment.CenterVertically),
        ) {
            val (topLine, centerText, bottomLine) = createRefs()

            if (index != 0) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(lineHeight)
                        .background(MaterialTheme.colorScheme.onSurfaceVariant)
                        .constrainAs(topLine) {
                            centerHorizontallyTo(parent)
                            top.linkTo(parent.top)

                        }
                )
            }
            Column(
                modifier = Modifier.constrainAs(centerText) {
                    centerHorizontallyTo(parent)
                    centerVerticallyTo(parent)
                    top.linkTo(parent.top)
                }
            ) {
                Text(
                    modifier = Modifier,
                    text = stringResource(id = R.string.day_caps),
                    style = MaterialTheme.typography.titleSmall,
                    color = color
                )
                Text(
                    modifier = Modifier,
                    text = if (index > 10) "$index" else "0$index",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = color
                )
            }
            Box(
                modifier = Modifier
                    .width(2.dp)
                    .height(lineHeight)
                    .background(MaterialTheme.colorScheme.onSurfaceVariant)
                    .constrainAs(bottomLine) {
                        centerHorizontallyTo(parent)
                        top.linkTo(centerText.bottom)
                        bottom.linkTo(parent.bottom)
                    }
            )
        }
        // Right side
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .size(150.dp)
                .padding(start = 16.dp, end = 8.dp, top = 8.dp, bottom = 8.dp)
                .shadow(
                    elevation = 1.dp,
                    shape = RoundedCornerShape(16.dp)
                )
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(16.dp)
                )
                .align(Alignment.CenterVertically)
        ) {
            val (image, title, subtitle) = createRefs()

            Box(
                modifier = Modifier
                    .constrainAs(image) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                    }
                    .padding(start = 8.dp)
                    .size(80.dp)
                    .background(MaterialTheme.colorScheme.background, shape = CircleShape),
            ) {
                AsyncImage(
                    modifier = Modifier
                        .size(70.dp)
                        .clip(CircleShape)
                        .align(Alignment.Center),
                    contentDescription = stringResource(id = R.string.description_lesson_instructor_image),
                    model = day.thumbnailImageUrl,
                    placeholder = painterResource(id = R.drawable.profile_placeholder),
                    error = painterResource(id = R.drawable.profile_placeholder),
                    fallback = painterResource(id = R.drawable.profile_placeholder)
                )
            }
            Text(
                modifier = Modifier
                    .padding(start = 8.dp, top = 8.dp)
                    .constrainAs(title) {
                        top.linkTo(parent.top)
                        start.linkTo(image.end)
                        end.linkTo(parent.end)
                        bottom.linkTo(subtitle.top)
                        horizontalBias = 0f
                        width = Dimension.fillToConstraints
                    },
                text = day.title,
                style = MaterialTheme.typography.bodyLarge,
            )
            Text(
                modifier = Modifier
                    .padding(start = 8.dp, bottom = 8.dp)
                    .constrainAs(subtitle) {
                        top.linkTo(title.bottom)
                        start.linkTo(image.end)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                        horizontalBias = 0f
                        width = Dimension.fillToConstraints
                    },
                text = day.subtitle,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Preview
@Composable
fun CourseScreenUnitListItemDayPreview() {
    CourseScreenUnitListDayItem(
        modifier = Modifier.background(color = MaterialTheme.colorScheme.background),
        index = 0,
        day = Day("1", "Learning Objective", "Some Topic", "Additional Info"),
        onUnitDayClick = { }
    )
}

@Preview
@Composable
fun CourseScreenUnitListItemDayNonFirstAndSelectedPreview() {
    CourseScreenUnitListDayItem(
        modifier = Modifier.background(color = MaterialTheme.colorScheme.background),
        index = 6,
        day = Day("1", "Learning Objective", "Some Topic", "Additional Info"),
        selected = true,
        onUnitDayClick = { }
    )
}