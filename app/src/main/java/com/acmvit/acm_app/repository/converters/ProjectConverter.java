package com.acmvit.acm_app.repository.converters;

import com.acmvit.acm_app.db.model.ProjectDb;
import com.acmvit.acm_app.model.Project;
import com.acmvit.acm_app.repository.converters.Converter;

import java.util.ArrayList;
import java.util.List;

public class ProjectConverter extends Converter<Project, ProjectDb> {

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
