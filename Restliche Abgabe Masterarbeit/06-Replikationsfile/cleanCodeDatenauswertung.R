# Bibliotheken müssen zuerst mit install.packages("libraryName") installiert werden.

# 1. Laden der Bibliotheken (vorige Installation beachten)
library(RMariaDB)
library(DBI)
library (data.table)
library(psych)
library(ggplot2)
library(stargazer)

# 2. Einstellungen zur MySQL-Datenbank 
# (vorher RohdatenLesbarkeitsstudie+Tool.sql-File in PHPMyAdmin auf der Datenbank ausführen)
# Benutzername, Passwort, Hostname und Port eingeben
db_mySQL_cc_survey_user <- ''
db_mySQL_cc_survey_password <- ''
db_mySQL_cc_survey_name <- ''
db_mySQL_cc_survey_table_control_variables_survey <- 't_control_variables_survey'
db_mySQL_cc_survey_table_cc_scores_final <- 't_cc_scores_final'
db_mySQL_cc_survey_host <- ''
db_mySQL_cc_survey_port <- 


# 3. Lese die Daten aus der Datenbank.
con_sql <- dbConnect(RMariaDB::MariaDB(), user = db_mySQL_cc_survey_user, password = db_mySQL_cc_survey_password,
                   dbname = db_mySQL_cc_survey_name, host = db_mySQL_cc_survey_host, port = db_mySQL_cc_survey_port)

# Lese MySQL Tabelle in den Dataframe (Die Clean-Code-Lesbarkeitsstudie).
t_control_variables_survey <- dbReadTable(conn = con_sql, name = db_mySQL_cc_survey_table_control_variables_survey)
t_cc_scores_final <- dbReadTable(conn = con_sql, name = db_mySQL_cc_survey_table_cc_scores_final)

# Verbindung zur Datenbank lösen
on.exit(dbDisconnect(con_sql))

# 4. Deskriptive Statistik
# Kontrollvariablen
summary(t_control_variables_survey$interview_duration)
sd(t_control_variables_survey$interview_duration)
table(gender = t_control_variables_survey$gender)
summary(t_control_variables_survey$age)
sd(t_control_variables_survey$age)
table(advertised_at = t_control_variables_survey$advertised_at)
summary(t_control_variables_survey$years_of_programming)
sd(t_control_variables_survey$years_of_programming)
table(familiar_with_oop = t_control_variables_survey$familiar_with_oop)
table(familiar_with_cc = t_control_variables_survey$familiar_with_cc)
summary(t_control_variables_survey$years_of_cpp_programming)
sd(t_control_variables_survey$years_of_cpp_programming)

# Codebeispiele Umfrage
# Code-Beispiel 1
summary(t_cc_scores_final$vars_score_survey[t_cc_scores_final$code_example == 1])
sd(t_cc_scores_final$vars_score_survey[t_cc_scores_final$code_example == 1])
summary(t_cc_scores_final$methods_score_survey[t_cc_scores_final$code_example == 1])
sd(t_cc_scores_final$methods_score_survey[t_cc_scores_final$code_example == 1])
summary(t_cc_scores_final$class_score_survey[t_cc_scores_final$code_example == 1])
sd(t_cc_scores_final$class_score_survey[t_cc_scores_final$code_example == 1])
summary(t_cc_scores_final$general_score_survey[t_cc_scores_final$code_example == 1])
sd(t_cc_scores_final$general_score_survey[t_cc_scores_final$code_example == 1])

# Code-Beispiel 2
summary(t_cc_scores_final$vars_score_survey[t_cc_scores_final$code_example == 2])
sd(t_cc_scores_final$vars_score_survey[t_cc_scores_final$code_example == 2])
summary(t_cc_scores_final$methods_score_survey[t_cc_scores_final$code_example == 2])
sd(t_cc_scores_final$methods_score_survey[t_cc_scores_final$code_example == 2])
summary(t_cc_scores_final$class_score_survey[t_cc_scores_final$code_example == 2])
sd(t_cc_scores_final$class_score_survey[t_cc_scores_final$code_example == 2])
summary(t_cc_scores_final$general_score_survey[t_cc_scores_final$code_example == 2])
sd(t_cc_scores_final$general_score_survey[t_cc_scores_final$code_example == 2])

# Code-Beispiel 3
summary(t_cc_scores_final$vars_score_survey[t_cc_scores_final$code_example == 3])
sd(t_cc_scores_final$vars_score_survey[t_cc_scores_final$code_example == 3])
summary(t_cc_scores_final$methods_score_survey[t_cc_scores_final$code_example == 3])
sd(t_cc_scores_final$methods_score_survey[t_cc_scores_final$code_example == 3])
summary(t_cc_scores_final$class_score_survey[t_cc_scores_final$code_example == 3])
sd(t_cc_scores_final$class_score_survey[t_cc_scores_final$code_example == 3])
summary(t_cc_scores_final$general_score_survey[t_cc_scores_final$code_example == 3])
sd(t_cc_scores_final$general_score_survey[t_cc_scores_final$code_example == 3])

# Code-Beispiel 4
summary(t_cc_scores_final$vars_score_survey[t_cc_scores_final$code_example == 4])
sd(t_cc_scores_final$vars_score_survey[t_cc_scores_final$code_example == 4])
summary(t_cc_scores_final$methods_score_survey[t_cc_scores_final$code_example == 4])
sd(t_cc_scores_final$methods_score_survey[t_cc_scores_final$code_example == 4])
summary(t_cc_scores_final$class_score_survey[t_cc_scores_final$code_example == 4])
sd(t_cc_scores_final$class_score_survey[t_cc_scores_final$code_example == 4])
summary(t_cc_scores_final$general_score_survey[t_cc_scores_final$code_example == 4])
sd(t_cc_scores_final$general_score_survey[t_cc_scores_final$code_example == 4])

# Code-Beispiel 5
summary(t_cc_scores_final$vars_score_survey[t_cc_scores_final$code_example == 5])
sd(t_cc_scores_final$vars_score_survey[t_cc_scores_final$code_example == 5])
summary(t_cc_scores_final$methods_score_survey[t_cc_scores_final$code_example == 5])
sd(t_cc_scores_final$methods_score_survey[t_cc_scores_final$code_example == 5])
summary(t_cc_scores_final$class_score_survey[t_cc_scores_final$code_example == 5])
sd(t_cc_scores_final$class_score_survey[t_cc_scores_final$code_example == 5])
summary(t_cc_scores_final$general_score_survey[t_cc_scores_final$code_example == 5])
sd(t_cc_scores_final$general_score_survey[t_cc_scores_final$code_example == 5])

# Code-Beispiele Gesamt
summary(t_cc_scores_final$vars_score_survey)
sd(t_cc_scores_final$vars_score_survey)
summary(t_cc_scores_final$methods_score_survey)
sd(t_cc_scores_final$methods_score_survey)
summary(t_cc_scores_final$class_score_survey[t_cc_scores_final$code_example != 4])
sd(t_cc_scores_final$class_score_survey[t_cc_scores_final$code_example != 4])
summary(t_cc_scores_final$general_score_survey)
sd(t_cc_scores_final$general_score_survey)

# Codebeispiele Tool
# Code-Beispiele Gesamt
summary(t_cc_scores_final$vars_score_analyzer)
sd(t_cc_scores_final$vars_score_analyzer)
summary(t_cc_scores_final$methods_score_analyzer)
sd(t_cc_scores_final$methods_score_analyzer)
summary(t_cc_scores_final$class_score_analyzer)
sd(t_cc_scores_final$class_score_analyzer)
summary(t_cc_scores_final$global_score_analyzer)
sd(t_cc_scores_final$global_score_analyzer)
summary(t_cc_scores_final$total_score_analyzer)
sd(t_cc_scores_final$total_score_analyzer)

# 6. Lineare Regressionen
vars_score.lm <- lm(vars_score_analyzer ~ vars_score_survey + interview_duration + gender + age + advertised_at + years_of_programming + familiar_with_oop + familiar_with_cc + years_of_cpp_programming, data = t_cc_scores_final)
methods_score.lm <- lm(methods_score_analyzer ~ methods_score_survey + interview_duration + gender + age + advertised_at + years_of_programming + familiar_with_oop + familiar_with_cc + years_of_cpp_programming, data = t_cc_scores_final)
class_score.lm <- lm(class_score_analyzer ~ class_score_survey + interview_duration + gender + age + advertised_at + years_of_programming + familiar_with_oop + familiar_with_cc + years_of_cpp_programming, data = t_cc_scores_final)
general_score.lm <- lm(total_score_analyzer ~ general_score_survey + interview_duration + gender + age + advertised_at + years_of_programming + familiar_with_oop + familiar_with_cc + years_of_cpp_programming, data = t_cc_scores_final)

# 7. Ausgeben der Regressionsergebnisse in eine Tabelle für LaTex (wurde noch einmal zusammengefasst)
stargazer(vars_score.lm, title = "Regressionsergebnisse Variablen-Modell", style = "default", decimal.mark = ',') 
stargazer(methods_score.lm, title = "Regressionsergebnisse Methoden-Modell", style = "default", decimal.mark = ',') 
stargazer(class_score.lm, title = "Regressionsergebnisse Klassen-Modell", style = "default", decimal.mark = ',') 
stargazer(general_score.lm, title = "Regressionsergebnisse Allgemein-Modell", style = "default", decimal.mark = ',') 