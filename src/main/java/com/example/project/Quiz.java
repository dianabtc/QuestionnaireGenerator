package com.example.project;

import java.io.*;

public class Quiz {
    protected User user;
    private String name;
    private int[] questionIDs;
    private static int id = 0;

    public Quiz() {
    }

    public void setUser(User user) {
        this.user = user;
    }

    public static void setId(int id) {
        Quiz.id = id;
    }

    public static int getId() {
        return id;
    }

    public Quiz(User user, String name, int[] ids) {
        this.user = user;
        this.name = name;
        this.questionIDs = ids;
        id++;
    }

    public Quiz(User user, String name) {
        this.user = user;
        this.name = name;
    }

    /* functie care adauga un chestionar in "quizzes.csv"
    adauga id-ul chestionarului, username-ul si parola user-ului care l-a creat,
    numele chestionarului si id-urile intrebarilor folosite, separate prin " " */
    public void addQuizCSV() {
        try (FileWriter fw = new FileWriter("quizzes.csv", true)) {
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw);
            out.print(id + "," + user.username + "," + user.password + "," + name + ",");
            for (int i = 0; i < questionIDs.length; i++) {
                out.print(questionIDs[i] + " ");
            }
            out.println();
            out.close();
        } catch (IOException ioe) {
            System.out.println("An error occurred.");
            ioe.printStackTrace();
        }
    }

    /* functie care cauta in "questions.csv" id-urile intrebarilor pe care
    utilizatorul vrea sa le puna in chestionar; daca nu gaseste vreo intrebare,
    returneaza mesajul respectiv */
    public String checkQuestionIDsCSV() {
        try (BufferedReader br = new BufferedReader(new FileReader("questions.csv"))) {
            String line;
            String[] lineArray;
            for (int i = 0; i < questionIDs.length; i++) {
                int ok = 0, id = questionIDs[i];
                //parcurg fisierul de intrebari linie cu linie
                //si verific egalitatea cu id-urile din vectorul de questionIDs
                while ((line = br.readLine()) != null) {
                    lineArray = line.split(",");
                    if (lineArray.length > 0) {
                        if (lineArray[0].equals(String.valueOf(id))) {
                            //am gasit id-ul in fisierul de intrebari
                            ok = 1;
                            break;
                        }
                    }
                }
                if (ok == 0) {
                    //nu am gasit intrebarea respectiva
                    return "Question ID for question " + (i+1) + " does not exist";
                }
            }
        } catch (IOException ioe) {
            System.out.println("An error occurred.");
            ioe.printStackTrace();
        }
        return null;
    }

    /* functie care verifica existenta unui chestionar deja in fisierul
    de chestionare dupa numele chestionarului; daca nu exista, returneaza 0
    daca exista, returneaza id-ul */
    public String checkQuizCSV() {
        try (BufferedReader br = new BufferedReader(new FileReader("quizzes.csv"))) {
            String line;
            String[] lineArray;
            while ((line = br.readLine()) != null) {
                lineArray = line.split(",");
                if (lineArray.length > 0) {
                    if (lineArray[3].equals(name)) {
                        return lineArray[0]; //am gasit quiz-ul
                    }
                }
            }
        } catch (IOException ioe) {
            System.out.println("An error occurred.");
            ioe.printStackTrace();
        }
        return null;
    }

    /* functie care construieste un vector de String in care se retine
    statusul completarii tuturor quiz-urilor ale unui user (cauta dupa
    id-ul user-ului */
    public static String[] findStatusQuizForUser(String userID) {
        String[] quizStatuses = new String[id];
        int contor = 0;
        try (BufferedReader br = new BufferedReader(new FileReader("usersQuizzes.csv"))) {
            String line;
            String[] lineArray;
            String[] lineArray2;
            String[] lineArray3;
            while ((line = br.readLine()) != null) {
                lineArray = line.split(",");
                if (lineArray.length > 0) {
                    if (lineArray[0].equals(userID)) {
                        lineArray2 = lineArray[1].split(";");
                        for (int i = 0; i < lineArray2.length; i++) {
                            lineArray3 = lineArray2[i].split(" ");
                            quizStatuses[contor] = lineArray3[1]; //"True" sau "False"
                            contor++;
                        }
                    }
                }
            }
        } catch (IOException ioe) {
            System.out.println("An error occurred.");
            ioe.printStackTrace();
        }
        return quizStatuses;
    }

    /* functie care returneaza un vector de Strings format din id-urile
    intrebarilor din quiz */
    public static String[] findQuizIDCSV(String id) {
        try (BufferedReader br = new BufferedReader(new FileReader("quizzes.csv"))) {
            String line;
            String[] lineArray;
            while ((line = br.readLine()) != null) {
                lineArray = line.split(",");
                if (lineArray.length > 0) {
                    if (lineArray[0].equals(id)) {
                        //questionIDs
                        return lineArray[4].split(" ");
                    }
                }
            }
        } catch (IOException ioe) {
            System.out.println("An error occurred.");
            ioe.printStackTrace();
        }
        return null; //nu am gasit quiz cu id-ul respectiv
    }

    /* functie care cauta in fisierul "quizzes.csv" un chestionar
    dupa id si il sterge */
    public void deleteQuiz(String quizID) {
        try {
            FileInputStream fstream = new FileInputStream("quizzes.csv");
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
            String strLine;
            StringBuilder fileContent = new StringBuilder();
            while ((strLine = br.readLine()) != null) {
                String[] tokens = strLine.split(",");
                if (tokens.length > 0) {
                    if (tokens[0].equals(quizID)) {
                        String newLine = "Deleted quiz";
                        fileContent.append(newLine);
                        fileContent.append("\n");
                    } else {
                        fileContent.append(strLine);
                        fileContent.append("\n");
                    }
                }
            }
            FileWriter fstreamWrite = new FileWriter("quizzes.csv");
            BufferedWriter out = new BufferedWriter(fstreamWrite);
            out.write(fileContent.toString());
            out.close();
            fstream.close();
        } catch (IOException ioe) {
            System.out.println("An error occurred.");
            ioe.printStackTrace();
        }
    }
}
