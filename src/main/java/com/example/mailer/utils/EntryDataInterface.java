package com.example.mailer.utils;

import java.util.Map;

@FunctionalInterface
public interface EntryDataInterface {
    void accept(int counter, String email, String name, Map<String, String> placeholder);
}
