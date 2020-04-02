package com.company;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import java.util.Date;

// Aufgabe Digitales Notizbuch 2.0 Mathias Angerer

public class Main {

    public static void main(String[] args) {
        boolean read;
        boolean write;
        System.out.println("digital notebook 2.0");
        do {

            // Task 2
            Scanner userInput = new Scanner(System.in);
            System.out.println("write new note or x");
            String note = userInput.nextLine();
            write = (!note.equalsIgnoreCase("x"));

            // Task 1
            System.out.println("To read out notes, type in true or false");
            try {
                read = userInput.nextBoolean();
            } catch (Exception e) {
                throw new Error("wrong input");
            }
            Connection connection = null;
            try {
                String url = "jdbc:mysql://localhost:3306/notizbuch?user=root";
                connection = DriverManager.getConnection(url);

                // Task 2
                // write into database
                try (Statement statementWrite = connection.createStatement()) {
                    if (write) {
                        Date date = new Date();
                        SimpleDateFormat ft = new SimpleDateFormat("yyyy.MM.dd");
                        statementWrite.executeUpdate("INSERT INTO notes (note_text, note_date)" +
                                "Values ('" + note + "', '" + ft.format(date) + "')");
                        statementWrite.close();
                    }
                } catch (SQLException e) {
                    throw new Error("Problem", e);
                }

                // Task 1
                // read out from database
                String query = "select * from notes";
                try (Statement statementRead = connection.createStatement()) {
                    ResultSet resultSet = statementRead.executeQuery(query);
                    if (read) System.out.println("current notes: ");
                    while (resultSet.next() && read) {
                        int note_id = resultSet.getInt("note_id");
                        String note_text = resultSet.getString("note_text");
                        String note_date = resultSet.getString("note_date");
                        System.out.println(note_id + " " + note_text + " " + note_date);
                    }
                } catch (SQLException e) {
                    throw new Error("Problem", e);
                }
            } catch (SQLException e) {
                throw new Error("Problem", e);
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
}



