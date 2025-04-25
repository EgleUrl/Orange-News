package com.example.orangenews

// This file:
// Defines the Room database that persists bookmarked articles locally.
// Uses a singleton pattern to provide safe access from anywhere in the app.
// Automatically recreates the database if you change the schema (fallbackToDestructiveMigration()).
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Room Database for storing bookmarked news articles locally.
 * Defines the database configuration and serves as the app's main access point to persistent data.
 */
@Database(entities = [ArticleEntity::class], version = 3, exportSchema = false)
abstract class ArticleDatabase : RoomDatabase() {
    // Abstract method to access DAO operations
    abstract fun articleDao(): ArticleDao

    companion object {
        @Volatile
        private var INSTANCE: ArticleDatabase? = null

        /**
         * Provides a singleton instance of the database.
         * Ensures only one instance exists throughout the app lifecycle.
         */
        fun getDatabase(context: Context): ArticleDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ArticleDatabase::class.java,
                    "article_database" // Name of the database file
                )
                    .fallbackToDestructiveMigration() // If schema changes, clears and rebuilds database (use carefully in production)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
