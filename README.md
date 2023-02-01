# QuestionnaireGenerator
Proiectul prezinta implementarea unui generator de chestionare, unde userii se autentifica si
pot sa creeze sau sa rezolve un chestionar in stilul grilei franceze si sa-si vada
punctajele obtinute pe chestionarele completate.

Am stocat informatiile in fisiere, astfel:

-"users.csv": username,password,id

-"questions.csv": id,username,password,text,type,answers(text,value,id pt fiecare
element din vector)

-"quizzes.csv": id,username,password,name,int[] questionids

-"usersQuizzes.csv": pentru fiecare user din sistem -> toate chestionarele din sistem
cu statusul de "True" sau "False"

-"completedQuizzes.csv": username,id,name,score,index in cadrul chestionarelor 
completate

Cu ajutorul clasei SystemResponse care contine campurile "status" si "message"
am reusit sa afisez output-ul comenzilor de manevrare a sistemului:

->create-user

i) cazul in care utilizatorul nu furnizeaza username (args.length=1)
ii) cazul in care utilizatorul nu furnizeaza parola (args.length=2)
iii) daca utilizatorul furnizeaza datele necesare, verific mai intai existenta
utilizatorului in fisierul "users.csv" si daca nu exista, il adaug cu succes

->create-question
i) cazurile in care utilizatorul nu furnizeaza niciun username sau eventual nu
furnizeaza parola pentru username (args.length=1|2)
ii) cazul in care nu exista user-ul respectiv sau parola este gresita pentru
user-functia checkUserCredentialsCSV()
iii) intrebarea nu primeste niciun raspuns (args.length=5)
iv) intrebarea are doar un raspuns/nu are text sau raspunsul 1 sau 2 nu are
descriere/flag => getSystemResponseForCreateQuestionMissingComponents
v) daca nu are loc iv), verific ii) si existenta textului intrebarii; construiesc
vectorul de raspunsuri unde fiecare element este format din textul intrebarii si
valoarea de adevar; in caz ca nu exista, campul respectiv ramane null. Verific
cazurile: mai mult de 5 raspunsuri oferite, o intrebare de tip 'single' are
mai multe raspunsuri corecte, raspunsuri duplicate, existenta textului si flag-ului
pentru fiecare raspuns. Verific si daca intrebarea exista deja in sistem; daca nu,
o adaug si cresc incrementez id-ul

->get-question-id-by-test: obtin id-ul intrebarii respective-getQuestionIDCSV() 
si daca este 0, intrebarea nu exista in sistem; altfel, afisez id-ul

->create-quiz: creez vectorul format din id-urile intrebarilor pentru quiz;
verific daca sunt mai mult de 10 intrebari, existenta intrebarilor cu id-ul respectiv
in fisierul "questions.csv", existenta unui quiz cu acelasi nume; altfel, 
adaug quiz-ul.

->get-quiz-by-name: verific existenta quiz-ului in fisier; daca exista, ii afisez id-ul

->get-all-quizzes: creez fisierul "usersQuizzes.csv" daca nu a fost nicio intrebare submitted
pana atunci; aflu id-ul user-ului cu username-ul respectiv si statusul completarii
quiz-urilor pentru el: String []findStatusQuizForUser(userID); cu functia 
findQuizzesByUser afisez detaliile quiz-urilor impreuna cu statusul de True/False.

->get-quiz-details-by-id: gasesc vectorul de id-uri ale intrebarilor quiz-ului cu 
id-ul respectiv cu functia si afisez detaliile intrebarilor din chestionar

->submit-quiz: verific daca utilizatorul introduce id-ul quiz-ului pe care vrea sa il rezolve
(args.length=3); verific existenta quiz-ului; creez vectorul de ids de raspunsuri si verific
existenta fiecarui id in fisierul "questions.csv"; daca existatoate raspunsurile in sistem, 
creez fisierul "usersQuizzes.csv"; daca s-au adaugat useri sau quiz-uri noi updatez 
fisierul; daca utilizatorul vrea sa raspunda la un chestionar la care a raspuns deja
(statusul e True) => eroare; altfel, modific statusul quiz-ului din False in True 
getSystemResponseForSubmitQuiz: calculez raspunsul la fiecare intrebare din 
chestionar (pe care o gasesc dupa id), si il adaug la punctajul chestionarului; 
adaug quiz-ul completat in "completedQuizzes.csv".

->delete-quiz-by-id: verific daca utilizatorul introduce id-ul quiz-ului si ulterior daca exista
quiz-ul; il sterg din fisierele create iar din "usersQuizzes.csv" sterg de la fiecare user in parte
id-ul quiz-ului cu statusul.

->get-my-solutions: afisez detaliile quiz-ului completat de catre user din "completedQuizzes.csv"

->cleanup-all: setez toate campurile statice cu 0 si sterg continutul fisierelor.

In plus, ma folosesc si de clasa QuestionnaireSystem ce implementeaza interfata
QuestionnaireHandler, cu ajutorul careia manipulez operatii de fisiere: numar linii,
sterg continutul, returnez anumite detalii.
