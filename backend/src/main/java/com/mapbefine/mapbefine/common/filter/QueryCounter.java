package com.mapbefine.mapbefine.common.filter;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Component
@RequestScope
public class QueryCounter {

    private int count = 0;

    public void increase() {
        count++;
    }

    public int getCount() {
        return count;
    }

}
