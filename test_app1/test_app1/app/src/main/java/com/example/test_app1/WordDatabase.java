package com.example.test_app1;
import androidx.room.Database;
import  androidx.room.RoomDatabase;
@Database(entities = {Word.class},version = 5,exportSchema = false)
public abstract class WordDatabase extends RoomDatabase{
    public abstract WordDao getWordDao();
}
