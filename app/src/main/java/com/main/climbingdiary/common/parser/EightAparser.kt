package com.main.climbingdiary.common.parser

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import com.fasterxml.jackson.dataformat.csv.CsvMapper
import com.fasterxml.jackson.dataformat.csv.CsvParser
import com.fasterxml.jackson.dataformat.csv.CsvSchema
import com.main.climbingdiary.common.GradeConverter
import com.main.climbingdiary.database.entities.Route
import com.main.climbingdiary.database.entities.RouteRepository
import com.main.climbingdiary.models.SportType
import com.main.climbingdiary.models.Styles
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class EightAparser(
    private val context: Context) {

    fun selectCsvFile(launcher: ActivityResultLauncher<Intent>) {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "*/*"
        }
        launcher.launch(intent)
    }

    suspend fun parseCsv(uri: Uri, type: SportType) {
        val routeRepository: RouteRepository<Route> = RouteRepository(Route::class)

        withContext(Dispatchers.IO) {
            try {
                context.contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )

                context.contentResolver.openInputStream(uri)?.use { inputStream ->
                    val reader = InputStreamReader(inputStream)
                    val csvMapper = CsvMapper().apply {
                        enable(CsvParser.Feature.TRIM_SPACES)
                        enable(CsvParser.Feature.SKIP_EMPTY_LINES)
                    }
                    val schema = CsvSchema.emptySchema().withHeader()
                    val csvParser = csvMapper.readerFor(Map::class.java)
                        .with(schema.withSkipFirstDataRow(true))

                    val rows: List<Map<String, String>> =
                        csvParser.readValues<Map<String, String>>(reader).readAll()

                    for (row in rows) {
                        try {
                            if (eightAtoRouteType(row["route_boulder"] ?: "", row) == type) {
                                val route = Route(
                                    name = row["name"] ?: "",
                                    sector = row["sector_name"] ?: "",
                                    area = row["location_name"] ?: "",
                                    date = formatDate(row["date"] ?: ""),
                                    style = eightAtoStyle(row["type"] ?: ""),
                                    rating = row["rating"]?.toIntOrNull(),
                                    tries = row["tries"]?.toIntOrNull(),
                                    level = GradeConverter.convertAnyToFrench(row["difficulty"] ?: ""),
                                    soft = row["perceived_hardness"]?.toIntOrNull(),
                                    comment = row["comment"] ?: ""
                                )
                                routeRepository.insertRoute(route)
                            }
                        } catch (ex: Exception) {
                            Log.d("EightAparser", "Fehler beim Parsen einer Zeile", ex)
                        }
                    }
                }
                Log.d("EightAparser", "CSV-Datei wurde vollständig verarbeitet.")
            } catch (e: Exception) {
                Log.e("EightAparser", "Fehler beim Parsen der CSV-Datei", e)
            }
        }
    }

    private fun eightAtoStyle(eightA: String): String {
        return when (eightA.lowercase()) {
            "rp" -> Styles.getRP()
            "os" -> Styles.getOS()
            "f" -> Styles.getFLASH()
            else -> throw IllegalArgumentException("Ungültiger Stil: $eightA")
        }
    }

    private fun eightAtoRouteType(eightA: String, row: Map<String, String>): SportType {
        return when (eightA.trim().uppercase()) {
            "ROUTE" -> SportType.KLETTERN
            "BOULDER" -> SportType.BOULDERN
            else -> throw IllegalArgumentException("Ungültiger Typ: $eightA, $row")
        }
    }

    private fun formatDate(inputDate: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        inputFormat.timeZone = TimeZone.getTimeZone("UTC")
        val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        val date = inputFormat.parse(inputDate)
        return outputFormat.format(date!!)
    }
}
