package com.androideradev.www.moviespots.data;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

class GenreIdConverter {
    private static final Gson mGson = new Gson();

    @TypeConverter
    public static List<Integer> stringToGenreList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }
        Type listType = new TypeToken<List<Integer>>() {
        }.getType();
        return mGson.fromJson(data, listType);
    }

    @TypeConverter
    public static String genreListToString(List<Integer> genreIds) {
        if (genreIds == null) {
            return mGson.toJson(Collections.emptyList());
        }
        return mGson.toJson(genreIds);
    }
}
