package com.Scrip0.numble.Database;

import android.util.Log;

import androidx.room.TypeConverter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Converters {
    @TypeConverter
    public static char[][] charArrayFromString(String value) {
        String[] rows = value.split("\\|");
        if (rows.length == 0) return new char[0][0];
        String[] values = rows[0].split(";");
        char[][] c = new char[rows.length][values.length];
        for (int i = 0; i < rows.length; i++) {
            values = rows[i].split(";");
            for (int j = 0; j < values.length; j++) {
                c[i][j] =
                values[j].charAt(0);
            }
        }
        return c;
    }

    @TypeConverter
    public static String stringFromCharArray(char[][] list) {
        StringBuilder str = new StringBuilder();
        ArrayList<Character> allowedCharacters = new ArrayList<>(Arrays.asList('1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '+', '-', '=', '*', '/', '^', '!'));
        for (char[] row : list) {
            for (char c : row) {
                if (allowedCharacters.contains(c)) str.append(c).append(";");
                else str.append(' ').append(";");
            }
            str = new StringBuilder(str.substring(0, str.length() - 1));
            str.append("|");
        }
        str = new StringBuilder(str.substring(0, str.length() - 1));
        return str.toString();
    }
}