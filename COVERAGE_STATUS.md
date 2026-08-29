# Code Coverage Status

Last measured: 2026-07-14 (JaCoCo 0.8.12, JDK 21, JVM test harness)

## Current Status

**94 unit tests, 0 failures.**

| Class | Instructions | Branches | Lines | Methods |
|---|---|---|---|---|
| `Calculator.kt` | 98.7% (513/520) | 87.7% (57/65) | **100%** (96/96) | 100% (21/21) |
| `ThemeManager.kt` | 97.0% (65/67) | 83.3% (5/6) | **100%** (17/17) | 100% (6/6) |
| `MainActivity.kt` | n/a in JVM harness | – | – | – |

`MainActivity.kt` is exercised by `MainActivityTest.kt` (Robolectric), which
requires the Android SDK and runs in CI via `./gradlew test`. The remaining
missed branches in `Calculator.kt` are composite boolean sub-conditions
(e.g. the `"," / "."` duplicate-separator checks), not untested behavior.

### Test suites
- CalculatorTest: 39 tests (business logic, error states, formatting, input limit)
- MainActivityUnitTest: 11 tests
- ThemeManagerTest: 11 tests (exercises the real `ThemeManager` via mocked `SharedPreferences`)
- ErrorHandlingTest: 9 tests
- InputValidationTest: 10 tests
- NameStorageTest: 7 tests
- StringResourcesTest: 7 tests
- MainActivityTest: 12 Robolectric tests (Android SDK required)

## How to measure

```bash
./calculate_coverage.sh
# or directly:
./gradlew testDebugUnitTest jacocoTestReport
# report: app/build/reports/jacoco/jacocoTestReport/html/index.html
```

JaCoCo is enabled in `app/build.gradle` (toolVersion 0.8.12). The previously
documented "Java 24 incompatibility" no longer applies on JDK 17–21 builds.

## History

- Earlier versions of this file estimated coverage by counting test methods
  with `grep`; the numbers above are measured by JaCoCo instrumentation.
- Before the ThemeManagerTest rewrite, `ThemeManager.kt` had **0%** measured
  coverage: the old tests re-implemented the toggle logic locally instead of
  calling the production class.
