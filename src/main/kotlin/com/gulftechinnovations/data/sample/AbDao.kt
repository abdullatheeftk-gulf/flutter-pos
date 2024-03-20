package com.gulftechinnovations.data.sample

import com.gulftechinnovations.model.test_samples.Ab

interface AbDao {
    suspend fun insertAb(ab:Ab)
    suspend fun getAllAb():List<Ab>
    suspend fun updateAb(ab:Ab)
    suspend fun deleteOneAb(id:Int)
}