#!/bin/bash
# Runs the unit tests and prints real JaCoCo coverage per class.
# Requires a JDK (17+) and the Android SDK (for the Gradle build).
set -e

echo "🧮 Running unit tests with JaCoCo coverage..."
./gradlew testDebugUnitTest jacocoTestReport

CSV="app/build/reports/jacoco/jacocoTestReport/jacocoTestReport.csv"
if [ ! -f "$CSV" ]; then
    echo "❌ Coverage CSV not found at $CSV"
    exit 1
fi

echo ""
echo "📊 Coverage by class (instructions / branches / lines):"
awk -F',' 'NR > 1 {
    instr_total = $4 + $5; branch_total = $6 + $7; line_total = $8 + $9
    instr_pct  = instr_total  ? 100 * $5 / instr_total  : 100
    branch_pct = branch_total ? 100 * $7 / branch_total : 100
    line_pct   = line_total   ? 100 * $9 / line_total   : 100
    printf "  %-40s %5.1f%% / %5.1f%% / %5.1f%%\n", $3, instr_pct, branch_pct, line_pct
    covered += $5; missed += $4
}
END {
    total = covered + missed
    if (total) printf "\n📈 Overall instruction coverage: %.1f%%\n", 100 * covered / total
}' "$CSV"

echo ""
echo "📄 Full HTML report: app/build/reports/jacoco/jacocoTestReport/html/index.html"
