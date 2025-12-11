package com.devmasterteam.mybooks.repository

import androidx.room.*
import com.devmasterteam.mybooks.entity.UserEntity

@Dao
interface UserDAO {
    
    @Insert
    suspend fun insert(user: UserEntity): Long
    
    @Query("SELECT * FROM users WHERE username = :username AND password = :password LIMIT 1")
    suspend fun login(username: String, password: String): UserEntity?
    
    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    suspend fun getUserByUsername(username: String): UserEntity?
    
    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): UserEntity?
    
    @Query("SELECT * FROM users WHERE id = :userId LIMIT 1")
    suspend fun getUserById(userId: Int): UserEntity?
    
    @Update
    suspend fun update(user: UserEntity)
    
    @Delete
    suspend fun delete(user: UserEntity)
}
