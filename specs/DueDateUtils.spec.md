# Spec: `DueDateUtils.kt`

> Post-conditions are the test oracle: write the expected behavior from intent, not from the code.
> Tag each post-condition [Unit] if its test runs on the JVM; otherwise tag it [Integration].
> End each valid case with `Tested by <test>` if an existing test checks it, or `Tip:` describing the test to write.

## `dueDateLabel(dueDate: LocalDate?, today: LocalDate, isCompleted: Boolean): String`

**Intent**: For an active task, count whole calendar days from `today` to the optional due date and return the exact D-day label. A completed task always returns `CLEAR`, regardless of its due date.

**Pre-conditions**
- `today` is the date used as the reference; `dueDate` may be null. No clock or time zone is read by this function.

**Post-conditions**
- Success cases
  - `isCompleted=true`, `today=2026-10-21`, `dueDate=2026-10-25` returns `CLEAR`. `[Unit]`
    - Tested by `DueDateUtilsTest.dueDateLabel_completedWithDueDate_returnsClear`
  - `isCompleted=true`, `today=2026-10-21`, `dueDate=null` returns `CLEAR`. `[Unit]`
    - Tested by `DueDateUtilsTest.dueDateLabel_completedWithoutDueDate_returnsClear`
  - `isCompleted=false`, `today=2026-10-21`, `dueDate=null` returns `""`. `[Unit]`
    - Tested by `DueDateUtilsTest.dueDateLabel_activeWithoutDueDate_returnsEmpty`
  - `isCompleted=false`, `today=2026-10-21`, `dueDate=2026-10-24` returns `D-3`. `[Unit]`
    - Tested by `DueDateUtilsTest.dueDateLabel_dueInThreeDays_returnsDMinusThree`
  - `isCompleted=false`, `today=2026-10-21`, `dueDate=2026-10-18` returns `D+3`. `[Unit]`
    - Tested by `DueDateUtilsTest.dueDateLabel_overdueThreeDays_returnsDPlusThree`
  - `isCompleted=false`, `today=2026-01-31`, `dueDate=2026-03-02` returns `D-30`: the month component must count as days. `[Unit]`
    - Tested by `DueDateUtilsTest.dueDateLabel_acrossMonth_returnsDMinusThirty`
  - `isCompleted=false`, `today=2026-03-02`, `dueDate=2026-01-31` returns `D+30`. `[Unit]`
    - Tested by `DueDateUtilsTest.dueDateLabel_overdueAcrossMonth_returnsDPlusThirty`
- Error cases
  - None; null due date and both completion states are valid inputs.
- Edge cases
  - `isCompleted=false`, `today=2026-10-21`, `dueDate=2026-10-20` returns `D+1`, immediately before the today boundary. `[Unit]`
    - Tested by `DueDateUtilsTest.dueDateLabel_oneDayOverdue_returnsDPlusOne`
  - `isCompleted=false`, `today=2026-10-21`, `dueDate=2026-10-21` returns `D-Day`, at the boundary. `[Unit]`
    - Tested by `DueDateUtilsTest.dueDateLabel_dueToday_returnsDDay`
  - `isCompleted=false`, `today=2026-10-21`, `dueDate=2026-10-22` returns `D-1`, immediately after the today boundary. `[Unit]`
    - Tested by `DueDateUtilsTest.dueDateLabel_oneDayAhead_returnsDMinusOne`
  - `isCompleted=false`, `today=2024-02-28`, `dueDate=2024-03-01` returns `D-2` across leap day. `[Unit]`
    - Tested by `DueDateUtilsTest.dueDateLabel_acrossLeapDay_returnsDMinusTwo`
  - `isCompleted=false`, `today=2025-12-31`, `dueDate=2026-01-01` returns `D-1` across year end. `[Unit]`
    - Tested by `DueDateUtilsTest.dueDateLabel_acrossYearEnd_returnsDMinusOne`

**Undefined behavior** (optional)
- None.
