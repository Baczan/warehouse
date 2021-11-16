package com.example.resource.formatters;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class AllegroEventFormatter extends Formatter {

    private static final DateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss.SSS");

    @Override
    public String format(LogRecord record) {

        StringBuilder builder = new StringBuilder(1000);
        builder.append(df.format(new Date(record.getMillis()))).append(" - ");
        builder.append(record.getMessage());
        builder.append("\n");
        return builder.toString();
    }
}
