package com.marvelapi.database

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

class DatabaseProvider(private val context: Context) {

    @Volatile
    private var _database: CharacterDatabase? = null

    private val migration1to2 = object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("CREATE TABLE IF NOT EXISTS `characters_new` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT, `description` TEXT, `url` TEXT, `isFavorite` INTEGER NOT NULL DEFAULT 0)")
            db.execSQL("INSERT INTO characters_new (id, name, description, url, isFavorite) SELECT id, name, description, url, CASE WHEN isFavorite = 1 THEN 1 ELSE 0 END FROM characters")
            db.execSQL("DROP TABLE characters")
            db.execSQL("ALTER TABLE characters_new RENAME TO characters")
            db.execSQL("ALTER TABLE characters ADD COLUMN new_column INTEGER DEFAULT 0")

        }
    }


    fun getDatabase(): CharacterDatabase {
        return _database ?: synchronized(this) {
            _database ?: Room.databaseBuilder(
                context.applicationContext,
                CharacterDatabase::class.java,
                "character_database"
            )
                .addMigrations(migration1to2)
                .fallbackToDestructiveMigration()
                .build().also {
                    _database = it
                }
        }
    }
}