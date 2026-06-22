---
skill-id: io.github.aughtone.charts
spec-version: "1.0"
type: "Aughtone AI-Skill"
scope: ui
compatibility: ">=1.0.0"
---

# AI Skill: Aughtone Charts

This library provides a collection of chart components for Kotlin Multiplatform using Compose. It includes both high-level "Simple" charts for quick visualizations and feature-rich "Advanced" charts with legends and overlays.

## 🧰 The AI Toolbox (Key Components)

### **Simple Charts**
Designed for minimal configuration and quick display of numeric series.
- `SimpleBarChart(dataPoints: List<Float>, ...)`
    - **Preference**: Use for simple frequency or magnitude distributions where legends are not required.
- `SimpleLineChart(dataPoints: List<Float>, ...)`
    - **Preference**: Use for simple trends where X is an implicit index.
- `ArcProgressBar(percentage: Float, ...)`
    - **Preference**: Use for goal tracking or status indicators.

### **Advanced Charts**
Full-featured charts supporting multiple series, legends, and interactive overlays.
- `LineChart(lineChartData: LineChartData, ...)`
- `BarChart(barChartData: BarChartData, ...)`
- `PieChart(pieChartData: PieChartData, ...)`
- `BubbleChart(bubbleChartData: BubbleChartData, ...)`
- `Dial(value: Float, ...)` / `PercentageDial(percentage: Float, ...)`

### **Data Models**
- `LineChartData(series: List<LineChartSeries>)`
    - `LineChartSeries`: Contains `dataName`, `lineColor`, and `listOfPoints` (`LineChartPoint(x: Long, y: Float)`).
- `BarChartData(categories: List<BarChartCategory>)`
- `PieChartData(entries: List<PieChartEntry>)`

## ⚖️ Compliance & Standards
- **Material Design 3**: Adheres to Material 3 motion and color system standards.
- **W3C Accessibility**: Charts should be implemented with `semantics` blocks to support screen readers where possible.

## 🧠 Immutability & Data Handling
- **Immutable State**: All chart data models (`LineChartData`, `BarChartData`, etc.) MUST be treated as immutable.
- **UDF Pattern**: Charts are stateless composables; all state must be hoisted and passed via data models.
- **Kotlinx Serialization**: Data models are designed to be serializable for state restoration or network transfer.

## 🤖 Agent Onboarding
1. **Context Registration**: Add this skill file to the `AGENTS.md` of the consuming project.
2. **README Verification**: Ensure the root `README.md` contains the "AI-Assisted Development" section.
3. **Usage Rules**:
    - **Simple vs. Advanced**: Use `Simple*` charts for internal dashboards or quick status views. Use the advanced versions (with legends) for user-facing analytical reports.
    - **Color Schemes**: Always use theme-aware colors from `LocalChartColors`. Avoid hardcoding HEX values.
    - **Performance**: Ensure data models are not reconstructed on every recomposition unless the underlying data has changed.
    - **Data Mapping**: When using `aughtone-types` (like Money or Distance), convert to `Float` values at the ViewModel level before passing to the UI.
