package com.acmvit.acm_app.converters;

import com.acmvit.acm_app.db.model.ProjectDb;
import com.acmvit.acm_app.model.Project;

public class ProjectProjectDb extends Converter<Project, ProjectDb> {

    @Override
    public ProjectDb modelToEntity(Project project) {
        return new ProjectDb (
                project.getProject_id(),
                project.getStatus(),
                project.getName(),
                project.getDescription(),
                project.getIcon(),
                project.getFounder().getUser_id(),
                project.getTimestamp().getSeconds()
        );
    }

    @Override
    public Project entityToModel(ProjectDb model) {
        return null; //TODO: Implement this
    }

}
