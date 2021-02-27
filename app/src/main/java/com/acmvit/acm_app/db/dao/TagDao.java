package com.acmvit.acm_app.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.acmvit.acm_app.model.Tag;

import java.util.List;

import io.reactivex.Completable;

@Dao
public interface TagDao {
    @Query("SELECT * FROM Tag ORDER BY tag ASC")
    LiveData<List<Tag>> getAllTags();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    Completable insertTags(List<Tag> tags);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    Completable insertTag(Tag tag);
}
