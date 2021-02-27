package com.acmvit.acm_app.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Transaction;

import com.acmvit.acm_app.db.model.ProjectTagCrossRef;
import com.acmvit.acm_app.model.Project;

import java.util.List;

import io.reactivex.Completable;

@Dao
public abstract class ProjectTagCrossRefDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public abstract Completable insert(ProjectTagCrossRef ref);

    @Transaction
    @Insert
    public Completable insert(Project project) {
        List<String> tags = project.getTags();
        String projectId = project.getProject_id();
        Completable completable = Completable.complete();

        if (tags != null) {
            for (String tag : tags) {
                ProjectTagCrossRef crossRef = new ProjectTagCrossRef(projectId, tag);
                completable = completable.andThen(insert(crossRef));
            }
        }

        return completable;
    }

}
