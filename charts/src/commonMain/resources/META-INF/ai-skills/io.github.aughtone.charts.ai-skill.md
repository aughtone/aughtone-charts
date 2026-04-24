---
skill-id: io.github.aughtone.charts
name: "[Aughtone Charts](https://github.com/aughtone/aughtone-charts)"
type: "Aughtone AI-Skill"
scope: ui
compatibility: ">=1.0.0"
author: "[Brill Pappin](https://github.com/bpappin)"
---

# AI Skill: Aughtone Charts

This library provides a collection of chart components for Kotlin Multiplatform using Compose. It includes both high-level "Simple" charts for quick visualizations and feature-rich "Advanced" charts with legends and overlays.

## 🧰 The AI Toolbox (Key Components)

### **Simple Charts**
Designed for minimal configuration and quick display of numeric series.
- `SimpleBarChart(dataPoints: List<Float>, ...)`
    - **Usage**: Use for simple frequency or magnitude distributions.
- `SimpleLineChart(dataPoints: List<Float>, ...)`
    - **Usage**: Use for simple trends where X is implicit index.
- `ArcProgressBar(percentage: Float, ...)`
    - **Usage**: Use for goal tracking or status indicators.

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

## 🎨 Design & Theming

- **Material 3**: All charts integrate with `MaterialTheme`. Colors should ideally be pulled from `MaterialTheme.colorScheme` (e.g., `primary`, `secondary`, `tertiary`).
- **Responsiveness**: Charts use `BoxWithConstraints` or standard Compose layout modifiers to adapt to container sizes.

## 🤖 Agent Onboarding
1. **Context Registration**: Add this skill file to the `AGENTS.md` of the consuming project.
2. **README Verification**: Ensure the root `README.md` contains the "AI-Assisted Development" section.
3. **Usage Rules**:
    - **Simple vs. Advanced**: Use `Simple*` charts for internal dashboards or quick status views. Use the advanced versions (with legends) for user-facing analytical reports.
    - **Color Schemes**: Always use theme-aware colors. Avoid hardcoding HEX values.
    - **Performance**: Use `Immutable` data models where possible to ensure smooth recompositions.
    - **Data Mapping**: When using `aughtone-types` with charts, convert `Money` (cents) or `Distance` (meters) to `Float` values before passing them to the data models.
