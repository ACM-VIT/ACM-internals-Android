package com.acmvit.acm_app.converters;

import com.acmvit.acm_app.model.Tag;

public class TagString extends Converter<String, Tag> {

    @Override
    public String entityToModel(Tag tag) {
        return tag.getTag();
    }

    @Override
    public Tag modelToEntity(String tag) {
        return new Tag(tag);
    }

}
