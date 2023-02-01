package com.example.project;

public interface QuestionnaireHandler {
    int countLinesFile(String filename);
    String returnAllStatusQuizForUsers(String filename);
    void updateQuizzesInUsersQuizzes(String filename, String newStatuses);
    String returnStatusQuizForUser(String filename, String userID);
    void modifyStatusQuizForUser(String filename, String userID, String result);
    String findNameQuizCSV(String filename, String quizID);
    void deleteFileCSV(String filename);
}
