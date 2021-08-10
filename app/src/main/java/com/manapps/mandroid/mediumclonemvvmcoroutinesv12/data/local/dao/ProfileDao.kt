package com.manapps.mandroid.mediumclonemvvmcoroutinesv12.data.local.dao

import androidx.room.*
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.data.models.entities.Profile

@Dao
interface ProfileDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfiles(profileList: List<Profile>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: Profile)

    @Query("select * from Profile")
    fun getProfiles(): List<Profile>

    @Query("DELETE FROM Profile")
    suspend fun deleteAllProfiles()

     /*@Transaction
     @Query("Select * From Profile where user_id=:id")
     fun getById(id:Int) : List<ProfileData>*/
}