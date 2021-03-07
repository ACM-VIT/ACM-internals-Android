package com.acmvit.acm_app.converters;

import com.acmvit.acm_app.model.Project;
import com.acmvit.acm_app.model.ProjectUpdateBody;

public class ProjectProjectUb extends Converter<Project, ProjectUpdateBody>{
    @Override
    public ProjectUpdateBody modelToEntity(Project project) {
        return new ProjectUpdateBody(project.getName(), project.getDescription(), project.getIcon(), project.getTags());
    }

    @Override
    public Project entityToModel(ProjectUpdateBody project) {
        throw new UnsupportedOperationException("Cannot convert back to Project from ProjectUpdateBody");
    }
}
