package com.company;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import java.util.Date;

// Aufgabe Digitales Notizbuch 2.0 Mathias Angerer

public class Main {

    public static void main(String[] args) {
        boolean read = true;
        boolean write;
        System.out.println("digital notebook 2.0");
        do {
            // Task 2, write new note or x for note, checking user input
            Scanner userInput = new Scanner(System.in);
            System.out.println("write new note or x");
            String note = userInput.nextLine();
            write = (!note.equalsIgnoreCase("x"));

            // Task 1 checking if user want to read out DB or not
            System.out.println("To read out notes, type in true or false");
            try {
                read = userInput.nextBoolean();
            } catch (Exception e) {
                System.out.println("wrong input, expected input: true/false");
            }
            Connection connection = null;

            // try to build connection
            try {
                String url = "jdbc:mysql://localhost:3306/notizbuch?user=root";
                connection = DriverManager.getConnection(url);

                // Task 2
                // write into database in case user wants to add new note to DB
                if (write) writeInDB(connection, note);

                // Task 1
                // read out from database in case user want to read out DB
                if (read) readOutDB(connection);

            } catch (SQLException e) {
                throw new Error("connection problem", e);
            } finally {
                try {
                    if (connection != null) {
                        connection.close();
                    }
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        } while (write || read);
    }

    // Methods
    public static void writeInDB(Connection connection, String note) {
        try (Statement statementWrite = connection.createStatement()) {
            Date date = new Date();
            SimpleDateFormat ft = new SimpleDateFormat("yyyy.MM.dd");
            statementWrite.executeUpdate("INSERT INTO notes (note_text, note_date)" +
                    "Values ('" + note + "', '" + ft.format(date) + "')");
        } catch (SQLException e) {
            throw new Error("Problem", e);
        }
    }

    public static void readOutDB(Connection connection) {
        String query = "select * from notes";
        try (Statement statementRead = connection.createStatement()) {
            ResultSet resultSet = statementRead.executeQuery(query);
            System.out.println("current notes: ");
            while (resultSet.next()) {
                int note_id = resultSet.getInt("note_id");
                String note_text = resultSet.getString("note_text");
                String note_date = resultSet.getString("note_date");
                System.out.println(note_id + " " + note_text + " " + note_date);
            }
        } catch (SQLException e) {
            System.out.println("wrong input");
        }
    }
}



