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

//    //Update Data
//    @Query("update user SET time =:time, cells =:cells, equation = :equation, won = :won, finished = :finished where `key`= :key")
//    void updateData(String time, char[][] cells, String equation, boolean won, boolean finished, int key);

    @Query("SELECT * FROM user ORDER BY `key` DESC LIMIT 1")
    HistoryModel selectLast();

}