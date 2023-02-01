package com.example.project;

import java.io.*;

public class QuestionnaireSystem implements QuestionnaireHandler {
    public QuestionnaireSystem() {
    }

    /* functie care numara liniile dintr-un fisier */
    public int countLinesFile(String filename) {
        int lines = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            while (reader.readLine() != null) {
                lines++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

    /* functie care returneaza statusul de la toate intrebarile pentru
    toti userii din fisierul "usersQuizzes.csv" cu separatorul "/" */
    public String returnAllStatusQuizForUsers(String filename) {
        String result = "/";
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            String[] lineArray;
            while ((line = br.readLine()) != null) {
                lineArray = line.split(",");
                if (lineArray.length > 0) {
                    result = result.concat(lineArray[1] + "/");
                }
            }
        } catch (IOException ioe) {
            System.out.println("An error occurred.");
            ioe.printStackTrace();
        }
        result = result.substring(1);
        return result;
    }

    /* functie care updateaza fisierul "usersQuizzes.csv" dupa
    adaugarea de quiz-uri noi, adaugand la fiecare user id-ul
    noilor quiz-uri si statusul de "False" */
    public void updateQuizzesInUsersQuizzes(String filename, String newStatuses) {
        int index = 0;
        try {
            FileInputStream fstream = new FileInputStream(filename);
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
            String strLine;
            StringBuilder fileContent = new StringBuilder();
            while ((strLine = br.readLine()) != null) {
                String[] tokens = strLine.split(",");
                if (tokens.length > 0) {
                    if (tokens[0].equals(String.valueOf(index + 1))) {
                        String[] array = newStatuses.split("/");
                        tokens[1] = array[index];
                        String newLine = tokens[0] + "," + tokens[1];
                        fileContent.append(newLine);
                        fileContent.append("\n");
                    } else {
                        fileContent.append(strLine);
                        fileContent.append("\n");
                    }
                }
                index++;
            }
            FileWriter fstreamWrite = new FileWriter(filename);
            BufferedWriter out = new BufferedWriter(fstreamWrite);
            out.write(fileContent.toString());
            out.close();
            fstream.close();
        } catch (IOException ioe) {
            System.out.println("An error occurred.");
            ioe.printStackTrace();
        }
    }

    /* functie care cauta in "usersQuizzes.csv" un user dupa ID
    si returneaza quiz-urile cu statusurile lor */
    public String returnStatusQuizForUser(String filename, String userID) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            String[] lineArray;
            while ((line = br.readLine()) != null) {
                lineArray = line.split(",");
                if (lineArray.length > 0) {
                    if (lineArray[0].equals(userID)) {
                        return lineArray[1];
                    }
                }
            }
        } catch (IOException ioe) {
            System.out.println("An error occurred.");
            ioe.printStackTrace();
        }
        return null;
    }

    /* functie care modifica in "usersQuizzes.csv" statusul quiz-ului
    completat de user-ul cu userID din "False" in "True" */
    public void modifyStatusQuizForUser(String filename, String userID, String result) {
        try {
            FileInputStream fstream = new FileInputStream(filename);
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
            String strLine;
            StringBuilder fileContent = new StringBuilder();
            while ((strLine = br.readLine()) != null) {
                String[] tokens = strLine.split(",");
                if (tokens.length > 0) {
                    if (tokens[0].equals(userID)) {
                        tokens[1] = result;
                        String newLine = tokens[0] + "," + tokens[1];
                        fileContent.append(newLine);
                        fileContent.append("\n");
                    } else {
                        fileContent.append(strLine);
                        fileContent.append("\n");
                    }
                }
            }
            FileWriter fstreamWrite = new FileWriter(filename);
            BufferedWriter out = new BufferedWriter(fstreamWrite);
            out.write(fileContent.toString());
            out.close();
            fstream.close();
        } catch (IOException ioe) {
            System.out.println("An error occurred.");
            ioe.printStackTrace();
        }
    }

    /* functie care gaseste numele unui chestionar in functie de id in "quizzes.csv" */
    public String findNameQuizCSV(String filename, String quizID) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            String[] lineArray;
            while ((line = br.readLine()) != null) {
                lineArray = line.split(",");
                if (lineArray.length > 0) {
                    if (lineArray[0].equals(quizID)) {
                        return lineArray[3];
                    }
                }
            }
        } catch (IOException ioe) {
            System.out.println("An error occurred.");
            ioe.printStackTrace();
        }
        return null; //nu am gasit quiz cu id-ul respectiv
    }

    /* functie care sterge continutul unui fisier CSV */
    public void deleteFileCSV(String filename) {
        try (FileWriter fw = new FileWriter(filename, false)) {
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw);
            out.print("");
            out.close();
        } catch (IOException ioe) {
            System.out.println("An error occurred.");
            ioe.printStackTrace();
        }
    }
}
