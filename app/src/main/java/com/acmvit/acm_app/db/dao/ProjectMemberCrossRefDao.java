package com.acmvit.acm_app.db.dao;

import androidx.annotation.NonNull;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Transaction;

import com.acmvit.acm_app.db.model.ProjectMemberCrossRef;
import com.acmvit.acm_app.model.ListChange;
import com.acmvit.acm_app.model.Project;
import com.acmvit.acm_app.model.User;

import java.util.List;

import io.reactivex.Completable;

@Dao
public abstract class ProjectMemberCrossRefDao {
    private static final String TAG = "ProjectMemberCrossRefDa";

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public abstract Completable insert(ProjectMemberCrossRef ref);

    @Delete
    public abstract Completable delete(ProjectMemberCrossRef ref);

    @Transaction
    public Completable insert(Project project) {
        if (project.getMembersWithFounder() == null) {
            return Completable.complete();
        }
        return updateUsers(project.getProject_id(), project.getMembersWithFounder(), true);
    }

    public Completable updateProjectMembers(@NonNull String projectId, ListChange<User> change) {
        return updateUsers(projectId, change.added, true)
                .andThen(updateUsers(projectId, change.removed, false));
    }

    public Completable updateUsers(@NonNull String projectId, @NonNull List<User> users, boolean isInsert) {
        Completable completable = Completable.complete();
        for (User user : users) {
            ProjectMemberCrossRef crossRef = new ProjectMemberCrossRef(user.getUser_id(), projectId);
            completable = completable.andThen(isInsert ? insert(crossRef) : delete(crossRef));
        }
        return completable;
    }

}
