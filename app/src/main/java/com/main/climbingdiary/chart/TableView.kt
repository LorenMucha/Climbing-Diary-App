package com.main.climbingdiary.chart

import android.content.Context
import android.graphics.Rect
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.main.climbingdiary.R
import com.main.climbingdiary.database.TaskRepository
import com.main.climbingdiary.models.Colors.getGradeColor
import java.util.Locale

class TableView(val context: Context, val view: View) {

    private val tableScrollView: ScrollView = view.findViewById(R.id.table_scroll_view)
    private val tableContentContainer: LinearLayout = view.findViewById(R.id.table_content_container)
    private val summaryRow: LinearLayout = view.findViewById(R.id.table_summary_row)
    private val groupSummaryList: LinearLayout = view.findViewById(R.id.table_group_summary_list)
    private val cardList: LinearLayout = view.findViewById(R.id.table_card_list)
    private val emptyState: TextView = view.findViewById(R.id.table_empty_state)
    private val detailSectionViews = linkedMapOf<String, View>()

    private data class GradeStat(
        val level: String,
        val os: Int,
        val rp: Int,
        val flash: Int,
        val total: Int
    )

    private data class GradeGroupStat(
        val groupKey: String,
        val total: Int,
        val os: Int,
        val rp: Int,
        val flash: Int
    )

    fun show() {
        this.tableScrollView.visibility = View.VISIBLE
    }

    fun hide() {
        this.tableScrollView.visibility = View.GONE
    }

    fun createTableView() {
        summaryRow.removeAllViews()
        groupSummaryList.removeAllViews()
        cardList.removeAllViews()
        detailSectionViews.clear()

        val rows = mutableListOf<GradeStat>()
        TaskRepository.getTableValues().use { tableCursor ->
            while (!tableCursor.isAfterLast) {
                rows.add(
                    GradeStat(
                        level = tableCursor.getString(0),
                        os = tableCursor.getString(1)?.toIntOrNull() ?: 0,
                        rp = tableCursor.getString(2)?.toIntOrNull() ?: 0,
                        flash = tableCursor.getString(3)?.toIntOrNull() ?: 0,
                        total = tableCursor.getString(4)?.toIntOrNull() ?: 0
                    )
                )
                tableCursor.moveToNext()
            }
        }

        if (rows.isEmpty()) {
            emptyState.visibility = View.VISIBLE
            return
        }

        emptyState.visibility = View.GONE
        createSummaryCards(rows)
        val groupedRows = rows
            .groupBy { extractGradeGroup(it.level) }
            .toList()
            .sortedByDescending { extractGroupNumber(it.first) }

        createGroupedSummary(groupedRows)
        createGroupedDetailSections(groupedRows)
    }

    private fun createSummaryCards(rows: List<GradeStat>) {
        val totalAscents = rows.sumOf { it.total }
        val hardestGrade = rows.firstOrNull()?.level ?: "-"
        val trackedGrades = rows.size

        summaryRow.addView(
            createSummaryCard(
                label = context.getString(R.string.stats_table_summary_total),
                value = totalAscents.toString()
            )
        )
        summaryRow.addView(
            createSummaryCard(
                label = context.getString(R.string.stats_table_summary_hardest),
                value = hardestGrade
            )
        )
        summaryRow.addView(
            createSummaryCard(
                label = context.getString(R.string.stats_table_summary_levels),
                value = trackedGrades.toString()
            )
        )
    }

    private fun createGroupedSummary(groupedRows: List<Pair<String, List<GradeStat>>>) {
        groupedRows
            .map { (groupKey, stats) ->
                GradeGroupStat(
                    groupKey = groupKey,
                    total = stats.sumOf { it.total },
                    os = stats.sumOf { it.os },
                    rp = stats.sumOf { it.rp },
                    flash = stats.sumOf { it.flash }
                )
            }
            .forEach { groupSummaryList.addView(createGroupSummaryCard(it)) }
    }

    private fun createGroupedDetailSections(groupedRows: List<Pair<String, List<GradeStat>>>) {
        groupedRows.forEach { (groupKey, stats) ->
            val section = createGroupDetailSection(groupKey, stats)
            detailSectionViews[groupKey] = section
            cardList.addView(section)
        }
    }

    private fun createSummaryCard(label: String, value: String): View {
        val container = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            background = ContextCompat.getDrawable(context, R.drawable.bg_action_surface)
            setPadding(dp(14), dp(14), dp(14), dp(14))
            layoutParams = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f).apply {
                marginEnd = dp(8)
            }
        }

        container.addView(
            TextView(context).apply {
                text = label
                setTextColor(ContextCompat.getColor(context, R.color.coveredFontColor))
                textSize = 12f
            }
        )
        container.addView(
            TextView(context).apply {
                text = value
                setTextColor(ContextCompat.getColor(context, R.color.black))
                textSize = 24f
                setTypeface(typeface, Typeface.BOLD)
            }
        )

        return container
    }

    private fun createGradeCard(stat: GradeStat): View {
        val container = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            background = ContextCompat.getDrawable(context, R.drawable.bg_action_surface)
            setPadding(dp(18), dp(18), dp(18), dp(18))
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                bottomMargin = dp(12)
            }
        }

        val headerRow = LinearLayout(context).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }

        headerRow.addView(createGradePill(stat.level))
        headerRow.addView(
            TextView(context).apply {
                text = context.getString(R.string.stats_table_total_inline, stat.total)
                setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryDark))
                setTypeface(typeface, Typeface.BOLD)
                textSize = 14f
                gravity = android.view.Gravity.END
                layoutParams = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
            }
        )
        container.addView(headerRow)

        val metricsGrid = GridLayout(context).apply {
            columnCount = 2
            rowCount = 2
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin = dp(16)
            }
        }

        metricsGrid.addView(createMetricCard("OS", stat.os))
        metricsGrid.addView(createMetricCard("RP", stat.rp))
        metricsGrid.addView(createMetricCard("FLASH", stat.flash))
        metricsGrid.addView(
            createMetricCard(
                context.getString(R.string.table_gesamt_header),
                stat.total
            )
        )

        container.addView(metricsGrid)
        return container
    }

    private fun createGroupDetailSection(groupKey: String, stats: List<GradeStat>): View {
        val sectionContainer = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            background = ContextCompat.getDrawable(context, R.drawable.bg_dialog_section)
            setPadding(dp(18), dp(18), dp(18), dp(18))
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                bottomMargin = dp(14)
            }
        }

        val total = stats.sumOf { it.total }
        val headerRow = LinearLayout(context).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }

        headerRow.addView(
            TextView(context).apply {
                text = context.getString(R.string.stats_table_group_title, groupKey)
                setTextColor(ContextCompat.getColor(context, R.color.black))
                textSize = 17f
                setTypeface(typeface, Typeface.BOLD)
                layoutParams = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
            }
        )
        headerRow.addView(
            TextView(context).apply {
                text = context.getString(R.string.stats_table_total_inline, total)
                setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryDark))
                textSize = 14f
                setTypeface(typeface, Typeface.BOLD)
            }
        )
        sectionContainer.addView(headerRow)

        stats.sortedByDescending { it.level }.forEach { stat ->
            sectionContainer.addView(createGradeCard(stat))
        }

        return sectionContainer
    }

    private fun createGroupSummaryCard(stat: GradeGroupStat): View {
        val container = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            background = ContextCompat.getDrawable(context, R.drawable.bg_action_surface)
            setPadding(dp(16), dp(16), dp(16), dp(16))
            isClickable = true
            isFocusable = true
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                bottomMargin = dp(10)
            }
            setOnClickListener {
                scrollToDetailSection(stat.groupKey)
            }
        }

        val headerRow = LinearLayout(context).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }

        headerRow.addView(
            TextView(context).apply {
                text = context.getString(R.string.stats_table_group_title, stat.groupKey)
                setTextColor(ContextCompat.getColor(context, R.color.black))
                textSize = 16f
                setTypeface(typeface, Typeface.BOLD)
                layoutParams = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
            }
        )

        headerRow.addView(
            TextView(context).apply {
                text = context.getString(R.string.stats_table_total_inline, stat.total)
                setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryDark))
                textSize = 14f
                setTypeface(typeface, Typeface.BOLD)
            }
        )

        container.addView(headerRow)
        val metricsRow = LinearLayout(context).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin = dp(14)
            }
        }
        metricsRow.addView(createInlineMetric("OS", stat.os))
        metricsRow.addView(createInlineMetric("RP", stat.rp))
        metricsRow.addView(createInlineMetric("FLASH", stat.flash))
        container.addView(metricsRow)

        return container
    }

    private fun createMetricCard(label: String, value: Int): View {
        val card = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            background = ContextCompat.getDrawable(context, R.drawable.bg_action_surface)
            setPadding(dp(14), dp(14), dp(14), dp(14))
            layoutParams = GridLayout.LayoutParams().apply {
                width = 0
                columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                setMargins(0, 0, dp(10), dp(10))
            }
        }

        card.addView(
            TextView(context).apply {
                text = label
                setTextColor(ContextCompat.getColor(context, R.color.coveredFontColor))
                textSize = 12f
            }
        )
        card.addView(
            TextView(context).apply {
                text = value.toString()
                setTextColor(ContextCompat.getColor(context, R.color.black))
                textSize = 22f
                setTypeface(typeface, Typeface.BOLD)
            }
        )

        return card
    }

    private fun createInlineMetric(label: String, value: Int): View {
        return LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            background = ContextCompat.getDrawable(context, R.drawable.bg_dialog_section)
            setPadding(dp(12), dp(10), dp(12), dp(10))
            layoutParams = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f).apply {
                marginEnd = dp(8)
            }
            addView(
                TextView(context).apply {
                    text = label
                    setTextColor(ContextCompat.getColor(context, R.color.coveredFontColor))
                    textSize = 11f
                }
            )
            addView(
                TextView(context).apply {
                    text = value.toString()
                    setTextColor(ContextCompat.getColor(context, R.color.black))
                    textSize = 18f
                    setTypeface(typeface, Typeface.BOLD)
                }
            )
        }
    }

    private fun createGradePill(level: String): View {
        return TextView(context).apply {
            text = level
            setTextColor(ContextCompat.getColor(context, R.color.white))
            textSize = 16f
            setTypeface(typeface, Typeface.BOLD)
            setPadding(dp(16), dp(10), dp(16), dp(10))
            background = GradientDrawable().apply {
                shape = GradientDrawable.RECTANGLE
                cornerRadius = dp(18).toFloat()
                setColor(getGradeColor(level))
            }
        }
    }

    private fun dp(value: Int): Int {
        return (value * context.resources.displayMetrics.density).toInt()
    }

    private fun extractGradeGroup(level: String): String {
        val match = Regex("""^\d+""").find(level.trim())
        return match?.value ?: level.trim().uppercase(Locale.ROOT)
    }

    private fun extractGroupNumber(groupKey: String): Int {
        return groupKey.toIntOrNull() ?: Int.MIN_VALUE
    }

    private fun scrollToDetailSection(groupKey: String) {
        val targetView = detailSectionViews[groupKey] ?: return
        tableScrollView.post {
            val targetRect = Rect()
            targetView.getDrawingRect(targetRect)
            tableContentContainer.offsetDescendantRectToMyCoords(targetView, targetRect)
            tableScrollView.smoothScrollTo(0, (targetRect.top - dp(12)).coerceAtLeast(0))
        }
    }
}
