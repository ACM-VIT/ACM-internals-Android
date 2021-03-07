package com.acmvit.acm_app.db.dao;

import androidx.annotation.NonNull;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Transaction;

import com.acmvit.acm_app.db.model.ProjectMemberCrossRef;
import com.acmvit.acm_app.db.model.ProjectTagCrossRef;
import com.acmvit.acm_app.model.ListChange;
import com.acmvit.acm_app.model.Project;
import com.acmvit.acm_app.model.User;

import java.util.List;

import io.reactivex.Completable;

@Dao
public abstract class ProjectTagCrossRefDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public abstract Completable insert(ProjectTagCrossRef ref);

    @Delete
    public abstract Completable delete(ProjectTagCrossRef ref);

    @Transaction
    @Insert
    public Completable insert(Project project) {
        List<String> tags = project.getTags();
        String projectId = project.getProject_id();
        Completable completable = Completable.complete();

        if (tags != null) {
            return updateTags(project.getProject_id(), project.getTags(), true);
        }

        return completable;
    }

    public Completable updateProjectTags(@NonNull String projectId, ListChange<String> change) {
        return updateTags(projectId, change.added, true)
                .andThen(updateTags(projectId, change.removed, false));
    }

    public Completable updateTags(@NonNull String projectId, @NonNull List<String> tags, boolean isInsert) {
        Completable completable = Completable.complete();
        for (String tag : tags) {
            ProjectTagCrossRef crossRef = new ProjectTagCrossRef(tag, projectId);
            completable = completable.andThen(isInsert ? insert(crossRef) : delete(crossRef));
        }
        return completable;
    }

}
