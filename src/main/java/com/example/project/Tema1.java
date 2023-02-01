package com.example.project;

public class Tema1 {
	private static int submittedQuiz = 0;

	public static void main(final String[] args) {
		if (args == null) {
			System.out.print("Hello world!");
		} else {
			SystemResponse sr = null;
			User user;
			CompletedQuiz completed;
			QuestionnaireSystem handler;
			if (args[0].equals("-create-user")) {
				if (args.length == 1) {
					sr = new SystemResponse("error", "Please provide username");
				}
				if (args.length == 2) {
					sr = getSystemResponseForCreateUserNoPassword(args);
				}
				if (args.length == 3) {
					sr = getSystemResponseForCreateUser(args);
				}
				System.out.println(sr);
			} else if (args[0].equals("-create-question")) {
				//comanda nu primeste niciun user ca argument sau
				//nu primeste parola pentru user
				if (args.length == 1 || args.length == 2) {
					sr = new SystemResponse("error", "You need to be authenticated");
				} else {
					String command1 = args[1], command2 = args[2];
					String username = command1.substring(4, command1.length() - 1);
					String password = command2.substring(4, command2.length() - 1);
					user = new User(username, password);
					//login failed
					sr = getSystemResponseLoginFailed(user);
					//intrebarea primeste user, password, text, type, dar nu raspunsuri
					if (args.length == 5) {
						sr = new SystemResponse("error", "No answer provided");
					}
					if (args.length > 5) {
						//daca intrebarea primeste doar un raspuns sau
						//nu are text sau raspunsul 1 sau 2 nu are descriere/flag
						// (si sunt date doar 2 raspunsuri ca input)
						sr = getSystemResponseForCreateQuestionMissingComponents(args);
						//altfel
						if (sr == null) {
							sr = getSystemResponseForCreateQuestion(args, user);
						}
					}
				}
				System.out.println(sr);
			} else if (args[0].equals("-get-question-id-by-text")) {
				if (args.length == 1 || args.length == 2) {
					sr = new SystemResponse("error", "You need to be authenticated");
				} else {
					String command1 = args[1], command2 = args[2];
					String username = command1.substring(4, command1.length() - 1);
					String password = command2.substring(4, command2.length() - 1);
					user = new User(username, password);
					sr = getSystemResponseLoginFailed(user);
					if (sr == null) {
						if (args.length == 4) {
							sr = getSystemResponseForGetQuestionIDByText(args, user);
						}
					}
				}
				System.out.println(sr);
			} else if (args[0].equals("-get-all-questions")) {
				if (args.length == 1 || args.length == 2) {
					sr = new SystemResponse("error", "You need to be authenticated");
				} else {
					String command1 = args[1], command2 = args[2];
					String username = command1.substring(4, command1.length() - 1);
					String password = command2.substring(4, command2.length() - 1);
					user = new User(username, password);
					sr = getSystemResponseLoginFailed(user);
					if (sr == null) {
						//daca am gasit intrebarile create de user
						if (user.findQuestionsByUser() != null) {
							String message = user.findQuestionsByUser();
							sr = new SystemResponse("ok", message);
						}
					}
				}
				System.out.println(sr);
			} else if (args[0].equals("-create-quizz")) {
				if (args.length == 1 || args.length == 2) {
					sr = new SystemResponse("error", "You need to be authenticated");
				} else {
					String command1 = args[1], command2 = args[2];
					String username = command1.substring(4, command1.length() - 1);
					String password = command2.substring(4, command2.length() - 1);
					user = new User(username, password);
					sr = getSystemResponseLoginFailed(user);
					if (args.length >= 4) {
						sr = getSystemResponseForCreateQuiz(args, user);
					}
				}
				System.out.println(sr);
			} else if (args[0].equals("-get-quizz-by-name")) {
				if (args.length == 1 || args.length == 2) {
					sr = new SystemResponse("error", "You need to be authenticated");
				} else {
					String command1 = args[1], command2 = args[2];
					String username = command1.substring(4, command1.length() - 1);
					String password = command2.substring(4, command2.length() - 1);
					user = new User(username, password);
					sr = getSystemResponseLoginFailed(user);
					if (args.length >= 4) {
						sr = getSystemResponseForGetQuizByName(args, user);
					}
				}
				System.out.println(sr);
			} else if (args[0].equals("-get-all-quizzes")) {
				if (args.length == 1 || args.length == 2) {
					sr = new SystemResponse("error", "You need to be authenticated");
				} else {
					String command1 = args[1], command2 = args[2];
					String username = command1.substring(4, command1.length() - 1);
					String password = command2.substring(4, command2.length() - 1);
					user = new User(username, password);
					sr = getSystemResponseLoginFailed(user);
					if (sr == null) {
						//adaug in fisierul "usersQuizzes.csv"
						if (submittedQuiz == 0) {
							user.addUsersQuizzesStatusCSV();
						}
						//id-ul user-ului cu username-ul respectiv
						String userID = user.checkUserCSV();
						//statusul completarii quiz-urilor pentru user din "usersQuizzes.csv"
						String[] quizStatusesForUser = Quiz.findStatusQuizForUser(userID);
						//afisez id-ul quiz-urilor si numele impreuna cu statusurile
						String message = user.findQuizzesByUser(quizStatusesForUser);
						sr = new SystemResponse("ok", message);
					}
				}
				System.out.println(sr);
			} else if (args[0].equals("-get-quizz-details-by-id")) {
				if (args.length == 1 || args.length == 2) {
					sr = new SystemResponse("error", "You need to be authenticated");
				} else {
					String command1 = args[1], command2 = args[2];
					String username = command1.substring(4, command1.length() - 1);
					String password = command2.substring(4, command2.length() - 1);
					user = new User(username, password);
					sr = getSystemResponseLoginFailed(user);
					if (sr == null) {
						if (args.length == 4) {
							String quizID = args[3].substring(5, args[3].length() - 1);
							//id-urile intrebarilor din chestionarul cu id-ul quizID
							String[] questionIDs = Quiz.findQuizIDCSV(quizID);
							//detaliile tuturor intrebarilor din chestionar
							if (questionIDs != null) {
								String result = Question.findQuestionsIDs(questionIDs);
								sr = new SystemResponse("ok", result);
							}
						}
					}
				}
				System.out.println(sr);
			} else if (args[0].equals("-submit-quizz")) {
				if (args.length == 1 || args.length == 2) {
					sr = new SystemResponse("error", "You need to be authenticated");
				} else {
					String command1 = args[1], command2 = args[2];
					String username = command1.substring(4, command1.length() - 1);
					String password = command2.substring(4, command2.length() - 1);
					user = new User(username, password);
					sr = getSystemResponseLoginFailed(user);
					if (sr == null) {
						if (args.length == 3) {
							sr = new SystemResponse("error", "No quizz identifier was provided");
						} else {
							String quizID = args[3].substring(10, args[3].length() - 1);
							if (Quiz.findQuizIDCSV(quizID) == null) {
								sr = new SystemResponse("error", "No quiz was found");
							} else {
								//cazul in care nu exista raspunsul cu id-ul i
								int ok = 0;
								int numberOfAnswers = args.length - 4;
								String[] answerIDs = new String[numberOfAnswers];
								int index = 4;
								//creez vectori de id-uri de raspunsuri
								for (int i = 0; i < numberOfAnswers; i++) {
									answerIDs[i] = args[index].substring(14, args[index].length() - 1);
									//verifica existenta fiecarui id din vectorul de raspunsuri
									if (Question.checkAnswerIDsCSV(answerIDs[i]) != null) {
										ok = 1;
										String message = Question.checkAnswerIDsCSV(answerIDs[i]);
										sr = new SystemResponse("error", message);
									}
									index++;
								}
								//daca exista toate raspunsurile date de user
								if (ok == 0) {
									//daca utilizatorul vrea sa raspunda la propriul chestionar
									if (user.checkQuizUserCSV(quizID) != null) {
										String message = user.checkQuizUserCSV(quizID);
										sr = new SystemResponse("error", message);
									} else {
										//creez fisierul "usersQuizzes.csv" in care retin
										//id-urile user-ilor cu id-urile tuturor quiz-urilor
										//si statusul de completare
										if (submittedQuiz == 0) {
											user.addUsersQuizzesStatusCSV();
										}
										handler = new QuestionnaireSystem();
										//daca s-au adaugat useri noi, updatez fisierul "usersQuizzes.csv"
										if (handler.countLinesFile("usersQuizzes.csv") < User.getId()) {
											int lines = handler.countLinesFile("usersQuizzes.csv");
											user.updateUsersInQuizzesStatusCSV(lines);
										}
										//daca s-au adaugat quiz-uri noi, updatez fisierul "usersQuizzes.csv"
										if (handler.countLinesFile("quizzes.csv") < Quiz.getId()) {
											int lines = handler.countLinesFile("quizzes.csv");
											String oldStatuses = handler.returnAllStatusQuizForUsers("usersQuizzes.csv");
											//adaug id-ul noilor quiz-uri cu statusul "False"
											//creez string-ul newStatuses
											String newStatuses = createNewStatusesForQuiz(oldStatuses, lines);
											//updatez fisierul
											handler.updateQuizzesInUsersQuizzes("usersQuizzes.csv", newStatuses);
										}
										String userID = user.checkUserCSV();
										//statusurile quiz-urilor pentru user-ul cu userID: vector de "True"/"False"
										String[] quizStatusesForUser = Quiz.findStatusQuizForUser(userID);
										int quizID2 = Integer.parseInt(quizID);
										//daca s-a completat deja chestionarul cu quizID de user
										if (quizStatusesForUser[quizID2 - 1].equals("True")) {
											sr = new SystemResponse("error", "You already submitted this quizz");
										} else {
											//index-urile quiz-urilor cu statusul de "True" sau "False"
											String quizStatus = handler.returnStatusQuizForUser("usersQuizzes.csv", userID);
											//modific statusul quiz-ului cu id-ul quizID din "False" in "True
											String result = modifyQuizStatusFromFalseToTrue(quizStatus, quizID);
											//updatez fisierul cu statusul nou al quiz-ului
											handler.modifyStatusQuizForUser("usersQuizzes.csv", userID, result);
											//vector cu id-urile intrebarilor pentru chestionar
											String[] questionIDs = Quiz.findQuizIDCSV(quizID);
											if (questionIDs != null) {
												sr = getSystemResponseForSubmitQuiz(user, handler, quizID, answerIDs, questionIDs);
											}
											submittedQuiz++;
										}
									}
								}
							}
						}
					}
				}
				System.out.println(sr);
			} else if (args[0].equals("-delete-quizz-by-id")) {
				if (args.length == 1 || args.length == 2) {
					sr = new SystemResponse("error", "You need to be authenticated");
				} else {
					String command1 = args[1], command2 = args[2];
					String username = command1.substring(4, command1.length() - 1);
					String password = command2.substring(4, command2.length() - 1);
					user = new User(username, password);
					sr = getSystemResponseLoginFailed(user);
					if (sr == null) {
						if (args.length == 3) {
							sr = new SystemResponse("error", "No quizz identifier was provided");
						} else {
							String quizID = args[3].substring(5, args[3].length() - 1);
							if (Quiz.findQuizIDCSV(quizID) == null) {
								sr = new SystemResponse("error", "No quiz was found");
							} else {
								sr = getSystemResponseForDeleteQuizByID(quizID);
							}
						}
					}
				}
				System.out.println(sr);
			} else if (args[0].equals("-get-my-solutions")) {
				if (args.length == 1 || args.length == 2) {
					sr = new SystemResponse("error", "You need to be authenticated");
				} else {
					String command1 = args[1], command2 = args[2];
					String username = command1.substring(4, command1.length() - 1);
					String password = command2.substring(4, command2.length() - 1);
					user = new User(username, password);
					sr = getSystemResponseLoginFailed(user);
					if (sr == null) {
						completed = new CompletedQuiz(user);
						String solutions = completed.findSolutionsForUser();
						sr = new SystemResponse("ok", solutions);
					}
				}
				System.out.println(sr);
			} else if (args[0].equals("-cleanup-all")) {
				User.setId(0);
				Answer.setIdInc(0);
				Question.setId(0);
				Quiz.setId(0);
				CompletedQuiz.setIndexInList(0);
				submittedQuiz = 0;
				//sterg continutul fisierelor create
				handler = new QuestionnaireSystem();
				handler.deleteFileCSV("users.csv");
				handler.deleteFileCSV("questions.csv");
				handler.deleteFileCSV("quizzes.csv");
				handler.deleteFileCSV("usersQuizzes.csv");
				handler.deleteFileCSV("completedQuizzes.csv");
			}
		}
	}

	private static SystemResponse getSystemResponseForCreateUserNoPassword(String[] args) {
		SystemResponse sr;
		String command = args[1];
		String type = command.substring(1, 2); //u sau p
		//daca se incearca parola fara user
		if (type.equals("p")) {
			sr = new SystemResponse("error", "Please provide username");
		} else { //user fara parola
			sr = new SystemResponse("error", "Please provide password");
		}
		return sr;
	}

	private static SystemResponse getSystemResponseForCreateUser(String[] args) {
		SystemResponse sr;
		User user;
		String command1 = args[1];
		String command2 = args[2];
		String username = command1.substring(4, command1.length() - 1);
		String password = command2.substring(4, command2.length() - 1);
		user = new User(username, password);
		if (user.checkUserCSV() != null) {
			sr = new SystemResponse("error", "User already exists");
		} else {
			user.addUserCSV();
			sr = new SystemResponse("ok", "User created successfully");
		}
		return sr;
	}

	private static SystemResponse getSystemResponseLoginFailed(User user) {
		SystemResponse sr = null;
		//daca nu exista user-ul respectiv sau
		//daca parola este gresita la autentificare
		if ((user.checkUserCSV() == null) || (!user.checkUserCredentialsCSV())) {
			sr = new SystemResponse("error", "Login failed");
		}
		return sr;
	}

	private static SystemResponse getSystemResponseForCreateQuestionMissingComponents(String[] args) {
		SystemResponse sr = null;
		if (args.length == 7) {
			if (args[6].charAt(8) == '1') {
				sr = new SystemResponse("error", "Only one answer provided");
			}
		} else if (args.length == 8) {
			if (!args[3].startsWith("-text")) {
				sr = new SystemResponse("error", "No question text provided");
			} else if (!args[5].substring(8, 9).equals(args[6].substring(8, 9))) {
				//inseamna ca args[6] este pentru answer-2
				if (args[5].startsWith("-answer-1-")) {
					sr = new SystemResponse("error", "Answer 1 has no answer description");
				} else {
					sr = new SystemResponse("error", "Answer 1 has no answer correct flag");
				}
			} else if (!args[6].substring(8, 9).equals(args[7].substring(8, 9))) {
				//inseamna ca args[6] este pentru answer-1
				if (args[7].startsWith("answer-2-")) {
					sr = new SystemResponse("error", "Answer 2 as no answer description");
				} else {
					sr = new SystemResponse("error", "Answer 2 has no answer correct flag");
				}
			}
		}
		return sr;
	}

	private static SystemResponse getSystemResponseForCreateQuestion(String[] args, User user) {
		SystemResponse sr;
		Question question;
		if ((user.checkUserCSV() == null) || (!user.checkUserCredentialsCSV())) {
			sr = new SystemResponse("error", "Login failed");
		} else if (!args[3].startsWith("-text")) {
			sr = new SystemResponse("error", "No question text provided");
		} else {
			//intrebare valida
			String command3 = args[3], command4 = args[4];
			String text = command3.substring(7, command3.length() - 1);
			String type = command4.substring(7, command4.length() - 1);
			int numberOfAnswers;
			if (args.length % 2 == 1) {
				numberOfAnswers = (args.length - 5) / 2;
			} else {
				numberOfAnswers = (args.length - 5) / 2 + 1;
			}
			//array de raspunsuri care va contine descrierea raspunsului si flag-ul (0 sau 1)
			//pune null pe pozitia unde nu gaseste descriere/flag
			Answer[] answers = new Answer[numberOfAnswers];
			int i = 5, difference = 4, count = 0;
			for (int j = 0; j < numberOfAnswers; j++) {
				String answerText = null, answerValue = null;
				if (args[i].startsWith("-answer-" + (i - difference) + " ")) {
					answerText = args[i].substring(11, args[i].length() - 1);
				} //altfel e null
				if (args[i + 1] != null) {
					if (args[i + 1].startsWith("-answer-" + (i - difference) + "-")) {
						answerValue = args[i + 1].substring(22, 23);
					}
				} else {
					if (args[i].startsWith("-answer-" + (i - difference) + "-")) {
						answerValue = args[i].substring(22, 23);
					}
				}
				Answer answer = new Answer(answerText, answerValue);
				int idAnswer = Answer.getIdInc();
				answer.setId(idAnswer);
				//pun in vectorul de answers
				answers[j] = answer;
				//daca nu am gasit descriere sau flag pt o intrebare
				//answerText sau answerValue ramane null
				if (answerText == null || answerValue == null) {
					i += 1;
					difference = 4 + count;
				} else {
					i += 2;
					difference = 5 + count;
				}
				count++;
			}
			question = new Question(user, text, type, answers);
			if (numberOfAnswers > 5) {
				sr = new SystemResponse("error", "More than 5 answers were submitted");
			} else if (question.checkSingleQuestionMultipleAnswers()) {
				sr = new SystemResponse("error", "Single correct answer question has more than one correct answer");
			} else if (question.checkDuplicateAnswers()) {
				sr = new SystemResponse("error", "Same answer provided more than once");
			} else if (question.checkAnswerDescriptionsFlags() != null) {
				String message = question.checkAnswerDescriptionsFlags();
				sr = new SystemResponse("error", message);
			} else {
				if (question.checkQuestionCSV() != null) {
					sr = new SystemResponse("error", "Question already exists");
				} else {
					sr = new SystemResponse("ok", "Question added successfully");
					question.addQuestionCSV();
				}
			}
		}
		return sr;
	}

	private static SystemResponse getSystemResponseForGetQuestionIDByText(String[] args, User user) {
		Question question;
		SystemResponse sr;
		String command3 = args[3];
		String text = command3.substring(7, command3.length() - 1);
		question = new Question(user, text);
		int id = question.getQuestionIDCSV();
		if (id == 0) {
			sr = new SystemResponse("error", "Question does not exist");
		} else {
			String strID = Integer.toString(id);
			sr = new SystemResponse("ok", strID);
		}
		return sr;
	}

	private static int[] createQuestionIDsArray(String[] args) {
		int numberOfQuestions = args.length - 4;
		int index = 4;
		int[] questionIDs = new int[numberOfQuestions];
		for (int i = 0; i < numberOfQuestions; i++) {
			String idString = args[index].substring(13, args[index].length() - 1);
			int idInt = Integer.parseInt(idString);
			questionIDs[i] = idInt;
			index++;
		}
		return questionIDs;
	}

	private static SystemResponse getSystemResponseForCreateQuiz(String[] args, User user) {
		Quiz quiz;
		SystemResponse sr;
		int numberOfQuestions = args.length - 4;
		//vector in care retin id-urile intrebarilor puse in quiz-ul creat
		int[] questionIDs = createQuestionIDsArray(args);
		String quizName = args[3].substring(7, args[3].length() - 1);
		quiz = new Quiz(user, quizName, questionIDs);
		if (numberOfQuestions > 10) {
			sr = new SystemResponse("error", "Quizz has more than 10 questions");
		} else if (quiz.checkQuestionIDsCSV() != null) {
			String message = quiz.checkQuestionIDsCSV();
			sr = new SystemResponse("error", message);
		} else {
			if (quiz.checkQuizCSV() != null) {
				sr = new SystemResponse("error", "Quizz name already exists");
			} else {
				quiz.addQuizCSV();
				sr = new SystemResponse("ok", "Quizz added succesfully");
			}
		}
		return sr;
	}

	private static SystemResponse getSystemResponseForGetQuizByName(String[] args, User user) {
		SystemResponse sr;
		Quiz quiz;
		String quizName = args[3].substring(7, args[3].length() - 1);
		quiz = new Quiz(user, quizName);
		if (quiz.checkQuizCSV() == null) {
			sr = new SystemResponse("error", "Quizz does not exist");
		} else {
			String id = quiz.checkQuizCSV();
			sr = new SystemResponse("ok", id);
		}
		return sr;
	}

	private static String createNewStatusesForQuiz(String oldStatuses, int lines) {
		String newStatuses = "/";
		String[] statusesPerUser = oldStatuses.split("/");
		for (int i = 0; i < statusesPerUser.length; i++) {
			for (int j = lines; j < Quiz.getId(); j++) {
				statusesPerUser[i] = statusesPerUser[i].concat((j + 1) + " " + "False" + ";");
			}
			newStatuses = newStatuses.concat(statusesPerUser[i] + "/");
		}
		newStatuses = newStatuses.substring(1);
		return newStatuses;
	}

	private static String modifyQuizStatusFromFalseToTrue(String quizStatus, String quizID) {
		String[] tokens = quizStatus.split(";");
		String[] values;
		String result = null;
		//modific statusul quiz-ului cu id-ul quizID din "False" in "True
		for (int i = 0; i < tokens.length; i++) {
			values = tokens[i].split(" ");
			if (values[0].equals(quizID)) {
				values[1] = "True";
			}
			if (i == 0) {
				result = values[0];
				result = result.concat(" " + values[1] + ";");
			} else {
				result = result.concat(values[0] + " " + values[1] + ";");
			}
		}
		return result;
	}

	private static SystemResponse getSystemResponseForSubmitQuiz(User user, QuestionnaireSystem handler, String quizID, String[] answerIDs, String[] questionIDs) {
		CompletedQuiz completed;
		SystemResponse sr;
		int numberOfQuestionsQuiz = questionIDs.length;
		double percentage = (double) 100 / numberOfQuestionsQuiz;
		double points = 0;
		for (int j = 0; j < questionIDs.length; j++) {
			for (int k = 0; k < answerIDs.length; k++) {
				//raspunsul pentru o intrebare
				double questionResult = Question.calculatePointsPerQuestion(questionIDs[j], answerIDs[k]);
				//adaug la punctajul chestionarului
				points += percentage * questionResult;
			}
		}
		if (points < 0) {
			points = 0;
		}
		int finalPoints = (int) Math.round(points);
		sr = new SystemResponse("ok", finalPoints + " points");
		String quizName = handler.findNameQuizCSV("quizzes.csv", quizID);
		if (points == finalPoints) {
			completed = new CompletedQuiz(user, finalPoints);
		} else {
			completed = new CompletedQuiz(user, points);
		}
		//adaug in "completedQuizzes.csv" detaliile quiz-ului completat
		completed.addQuizCSV(quizID, quizName);
		return sr;
	}

	private static String modifyStatusByDeletingQuiz(String oldStatuses, String quizID) {
		String newStatuses = "/";
		String[] statusesPerUser = oldStatuses.split("/");
		for (int i = 0; i < statusesPerUser.length; i++) {
			String[] indexesAndStatuses = statusesPerUser[i].split(";");
			for (int j = 0; j < indexesAndStatuses.length; j++) {
				String[] indexAndStatus = indexesAndStatuses[j].split(" ");
				if (!indexAndStatus[0].equals(quizID)) {
					//refac string-ul fara id-ul quiz-ului pe care vreau
					//sa-l sterg
					newStatuses = newStatuses.concat(indexAndStatus[0] + " " + indexAndStatus[1] + ";");
				}
			}
			newStatuses = newStatuses.concat("/");
		}
		newStatuses = newStatuses.substring(1);
		return newStatuses;
	}

	private static SystemResponse getSystemResponseForDeleteQuizByID(String quizID) {
		QuestionnaireSystem handler;
		Quiz quiz;
		SystemResponse sr;
		CompletedQuiz completed;
		quiz = new Quiz();
		completed = new CompletedQuiz();
		//sterg quiz-ul din fisierul "quizzes.csv"
		quiz.deleteQuiz(quizID);
		//sterg quiz-ul din fisierul "completedQuizzes.csv"
		completed.deleteQuiz(quizID);
		handler = new QuestionnaireSystem();
		int lines = handler.countLinesFile("usersQuizzes.csv");
		//daca fisierul "usersQuizzes.csv" a fost creat sterg quiz-ul din el
		if (lines != 0) {
			String oldStatuses = handler.returnAllStatusQuizForUsers("usersQuizzes.csv");
			String newStatuses = modifyStatusByDeletingQuiz(oldStatuses, quizID);
			handler.updateQuizzesInUsersQuizzes("usersQuizzes.csv", newStatuses);
		}
		sr = new SystemResponse("ok", "Quizz deleted successfully");
		return sr;
	}
}
