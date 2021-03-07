package com.acmvit.acm_app.repository.converters;

import com.acmvit.acm_app.model.Tag;

import java.util.ArrayList;
import java.util.List;

public class TagConverter extends Converter<String, Tag> {

    @Override
    public String entityToModel(Tag tag) {
        return tag.getTag();
    }

    @Override
    public Tag modelToEntity(String tag) {
        return new Tag(tag);
    }

}
