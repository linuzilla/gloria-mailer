package com.example.mailer.utils;

import java.io.IOException;

public interface ReceptionService {
    void processing(EntryDataInterface handler) throws IOException;
}
