package com.example.project;

import java.io.*;

public class CompletedQuiz extends Quiz {
    private int score;
    private static int indexInList = 0;
    private double score2;

    public CompletedQuiz() {
    }

    public static void setIndexInList(int indexInList) {
        CompletedQuiz.indexInList = indexInList;
    }

    public CompletedQuiz(User user, int score) {
        super();
        setUser(user);
        this.score = score;
    }

    public CompletedQuiz(User user, double score2) {
        super();
        setUser(user);
        this.score2 = score2;
    }

    public CompletedQuiz(User user) {
        super();
        setUser(user);
    }

    /* functie care adauga in "completedQuizzes.csv" username-ul user-ului
    care a completat quiz-ul, id-ul quiz-ului, numele, punctajul, indexul */
    public void addQuizCSV(String quizID, String quizName) {
        indexInList++;
        try (FileWriter fw = new FileWriter("completedQuizzes.csv", true)) {
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw);
            out.println(user.username + "," + quizID + "," + quizName + "," + score + "," + indexInList);
            out.close();
        } catch (IOException ioe) {
            System.out.println("An error occurred.");
            ioe.printStackTrace();
        }
    }

    /* functie care cauta in fisierul "completedQuizzes.csv"
    id-ul unui quiz si sterge quiz-ul */
    public void deleteQuiz(String quizID) {
        try {
            FileInputStream fstream = new FileInputStream("completedQuizzes.csv");
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
            String strLine;
            StringBuilder fileContent = new StringBuilder();
            while ((strLine = br.readLine()) != null) {
                String[] tokens = strLine.split(",");
                if (tokens.length > 0) {
                    if (tokens[1].equals(quizID)) {
                        String newLine = "Deleted quiz";
                        fileContent.append(newLine);
                        fileContent.append("\n");
                    } else {
                        fileContent.append(strLine);
                        fileContent.append("\n");
                    }
                }
            }
            FileWriter fstreamWrite = new FileWriter("completedQuizzes.csv");
            BufferedWriter out = new BufferedWriter(fstreamWrite);
            out.write(fileContent.toString());
            out.close();
            fstream.close();
        } catch (IOException ioe) {
            System.out.println("An error occurred.");
            ioe.printStackTrace();
        }
    }

    /* functie care returneaza toate solutiile pentru un user in functie
    de username din fisierul "completedQuizzes.csv" */
    public String findSolutionsForUser() {
        String result = "[";
        try (BufferedReader br = new BufferedReader(new FileReader("completedQuizzes.csv"))) {
            String line;
            String[] lineArray;
            while ((line = br.readLine()) != null) {
                lineArray = line.split(",");
                if (lineArray.length > 0) {
                    if (lineArray[0].equals(user.username)) {
                        result = result.concat("{\"quiz-id\" : \"" + lineArray[1] + "\", \"quiz-name\" : \"" +
                                lineArray[2] + "\", \"score\" : \"" + lineArray[3] + "\", \"" +
                                "index_in_list\" : \"" + lineArray[4] + "\"}, ");
                        result = result.concat("[");
                    }
                }
            }
        } catch (IOException ioe) {
            System.out.println("An error occurred.");
            ioe.printStackTrace();
        }
        result = result.substring(0, result.length() - 3);
        result = result.concat("]");
        if (result.equals("[]")) {
            return null;
        }
        return result;
    }
}
