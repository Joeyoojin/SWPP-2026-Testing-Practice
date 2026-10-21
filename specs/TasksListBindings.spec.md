# Spec: `TasksListBindings.kt`

> Post-conditions are the test oracle: write the expected behavior from intent, not from the code.
> Tag each post-condition [Unit] if its test runs on the JVM; otherwise tag it [Integration].
> End each valid case with `Tested by <test>` if an existing test checks it, or `Tip:` describing the test to write.

## `setDueDateLabel(textView: TextView, task: Task?): Unit`

**Intent**: Apply the task's current label to a reusable list-row TextView. A nonempty label is visible; an empty label clears old text and hides the view, including after task state changes.

**Pre-conditions**
- `textView` is a live Android TextView; the call runs on the main thread. `task` may be null.
- For dated active tasks, the reference date is the device's `LocalDate.now()` on the day of the call.

**Post-conditions**
- Success cases
  - A completed task `id=completed-no-date`, `dueDate=null` displays `CLEAR` and is `VISIBLE`. `[Integration]`
    - Tested by `TasksListBindingsTest.setDueDateLabel_completedWithoutDueDate_showsClear`
  - A completed task `id=completed-with-date`, `dueDate=2026-10-25` displays `CLEAR` and is `VISIBLE`, regardless of the device date. `[Integration]`
    - Tested by `TasksListBindingsTest.setDueDateLabel_completedWithDueDate_showsClear`
  - An active task `id=due-today`, `dueDate=LocalDate.now()` displays `D-Day` and is `VISIBLE`. `[Integration]`
    - Tested by `TasksListBindingsTest.setDueDateLabel_dueToday_showsDDay`
  - An active task `id=no-date`, `dueDate=null` displays `""` and is `GONE`. `[Integration]`
    - Tested by `TasksListBindingsTest.setDueDateLabel_activeWithoutDueDate_hidesLabel`
- Error cases
  - None; `task=null` is valid and represents a row with no current task.
- Edge cases
  - With `textView` initially displaying `CLEAR` from completed task `id=no-date`, rebinding the same task as active and still `dueDate=null` changes text to `""` and visibility to `GONE`. `[Integration]`
    - Tested by `TasksListBindingsTest.setDueDateLabel_completedThenActiveWithoutDueDate_clearsStaleLabel`
  - With `textView` initially displaying `CLEAR` from completed task `id=completed-no-date`, rebinding `task=null` changes text to `""` and visibility to `GONE`. `[Integration]`
    - Tested by `TasksListBindingsTest.setDueDateLabel_labelThenNullTask_hidesLabel`
  - With `textView` initially displaying `D-Day` from active task `id=due-today`, rebinding active task `id=no-date`, `dueDate=null` changes text to `""` and visibility to `GONE`. `[Integration]`
    - Tested by `TasksListBindingsTest.setDueDateLabel_datedThenUndated_clearsStaleLabel`

**Undefined behavior** (optional)
- None.
