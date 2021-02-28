package com.acmvit.acm_app.repository.converters;

import com.acmvit.acm_app.db.model.ProjectDb;
import com.acmvit.acm_app.model.Project;
import java.util.ArrayList;
import java.util.List;

public abstract class Converter<MODEL, ENTITY> {

    public abstract ENTITY modelToEntity(MODEL model);

    public abstract MODEL entityToModel(ENTITY model);

    public List<ENTITY> modelToEntity(List<MODEL> models) {
        List<ENTITY> entities = new ArrayList<>();

        if (models == null) {
            return new ArrayList<>();
        }

        for (MODEL model : models) {
            entities.add(modelToEntity(model));
        }

        return entities;
    }

    public List<MODEL> entityToModel(List<ENTITY> entities) {
        List<MODEL> models = new ArrayList<>();
        if (entities == null) {
            return new ArrayList<>();
        }

        for (ENTITY entity : entities) {
            models.add(entityToModel(entity));
        }

        return models;
    }
}
