package com.acmvit.acm_app.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.acmvit.acm_app.db.dao.ProjectDao;
import com.acmvit.acm_app.db.dao.ProjectMemberCrossRefDao;
import com.acmvit.acm_app.db.dao.ProjectTagCrossRefDao;
import com.acmvit.acm_app.db.dao.TagDao;
import com.acmvit.acm_app.db.dao.UserDao;
import com.acmvit.acm_app.db.model.ProjectDb;
import com.acmvit.acm_app.db.model.ProjectMemberCrossRef;
import com.acmvit.acm_app.db.model.ProjectTagCrossRef;
import com.acmvit.acm_app.model.Tag;
import com.acmvit.acm_app.model.User;

@Database(entities = {ProjectDb.class, User.class, ProjectMemberCrossRef.class, ProjectTagCrossRef.class, Tag.class}, version = 7, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AcmDb extends RoomDatabase {
    private static AcmDb instance;

    public static AcmDb getInstance(Context appContext){
        if (instance == null){
            synchronized (AcmDb.class){
                if (instance == null){
                    instance = Room.databaseBuilder(appContext,
                            AcmDb.class, "acm.db").fallbackToDestructiveMigration().build();
                }
            }
        }

        return instance;
    }

    public abstract ProjectDao projectDao();
    public abstract TagDao tagDao();
    public abstract UserDao userDao();
    public abstract ProjectMemberCrossRefDao projectMemberCrossRefDao();
    public abstract ProjectTagCrossRefDao projectTagCrossRefDao();

}
