package com.mapbefine.mapbefine.config;

import com.mapbefine.mapbefine.entity.topic.Permission;
import org.springframework.core.convert.converter.Converter;

public class StringToPermissionConverter implements Converter<String, Permission> {

    @Override
    public Permission convert(String input) {
        return Permission.from(input);
    }

}
