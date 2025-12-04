package com.devmasterteam.mybooks.repository

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.devmasterteam.mybooks.entity.BookEntity

@Dao
interface BookDAO {
    @Query("SELECT * FROM Book")
    fun getAllBooks(): List<BookEntity>

    @Query("SELECT * FROM Book WHERE favorite = 1")
    fun getFavoriteBooks(): List<BookEntity>

    @Query("SELECT * FROM Book WHERE id = :id")
    fun getBookById(id: Int): BookEntity

    @Update
    fun update(book: BookEntity)

    @Delete
    fun delete(book: BookEntity): Int

    @Insert
    fun insert (book: List<BookEntity>)
}