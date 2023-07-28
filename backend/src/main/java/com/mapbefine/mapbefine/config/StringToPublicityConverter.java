package com.mapbefine.mapbefine.config;

import com.mapbefine.mapbefine.entity.topic.Publicity;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToPublicityConverter implements Converter<String, Publicity> {

    @Override
    public Publicity convert(String input) {
        return Publicity.from(input);
    }

}
