package utils;

import java.time.format.DateTimeFormatter;

public class Constants {
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm");
    public static final String DATABASE_USERNAME = "postgres";
    public static final String DATABASE_PASSWORD = "moni";
    public static final String DATABASE_URL = "jdbc:postgresql://localhost:5432/Facebook";
    public static final String DATABASE_URL_TEST = "jdbc:postgresql://localhost:5432/FacebookTest";
    public static final String QUERIES_FILE_NAME = "/DatabaseSQLQueries.xml";
    public static final String TEST_QUERIES_FILE_NAME = "/TestQueries.xml";
    public static final String MOON_EMOJI = "\uD83C\uDF19";
    public static final String SEND_FRIEND_REQUEST_ROCKET_EMOJI = "\uD83D\uDE80";
    public static final String PENDING_HOURGLASS_EMOJI = "⏳";
    public static final String REMOVE_FRIEND_EMOJI = "\uD83D\uDEAB";
    public static final String SENT_REQUEST_EMOJI = "➡";
    public static final String PROFILE_EMOJI = "\uD83D\uDE4E";


}


