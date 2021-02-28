package com.acmvit.acm_app.db.dao;

import android.util.Log;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Transaction;
import com.acmvit.acm_app.db.model.ProjectMemberCrossRef;
import com.acmvit.acm_app.db.model.ProjectTagCrossRef;
import com.acmvit.acm_app.model.Project;
import com.acmvit.acm_app.model.User;
import io.reactivex.Completable;
import java.util.ArrayList;
import java.util.List;

@Dao
public abstract class ProjectMemberCrossRefDao {

    private static final String TAG = "ProjectMemberCrossRefDa";

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public abstract Completable insert(ProjectMemberCrossRef ref);

    @Transaction
    @Insert
    public Completable insert(Project project) {
        List<String> userIds = new ArrayList<>();

        if (project.getMembersWithFounder() == null) {
            return Completable.complete();
        }

        for (User user : project.getMembersWithFounder()) {
            userIds.add(user.getUser_id());
        }

        String projectId = project.getProject_id();
        Completable completable = Completable.complete();

        for (String userId : userIds) {
            ProjectMemberCrossRef crossRef = new ProjectMemberCrossRef(
                userId,
                projectId
            );
            completable = completable.andThen(insert(crossRef));
        }

        return completable;
    }
}
