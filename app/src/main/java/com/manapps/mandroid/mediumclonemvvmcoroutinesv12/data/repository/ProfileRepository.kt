package com.manapps.mandroid.mediumclonemvckotlin.data.repository

import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.data.api.ApiService
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.data.local.dao.ProfileDao
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.data.models.entities.Profile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileRepository(private val dao: ProfileDao) {
    fun insertProfiles(profileList: List<Profile>) {
        CoroutineScope(Dispatchers.Default).launch {
            dao.insertProfiles(profileList)
        }
    }

    fun getProfiles() : List<Profile> {
        return dao.getProfiles()
    }

    fun deleteAllProfile() {
        CoroutineScope(Dispatchers.Default).launch {  dao.deleteAllProfiles() }
    }
}