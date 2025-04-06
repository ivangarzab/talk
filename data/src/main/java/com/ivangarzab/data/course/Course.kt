package com.ivangarzab.data.course

/**
 * The purpose of this file is to hold all of the data classes that are required
 * for the course directory inside the :data module.
 */
data class Course(
    val id: String,
    val info: Info,
    val units: List<Unit>
)

data class Info(
    val title: String,
    val thumbnailImageUrl: String,
    val backgroundImageUrl: String,
    val subtitle: String
)

data class Unit(
    val id: String,
    val title: String,
    val days: List<Day>
)

data class Day(
    val id: String,
    val title: String,
    val thumbnailImageUrl: String,
    val subtitle: String
)