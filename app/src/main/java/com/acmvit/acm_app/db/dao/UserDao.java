package com.acmvit.acm_app.db.dao;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.acmvit.acm_app.model.Project;
import com.acmvit.acm_app.model.User;

import java.util.List;

import io.reactivex.Completable;
@Dao
public interface UserDao {
    @Query("SELECT * FROM user ORDER BY name")
    LiveData<List<User>> getAllUsers();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertUsers(List<User> users);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertUser(User user);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    Completable insertUsersAndIgnoreIfExists(List<User> users);

}
