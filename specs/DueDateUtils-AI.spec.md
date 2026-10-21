# Spec: `DueDateUtils.kt`

> Post-conditions are the test oracle: write the expected behavior from intent, not from the code.
> Tag each post-condition [Unit] if its test runs on the JVM; otherwise tag it [Integration].
> End each valid case with `Tested by <test>` if an existing test checks it, or `Tip:` describing the test to write.

## `dueDateLabel(dueDate: LocalDate?, today: LocalDate, isCompleted: Boolean): String`

**Intent**: Show a D-day label for an active task and CLEAR for a completed task.

**Pre-conditions**
- `today` is a calendar date; `dueDate` may be null.

**Post-conditions**
- Success cases
  - A completed task due on 2026-10-25 returns `CLEAR`. `[Unit]`
    - Tested by `DueDateUtilsTest.dueDateLabel_completedWithDueDate_returnsClear`
  - An active task with no due date returns the empty string. `[Unit]`
    - Tested by `DueDateUtilsTest.dueDateLabel_activeWithoutDueDate_returnsEmpty`
  - An active task due on 2026-10-21 when today is 2026-10-21 returns `D-Day`. `[Unit]`
    - Tested by `DueDateUtilsTest.dueDateLabel_dueToday_returnsDDay`
  - An active task due on 2026-10-24 when today is 2026-10-21 returns `D-3`. `[Unit]`
    - Tested by `DueDateUtilsTest.dueDateLabel_dueInThreeDays_returnsDMinusThree`
  - An active task due on 2026-10-18 when today is 2026-10-21 returns `D+3`. `[Unit]`
    - Tested by `DueDateUtilsTest.dueDateLabel_overdueThreeDays_returnsDPlusThree`
- Error cases
  - None; all inputs within the declared types have defined output.
- Edge cases
  - One day before today, 2026-10-20 versus 2026-10-21, returns `D+1`. `[Unit]`
    - Tested by `DueDateUtilsTest.dueDateLabel_oneDayOverdue_returnsDPlusOne`
  - One day after today, 2026-10-22 versus 2026-10-21, returns `D-1`. `[Unit]`
    - Tested by `DueDateUtilsTest.dueDateLabel_oneDayAhead_returnsDMinusOne`

**Undefined behavior** (optional)
- None.
