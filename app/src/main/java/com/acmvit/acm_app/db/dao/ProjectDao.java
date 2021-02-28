package com.acmvit.acm_app.db.dao;

import androidx.annotation.Nullable;
import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RewriteQueriesToDropUnusedColumns;
import androidx.room.Transaction;
import com.acmvit.acm_app.db.model.ProjectDb;
import com.acmvit.acm_app.model.Project;
import com.acmvit.acm_app.model.ProjectStatus;
import com.acmvit.acm_app.model.User;
import io.reactivex.Completable;
import java.util.List;

@Dao
public interface ProjectDao {
    @Transaction
    @RewriteQueriesToDropUnusedColumns
    @Query(
        "SELECT * FROM Project as p INNER JOIN PROJECTMEMBERCROSSREF as cr " +
        "ON p.project_id = cr.project_id " +
        "WHERE (:status IS NULL OR status = :status)  " +
        "AND (:userId IS NULL OR user_id = :userId)" +
        "ORDER BY last_updated DESC"
    )
    DataSource.Factory<Integer, Project> getAllProjectsWithUserStatus(
        @Nullable ProjectStatus status,
        @Nullable String userId
    );

    @Transaction
    @RewriteQueriesToDropUnusedColumns
    @Query(
        "SELECT * FROM Project WHERE name LIKE :name || '%' ORDER BY last_updated DESC"
    )
    DataSource.Factory<Integer, Project> getAllProjectsWithName(String name);

    @Transaction
    @RewriteQueriesToDropUnusedColumns
    @Query(
        "SELECT * FROM Project as p INNER JOIN ProjectTagCrossRef as cr " +
        "ON p.project_id = cr.project_id " +
        "WHERE tag = :tag  " +
        "ORDER BY last_updated DESC"
    )
    DataSource.Factory<Integer, Project> getAllProjectsWithTag(String tag);

    @Transaction
    @RewriteQueriesToDropUnusedColumns
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertProject(List<ProjectDb> project);

    @Transaction
    @RewriteQueriesToDropUnusedColumns
    @Query("DELETE FROM Project")
    Completable deleteProjects();
}
