package com.example.mailer.utils;

import com.example.mailer.properties.MailerProperties;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class ReceptionServiceImpl implements ReceptionService {
    private final MailerProperties properties;

    private record IndexHolder(int nameIndex, int emailIndex, Map<String, Integer> placeHolder) {
    }

    public ReceptionServiceImpl(MailerProperties properties) {
        this.properties = properties;
    }

    private IndexHolder findIndexes(Iterator<Cell> cellIterator) {
        int nameIndex = -1;
        int emailIndex = -1;

        int column = 0;
        final var placeHolderMap = new HashMap<String, Integer>();

        while (cellIterator.hasNext()) {
            final var currentCell = cellIterator.next();
            if (currentCell.getCellTypeEnum() == CellType.STRING) {
                final var value = currentCell.getStringCellValue().trim();

                if (StringUtils.hasLength(value)) {
                    if (properties.getNameField().equalsIgnoreCase(value)) {
                        nameIndex = column;
                    }
                    if (properties.getEmailField().equalsIgnoreCase(value)) {
                        emailIndex = column;
                    }

                    placeHolderMap.put(value, column);
                }
            }
            column++;
        }

        return new IndexHolder(nameIndex, emailIndex, placeHolderMap);
    }

    @Override
    public void processing(EntryDataInterface handler) throws IOException {
        Workbook workbook = new XSSFWorkbook(ReadFileUtil.readFrom(properties.getDataFile()));

        Sheet datatypeSheet = workbook.getSheetAt(0);
        Iterator<Row> iterator = datatypeSheet.iterator();

        IndexHolder indexHolder = null;

        AtomicInteger counter = new AtomicInteger(0);

        while (iterator.hasNext()) {
            final var currentRow = iterator.next();
            final var cellIterator = currentRow.iterator();

            if (indexHolder == null) {
                indexHolder = findIndexes(cellIterator);
            } else {
                final var column = new AtomicInteger(0);
                String name = null;
                String email = null;

                final var placeholder = new HashMap<String, String>();
                while (cellIterator.hasNext()) {
                    final var currentCell = cellIterator.next();

                    final var value = switch (currentCell.getCellTypeEnum()) {
                        case NUMERIC -> Double.toString(currentCell.getNumericCellValue());
                        default -> currentCell.getStringCellValue().trim();
                    };

                    if (StringUtils.hasLength(value)) {
                        if (column.get() == indexHolder.nameIndex) {
                            name = value;
                        } else if (column.get() == indexHolder.emailIndex) {
                            email = value;
                        }
                        indexHolder.placeHolder.forEach((k, v) -> {
                            if (v == column.get()) {
                                placeholder.put(k, value);
                            }
                        });
                    }
                    column.incrementAndGet();
                }

                if (StringUtils.hasLength(name) && StringUtils.hasLength(email)) {
                    handler.accept(counter.getAndIncrement(), email, name, placeholder);
                }
            }
        }
    }
}
