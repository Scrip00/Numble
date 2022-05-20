package com.Scrip0.numble.Database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface HistoryDaoClass {

    @Insert
    void insertData(HistoryModel model);

    //Select All Data
    @Query("select * from  user")
    List<HistoryModel> getAllData();

    //Delete Data
    @Query("delete from user where `key`= :key")
    void deleteData(int key);

    // Get last added model
    @Query("SELECT * FROM user ORDER BY `key` DESC LIMIT 1")
    HistoryModel selectLast();

}