# Spec: `TasksListBindings.kt`

> Post-conditions are the test oracle: write the expected behavior from intent, not from the code.
> Tag each post-condition [Unit] if its test runs on the JVM; otherwise tag it [Integration].
> End each valid case with `Tested by <test>` if an existing test checks it, or `Tip:` describing the test to write.

## `setDueDateLabel(textView: TextView, task: Task?): Unit`

**Intent**: Show the task's D-day label in its list row.

**Pre-conditions**
- `textView` is a valid row label; `task` may be null.

**Post-conditions**
- Success cases
  - A completed task without a due date shows `CLEAR` in a visible label. `[Integration]`
    - Tested by `TasksListBindingsTest.setDueDateLabel_completedWithoutDueDate_showsClear`
  - An active task due on the device's current calendar date shows `D-Day` in a visible label. `[Integration]`
    - Tested by `TasksListBindingsTest.setDueDateLabel_dueToday_showsDDay`
  - An active task without a due date shows no label. `[Integration]`
    - Tested by `TasksListBindingsTest.setDueDateLabel_activeWithoutDueDate_hidesLabel`
- Error cases
  - None; null is accepted.
- Edge cases
  - A null task shows no label. `[Integration]`
    - Tested by `TasksListBindingsTest.setDueDateLabel_labelThenNullTask_hidesLabel`

**Undefined behavior** (optional)
- None.
