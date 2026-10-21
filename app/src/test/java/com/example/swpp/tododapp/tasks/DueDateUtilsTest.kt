package com.example.swpp.tododapp.tasks

import java.time.LocalDate
import org.junit.Assert.assertEquals
import org.junit.Test

class DueDateUtilsTest {
    private fun date(value: String) = LocalDate.parse(value)

    @Test
    fun dueDateLabel_completedWithDueDate_returnsClear() {
        // Given a completed task with a future due date.
        // When its label is calculated.
        val label = dueDateLabel(date("2026-10-25"), date("2026-10-21"), true)
        // Then completion takes precedence over the date.
        assertEquals("CLEAR", label)
    }

    @Test
    fun dueDateLabel_completedWithoutDueDate_returnsClear() {
        // Given a completed task without a due date.
        // When its label is calculated.
        val label = dueDateLabel(null, date("2026-10-21"), true)
        // Then completion still has a visible label.
        assertEquals("CLEAR", label)
    }

    @Test
    fun dueDateLabel_activeWithoutDueDate_returnsEmpty() {
        // Given an active task without a due date.
        // When its label is calculated.
        val label = dueDateLabel(null, date("2026-10-21"), false)
        // Then no label is needed.
        assertEquals("", label)
    }

    @Test
    fun dueDateLabel_dueInThreeDays_returnsDMinusThree() {
        // Given an active task due three calendar days after today.
        // When its label is calculated.
        val label = dueDateLabel(date("2026-10-24"), date("2026-10-21"), false)
        // Then the remaining days are shown.
        assertEquals("D-3", label)
    }

    @Test
    fun dueDateLabel_overdueThreeDays_returnsDPlusThree() {
        // Given an active task due three calendar days before today.
        // When its label is calculated.
        val label = dueDateLabel(date("2026-10-18"), date("2026-10-21"), false)
        // Then the elapsed days are shown.
        assertEquals("D+3", label)
    }

    @Test
    fun dueDateLabel_acrossMonth_returnsDMinusThirty() {
        // Given January 31 and a due date 30 calendar days later.
        // When its label is calculated.
        val label = dueDateLabel(date("2026-03-02"), date("2026-01-31"), false)
        // Then the full distance, including February, is shown.
        assertEquals("D-30", label)
    }

    @Test
    fun dueDateLabel_overdueAcrossMonth_returnsDPlusThirty() {
        // Given March 2 and a due date 30 calendar days earlier.
        // When its label is calculated.
        val label = dueDateLabel(date("2026-01-31"), date("2026-03-02"), false)
        // Then the full overdue distance is shown.
        assertEquals("D+30", label)
    }

    @Test
    fun dueDateLabel_oneDayOverdue_returnsDPlusOne() {
        // Given the day immediately before today.
        // When its label is calculated.
        val label = dueDateLabel(date("2026-10-20"), date("2026-10-21"), false)
        // Then it is one day overdue.
        assertEquals("D+1", label)
    }

    @Test
    fun dueDateLabel_dueToday_returnsDDay() {
        // Given a task due today.
        // When its label is calculated.
        val label = dueDateLabel(date("2026-10-21"), date("2026-10-21"), false)
        // Then the exact due-day label is shown.
        assertEquals("D-Day", label)
    }

    @Test
    fun dueDateLabel_oneDayAhead_returnsDMinusOne() {
        // Given the day immediately after today.
        // When its label is calculated.
        val label = dueDateLabel(date("2026-10-22"), date("2026-10-21"), false)
        // Then it is one day away.
        assertEquals("D-1", label)
    }

    @Test
    fun dueDateLabel_acrossLeapDay_returnsDMinusTwo() {
        // Given February 28 in a leap year and March 1.
        // When its label is calculated.
        val label = dueDateLabel(date("2024-03-01"), date("2024-02-28"), false)
        // Then February 29 is included.
        assertEquals("D-2", label)
    }

    @Test
    fun dueDateLabel_acrossYearEnd_returnsDMinusOne() {
        // Given consecutive days across a year boundary.
        // When its label is calculated.
        val label = dueDateLabel(date("2026-01-01"), date("2025-12-31"), false)
        // Then the label reflects one calendar day.
        assertEquals("D-1", label)
    }
}
