package com.ivangarzab.course.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ivangarzab.data.course.Day
import com.ivangarzab.resources.R
import com.ivangarzab.resources.ui.theme.TalkTheme
import com.ivangarzab.data.course.Unit as CourseUnit

/**
 * The purpose of this composable is to display the [com.ivangarzab.data.course.Course]'s
 * [CourseUnit] list, and
 */
@Composable
fun CourseScreenUnitList(
    modifier: Modifier = Modifier,
    units: List<CourseUnit> = emptyList(),
    onUnitDayClick: (Day) -> Unit
) {
    val listState = rememberLazyListState()

    val indexMapping = remember(units) {
        val mapping = mutableMapOf<Int, Pair<Int, Int>>()
        var flatIndex = 0

        units.forEachIndexed { unitIndex, unit ->
            // Skip unit header
            flatIndex++

            // Map each day's position
            unit.days.forEachIndexed { dayIndex, _ ->
                mapping[flatIndex] = Pair(unitIndex, dayIndex)
                flatIndex++
            }
        }
        mapping
    }

    // Get the top visible day using the mapping
    val topVisibleDayInfo by remember {
        derivedStateOf {
            // Try the first visible item
            var dayInfo = indexMapping[listState.firstVisibleItemIndex]

            // If the first visible item isn't a day (it's a header),
            // check the next item if available
            if (dayInfo == null && listState.firstVisibleItemIndex + 1 < listState.layoutInfo.totalItemsCount) {
                dayInfo = indexMapping[listState.firstVisibleItemIndex + 1]
            }

            dayInfo
        }
    }

    LazyColumn(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        state = listState,
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(top = 16.dp)
    ) {
        if (units.isNotEmpty()) {
            units.forEachIndexed { unitIndex, unit ->
                // Unit header
                item(key = "unit_$unitIndex") {
                    CourseScreenUnitListHeader(
                        modifier = modifier.padding(start = 8.dp),
                        index = unitIndex,
                        unit = unit
                    )
                }
                // day items per unit
                itemsIndexed(
                    items = unit.days,
                    key = { dayIndex, _ -> "unit_${unitIndex}_day_$dayIndex" }
                ) { dayIndex, day ->
                    val isSelected = topVisibleDayInfo == Pair(unitIndex, dayIndex)
                    CourseScreenUnitListDayItem(
                        index = dayIndex,
                        day = day,
                        selected = isSelected,
                        onUnitDayClick = { onUnitDayClick(it) }
                    )
                }
            }
        } else { // The list is empty!
            item {
                Text(
                    modifier = Modifier.padding(top = 16.dp, bottom = 16.dp),
                    text = stringResource(id = R.string.no_units_available),
                    style = MaterialTheme.typography.titleSmall,
                    fontStyle = FontStyle.Italic
                )
            }
        }
    }
}

@Composable
fun CourseScreenUnitListHeader(
    modifier: Modifier = Modifier,
    index: Int,
    unit: CourseUnit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            contentDescription = stringResource(id = R.string.description_unit_header_icon),
            painter = painterResource(id = R.drawable.course_unit_default_icon),
        )
        Text(
            modifier = modifier.padding(top = 8.dp),
            text = stringResource(id = R.string.unit_no, index + 1),
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            modifier = modifier.padding(top = 4.dp),
            text = unit.title,
            style = MaterialTheme.typography.headlineMedium
        )
    }
}

@Preview
@Composable
fun CourseScreenUnitListEmptyPreview() {
    TalkTheme {
        CourseScreenUnitList(
            modifier = Modifier.background(color = MaterialTheme.colorScheme.background),
            units = listOf(),
            onUnitDayClick = { }
        )
    }
}

@Preview
@Composable
fun CourseScreenUnitListPreview() {
    TalkTheme {
        CourseScreenUnitList(
            modifier = Modifier.background(color = MaterialTheme.colorScheme.background),
            units = listOf(
                CourseUnit("1", "Unit 1 Description", listOf(Day("1.1", "Learning Objective", "Some Topic", "Additional Info"), Day("1.2", "Learning Objective", "Some Topic", "Additional Info"))),
                CourseUnit("2", "Unit 2 Description", listOf(Day("2.1", "Learning Objective", "Some Topic", "Additional Info"))),
            ),
            onUnitDayClick = { }
        )
    }
}