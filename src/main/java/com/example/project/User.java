package com.example.project;

import java.io.*;
import java.util.Arrays;

public class User {
    protected String username, password;
    private static int id = 0;
    private String[] quizStatuses = new String[Quiz.getId()];

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        Arrays.fill(quizStatuses, "False");
    }

    public static int getId() {
        return id;
    }

    public static void setId(int id) {
        User.id = id;
    }

    /* functie care scrie in fisierul "users.csv" datele unui user:
        username, password, id */
    public void addUserCSV() {
        id++;
        try (FileWriter fw = new FileWriter("users.csv", true)) {
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw);
            out.println(username + "," + password + "," + id);
            out.close();
        } catch (IOException ioe) {
            System.out.println("An error occurred.");
            ioe.printStackTrace();
        }
    }

    /* functie care verifica in fisierul "users.csv" existenta unui user
    dupa username; intoarce null daca nu exista user-ul respectiv
    altfel, intoarce id-ul user-ului */
    public String checkUserCSV() {
        try (BufferedReader br = new BufferedReader(new FileReader("users.csv"))) {
            String line;
            String[] lineArray;
            while ((line = br.readLine()) != null) {
                lineArray = line.split(",");
                if (lineArray.length > 0) {
                    if (lineArray[0].equals(username)) {
                        return lineArray[2]; //id-ul user-ului
                    }
                }
            }
        } catch (IOException ioe) {
            System.out.println("An error occurred.");
            ioe.printStackTrace();
        }
        return null;
    }

    /* functie care verifica daca parola este corecta la autentificarea user-ului
    intoarce true in cazul in care datele sunt corecte */
    public boolean checkUserCredentialsCSV() {
        try (BufferedReader br = new BufferedReader(new FileReader("users.csv"))) {
            String line;
            String[] lineArray;
            while ((line = br.readLine()) != null) {
                lineArray = line.split(",");
                if (lineArray.length > 0) {
                    if (lineArray[0].equals(username) && !lineArray[1].equals(password)) {
                        return false;
                    }
                }
            }
        } catch (IOException ioe) {
            System.out.println("An error occurred.");
            ioe.printStackTrace();
        }
        return true;
    }

    /* functie care cauta toate intrebarile create de un user si afiseaza detaliile */
    public String findQuestionsByUser() {
        String questionInfo = "[";
        try (BufferedReader br = new BufferedReader(new FileReader("questions.csv"))) {
            String line;
            String[] lineArray;
            while ((line = br.readLine()) != null) {
                lineArray = line.split(",");
                if (lineArray.length > 0) {
                    if (lineArray[1].equals(username)) {
                        questionInfo = questionInfo.concat("{\"question_id\" : \"" + lineArray[0] +
                                "\", " + "\"question_name\" : \"" + lineArray[3] + "\"}, ");
                    }
                }
            }
        } catch (IOException ioe) {
            System.out.println("An error occurred.");
            ioe.printStackTrace();
        }
        questionInfo = questionInfo.substring(0, questionInfo.length() - 2);
        questionInfo = questionInfo.concat("]");
        if (questionInfo.equals("[]")) {
            return null;
        }
        return questionInfo;
    }

    /* functie care adauga in fisierul "usersQuizzes.csv" toate id-urile
    userilor existenti in sistem alaturi de id-urile quiz-urilor existente
    cu statusul fiecarui quiz: completed(True), notCompleted(false);
    initial, toate sunt "False" */
    public void addUsersQuizzesStatusCSV() {
        try (FileWriter fw = new FileWriter("usersQuizzes.csv", false)) {
            BufferedWriter bw = new BufferedWriter((fw));
            PrintWriter out = new PrintWriter(bw);
            for (int i = 0; i < User.id; i++) {
                int indexUser = i + 1;
                out.print(indexUser + ",");
                for (int j = 0; j < Quiz.getId(); j++) {
                    int indexQuiz = j + 1;
                    out.print(indexQuiz + " " + quizStatuses[j] + ";");
                }
                out.println();
            }
            out.close();
        } catch (IOException ioe) {
            System.out.println("An error occurred.");
            ioe.printStackTrace();
        }
    }

    /* functie care afiseaza detaliile quiz-urilor din "quizzes.csv"
    cu statusul completarii fiecarui quiz din vectorul quizStatusesForUser */
    public String findQuizzesByUser(String[] quizStatusesForUser) {
        String quizInfo = "[";
        int contor = 0;
        try (BufferedReader br = new BufferedReader(new FileReader("quizzes.csv"))) {
            String line;
            String[] lineArray;
            while ((line = br.readLine()) != null) {
                lineArray = line.split(",");
                if (lineArray.length > 0) {
                    quizInfo = quizInfo.concat("{\"quizz_id\" : \"" + lineArray[0] + "\", " +
                            "\"quizz_name\" : \"" + lineArray[3] + "\", \"is_completed\" : \"" +
                            quizStatusesForUser[contor] + "\"}, ");
                    contor++;
                }
            }
        } catch (IOException ioe) {
            System.out.println("An error occurred.");
            ioe.printStackTrace();
        }
        quizInfo = quizInfo.substring(0, quizInfo.length() - 2);
        quizInfo = quizInfo.concat("]");
        return quizInfo;
    }

    /* functie care verifica daca utilizatorul raspunde
    la propriul quiz (daca id-ul quiz-ului trimis ca parametru
    este creat de user-ul care vrea sa raspunda la chestionar) */
    public String checkQuizUserCSV(String quizID) {
        try (BufferedReader br = new BufferedReader(new FileReader("quizzes.csv"))) {
            String line;
            String[] lineArray;
            while ((line = br.readLine()) != null) {
                lineArray = line.split(",");
                if (lineArray.length > 0) {
                    if (lineArray[0].equals(quizID) && lineArray[1].equals(username)) {
                        return "You cannot answer your own quizz";
                    }
                }
            }
        } catch (IOException ioe) {
            System.out.println("An error occurred.");
            ioe.printStackTrace();
        }
        return null;
    }

    /* functie care actualizeaza fisierul "usersQuizzes.csv"
    prin adaugarea noilor utilizatori (daca exista) */
    public void updateUsersInQuizzesStatusCSV(int lines) {
        try (FileWriter fw = new FileWriter("usersQuizzes.csv", true)) {
            BufferedWriter bw = new BufferedWriter((fw));
            PrintWriter out = new PrintWriter(bw);
            for (int i = lines; i < User.id; i++) {
                int indexUser = i + 1;
                out.print(indexUser + ",");
                for (int j = 0; j < Quiz.getId(); j++) {
                    int indexQuiz = j + 1;
                    out.print(indexQuiz + " " + quizStatuses[j] + ";");
                }
                out.println();
            }
            out.close();
        } catch (IOException ioe) {
            System.out.println("An error occurred.");
            ioe.printStackTrace();
        }
    }
}
