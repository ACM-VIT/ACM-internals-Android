package com.acmvit.acm_app.converters;

import com.acmvit.acm_app.model.BasicProject;
import com.acmvit.acm_app.model.Project;

public class ProjectBasicProject extends Converter<Project, BasicProject> {

    @Override
    public BasicProject modelToEntity(Project project) {
        return new BasicProject(project.getProject_id(), project.getName(), project.getDescription(), project.getStatus());
    }

    @Override
    public Project entityToModel(BasicProject basicProject) {
        return new Project(basicProject.getId(), basicProject.getStatus(), basicProject.getName(), basicProject.getDisp());
    }
}
