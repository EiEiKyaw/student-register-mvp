package me.kaungmyatmin.studentreg

import androidx.room.*

@Dao
interface StudentDao {

    @Insert
    suspend fun insert(data: Student): Long

    @Query("SELECT * FROM Student")
    fun findAll(): List<Student>

    @Query("SELECT * FROM Student WHERE id=:id")
    fun findById(id: Long): Student

    @Update
    fun update(data: Student): Int

    @Delete
    fun delete(data: Student): Int
}