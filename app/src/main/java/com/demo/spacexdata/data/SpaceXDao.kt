package com.demo.spacexdata.data

import androidx.room.*
import com.demo.spacexdata.data.model.Launch
import kotlinx.coroutines.flow.Flow

@Dao
interface SpaceXDao {

    @Query("SELECT * FROM space_x_launches")
    fun getAllSpaceXLaunches(): Flow<List<Launch>>

    @Transaction
    suspend fun replaceAllLaunches(launches: List<Launch>) {
        deleteAllSpaceXLaunches()
        insertSpaceXLaunches(launches)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSpaceXLaunches(launches: List<Launch>)

    @Query("DELETE FROM space_x_launches")
    suspend fun deleteAllSpaceXLaunches()
}