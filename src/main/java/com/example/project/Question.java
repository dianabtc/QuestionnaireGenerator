package com.example.project;

import java.io.*;
import java.util.HashSet;

public class Question {
    private static int id = 0;
    private User user;
    private String text, type;
    private Answer[] answers;

    public static void setId(int id) {
        Question.id = id;
    }

    public Question(User user, String text, String type, Answer[] answers) {
        this.user = user;
        this.text = text;
        this.type = type;
        this.answers = answers;
        id++;
    }

    public Question(User user, String text) {
        this.user = user;
        this.text = text;
    }

    public String toString() {
        return "{\"question_id\" : \"" + id + "\", " +
                "\"question_name\" : \"" + text + "\"}";
    }

    /* functie care adauga in fisierul "questions.csv" o intrebare
    adauga id, username, parola, textul intrebarii, tipul, vectorul de raspunsuri
    fiecare raspuns este caracterizat de descriere si flag si sunt separate prin ";" */
    public void addQuestionCSV() {
        try (FileWriter fw = new FileWriter("questions.csv", true)) {
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw);
            out.print(id + "," + user.username + "," +
                    user.password + "," + text + "," +
                    type + ",");
            for (int i = 0; i < answers.length; i++) {
                out.print(answers[i].toString() + ";");
            }
            out.println();
            out.close();
        } catch (IOException ioe) {
            System.out.println("An error occurred.");
            ioe.printStackTrace();
        }
    }

    /* functie care verifica daca o intrebare de tip "single" primeste
    mai multe raspunsuri corecte prin verificarea flag-urilor din vectorul
    de raspunsuri (cand este 1, creste count), pentru count mai mare de 1
    se opreste */
    public boolean checkSingleQuestionMultipleAnswers() {
        int count = 0;
        boolean ok = false;
        if (type.equals("single")) {
            for (int i = 0; i < answers.length; i++) {
                if (answers[i].getValue().equals("1")) {
                    count++;
                }
                if (count == 2) {
                    ok = true;
                    break;
                }
            }
        }
        return ok;
    }

    /* functie care verifica daca o intrebare are mai multe raspunsuri cu
    acelasi text */
    public boolean checkDuplicateAnswers() {
        String[] tempAnswers = new String[answers.length];
        for (int i = 0; i < tempAnswers.length; i++) {
            tempAnswers[i] = answers[i].getText();
        }
        HashSet<String> set = new HashSet<>();
        for (int i = 0; i < tempAnswers.length; i++) {
            String text = tempAnswers[i];
            if (set.add(text) == false) {
                return true; //am gasit duplicate
            }
        }
        return false;
    }

    /* functie care verifica existenta descrierii/flag-ului unui raspuns
    din vectorul de answers */
    public String checkAnswerDescriptionsFlags() {
        for (int i = 0; i < answers.length; i++) {
            if (answers[i].getText() == null) {
                return "Answer " + (i+1) + " has no answer description";
            }
            if (answers[i].getValue() == null) {
                return "Answer " + (i+1) + " has no answer correct flag";
            }
        }
        //toate raspunsurile au descriere si flag
        return null;
    }

    /* functie care verifica daca in fisier exista deja o intrebare cu textul
    respectiv; daca nu exista, intoarce null */
    public  String[] checkQuestionCSV() {
        try (BufferedReader br = new BufferedReader(new FileReader("questions.csv"))) {
            String line;
            String[] lineArray;
            while ((line = br.readLine()) != null) {
                lineArray = line.split(",");
                if (lineArray.length > 0) {
                    if (lineArray[3].equals(text)) {
                        return lineArray; //datele intrebarii
                    }
                }
            }
        } catch (IOException ioe) {
            System.out.println("An error occurred.");
            ioe.printStackTrace();
        }
        return null;
    }

    /* functie care cauta in fisierul "questions.csv" intrebarea dupa
    text si intoarce id-ul ei; daca nu gaseste, intoarce 0 */
    public int getQuestionIDCSV() {
        try (BufferedReader br = new BufferedReader(new FileReader("questions.csv"))) {
            String line;
            String[] lineArray;
            while ((line = br.readLine()) != null) {
                lineArray = line.split(",");
                if (lineArray.length > 0) {
                    if (lineArray[3].equals(text)) {
                        return id;
                    }
                }
            }
        } catch (IOException ioe) {
            System.out.println("An error occurred.");
            ioe.printStackTrace();
        }
        return 0;
    }

    /* functie care cauta in "questions.csv" intrebarile cu id-urile dintr-un
    chestionar si afiseaza detaliile */
    public static String findQuestionsIDs(String[] ids) {
        String questionInfo = "[";
        int count = 1;
        try (BufferedReader br = new BufferedReader(new FileReader("questions.csv"))) {
            String line;
            String[] lineArray;
            for (int i = 0; i < ids.length; i++) {
                while ((line = br.readLine()) != null) {
                    lineArray = line.split(",");
                    if (lineArray.length > 0) {
                        if (lineArray[0].equals(ids[i])) {
                            questionInfo = questionInfo.concat("{\"question-name\":\"" +
                                    lineArray[3] + "\", \"question_index\":\"" + count +
                                    "\", \"question_type\":\"" + lineArray[4] + "\", " +
                                    "\"answers\":\"[");
                            String[] lineAnswers = lineArray[5].split(";");
                            for (int j = 0; j < lineAnswers.length; j++) {
                                String[] lineComponents = lineAnswers[j].split(" ");
                                questionInfo = questionInfo.concat("{\"answer_name\":\"" +
                                        lineComponents[0] + "\", \"answer_id\":\"" + lineComponents[2] +
                                        "\"}, ");
                            }
                            count++;
                            questionInfo = questionInfo.substring(0, questionInfo.length() - 2);
                            questionInfo = questionInfo.concat("]\"}, ");
                            break;
                        }
                    }
                }
            }
        } catch (IOException ioe) {
            System.out.println("An error occured.");
            ioe.printStackTrace();
        }
        questionInfo = questionInfo.substring(0, questionInfo.length() - 2);
        questionInfo = questionInfo.concat("]");
        if (questionInfo.equals("[]")) {
            return null;
        }
        return questionInfo;
    }

    /* functie care cauta in fisierul "questions.csv" existenta raspunsului
    cu id-ul oferit ca parametru; intoarce null daca a gasit */
    public static String checkAnswerIDsCSV(String answerID) {
        int ok = 0;
        try (BufferedReader br = new BufferedReader(new FileReader("questions.csv"))) {
            String line;
            String[] lineArray;
            String[] answers;
            String[] lineAnswer;
            while ((line = br.readLine()) != null) {
                lineArray = line.split(",");
                if (lineArray.length > 0) {
                    answers = lineArray[5].split(";");
                    for (int i = 0; i < answers.length; i++) {
                        lineAnswer = answers[i].split(" ");
                        if (lineAnswer[2].equals(answerID)) {
                            ok = 1;
                            break;
                        }
                    }
                    if (ok == 1) {
                        break;
                    }
                }
            }
        } catch (IOException ioe) {
            System.out.println("An error occurred.");
            ioe.printStackTrace();
        }
        if (ok == 0) {
            //nu am gasit un raspuns cu id-ul respectiv
            return "Answer ID for answer " + answerID + " does not exist";
        }
        return null;
    }

    /* functie care calculeaza punctajul unei intrebari cu id-ul questionID
    atunci cand raspunsul este cel cu answerID */
    public static double calculatePointsPerQuestion(String questionID, String answerID) {
        double result = 0;
        try (BufferedReader br = new BufferedReader(new FileReader("questions.csv"))) {
            String line;
            String[] lineArray = null;
            String[] answers = null;
            String[] lineAnswer = null;
            while ((line = br.readLine()) != null) {
                lineArray = line.split(",");
                if (lineArray.length > 0) {
                    //am gasit intrebarea la care user-ul vrea sa raspunda
                    if (lineArray[0].equals(questionID)) {
                        //vector de raspunsuri
                        answers = lineArray[5].split(";");
                        if (lineArray[4].equals("single")) {
                            for (int i = 0; i < answers.length; i++) {
                                lineAnswer = answers[i].split(" ");
                                //daca am gasit raspunsul dat de utilizator
                                if (lineAnswer[2].equals(answerID)) {
                                    if (lineAnswer[1].equals("1")) { //daca raspunsul este cel corect
                                        result = 1;
                                    } else { //daca raspunsul este gresit
                                        result = -1;
                                    }
                                    break;
                                }
                            }
                        } else {
                            int correctAnswers = 0, wrongAnswers = 0;
                            double pointsCorrectAnswers = 0, pointsWrongAnswers = 0;
                            if (answers.length == 2) {
                                //toate raspunsurile sunt corecte
                                for (int i = 0; i < answers.length; i++) {
                                    lineAnswer = answers[i].split(" ");
                                    if (lineAnswer[2].equals(answerID)) {
                                        //se adauga 0.5
                                        result += (double) 1 / 2;
                                    }
                                }
                            }
                            if (answers.length > 2) {
                                for (int i = 0; i < answers.length; i++) {
                                    lineAnswer = answers[i].split(" ");
                                    if (lineAnswer[1].equals("1")) { //numarul de raspunsuri corecte
                                        correctAnswers++;
                                    } else { //numarul de raspunsuri gresite
                                        wrongAnswers++;
                                    }
                                }
                                //punctajul pentru un raspuns corect
                                pointsCorrectAnswers = (double) 1 / correctAnswers;
                                //punctajul pentru un raspuns gresit
                                pointsWrongAnswers = (double) 1 / wrongAnswers;
                                for (int i = 0; i < answers.length; i++) {
                                    lineAnswer = answers[i].split(" ");
                                    //daca raspunsul dat de user este corect
                                    if (lineAnswer[2].equals(answerID) && lineAnswer[1].equals("1")) {
                                        result += pointsCorrectAnswers;
                                    }
                                    //daca raspunsul dat de user este gresit
                                    if (lineAnswer[2].equals(answerID) && lineAnswer[1].equals("0")) {
                                        result -= pointsWrongAnswers;
                                    }
                                }
                            }
                        }
                        break;
                    }
                }
            }
        } catch (IOException ioe) {
            System.out.println("An error occurred.");
            ioe.printStackTrace();
        }
        return result;
    }
}
