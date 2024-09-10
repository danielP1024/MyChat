package com.danielpasser.mychat.room

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.danielpasser.mychat.models.room.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Transaction
    @Query("SELECT * FROM user")
    fun getUsers(): Flow<List<UserEntity>>

    @Query("DELETE FROM user")
    fun deleteAll()

    @Upsert
    suspend fun insert(user: UserEntity)
}