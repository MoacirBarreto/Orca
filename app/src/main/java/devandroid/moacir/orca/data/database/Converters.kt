// /app/src/main/java/devandroid/moacir/orca/data/database/Converters.kt

package devandroid.moacir.orca.data.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import devandroid.moacir.orca.data.model.Total
import com.google.gson.reflect.TypeToken

class Converters {
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE
    private val gson = Gson()
    @TypeConverter
    fun fromTimestamp(value: Long?): LocalDate? {
        // Converte de Long (timestamp) para LocalDate
        return value?.let { LocalDate.ofEpochDay(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun bigDecimalToString(bigDecimal: java.math.BigDecimal?): String? {
        return bigDecimal?.toPlainString()
    }
    @TypeConverter
    fun fromLocalDate(value: LocalDate?): String? {
        return value?.format(formatter)
    }

    @TypeConverter
    fun toLocalDate(value: String?): LocalDate? {
        return value?.let {
            return LocalDate.parse(it, formatter)
        }
    }
    @TypeConverter
    fun fromTotalList(totals: List<Total>?): String? {
        if (totals == null) {
            return null
        }
        return gson.toJson(totals)
    }

    @TypeConverter
    fun toTotalList(totalsString: String?): List<Total>? {
        if (totalsString == null) {
            return null
        }
        val listType = object : TypeToken<List<Total>>() {}.type
        return gson.fromJson(totalsString, listType)
    }
}
