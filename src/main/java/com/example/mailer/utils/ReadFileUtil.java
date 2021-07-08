package com.example.mailer.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * @author Jiann-Ching Liu (saber@g.ncu.edu.tw)
 * @version 1.0
 * @since 1.0
 */
public class ReadFileUtil {
    private static final String CLASSPATH_PREFIX = "classpath:";

    public static InputStream readFrom(String file) throws FileNotFoundException {
        if (file.startsWith(CLASSPATH_PREFIX)) {
            return ReadFileUtil.class.getResourceAsStream(file.substring(CLASSPATH_PREFIX.length()));
        } else {
            return new FileInputStream(file);
        }
    }
}
