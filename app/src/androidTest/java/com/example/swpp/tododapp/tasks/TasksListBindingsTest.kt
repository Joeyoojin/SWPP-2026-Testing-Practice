package com.example.swpp.tododapp.tasks

import android.view.View
import android.widget.TextView
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import androidx.test.platform.app.InstrumentationRegistry
import com.example.swpp.tododapp.data.Task
import java.time.LocalDate
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@MediumTest
@RunWith(AndroidJUnit4::class)
class TasksListBindingsTest {
    private val instrumentation = InstrumentationRegistry.getInstrumentation()
    private lateinit var labelView: TextView

    @Before
    fun createLabelView() {
        instrumentation.runOnMainSync {
            labelView = TextView(instrumentation.targetContext)
        }
    }

    private fun bind(task: Task?) {
        instrumentation.runOnMainSync { setDueDateLabel(labelView, task) }
    }

    private fun assertLabel(text: String, visibility: Int) {
        assertEquals(text, labelView.text.toString())
        assertEquals(visibility, labelView.visibility)
    }

    @Test
    fun setDueDateLabel_completedWithoutDueDate_showsClear() {
        // Given a completed task without a due date.
        val task = Task(id = "completed-no-date", isCompleted = true)
        // When it is bound to a row.
        bind(task)
        // Then completion is visible even without a due date.
        assertLabel("CLEAR", View.VISIBLE)
    }

    @Test
    fun setDueDateLabel_completedWithDueDate_showsClear() {
        // Given a completed task with a due date.
        val task = Task(id = "completed-with-date", isCompleted = true,
            dueDate = LocalDate.parse("2026-10-25"))
        // When it is bound to a row.
        bind(task)
        // Then completion takes precedence over the date.
        assertLabel("CLEAR", View.VISIBLE)
    }

    @Test
    fun setDueDateLabel_dueToday_showsDDay() {
        // Given an active task due on the device's current calendar date.
        // When it is bound to a row, use the same date as the adapter's clock.
        instrumentation.runOnMainSync {
            val task = Task(id = "due-today", dueDate = LocalDate.now())
            setDueDateLabel(labelView, task)
        }
        // Then today's label is visible.
        assertLabel("D-Day", View.VISIBLE)
    }

    @Test
    fun setDueDateLabel_activeWithoutDueDate_hidesLabel() {
        // Given an active task without a due date and a newly visible TextView.
        val task = Task(id = "no-date")
        // When it is bound to a row.
        bind(task)
        // Then the empty label is hidden.
        assertLabel("", View.GONE)
    }

    @Test
    fun setDueDateLabel_completedThenActiveWithoutDueDate_clearsStaleLabel() {
        // Given a completed task with no due date and its displayed CLEAR label.
        val completed = Task(id = "no-date", isCompleted = true)
        bind(completed)
        assertLabel("CLEAR", View.VISIBLE)
        // When the same task becomes active and the row is rebound.
        bind(completed.copy(isCompleted = false))
        // Then the old completion label is removed.
        assertLabel("", View.GONE)
    }

    @Test
    fun setDueDateLabel_labelThenNullTask_hidesLabel() {
        // Given a row displaying CLEAR for a completed task.
        bind(Task(id = "completed-no-date", isCompleted = true))
        assertLabel("CLEAR", View.VISIBLE)
        // When no task is bound to the reused row.
        bind(null)
        // Then no stale label remains.
        assertLabel("", View.GONE)
    }

    @Test
    fun setDueDateLabel_datedThenUndated_clearsStaleLabel() {
        // Given a row displaying D-Day for an active task due today.
        instrumentation.runOnMainSync {
            setDueDateLabel(labelView, Task(id = "due-today", dueDate = LocalDate.now()))
        }
        assertLabel("D-Day", View.VISIBLE)
        // When an active task without a due date is bound to the same row.
        bind(Task(id = "no-date"))
        // Then the previous date label is removed.
        assertLabel("", View.GONE)
    }
}
