package com.ball.names.collector;

import java.util.List;

@FunctionalInterface
public interface NameCollector {

    List<String> collect();

}
