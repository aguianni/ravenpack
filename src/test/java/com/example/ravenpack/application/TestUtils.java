package com.example.ravenpack.application;

public class TestUtils {

    static public String getCSV() {
        return new StringBuilder()
                .append("user_id, message")
                .append(System.getProperty("line.separator"))
                .append("1,hello")
                .append(System.getProperty("line.separator"))
                .append("1,hello")
                .append(System.getProperty("line.separator"))
                .append("1,hello")
                .append(System.getProperty("line.separator"))
                .append("2,bye")
                .append(System.getProperty("line.separator"))
                .append("3,nice")
                .append(System.getProperty("line.separator"))
                .append("4,bad")
                .append(System.getProperty("line.separator"))
                .append("4,no")
                .toString();
    }


    static public String getOutCSV() {
        return new StringBuilder()
                .append("\"user_id\",\"total_messages\",\"avg_score\"")
                .append(System.getProperty("line.separator"))
                .append("\"1\",\"3\",\"1.0\"")
                .append(System.getProperty("line.separator"))
                .append("\"2\",\"1\",\"0.5\"")
                .append(System.getProperty("line.separator"))
                .append("\"3\",\"1\",\"0.4\"")
                .append(System.getProperty("line.separator"))
                .append("\"4\",\"2\",\"0.0\"")
                .append(System.getProperty("line.separator"))
                .toString();
    }
}
