package com.speter97.weatherforecast

import androidx.lifecycle.LiveData
import androidx.room.*

@Entity(tableName = "locationString_table")
data class LocationString(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    var latlng: String
)

@Dao
interface LocationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun setLocation(latlng: LocationString)

    @Query("SELECT * FROM locationString_table ORDER BY id DESC LIMIT 1")
    fun getLocation(): LiveData<LocationString>

    @Query("DELETE FROM locationString_table")
    suspend fun deleteAll()
}