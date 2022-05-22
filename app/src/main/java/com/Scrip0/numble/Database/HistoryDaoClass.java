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

    //Select Data
    @Query("select * from user where `key`= :key")
    HistoryModel getData(int key);

    // Get last added model
    @Query("select * from user order by time desc, 'key' desc limit 1")
    HistoryModel selectLast();

}