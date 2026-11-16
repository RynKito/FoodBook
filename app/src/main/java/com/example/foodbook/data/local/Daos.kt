package com.example.foodbook.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insert(user: UserEntity)
    @Query("DELETE FROM users") suspend fun clear()
}

@Dao
interface MealDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE) suspend fun insertAll(list: List<MealEntity>)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insert(meal: MealEntity)
    @Query("SELECT * FROM meals WHERE category = :c") suspend fun byCategory(c: String): List<MealEntity>
    @Query("SELECT * FROM meals WHERE id = :id") suspend fun byId(id: String): MealEntity?
    @Query("SELECT * FROM meals WHERE name LIKE '%' || :q || '%'") suspend fun search(q: String): List<MealEntity>
}

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun add(f: FavoriteEntity)
    @Query("DELETE FROM favorites WHERE userId = :u AND mealId = :m") suspend fun remove(u: String, m: String)
    @Query("SELECT * FROM favorites WHERE userId = :u") fun observe(u: String): LiveData<List<FavoriteEntity>>
    @Query("SELECT COUNT(*) FROM favorites WHERE userId = :u AND mealId = :m") fun observeCount(u: String, m: String): LiveData<Int>
}
