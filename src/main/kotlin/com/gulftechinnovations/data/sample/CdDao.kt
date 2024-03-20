package com.gulftechinnovations.data.sample

import com.gulftechinnovations.model.test_samples.Cd

interface CdDao {
    suspend fun insertCd(cd:Cd)
    suspend fun getAllCd():List<Cd>
    suspend fun updateACd(cd: Cd)
    suspend fun deleteACd(id:Int)
}