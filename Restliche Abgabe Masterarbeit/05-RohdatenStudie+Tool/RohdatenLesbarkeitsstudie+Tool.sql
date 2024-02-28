SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+02:00";

-- Tabellenstruktur für Tabelle `t_variables_legend`

CREATE TABLE `t_variables_legend_survey` (
  `VAR_QUESTIONNAIRE` varchar(8) DEFAULT NULL,
  `LABEL_QUESTIONNAIRE` varchar(87) DEFAULT NULL,
  `VAR_SQL` varchar(27) DEFAULT NULL,
  `TYPE_SQL` varchar(16) DEFAULT NULL,
  `TYPE_QUESTIONNAIRE` varchar(7) DEFAULT NULL,
  `INPUT_QUESTIONNAIRE` varchar(9) DEFAULT NULL,
  `QUESTION_QUESTIONNAIRE` varchar(1469) DEFAULT NULL,
  `DESCRIPTION` varchar(174) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Daten für Tabelle `variables_readability-survey_20`
--

INSERT INTO `t_variables_legend_survey` (`VAR_QUESTIONNAIRE`, `LABEL_QUESTIONNAIRE`, `VAR_SQL`, `TYPE_SQL`, `TYPE_QUESTIONNAIRE`, `INPUT_QUESTIONNAIRE`, `QUESTION_QUESTIONNAIRE`, `DESCRIPTION`) VALUES
('CASE', 'Interview-Nummer (fortlaufend)', 'case_id', 'INT', 'METRIC', 'SYSTEM', NULL, NULL),
('SERIAL', 'Seriennummer (sofern verwendet)', NULL, NULL, 'TEXT', 'SYSTEM', NULL, NULL),
('REF', 'Referenz (sofern im Link angegeben)', NULL, NULL, 'TEXT', 'SYSTEM', NULL, NULL),
('QUESTNNR', 'Fragebogen, der im Interview verwendet wurde', NULL, NULL, 'TEXT', 'SYSTEM', NULL, NULL),
('MODE', 'Interview-Modus', NULL, NULL, 'TEXT', 'SYSTEM', NULL, NULL),
('STARTED', 'Zeitpunkt zu dem das Interview begonnen hat (Europe/Berlin)', 'Teil von interview_duration', 'TIMESTAMP', 'TIME', 'SYSTEM', NULL, NULL),
('B002', 'DS', NULL, NULL, 'NOMINAL', 'SELECTION', 'Hiermit erkläre ich, dass ich über die Studie - insbesondere über ihre Ziele, ihren Ablauf samt Dauer und über die Vor- und Nachteile sowie Risiken, die mit der Teilnahme verbunden sein könnten - vollumfänglich aufgeklärt wurde. Die Teilnahmeinformation habe ich gelesen und verstanden. Ich hatte genügend Zeit, um meine Entscheidung zur Studienteilnahme zu überdenken und frei zu treffen. Mir ist bekannt, dass ich jederzeit und ohne Angabe von Gründen meine Einwilligung zur Teilnahme an der Studie zurückziehen kann, ohne dass mir daraus Nachteile entstehen. Ich nehme an der o.g. Studie freiwillig teil. Über den Umgang mit meinen Daten - insbesondere über die Erhebung, Auswertung, Speicherung und Veröffentlichung sowie die Möglichkeiten zur Löschung meiner Daten - wurde ich vollumfänglich aufgeklärt. Die Informationen habe ich gelesen und verstanden. Alle meine Fragen sind zu meiner Zufriedenheit beantwortet worden. Ich hatte genügend Zeit, um meine Entscheidung zum Umgang mit meinen Daten zu überdenken und frei zu treffen. Mir ist bekannt, dass ich meine Einwilligung bis zum Abschluss der Erhebung meiner Daten ohne Angabe von Gründen mit Wirkung für die Zukunft widerrufen kann, ohne dass mir daraus Nachteile entstehen. Ich erkläre mich ausdrücklich und freiwillig, d.h. frei von Zwang und Druck, damit einverstanden, dass meine personenbezogenen Daten im beschriebenen Umfang und zu den beschriebenen Zwecken verarbeitet werden.', NULL),
('GE03', 'G1', 'overall_clearness_item', 'INT', 'NOMINAL', 'SELECTION', '[Klarheit] Lesbarkeit kann so definiert werden, dass sie den mentalen Aufwand beschreibt, der benötigt wird, um den Code zu verstehen. Die Lesbarkeit des vorliegenden Codes ist angemessen.', NULL),
('GE05', 'G2', 'overall_clearness_item', 'INT', 'NOMINAL', 'SELECTION', '[Klarheit] Lesbarkeit kann so definiert werden, dass sie den mentalen Aufwand beschreibt, der benötigt wird, um den Code zu verstehen. Die Lesbarkeit des vorliegenden Codes ist angemessen.', NULL),
('GE07', 'G3', 'overall_clearness_item', 'INT', 'NOMINAL', 'SELECTION', '[Klarheit] Lesbarkeit kann so definiert werden, dass sie den mentalen Aufwand beschreibt, der benötigt wird, um den Code zu verstehen. Die Lesbarkeit des vorliegenden Codes ist angemessen.', NULL),
('GE09', 'G4', 'overall_clearness_item', 'INT', 'NOMINAL', 'SELECTION', '[Klarheit] Lesbarkeit kann so definiert werden, dass sie den mentalen Aufwand beschreibt, der benötigt wird, um den Code zu verstehen. Die Lesbarkeit des vorliegenden Codes ist angemessen.', NULL),
('GE11', 'G5', 'overall_clearness_item', 'INT', 'NOMINAL', 'SELECTION', '[Klarheit] Lesbarkeit kann so definiert werden, dass sie den mentalen Aufwand beschreibt, der benötigt wird, um den Code zu verstehen. Die Lesbarkeit des vorliegenden Codes ist angemessen.', NULL),
('KL03', 'K1', 'naming_class_item', 'INT', 'NOMINAL', 'SELECTION', '[Namensgebung] Der Klassenname ist aussagekräftig und zweckbeschreibend.', NULL),
('KL04', 'K2', 'complexity_class_item', 'INT', 'NOMINAL', 'SELECTION', '[Komplexität] Die Komplexität der Klasse ist passend.', NULL),
('KL05', 'K3', 'clearness_class_item', 'INT', 'NOMINAL', 'SELECTION', '[Klarheit] Die Funktionalitäten der Klasse sind eindeutig.', NULL),
('KL07', 'K4', 'naming_class_item', 'INT', 'NOMINAL', 'SELECTION', '[Namensgebung] Der Klassenname ist aussagekräftig und zweckbeschreibend.', NULL),
('KL08', 'K5', 'complexity_class_item', 'INT', 'NOMINAL', 'SELECTION', '[Komplexität] Die Komplexität der Klasse ist passend.', NULL),
('KL09', 'K6', 'clearness_class_item', 'INT', 'NOMINAL', 'SELECTION', '[Klarheit] Die Funktionalitäten der Klasse sind eindeutig.', NULL),
('KL11', 'K7', 'naming_class_item', 'INT', 'NOMINAL', 'SELECTION', '[Namensgebung] Der Klassenname ist aussagekräftig und zweckbeschreibend.', NULL),
('KL12', 'K8', 'complexity_class_item', 'INT', 'NOMINAL', 'SELECTION', '[Komplexität] Die Komplexität der Klasse ist passend.', NULL),
('KL13', 'K9', 'clearness_class_item', 'INT', 'NOMINAL', 'SELECTION', '[Klarheit] Die Funktionalitäten der Klasse sind eindeutig.', NULL),
('KL15', 'K10', 'naming_class_item', 'INT', 'NOMINAL', 'SELECTION', '[Namensgebung] Der Klassenname ist aussagekräftig und zweckbeschreibend.', NULL),
('KL16', 'K11', 'complexity_class_item', 'INT', 'NOMINAL', 'SELECTION', '[Komplexität] Die Komplexität der Klasse ist passend.', NULL),
('KL17', 'K12', 'clearness_class_item', 'INT', 'NOMINAL', 'SELECTION', '[Klarheit] Die Funktionalitäten der Klasse sind eindeutig.', NULL),
('KV01', 'Gender / Geschlecht', 'gender', 'BOOLEAN', 'NOMINAL', 'SELECTION', 'Was ist Ihr Gender (Geschlecht)?', NULL),
('KV02_01', 'Alter: Alter (in Jahren)', 'age', 'INT', 'TEXT', 'OPEN', 'Wie alt sind Sie?', '1 heißt männlich, 0 heißt weiblich.'),
('KV03', 'Zugang', 'advertised_at', 'INT', 'NOMINAL', 'SELECTION', 'Wie haben Sie von der Studie erfahren?', '0 bedeutet Universität, 1 bedeutet Firma, 2 bedeutet Online-Forum, 3 bedeutet Sonstige.'),
('KV04_01', 'Programmiererfahrung: Programmiererfahrung (in Jahren)', 'years_of_programming', 'VARCHAR, DECIMAL', 'TEXT', 'OPEN', 'Wie viele Jahre Erfahrung in Programmierung besitzen Sie circa?', NULL),
('KV05', 'OOP', 'familiar_with_oop', 'BOOLEAN', 'NOMINAL', 'SELECTION', 'Besitzen Sie bereits Erfahrung in Objektorientierter Programmierung?', '1 bedeutet Objektorientierte Programmierung ist bekannt, 0 bedeutet sie ist unbekannt.'),
('KV06', 'CC', 'familiar_with_cc', 'BOOLEAN', 'NOMINAL', 'SELECTION', 'Kenne Sie bereits das Clean Code Konzept von Robert C. Martin (Uncle Bob)?', '1 bedeutet das Clean-Code-Konzept ist bekannt, 0 bedeutet es ist unbekannt.'),
('KV07_01', 'C++: [01]', 'years_of_cpp_programming', 'VARCHAR, DECIMAL', 'TEXT', 'OPEN', 'Wie viele Jahre Erfahrung besitzen Sie circa in der Programmierung in C++?', NULL),
('ME03', 'M1', 'naming_methods_item1', 'INT', 'NOMINAL', 'SELECTION', '[Namensgebung] Die Methodennamen sind aussagekräftig und zweckbeschreibend.', NULL),
('ME04', 'M2', 'naming_methods_item2', 'INT', 'NOMINAL', 'SELECTION', '[Namensgebung] Die Namensgebung der Methoden ist konsistent.', NULL),
('ME05', 'M3', 'naming_methods_item3', 'INT', 'NOMINAL', 'SELECTION', '[Namensgebung] Die Methodennamen sind gut aussprechbar.', NULL),
('ME06', 'M4', 'formatting_methods_item', 'INT', 'NOMINAL', 'SELECTION', '[Formatierung] Die Einrückungen in den Methoden sind konsistent.', NULL),
('ME07', 'M5', 'clearness_methods_item', 'INT', 'NOMINAL', 'SELECTION', '[Klarheit] Die Funktionalitäten der Methoden sind eindeutig.', NULL),
('ME08', 'M6', 'complexity_methods_item1', 'INT', 'NOMINAL', 'SELECTION', '[Komplexität] Die Methode besitzt keine unerwarteten Effekte abseits der im\nMethodennamen beschriebenen Funktionalitäten.', NULL),
('ME09', 'M7', 'complexity_methods_item2', 'INT', 'NOMINAL', 'SELECTION', '[Komplexität] Die Komplexität der Methoden ist passend.', NULL),
('ME10', 'M8', 'complexity_methods_item3', 'INT', 'NOMINAL', 'SELECTION', '[Komplexität] Die Komplexität der Blöcke (if/else, ..) ist passend.', NULL),
('ME12', 'M9', 'naming_methods_item1', 'INT', 'NOMINAL', 'SELECTION', '[Namensgebung] Die Methodennamen sind aussagekräftig und zweckbeschreibend.', NULL),
('ME13', 'M10', 'naming_methods_item2', 'INT', 'NOMINAL', 'SELECTION', '[Namensgebung] Die Namensgebung der Methoden ist konsistent.', NULL),
('ME14', 'M11', 'naming_methods_item3', 'INT', 'NOMINAL', 'SELECTION', '[Namensgebung] Die Methodennamen sind gut aussprechbar.', NULL),
('ME15', 'M12', 'formatting_methods_item', 'INT', 'NOMINAL', 'SELECTION', '[Formatierung] Die Einrückungen in den Methoden sind konsistent.', NULL),
('ME16', 'M13', 'clearness_methods_item', 'INT', 'NOMINAL', 'SELECTION', '[Klarheit] Die Funktionalitäten der Methoden sind eindeutig.', NULL),
('ME17', 'M14', 'complexity_methods_item1', 'INT', 'NOMINAL', 'SELECTION', '[Komplexität] Die Methode besitzt keine unerwarteten Effekte abseits der im\nMethodennamen beschriebenen Funktionalitäten.', NULL),
('ME18', 'M15', 'complexity_methods_item2', 'INT', 'NOMINAL', 'SELECTION', '[Komplexität] Die Komplexität der Methoden ist passend.', NULL),
('ME19', 'M16', 'complexity_methods_item3', 'INT', 'NOMINAL', 'SELECTION', '[Komplexität] Die Komplexität der Blöcke (if/else, ..) ist passend.', NULL),
('ME21', 'M17', 'naming_methods_item1', 'INT', 'NOMINAL', 'SELECTION', '[Namensgebung] Die Methodennamen sind aussagekräftig und zweckbeschreibend.', NULL),
('ME22', 'M18', 'naming_methods_item2', 'INT', 'NOMINAL', 'SELECTION', '[Namensgebung] Die Namensgebung der Methoden ist konsistent.', NULL),
('ME23', 'M19', 'naming_methods_item3', 'INT', 'NOMINAL', 'SELECTION', '[Namensgebung] Die Methodennamen sind gut aussprechbar.', NULL),
('ME24', 'M20', 'formatting_methods_item', 'INT', 'NOMINAL', 'SELECTION', '[Formatierung] Die Einrückungen in den Methoden sind konsistent.', NULL),
('ME25', 'M21', 'clearness_methods_item', 'INT', 'NOMINAL', 'SELECTION', '[Klarheit] Die Funktionalitäten der Methoden sind eindeutig.', NULL),
('ME26', 'M22', 'complexity_methods_item1', 'INT', 'NOMINAL', 'SELECTION', '[Komplexität] Die Methode besitzt keine unerwarteten Effekte abseits der im\nMethodennamen beschriebenen Funktionalitäten.', NULL),
('ME27', 'M23', 'complexity_methods_item2', 'INT', 'NOMINAL', 'SELECTION', '[Komplexität] Die Komplexität der Methoden ist passend.', NULL),
('ME28', 'M24', 'complexity_methods_item3', 'INT', 'NOMINAL', 'SELECTION', '[Komplexität] Die Komplexität der Blöcke (if/else, ..) ist passend.', NULL),
('ME30', 'M25', 'naming_methods_item1', 'INT', 'NOMINAL', 'SELECTION', '[Namensgebung] Die Methodennamen sind aussagekräftig und zweckbeschreibend.', NULL),
('ME31', 'M26', 'naming_methods_item2', 'INT', 'NOMINAL', 'SELECTION', '[Namensgebung] Die Namensgebung der Methoden ist konsistent.', NULL),
('ME32', 'M27', 'naming_methods_item3', 'INT', 'NOMINAL', 'SELECTION', '[Namensgebung] Die Methodennamen sind gut aussprechbar.', NULL),
('ME33', 'M28', 'formatting_methods_item', 'INT', 'NOMINAL', 'SELECTION', '[Formatierung] Die Einrückungen in den Methoden sind konsistent.', NULL),
('ME34', 'M29', 'clearness_methods_item', 'INT', 'NOMINAL', 'SELECTION', '[Klarheit] Die Funktionalitäten der Methoden sind eindeutig.', NULL),
('ME35', 'M30', 'complexity_methods_item1', 'INT', 'NOMINAL', 'SELECTION', '[Komplexität] Die Methode besitzt keine unerwarteten Effekte abseits der im\nMethodennamen beschriebenen Funktionalitäten.', NULL),
('ME36', 'M31', 'complexity_methods_item2', 'INT', 'NOMINAL', 'SELECTION', '[Komplexität] Die Komplexität der Methoden ist passend.', NULL),
('ME37', 'M32', 'complexity_methods_item3', 'INT', 'NOMINAL', 'SELECTION', '[Komplexität] Die Komplexität der Blöcke (if/else, ..) ist passend.', NULL),
('ME39', 'M33', 'naming_methods_item1', 'INT', 'NOMINAL', 'SELECTION', '[Namensgebung] Die Methodennamen sind aussagekräftig und zweckbeschreibend.', NULL),
('ME40', 'M34', 'naming_methods_item2', 'INT', 'NOMINAL', 'SELECTION', '[Namensgebung] Die Namensgebung der Methoden ist konsistent.', NULL),
('ME41', 'M35', 'naming_methods_item3', 'INT', 'NOMINAL', 'SELECTION', '[Namensgebung] Die Methodennamen sind gut aussprechbar.', NULL),
('ME42', 'M36', 'formatting_methods_item', 'INT', 'NOMINAL', 'SELECTION', '[Formatierung] Die Einrückungen in den Methoden sind konsistent.', NULL),
('ME43', 'M37', 'clearness_methods_item', 'INT', 'NOMINAL', 'SELECTION', '[Klarheit] Die Funktionalitäten der Methoden sind eindeutig.', NULL),
('ME44', 'M38', 'complexity_methods_item1', 'INT', 'NOMINAL', 'SELECTION', '[Komplexität] Die Methode besitzt keine unerwarteten Effekte abseits der im\nMethodennamen beschriebenen Funktionalitäten.', NULL),
('ME45', 'M39', 'complexity_methods_item2', 'INT', 'NOMINAL', 'SELECTION', '[Komplexität] Die Komplexität der Methoden ist passend.', NULL),
('ME46', 'M40', 'complexity_methods_item3', 'INT', 'NOMINAL', 'SELECTION', '[Komplexität] Die Komplexität der Blöcke (if/else, ..) ist passend.', NULL),
('VA03', 'V1', 'naming_vars_item1', 'INT', 'NOMINAL', 'SELECTION', '[Namensgebung] Die Variablennamen sind aussagekräftig und zweckbeschreibend.', NULL),
('VA04', 'V2', 'naming_vars_item2', 'INT', 'NOMINAL', 'SELECTION', '[Namensgebung] Die Variablennamen sind gut aussprechbar.', NULL),
('VA05', 'V3', 'naming_vars_item3', 'INT', 'NOMINAL', 'SELECTION', '[Namensgebung] Die Variablennamen sind in einer Entwicklungsumgebung / einem Editor gut suchbar.', NULL),
('VA06', 'V4', 'naming_vars_item4', 'INT', 'NOMINAL', 'SELECTION', '[Namensgebung] Die Namensgebung der Variablen ist konsistent.', NULL),
('VA08', 'V5', 'naming_vars_item1', 'INT', 'NOMINAL', 'SELECTION', '[Namensgebung] Die Variablennamen sind aussagekräftig und zweckbeschreibend.', NULL),
('VA09', 'V6', 'naming_vars_item2', 'INT', 'NOMINAL', 'SELECTION', '[Namensgebung] Die Variablennamen sind gut aussprechbar.', NULL),
('VA10', 'V7', 'naming_vars_item3', 'INT', 'NOMINAL', 'SELECTION', '[Namensgebung] Die Variablennamen sind in einer Entwicklungsumgebung / einem Editor gut suchbar.', NULL),
('VA11', 'V8', 'naming_vars_item4', 'INT', 'NOMINAL', 'SELECTION', '[Namensgebung] Die Namensgebung der Variablen ist konsistent.', NULL),
('VA13', 'V9', 'naming_vars_item1', 'INT', 'NOMINAL', 'SELECTION', '[Namensgebung] Die Variablennamen sind aussagekräftig und zweckbeschreibend.', NULL),
('VA14', 'V10', 'naming_vars_item2', 'INT', 'NOMINAL', 'SELECTION', '[Namensgebung] Die Variablennamen sind gut aussprechbar.', NULL),
('VA15', 'V11', 'naming_vars_item3', 'INT', 'NOMINAL', 'SELECTION', '[Namensgebung] Die Variablennamen sind in einer Entwicklungsumgebung / einem Editor gut suchbar.', NULL),
('VA16', 'V12', 'naming_vars_item4', 'INT', 'NOMINAL', 'SELECTION', '[Namensgebung] Die Namensgebung der Variablen ist konsistent.', NULL),
('VA18', 'V13', 'naming_vars_item1', 'INT', 'NOMINAL', 'SELECTION', '[Namensgebung] Die Variablennamen sind aussagekräftig und zweckbeschreibend.', NULL),
('VA19', 'V14', 'naming_vars_item2', 'INT', 'NOMINAL', 'SELECTION', '[Namensgebung] Die Variablennamen sind gut aussprechbar.', NULL),
('VA20', 'V15', 'naming_vars_item3', 'INT', 'NOMINAL', 'SELECTION', '[Namensgebung] Die Variablennamen sind in einer Entwicklungsumgebung / einem Editor gut suchbar.', NULL),
('VA21', 'V16', 'naming_vars_item4', 'INT', 'NOMINAL', 'SELECTION', '[Namensgebung] Die Namensgebung der Variablen ist konsistent.', NULL),
('VA23', 'V17', 'naming_vars_item1', 'INT', 'NOMINAL', 'SELECTION', '[Namensgebung] Die Variablennamen sind aussagekräftig und zweckbeschreibend.', NULL),
('VA24', 'V18', 'naming_vars_item2', 'INT', 'NOMINAL', 'SELECTION', '[Namensgebung] Die Variablennamen sind gut aussprechbar.', NULL),
('VA25', 'V19', 'naming_vars_item3', 'INT', 'NOMINAL', 'SELECTION', '[Namensgebung] Die Variablennamen sind in einer Entwicklungsumgebung / einem Editor gut suchbar.', NULL),
('VA26', 'V20', 'naming_vars_item4', 'INT', 'NOMINAL', 'SELECTION', '[Namensgebung] Die Namensgebung der Variablen ist konsistent.', NULL),
('TIME001', 'Verweildauer Seite 1', NULL, NULL, 'METRIC', 'SYSTEM', NULL, NULL),
('TIME002', 'Verweildauer Seite 2', NULL, NULL, 'METRIC', 'SYSTEM', NULL, NULL),
('TIME003', 'Verweildauer Seite 3', NULL, NULL, 'METRIC', 'SYSTEM', NULL, NULL),
('TIME004', 'Verweildauer Seite 4', NULL, NULL, 'METRIC', 'SYSTEM', NULL, NULL),
('TIME005', 'Verweildauer Seite 5', NULL, NULL, 'METRIC', 'SYSTEM', NULL, NULL),
('TIME006', 'Verweildauer Seite 6', NULL, NULL, 'METRIC', 'SYSTEM', NULL, NULL),
('TIME007', 'Verweildauer Seite 7', NULL, NULL, 'METRIC', 'SYSTEM', NULL, NULL),
('TIME008', 'Verweildauer Seite 8', NULL, NULL, 'METRIC', 'SYSTEM', NULL, NULL),
('TIME009', 'Verweildauer Seite 9', NULL, NULL, 'METRIC', 'SYSTEM', NULL, NULL),
('TIME010', 'Verweildauer Seite 10', NULL, NULL, 'METRIC', 'SYSTEM', NULL, NULL),
('TIME011', 'Verweildauer Seite 11', NULL, NULL, 'METRIC', 'SYSTEM', NULL, NULL),
('TIME012', 'Verweildauer Seite 12', NULL, NULL, 'METRIC', 'SYSTEM', NULL, NULL),
('TIME013', 'Verweildauer Seite 13', NULL, NULL, 'METRIC', 'SYSTEM', NULL, NULL),
('TIME014', 'Verweildauer Seite 14', NULL, NULL, 'METRIC', 'SYSTEM', NULL, NULL),
('TIME015', 'Verweildauer Seite 15', NULL, NULL, 'METRIC', 'SYSTEM', NULL, NULL),
('TIME016', 'Verweildauer Seite 16', NULL, NULL, 'METRIC', 'SYSTEM', NULL, NULL),
('TIME017', 'Verweildauer Seite 17', NULL, NULL, 'METRIC', 'SYSTEM', NULL, NULL),
('TIME018', 'Verweildauer Seite 18', NULL, NULL, 'METRIC', 'SYSTEM', NULL, NULL),
('TIME019', 'Verweildauer Seite 19', NULL, NULL, 'METRIC', 'SYSTEM', NULL, NULL),
('TIME020', 'Verweildauer Seite 20', NULL, NULL, 'METRIC', 'SYSTEM', NULL, NULL),
('TIME021', 'Verweildauer Seite 21', NULL, NULL, 'METRIC', 'SYSTEM', NULL, NULL),
('TIME022', 'Verweildauer Seite 22', NULL, NULL, 'METRIC', 'SYSTEM', NULL, NULL),
('TIME023', 'Verweildauer Seite 23', NULL, NULL, 'METRIC', 'SYSTEM', NULL, NULL),
('TIME024', 'Verweildauer Seite 24', NULL, NULL, 'METRIC', 'SYSTEM', NULL, NULL),
('TIME025', 'Verweildauer Seite 25', NULL, NULL, 'METRIC', 'SYSTEM', NULL, NULL),
('TIME026', 'Verweildauer Seite 26', NULL, NULL, 'METRIC', 'SYSTEM', NULL, NULL),
('TIME027', 'Verweildauer Seite 27', NULL, NULL, 'METRIC', 'SYSTEM', NULL, NULL),
('TIME_SUM', 'Verweildauer gesamt (ohne Ausreißer)', NULL, NULL, 'METRIC', 'SYSTEM', NULL, NULL),
('MAILSENT', 'Versandzeitpunkt der Einladungsmail (nur für nicht-anonyme Adressaten)', NULL, NULL, 'TIME', 'SYSTEM', NULL, NULL),
('LASTDATA', 'Zeitpunkt als der Datensatz das letzte mal geändert wurde', 'Teil von interview_duration', 'TIMESTAMP', 'TIME', 'SYSTEM', NULL, NULL),
('FINISHED', 'Wurde die Befragung abgeschlossen (letzte Seite erreicht)?', NULL, NULL, 'BOOL', 'SYSTEM', NULL, NULL),
('Q_VIEWER', 'Hat der Teilnehmer den Fragebogen nur angesehen, ohne die Pflichtfragen zu beantworten?', NULL, NULL, 'BOOL', 'SYSTEM', NULL, NULL),
('LASTPAGE', 'Seite, die der Teilnehmer zuletzt bearbeitet hat', NULL, NULL, 'METRIC', 'SYSTEM', NULL, NULL),
('MAXPAGE', 'Letzte Seite, die im Fragebogen bearbeitet wurde', NULL, NULL, 'METRIC', 'SYSTEM', NULL, NULL),
('MISSING', 'Anteil fehlender Antworten in Prozent', NULL, NULL, 'METRIC', 'SYSTEM', NULL, NULL),
('MISSREL', 'Anteil fehlender Antworten (gewichtet nach Relevanz)', NULL, NULL, 'METRIC', 'SYSTEM', NULL, NULL),
('TIME_RSI', 'Maluspunkte für schnelles Ausfüllen', NULL, NULL, 'METRIC', 'SYSTEM', NULL, NULL),
('DEG_TIME', 'Maluspunkte für schnelles Ausfüllen', NULL, NULL, 'METRIC', 'SYSTEM', NULL, NULL),
(NULL, NULL, 'vars_score', 'DECIMAL', NULL, NULL, NULL, 'Der Parameter ist ein normalisierter Wert zwischen 0 und 1, wobei ein höherer Wert als sauberer bewertete Variablen in dem Codebeispiel meint.'),
(NULL, NULL, 'naming_methods_score', 'DECIMAL', NULL, NULL, NULL, 'Der Parameter ist ein normalisierter Wert zwischen 0 und 1, wobei ein höherer Wert eine als sauberer bewertete Namensgebung der bewerteten Methoden in dem Codebeispiel meint.'),
(NULL, NULL, 'complexity_methods_score', 'DECIMAL', NULL, NULL, NULL, 'Der Parameter ist ein normalisierter Wert zwischen 0 und 1, wobei ein höherer Wert eine als sauberer bewertete Komplexität der bewerteten Methoden in dem Codebeispiel meint.'),
(NULL, NULL, 'methods_score', 'DECIMAL', NULL, NULL, NULL, 'Der Parameter ist ein normalisierter Wert zwischen 0 und 1, wobei ein höherer Wert als sauberer bewertete Methoden in dem Codebeispiel meint.'),
(NULL, NULL, 'class_score', 'DECIMAL', NULL, NULL, NULL, 'Der Parameter ist ein normalisierter Wert zwischen 0 und 1, wobei ein höherer Wert eine als sauberer bewertete Klasse meint.'),
(NULL, NULL, 'general_score', 'DECIMAL', NULL, NULL, NULL, 'Der Parameter ist ein normalisierter Wert zwischen 0 und 1, wobei ein höherer Wert einen als klarer bewerteten Code meint.'),
(NULL, NULL, 'total_score', 'DECIMAL', NULL, NULL, NULL, 'Der Parameter ist ein normalisierter Wert zwischen 0 und 1, wobei ein höherer Wert einen als sauberer bewerteten Code in dem Codebeispiel meint.'),
(NULL, NULL, 'code_example', 'INT', NULL, NULL, NULL, 'Beschreibt, zu welchem Codebeispiel die Daten gehören.'),
(NULL, NULL, 'interview_duration', 'INT', NULL, NULL, NULL, 'Dauer des Interviews in Sekunden. Wird aus den beiden Zeitstempeln LASTDATA minus STARTED berechnet.');
COMMIT;

CREATE TABLE IF NOT EXISTS `t_data_readability_survey` (
	`CASE` INT COMMENT 'Interview-Nummer (fortlaufend)',
	`SERIAL` VARCHAR(3) COMMENT 'Seriennummer (sofern verwendet)',
	`REF` VARCHAR(3) COMMENT 'Referenz (sofern im Link angegeben)',
	`QUESTNNR` VARCHAR(3) COMMENT 'Fragebogen, der im Interview verwendet wurde',
	`MODE` VARCHAR(16) COMMENT 'Interview-Modus',
	`STARTED` DATETIME COMMENT 'Zeitpunkt zu dem das Interview begonnen hat (Europe/Berlin)',
	`B002` INT COMMENT 'DS',
	`GE03` INT COMMENT 'G1',
	`GE05` INT COMMENT 'G2',
	`GE07` INT COMMENT 'G3',
	`GE09` INT COMMENT 'G4',
	`GE11` INT COMMENT 'G5',
	`KL03` INT COMMENT 'K1',
	`KL04` INT COMMENT 'K2',
	`KL05` INT COMMENT 'K3',
	`KL07` INT COMMENT 'K4',
	`KL08` INT COMMENT 'K5',
	`KL09` INT COMMENT 'K6',
	`KL11` INT COMMENT 'K7',
	`KL12` INT COMMENT 'K8',
	`KL13` INT COMMENT 'K9',
	`KL15` INT COMMENT 'K10',
	`KL16` INT COMMENT 'K11',
	`KL17` INT COMMENT 'K12',
	`KV01` INT COMMENT 'Gender / Geschlecht',
	`KV02_01` VARCHAR(3) COMMENT 'Alter: Alter (in Jahren)',
	`KV03` INT COMMENT 'Zugang',
	`KV04_01` VARCHAR(15) COMMENT 'Programmiererfahrung: Programmiererfahrung (in Jahren)',
	`KV05` INT COMMENT 'OOP',
	`KV06` INT COMMENT 'CC',
	`KV07_01` VARCHAR(31) COMMENT 'C++: [01]',
	`ME03` INT COMMENT 'M1',
	`ME04` INT COMMENT 'M2',
	`ME05` INT COMMENT 'M3',
	`ME06` INT COMMENT 'M4',
	`ME07` INT COMMENT 'M5',
	`ME08` INT COMMENT 'M6',
	`ME09` INT COMMENT 'M7',
	`ME10` INT COMMENT 'M8',
	`ME12` INT COMMENT 'M9',
	`ME13` INT COMMENT 'M10',
	`ME14` INT COMMENT 'M11',
	`ME15` INT COMMENT 'M12',
	`ME16` INT COMMENT 'M13',
	`ME17` INT COMMENT 'M14',
	`ME18` INT COMMENT 'M15',
	`ME19` INT COMMENT 'M16',
	`ME21` INT COMMENT 'M17',
	`ME22` INT COMMENT 'M18',
	`ME23` INT COMMENT 'M19',
	`ME24` INT COMMENT 'M20',
	`ME25` INT COMMENT 'M21',
	`ME26` INT COMMENT 'M22',
	`ME27` INT COMMENT 'M23',
	`ME28` INT COMMENT 'M24',
	`ME30` INT COMMENT 'M25',
	`ME31` INT COMMENT 'M26',
	`ME32` INT COMMENT 'M27',
	`ME33` INT COMMENT 'M28',
	`ME34` INT COMMENT 'M29',
	`ME35` INT COMMENT 'M30',
	`ME36` INT COMMENT 'M31',
	`ME37` INT COMMENT 'M32',
	`ME39` INT COMMENT 'M33',
	`ME40` INT COMMENT 'M34',
	`ME41` INT COMMENT 'M35',
	`ME42` INT COMMENT 'M36',
	`ME43` INT COMMENT 'M37',
	`ME44` INT COMMENT 'M38',
	`ME45` INT COMMENT 'M39',
	`ME46` INT COMMENT 'M40',
	`VA03` INT COMMENT 'V1',
	`VA04` INT COMMENT 'V2',
	`VA05` INT COMMENT 'V3',
	`VA06` INT COMMENT 'V4',
	`VA08` INT COMMENT 'V5',
	`VA09` INT COMMENT 'V6',
	`VA10` INT COMMENT 'V7',
	`VA11` INT COMMENT 'V8',
	`VA13` INT COMMENT 'V9',
	`VA14` INT COMMENT 'V10',
	`VA15` INT COMMENT 'V11',
	`VA16` INT COMMENT 'V12',
	`VA18` INT COMMENT 'V13',
	`VA19` INT COMMENT 'V14',
	`VA20` INT COMMENT 'V15',
	`VA21` INT COMMENT 'V16',
	`VA23` INT COMMENT 'V17',
	`VA24` INT COMMENT 'V18',
	`VA25` INT COMMENT 'V19',
	`VA26` INT COMMENT 'V20',
	`TIME001` INT COMMENT 'Verweildauer Seite 1',
	`TIME002` INT COMMENT 'Verweildauer Seite 2',
	`TIME003` INT COMMENT 'Verweildauer Seite 3',
	`TIME004` INT COMMENT 'Verweildauer Seite 4',
	`TIME005` INT COMMENT 'Verweildauer Seite 5',
	`TIME006` INT COMMENT 'Verweildauer Seite 6',
	`TIME007` INT COMMENT 'Verweildauer Seite 7',
	`TIME008` INT COMMENT 'Verweildauer Seite 8',
	`TIME009` INT COMMENT 'Verweildauer Seite 9',
	`TIME010` INT COMMENT 'Verweildauer Seite 10',
	`TIME011` INT COMMENT 'Verweildauer Seite 11',
	`TIME012` INT COMMENT 'Verweildauer Seite 12',
	`TIME013` INT COMMENT 'Verweildauer Seite 13',
	`TIME014` INT COMMENT 'Verweildauer Seite 14',
	`TIME015` INT COMMENT 'Verweildauer Seite 15',
	`TIME016` INT COMMENT 'Verweildauer Seite 16',
	`TIME017` INT COMMENT 'Verweildauer Seite 17',
	`TIME018` INT COMMENT 'Verweildauer Seite 18',
	`TIME019` INT COMMENT 'Verweildauer Seite 19',
	`TIME020` INT COMMENT 'Verweildauer Seite 20',
	`TIME021` INT COMMENT 'Verweildauer Seite 21',
	`TIME022` INT COMMENT 'Verweildauer Seite 22',
	`TIME023` INT COMMENT 'Verweildauer Seite 23',
	`TIME024` INT COMMENT 'Verweildauer Seite 24',
	`TIME025` INT COMMENT 'Verweildauer Seite 25',
	`TIME026` INT COMMENT 'Verweildauer Seite 26',
	`TIME027` INT COMMENT 'Verweildauer Seite 27',
	`TIME_SUM` INT COMMENT 'Verweildauer gesamt (ohne Ausreißer)',
	`MAILSENT` DATETIME COMMENT 'Versandzeitpunkt der Einladungsmail (nur für nicht-anonyme Adressaten)',
	`LASTDATA` DATETIME COMMENT 'Zeitpunkt als der Datensatz das letzte mal geändert wurde',
	`FINISHED` INT(1) COMMENT 'Wurde die Befragung abgeschlossen (letzte Seite erreicht)?',
	`Q_VIEWER` INT(1) COMMENT 'Hat der Teilnehmer den Fragebogen nur angesehen, ohne die Pflichtfragen zu beantworten?',
	`LASTPAGE` INT COMMENT 'Seite, die der Teilnehmer zuletzt bearbeitet hat',
	`MAXPAGE` INT COMMENT 'Letzte Seite, die im Fragebogen bearbeitet wurde',
	`MISSING` INT COMMENT 'Anteil fehlender Antworten in Prozent',
	`MISSREL` INT COMMENT 'Anteil fehlender Antworten (gewichtet nach Relevanz)',
	`TIME_RSI` DECIMAL(24,4) COMMENT 'Maluspunkte für schnelles Ausfüllen',
	`DEG_TIME` INT COMMENT 'Maluspunkte für schnelles Ausfüllen'
)
CHARACTER SET utf8
COLLATE utf8_general_ci;
INSERT INTO t_data_readability_survey (`CASE`, `SERIAL`, `REF`, `QUESTNNR`, `MODE`, `STARTED`, `B002`, `GE03`, `GE05`, `GE07`, `GE09`, `GE11`, `KL03`, `KL04`, `KL05`, `KL07`, `KL08`, `KL09`, `KL11`, `KL12`, `KL13`, `KL15`, `KL16`, `KL17`, `KV01`, `KV02_01`, `KV03`, `KV04_01`, `KV05`, `KV06`, `KV07_01`, `ME03`, `ME04`, `ME05`, `ME06`, `ME07`, `ME08`, `ME09`, `ME10`, `ME12`, `ME13`, `ME14`, `ME15`, `ME16`, `ME17`, `ME18`, `ME19`, `ME21`, `ME22`, `ME23`, `ME24`, `ME25`, `ME26`, `ME27`, `ME28`, `ME30`, `ME31`, `ME32`, `ME33`, `ME34`, `ME35`, `ME36`, `ME37`, `ME39`, `ME40`, `ME41`, `ME42`, `ME43`, `ME44`, `ME45`, `ME46`, `VA03`, `VA04`, `VA05`, `VA06`, `VA08`, `VA09`, `VA10`, `VA11`, `VA13`, `VA14`, `VA15`, `VA16`, `VA18`, `VA19`, `VA20`, `VA21`, `VA23`, `VA24`, `VA25`, `VA26`, `TIME001`, `TIME002`, `TIME003`, `TIME004`, `TIME005`, `TIME006`, `TIME007`, `TIME008`, `TIME009`, `TIME010`, `TIME011`, `TIME012`, `TIME013`, `TIME014`, `TIME015`, `TIME016`, `TIME017`, `TIME018`, `TIME019`, `TIME020`, `TIME021`, `TIME022`, `TIME023`, `TIME024`, `TIME025`, `TIME026`, `TIME027`, `TIME_SUM`, `MAILSENT`, `LASTDATA`, `FINISHED`, `Q_VIEWER`, `LASTPAGE`, `MAXPAGE`, `MISSING`, `MISSREL`, `TIME_RSI`, `DEG_TIME`)
VALUES
(34, '', '', 'CR', 'interview', '2022-07-10 13:42:12', 1, 5, 1, 5, 1, 3, 5, 5, 5, 5, 5, 5, 5, 5, 5, 2, 4, 2, 1, '30', 1, '4', 1, 1, '', 5, 4, 4, 4, 4, 4, 5, 5, 1, 1, 1, 1, 1, 1, 1, 1, 5, 5, 5, 5, 5, 5, 5, 5, 2, 3, 2, 3, 1, 2, 1, 2, 2, 2, 4, 3, 4, 4, 2, 3, 5, 5, 5, 5, 3, 3, 3, 3, 5, 5, 5, 5, 2, 3, 4, 3, 2, 4, 2, 2, 4, 48, 40, 3, 21, 138, 84, 602, 334, 83, 9, 306, 29, 36, 97, 44, 3, 11, 26, 12, 35, 4, 8, 4, 3, 3, 6, 946, NULL, '2022-07-10 14:15:25', 1, 0, 27, 27, 0, 0, 1.23, 24),
(41, '', '', 'CR', 'interview', '2022-07-10 15:35:30', 1, 4, 3, 4, 3, 3, 4, 4, 4, 3, 3, 3, 4, 3, 4, 4, 3, 4, 1, '27', 1, '3', 1, 1, '', 4, 4, 5, 5, 4, 4, 4, 4, 2, 2, 4, 5, 2, 1, 2, 4, 4, 4, 4, 4, 3, 4, 4, 4, 2, 3, 2, 3, 2, 2, 4, 4, 4, 3, 4, 4, 4, 2, 4, 4, 4, 4, 4, 4, 3, 2, 2, 2, 4, 4, 4, 5, 4, 4, 3, 4, 3, 4, 3, 3, 9, 7, 23, 3, 6, 17, 24, 21, 130, 68, 11, 188, 59, 137, 111, 65, 4, 23, 133, 142, 18, 2, 13, 18, 22, 10, 6, 1038, NULL, '2022-07-10 15:56:40', 1, 0, 27, 27, 0, 0, 1.21, 26),
(42, '', '', 'CR', 'interview', '2022-07-10 16:39:40', 1, 5, 3, 4, 5, 3, 5, 4, 5, 5, 5, 5, 5, 3, 2, 2, 4, 2, 1, '31', 4, '15', 1, 1, '', 5, 5, 5, 5, 5, 5, 5, 5, 2, 3, 5, 2, 1, 2, 2, 3, 1, 1, 5, 5, 1, 1, 1, 4, 3, 3, 3, 5, 3, 3, 4, 5, 1, 1, 5, 5, 2, 5, 5, 5, 5, 5, 3, 5, 2, 3, 4, 3, 5, 5, 5, 5, 4, 4, 3, 4, 2, 2, 2, 4, 45, 170, 42, 2, 12, 63, 65, 30, 29, 40, 68, 88, 93, 93, 109, 89, 31, 129, 25, 163, 74, 8, 13, 20, 15, 13, 21, 1144, NULL, '2022-07-10 17:05:31', 1, 0, 27, 27, 0, 0, 0.77, 7),
(43, '', '', 'CR', 'interview', '2022-07-10 17:05:13', 1, 5, 1, 4, 3, 2, 4, 4, 4, 1, 1, 1, 5, 5, 5, 2, 3, 2, 1, '26', 4, '4', 1, 1, '', 5, 4, 5, 5, 5, 5, 5, 5, 1, 1, 2, 5, 1, 1, 1, 1, 5, 5, 5, 5, 5, 5, 5, 5, 2, 3, 2, 1, 1, 1, 1, 1, 2, 2, 2, 5, 2, 4, 3, 3, 4, 5, 4, 4, 1, 1, 2, 1, 3, 5, 4, 3, 4, 2, 4, 4, 1, 2, 3, 2, 31, 971, 54, 79, 16, 87, 36, 58, 98, 40, 30, 70, 46, 78, 59, 46, 5, 14, 6, 12, 11, 4, 8, 4, 5, 5, 6, 864, NULL, '2022-07-10 17:36:32', 1, 0, 27, 27, 0, 0, 1.11, 14),
(49, '', '', 'CR', 'interview', '2022-07-10 20:57:56', 1, 5, 3, 4, 2, 4, 4, 4, 4, 4, 3, 2, 5, 5, 5, 1, 3, 1, 2, '28', 1, '3', 1, 2, '1', 5, 5, 5, 5, 5, 4, 4, 4, 3, 4, 5, 5, 3, 2, 2, 2, 2, 2, 2, 2, 2, 3, 3, 3, 2, 4, 3, 2, 2, 4, 1, 3, 3, 4, 3, 3, 3, 3, 4, 4, 4, 3, 3, 4, 1, 4, 2, 3, 5, 4, 4, 4, 3, 3, 3, 2, 2, 4, 4, 3, 2, 4, 18, 38, 17, 112, 147, 81, 163, 121, 31, 173, 141, 140, 296, 140, 14, 40, 44, 35, 57, 15, 20, 12, 6, 7, 8, 1545, NULL, '2022-07-10 21:29:19', 1, 0, 27, 27, 0, 0, 0.76, 27),
(55, '', '', 'CR', 'interview', '2022-07-11 09:36:13', 1, 4, 4, 4, 4, 4, 5, 5, 5, 5, 5, 5, 5, 5, 5, 4, 4, 4, 1, '64', 1, '41', 1, 1, '32', 5, 5, 5, 5, 5, 5, 5, 5, 4, 3, 3, 3, 3, 3, 3, 3, 5, 5, 5, 5, 5, 5, 5, 5, 3, 3, 3, 3, 3, 3, 3, 3, 2, 2, 2, 2, 2, 2, 2, 2, 5, 5, 5, 5, 2, 3, 2, 2, 5, 5, 5, 5, 5, 5, 5, 5, 4, 4, 5, 5, 8, 18, 49, 3, 4, 30, 62, 48, 24, 39, 4, 28, 46, 36, 36, 33, 2, 9, 15, 11, 14, 3, 12, 7, 12, 6, 7, 566, NULL, '2022-07-11 09:45:39', 1, 0, 27, 27, 0, 0, 1.63, 36),
(60, '', '', 'CR', 'interview', '2022-07-12 07:34:06', 1, 4, 3, 5, 1, 2, 4, 3, 3, 1, 1, 1, 2, 2, 2, 1, 1, 1, 2, '27', 1, '1', 1, 2, '1', 5, 5, 5, 5, 4, 4, 4, 4, 1, 4, 5, 3, 1, 1, 2, 2, 5, 5, 4, 4, 4, 4, 5, 5, 3, 3, 2, 2, 1, 2, 1, 1, 2, 1, 2, 2, 1, 2, 2, 2, 4, 5, 5, 5, 2, 2, 1, 2, 4, 3, 3, 4, 4, 4, 4, 4, 3, 4, 2, 1, 65, 11, 70, 21, 24, 62, 49, 44, 47, 34, 5, 40, 43, 37, 33, 30, 2, 30, 16, 6, 9, 2, 7, 5, 16, 5, 5, 718, NULL, '2022-07-12 07:46:04', 1, 0, 27, 27, 0, 0, 1.42, 29),
(61, '', '', 'CR', 'interview', '2022-07-12 07:34:11', 1, 3, 4, 3, 2, 3, 4, 5, 5, 5, 5, 5, 2, 2, 2, 5, 5, 5, 2, '22', 1, '3/4', 1, 2, '3/4', 4, 4, 3, 5, 4, 4, 4, 4, 2, 3, 3, 4, 3, 3, 3, 3, 5, 4, 2, 3, 3, 3, 3, 3, 4, 4, 3, 2, 3, 3, 3, 3, 4, 4, 4, 4, 3, 3, 3, 3, 4, 4, 4, 4, 2, 3, 4, 3, 4, 3, 4, 3, 4, 4, 4, 4, 4, 4, 4, 4, 9, 45, 54, 5, 22, 66, 71, 83, 55, 23, 9, 86, 68, 36, 56, 38, 28, 19, 18, 20, 9, 6, 11, 12, 8, 8, 5, 846, NULL, '2022-07-12 07:48:41', 1, 0, 27, 27, 0, 0, 1.06, 9),
(62, '', '', 'CR', 'interview', '2022-07-12 07:34:30', 1, 2, 3, 2, 4, 2, 3, 3, 3, 2, 4, 3, 3, 4, 3, 4, 3, 3, 1, '20', 1, '1,5', 2, 2, '1', 3, 4, 3, 4, 3, 3, 3, 3, 4, 4, 4, 5, 3, 4, 3, 4, 4, 4, 3, 4, 4, 3, 4, 4, 3, 3, 3, 4, 3, 3, 2, 3, 3, 2, 2, 2, 3, 3, 3, 3, 2, 3, 2, 3, 1, 2, 1, 2, 4, 5, 5, 4, 3, 2, 2, 3, 2, 2, 3, 2, 39, 14, 54, 6, 18, 43, 27, 36, 41, 42, 21, 49, 41, 45, 47, 41, 4, 18, 18, 7, 8, 3, 8, 11, 7, 7, 4, 659, NULL, '2022-07-12 07:45:31', 1, 0, 27, 27, 0, 0, 1.33, 21),
(82, '', '', 'CR', 'interview', '2022-07-12 11:34:50', 1, 5, 2, 4, 4, 3, 5, 5, 5, 3, 3, 3, 4, 4, 4, 3, 4, 3, 1, '33', 1, '1', 2, 2, '1', 4, 5, 5, 5, 5, 2, 5, 5, 3, 3, 2, 4, 4, 2, 4, 5, 5, 4, 5, 5, 4, 2, 4, 5, 5, 5, 4, 5, 5, 2, 5, 5, 3, 4, 3, 4, 3, 2, 4, 5, 5, 4, 5, 5, 3, 4, 4, 2, 4, 3, 5, 5, 5, 4, 5, 5, 5, 4, 5, 5, 4377, 42, 25, 18, 14, 44, 47, 28, 16, 14, 340, 54, 44, 41, 36, 36, 5, 9, 16, 18, 12, 5, 7, 4, 7, 5, 8, 580, NULL, '2022-07-12 13:02:43', 1, 0, 27, 27, 0, 0, 1.36, 25),
(85, '', '', 'CR', 'interview', '2022-07-12 14:00:49', 1, 4, 1, 2, 1, 2, 5, 3, 5, 5, 3, 5, 3, 5, 4, 2, 3, 1, 1, '20', 1, '1', 1, 2, '0.5', 4, 5, 5, 5, 5, 5, 5, 5, 1, 5, 5, 1, 1, 1, 1, 1, 5, 5, 5, 5, 5, 5, 5, 5, 1, 5, 5, 1, 1, 1, 1, 1, 1, 5, 4, 1, 1, 2, 4, 5, 3, 5, 5, 5, 1, 2, 1, 1, 4, 5, 3, 4, 1, 4, 1, 1, 1, 3, 3, 1, 3, 29, 33, 25, 17, 191, 57, 140, 87, 81, 4, 235, 106, 152, 130, 145, 4, 20, 37, 66, 36, 6, 24, 5, 11, 8, 9, 1392, NULL, '2022-07-12 14:28:31', 1, 0, 27, 27, 0, 0, 0.8, 11),
(89, '', '', 'CR', 'interview', '2022-07-12 21:00:39', 1, 5, 1, 3, 4, 2, 5, 5, 5, 5, 2, 4, 4, 5, 3, 2, 3, 4, 1, '30', 1, '2', 1, 2, '1', 5, 5, 5, 5, 5, 4, 5, 5, 1, 3, 2, 4, 1, 1, 3, 1, 1, 5, 5, 5, 1, 3, 4, 5, 4, 3, 5, 5, 3, 2, 2, 4, 2, 2, 2, 4, 3, 4, 2, 4, 5, 5, 5, 5, 1, 4, 4, 1, 5, 4, 5, 4, 5, 4, 4, 4, 5, 5, 5, 5, 18, 30, 30, 7, 11, 58, 48, 63, 60, 59, 15, 97, 111, 103, 104, 94, 6, 17, 34, 30, 20, 6, 19, 7, 18, 27, 7, 1079, NULL, '2022-07-12 21:18:59', 1, 0, 27, 27, 0, 0, 0.81, 2),
(92, '', '', 'CR', 'interview', '2022-07-12 21:31:18', 1, 5, 1, 5, 3, 2, 5, 5, 5, 5, 5, 5, 5, 5, 5, 3, 3, 3, 1, '30', 4, '5', 1, 2, '3', 5, 5, 5, 5, 5, 5, 5, 5, 1, 1, 1, 1, 1, 1, 1, 1, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 3, 3, 3, 3, 5, 5, 5, 5, 3, 3, 3, 5, 5, 5, 5, 5, 1, 1, 1, 1, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 9, 70, 34, 29, 18, 33, 22, 32, 27, 23, 21, 21, 15, 29, 67, 35, 16, 8, 26, 36, 15, 5, 8, 4, 6, 6, 3, 618, NULL, '2022-07-12 21:41:36', 1, 0, 27, 27, 0, 0, 1.45, 35),
(94, '', '', 'CR', 'interview', '2022-07-12 22:17:32', 1, 4, 4, 3, 3, 4, 3, 4, 4, 3, 4, 3, 4, 4, 3, 4, 2, 3, 2, '29', 2, '3', 1, 2, '0', 3, 4, 4, 4, 3, 4, 3, 4, 4, 3, 4, 4, 1, 3, 2, 3, 2, 3, 4, 3, 4, 4, 4, 3, 3, 3, 4, 4, 4, 3, 4, 3, 2, 2, 3, 4, 3, 3, 4, 4, 4, 4, 3, 4, 4, 2, 2, 2, 4, 2, 3, 2, 2, 3, 2, 3, 4, 4, 2, 3, 5, 12, 17, 3, 1, 12, 8, 5, 7, 6, 3, 9, 10, 9, 7, 19, 2, 4, 5, 4, 3, 2, 2, 3, 3, 2, 3, 166, NULL, '2022-07-12 22:20:18', 1, 0, 27, 27, 0, 0, 2.75, 207),
(95, '', '', 'CR', 'interview', '2022-07-12 22:24:08', 1, 4, 3, 4, 3, 3, 4, 4, 4, 4, 2, 4, 5, 4, 4, 2, 2, 2, 1, '26', 1, '2', 1, 2, '2', 4, 4, 4, 5, 4, 4, 4, 4, 2, 2, 3, 2, 2, 3, 3, 4, 4, 4, 4, 4, 4, 4, 4, 4, 3, 4, 3, 2, 3, 3, 3, 3, 2, 4, 3, 4, 3, 3, 4, 4, 4, 4, 4, 4, 3, 4, 2, 1, 5, 3, 4, 4, 3, 3, 4, 4, 2, 2, 3, 4, 40, 86, 49, 9, 20, 87, 70, 78, 53, 53, 7, 73, 95, 59, 63, 50, 1, 26, 21, 32, 28, 5, 19, 12, 8, 5, 6, 1055, NULL, '2022-07-12 22:41:43', 1, 0, 27, 27, 0, 0, 0.93, 8),
(97, '', '', 'CR', 'interview', '2022-07-13 07:19:12', 1, 4, 2, 3, 2, 3, 4, 4, 4, 2, 4, 2, 4, 2, 3, 3, 4, 3, 1, '22', 1, '1.5', 1, 2, '0.5', 4, 4, 4, 4, 4, 4, 4, 4, 2, 3, 3, 4, 2, 3, 2, 3, 5, 5, 5, 5, 5, 5, 5, 5, 3, 3, 2, 2, 2, 2, 2, 3, 4, 4, 3, 3, 2, 3, 3, 4, 3, 2, 4, 3, 1, 3, 2, 2, 4, 4, 4, 4, 4, 3, 3, 4, 2, 2, 3, 3, 782, 14, 41, 6, 20, 55, 49, 24, 28, 20, 8, 48, 49, 28, 41, 52, 3, 12, 13, 14, 17, 3, 17, 3, 6, 5, 6, 598, NULL, '2022-07-13 07:41:56', 1, 0, 27, 27, 0, 0, 1.41, 23),
(100, '', '', 'CR', 'interview', '2022-07-13 07:20:27', 1, 3, 2, 3, 2, 1, 4, 4, 4, 4, 4, 4, 4, 3, 3, 2, 2, 2, 2, '20', 1, '2', 1, 2, 'Mittelmäßig', 2, 3, 4, 2, 2, 3, 3, 2, 3, 3, 2, 2, 2, 3, 1, 2, 3, 3, 2, 2, 3, 2, 2, 2, 2, 3, 2, 2, 2, 2, 2, 2, 1, 2, 1, 2, 1, 2, 1, 1, 4, 4, 3, 4, 2, 3, 1, 2, 2, 2, 2, 3, 1, 3, 3, 3, 2, 3, 2, 3, 711, 8, 34, 3, 3, 50, 43, 35, 29, 29, 2, 69, 41, 40, 37, 29, 1, 12, 10, 14, 9, 1, 14, 8, 11, 7, 8, 563, NULL, '2022-07-13 07:41:25', 1, 0, 27, 27, 0, 0, 1.67, 50),
(104, '', '', 'CR', 'interview', '2022-07-13 07:28:23', 1, 3, 4, 3, 2, 3, 4, 3, 3, 5, 4, 4, 2, 3, 3, 2, 2, 1, 1, '20', 1, '2', 1, 1, '2', 5, 4, 4, 4, 4, 3, 3, 4, 3, 2, 4, 4, 1, 2, 1, 2, 3, 3, 4, 4, 3, 3, 2, 3, 3, 4, 4, 3, 2, 1, 2, 2, 2, 2, 4, 3, 1, 2, 2, 3, 4, 3, 4, 3, 1, 5, 4, 2, 4, 3, 4, 3, 1, 2, 3, 3, 4, 4, 4, 4, 237, 33, 33, 3, 21, 49, 35, 42, 34, 28, 4, 42, 36, 49, 43, 36, 3, 13, 19, 24, 9, 3, 12, 12, 19, 13, 8, 639, NULL, '2022-07-13 07:42:43', 1, 0, 27, 27, 0, 0, 1.27, 19),
(105, '', '', 'CR', 'interview', '2022-07-13 07:32:13', 1, 4, 1, 3, 2, 2, 4, 4, 4, 4, 3, 1, 4, 3, 4, 2, 3, 1, 1, '21', 1, '1', 1, 2, 'Nicht sonderlich viel ', 4, 4, 3, 5, 3, 3, 3, 3, 2, 1, 3, 4, 1, 2, 1, 1, 4, 4, 5, 5, 4, 4, 5, 4, 4, 3, 3, 4, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 2, 2, 4, 5, 4, 4, 1, 3, 4, 1, 4, 3, 4, 4, 3, 4, 5, 5, 2, 2, 3, 4, 2, 3, 47, 3, 36, 69, 41, 56, 56, 59, 8, 74, 50, 97, 89, 19, 4, 18, 15, 20, 14, 5, 11, 2, 7, 4, 4, 813, NULL, '2022-07-13 07:45:47', 1, 0, 27, 27, 0, 0, 1.37, 43),
(106, '', '', 'CR', 'interview', '2022-07-13 07:32:14', 1, 4, 3, 2, 3, 4, 3, 4, 4, 2, 2, 2, 4, 4, 4, 3, 4, 2, 1, '20', 1, '2', 1, 2, '1', 5, 5, 5, 5, 4, 2, 4, 4, 1, 3, 4, 1, 1, 3, 2, 3, 4, 4, 4, 4, 4, 4, 4, 4, 2, 2, 3, 2, 2, 3, 2, 3, 4, 4, 4, 4, 4, 4, 4, 4, 4, 5, 4, 4, 1, 3, 1, 2, 4, 4, 4, 4, 4, 3, 3, 4, 2, 2, 4, 2, 32, 29, 43, 3, 5, 85, 41, 50, 72, 51, 4, 93, 105, 58, 99, 33, 2, 16, 19, 7, 15, 1, 11, 4, 10, 7, 9, 904, NULL, '2022-07-13 07:47:18', 1, 0, 27, 27, 0, 0, 1.32, 25),
(107, '', '', 'CR', 'interview', '2022-07-13 07:32:15', 1, 4, 1, 5, 1, 3, 5, 5, 5, 2, 2, 2, 5, 5, 5, 5, 5, 5, 1, '20', 1, '4', 1, 2, '1', 5, 5, 5, 5, 5, 5, 5, 5, 1, 3, 4, 1, 1, 1, 1, 1, 5, 5, 5, 2, 4, 4, 4, 4, 2, 4, 4, 2, 2, 3, 2, 2, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 2, 2, 2, 4, 4, 4, 4, 4, 4, 4, 4, 4, 5, 5, 5, 5, 12, 24, 30, 12, 7, 58, 63, 35, 25, 17, 4, 69, 43, 61, 52, 14, 3, 42, 8, 8, 8, 2, 11, 3, 4, 5, 4, 624, NULL, '2022-07-13 07:42:39', 1, 0, 27, 27, 0, 0, 1.66, 37),
(108, '', '', 'CR', 'interview', '2022-07-13 07:32:19', 1, 4, 2, 4, 4, 3, 5, 4, 4, 4, 3, 4, 4, 4, 4, 3, 3, 2, 1, '21', 1, '1,5', 1, 2, '0,5', 4, 5, 5, 5, 2, 3, 4, 4, 3, 5, 5, 4, 2, 3, 1, 2, 3, 2, 3, 5, 4, 5, 5, 4, 4, 4, 2, 3, 2, 2, 3, 2, 2, 4, 1, 3, 3, 3, 4, 4, 4, 5, 5, 5, 3, 3, 4, 4, 4, 4, 4, 4, 4, 5, 3, 3, 3, 4, 4, 4, 5, 16, 56, 19, 14, 57, 40, 78, 59, 47, 7, 88, 107, 95, 66, 43, 4, 40, 17, 19, 20, 1, 8, 7, 13, 5, 4, 935, NULL, '2022-07-13 07:47:54', 1, 0, 27, 27, 0, 0, 1.16, 17),
(109, '', '', 'CR', 'interview', '2022-07-13 07:32:28', 1, 3, 2, 3, 4, 4, 4, 4, 4, 2, 4, 2, 4, 4, 4, 2, 4, 2, 1, '20', 1, '2', 1, 2, '1', 5, 4, 5, 5, 3, 4, 4, 4, 1, 1, 4, 5, 1, 2, 4, 4, 5, 4, 5, 5, 4, 4, 4, 4, 4, 5, 4, 3, 4, 4, 4, 4, 2, 3, 2, 4, 4, 4, 3, 4, 4, 3, 4, 4, 1, 3, 3, 4, 5, 3, 3, 4, 3, 4, 2, 4, 4, 4, 3, 4, 8, 18, 31, 4, 11, 38, 39, 29, 30, 40, 6, 48, 43, 21, 29, 25, 4, 14, 14, 10, 11, 2, 6, 4, 8, 6, 3, 502, NULL, '2022-07-13 07:40:50', 1, 0, 27, 27, 0, 0, 1.68, 35),
(110, '', '', 'CR', 'interview', '2022-07-13 07:32:29', 1, 4, 2, 2, 3, 2, 5, 2, 4, 2, 2, 4, 2, 3, 2, 3, 4, 2, 1, '23', 1, '10', 1, 2, '3', 3, 3, 4, 1, 4, 3, 4, 5, 4, 2, 3, 4, 4, 4, 4, 5, 5, 4, 4, 5, 5, 4, 5, 4, 4, 4, 5, 1, 4, 3, 4, 5, 3, 4, 3, 5, 4, 3, 4, 4, 2, 4, 3, 1, 1, 2, 3, 2, 5, 5, 4, 5, 4, 4, 4, 5, 3, 4, 4, 4, 5, 5, 33, 3, 14, 63, 44, 32, 38, 39, 3, 79, 57, 54, 52, 55, 8, 21, 18, 24, 11, 2, 20, 4, 5, 7, 5, 701, NULL, '2022-07-13 07:44:10', 1, 0, 27, 27, 0, 0, 1.43, 29),
(111, '', '', 'CR', 'interview', '2022-07-13 07:32:30', 1, 5, 2, 4, 1, 4, 5, 4, 5, 1, 1, 1, 5, 2, 4, 5, 5, 5, 1, '21', 1, '6', 1, 2, '0,5', 4, 3, 4, 5, 4, 1, 4, 4, 1, 3, 5, 4, 1, 1, 1, 1, 5, 5, 5, 1, 5, 4, 4, 5, 3, 3, 3, 1, 3, 2, 1, 1, 3, 2, 4, 1, 3, 3, 5, 5, 2, 3, 1, 1, 1, 2, 1, 4, 4, 5, 4, 5, 4, 3, 4, 3, 2, 2, 2, 2, 7, 7, 37, 26, 14, 88, 57, 42, 58, 30, 19, 101, 148, 57, 74, 76, 8, 22, 14, 112, 10, 4, 15, 6, 7, 7, 11, 969, NULL, '2022-07-13 07:50:07', 1, 0, 27, 27, 0, 0, 1.03, 13),
(112, '', '', 'CR', 'interview', '2022-07-13 07:32:44', 1, 5, 1, 4, 3, 2, 5, 5, 5, 5, 4, 4, 5, 5, 5, 5, 5, 5, 1, '20', 1, '1', 1, 2, '0', 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 1, 2, 2, 2, 5, 4, 5, 5, 3, 3, 3, 3, 5, 5, 5, 5, 3, 4, 3, 3, 2, 2, 2, 2, 2, 3, 3, 3, 2, 4, 5, 2, 1, 1, 5, 1, 5, 4, 5, 4, 4, 3, 5, 2, 4, 4, 5, 4, 4, 15, 39, 3, 16, 51, 36, 46, 58, 43, 3, 61, 63, 99, 57, 42, 1, 22, 22, 28, 8, 6, 10, 4, 7, 5, 5, 754, NULL, '2022-07-13 07:45:18', 1, 0, 27, 27, 0, 0, 1.41, 26),
(113, '', '', 'CR', 'interview', '2022-07-13 07:32:49', 1, 5, 1, 2, 3, 3, 2, 3, 1, 4, 2, 4, 4, 2, 1, 4, 4, 1, 1, '19', 1, '2', 1, 2, '0.2', 2, 2, 5, 5, 1, 1, 4, 1, 1, 1, 5, 2, 1, 1, 1, 1, 4, 4, 3, 4, 3, 2, 4, 1, 1, 1, 3, 3, 1, 3, 2, 1, 1, 2, 4, 3, 3, 3, 2, 2, 5, 5, 5, 5, 1, 2, 1, 3, 4, 4, 1, 3, 4, 4, 2, 4, 2, 3, 4, 3, 5, 8, 53, 13, 10, 48, 34, 61, 58, 43, 7, 78, 43, 93, 63, 25, 3, 16, 16, 27, 15, 3, 9, 3, 4, 4, 6, 748, NULL, '2022-07-13 07:45:17', 1, 0, 27, 27, 0, 0, 1.39, 23),
(115, '', '', 'CR', 'interview', '2022-07-13 07:33:00', 1, 4, 2, 2, 2, 4, 4, 4, 4, 4, 4, 3, 4, 3, 3, 2, 2, 3, 1, '20', 1, '1,5', 1, 2, '1', 4, 4, 4, 4, 4, 2, 3, 4, 1, 1, 3, 4, 1, 1, 1, 1, 5, 5, 4, 4, 5, 3, 4, 4, 4, 4, 3, 2, 1, 2, 2, 1, 2, 2, 2, 3, 4, 4, 4, 4, 5, 4, 3, 4, 2, 4, 1, 2, 4, 4, 3, 4, 4, 4, 3, 4, 2, 2, 2, 1, 12, 13, 35, 1, 11, 102, 89, 117, 84, 53, 15, 76, 53, 79, 57, 41, 5, 12, 11, 29, 16, 3, 22, 16, 6, 2, 3, 963, NULL, '2022-07-13 07:49:03', 1, 0, 27, 27, 0, 0, 1.24, 26),
(116, '', '', 'CR', 'interview', '2022-07-13 07:33:13', 1, 4, 1, 2, 2, 4, 5, 4, 4, 5, 3, 2, 3, 3, 2, 4, 5, 4, 1, '20', 1, '5', 1, 2, '0', 5, 5, 5, 5, 5, 5, 5, 5, 1, 2, 5, 5, 1, 2, 1, 1, 5, 5, 5, 5, 4, 5, 5, 5, 1, 2, 5, 4, 1, 2, 1, 1, 4, 4, 5, 5, 4, 4, 3, 4, 5, 5, 5, 5, 1, 4, 3, 2, 4, 5, 4, 4, 3, 5, 4, 5, 4, 5, 4, 3, 9, 9, 42, 6, 19, 59, 85, 74, 63, 50, 5, 65, 75, 48, 44, 41, 3, 11, 15, 20, 24, 5, 5, 3, 9, 6, 4, 799, NULL, '2022-07-13 07:46:32', 1, 0, 27, 27, 0, 0, 1.3, 19),
(118, '', '', 'CR', 'interview', '2022-07-13 07:33:32', 1, 4, 4, 4, 4, 4, 4, 4, 3, 4, 3, 4, 4, 3, 4, 4, 4, 2, 1, '24', 4, '1', 1, 2, '1', 3, 4, 2, 3, 4, 2, 4, 2, 3, 2, 4, 2, 4, 3, 4, 3, 4, 4, 3, 4, 3, 4, 3, 4, 2, 3, 2, 3, 3, 3, 3, 4, 3, 4, 4, 4, 3, 3, 4, 3, 3, 3, 3, 2, 2, 3, 4, 3, 2, 3, 3, 4, 2, 3, 2, 3, 4, 2, 4, 3, 46, 16, 41, 3, 12, 87, 37, 31, 11, 12, 7, 25, 53, 21, 11, 15, 4, 11, 6, 9, 6, 2, 21, 3, 4, 2, 3, 499, NULL, '2022-07-13 07:41:51', 1, 0, 27, 27, 0, 0, 1.98, 67),
(119, '', '', 'CR', 'interview', '2022-07-13 07:34:04', 1, 1, 2, 1, 2, 1, 2, 1, 2, 2, 1, 4, 2, 1, 1, 1, 4, 1, 1, '23', 1, '1', 2, 2, '1', 3, 2, 3, 3, 2, 3, 2, 4, 2, 1, 2, 2, 3, 1, 1, 4, 4, 1, 2, 2, 4, 2, 4, 1, 2, 3, 2, 3, 2, 3, 2, 4, 2, 3, 2, 4, 1, 4, 1, 1, 1, 1, 2, 2, 2, 1, 1, 4, 4, 4, 3, 3, 1, 4, 3, 3, 2, 3, 2, 3, 4, 8, 58, 1, 2, 40, 21, 27, 5, 6, 2, 9, 10, 16, 14, 12, 1, 12, 4, 4, 12, 2, 3, 2, 3, 3, 3, 284, NULL, '2022-07-13 07:38:48', 1, 0, 27, 27, 0, 0, 2.57, 164),
(120, '', '', 'CR', 'interview', '2022-07-13 07:34:07', 1, 5, 1, 2, 3, 4, 5, 3, 5, 5, 5, 4, 5, 4, 3, 3, 4, 5, 1, '23', 1, '2 Jahren', 1, 1, '1 Jahr', 4, 4, 5, 5, 4, 4, 4, 4, 4, 3, 4, 3, 3, 3, 4, 4, 4, 4, 4, 4, 5, 5, 5, 5, 4, 4, 5, 5, 4, 5, 5, 5, 1, 2, 2, 1, 1, 3, 3, 1, 5, 4, 5, 4, 2, 4, 1, 1, 5, 4, 5, 5, 3, 4, 5, 3, 2, 2, 2, 2, 8, 8, 33, 5, 5, 71, 25, 32, 55, 23, 7, 96, 56, 38, 36, 11, 2, 5, 4, 4, 2, 2, 3, 2, 3, 3, 2, 541, NULL, '2022-07-13 07:43:08', 1, 0, 27, 27, 0, 0, 2.15, 85),
(123, '', '', 'CR', 'interview', '2022-07-13 08:21:07', 1, 5, 1, 5, 2, 3, 5, 5, 4, 5, 1, 1, 5, 5, 4, 3, 4, 4, 2, '24', 4, '7', 1, 1, '1', 5, 5, 5, 5, 2, 1, 4, 5, 1, 3, 2, 4, 1, 5, 1, 2, 3, 2, 4, 5, 4, 4, 5, 5, 2, 3, 4, 4, 4, 4, 2, 3, 2, 3, 3, 2, 3, 4, 4, 4, 5, 5, 5, 5, 3, 2, 2, 1, 3, 3, 3, 2, 5, 5, 5, 5, 3, 2, 4, 2, 51, 735, 302, 25, 18, 583, 462, 348, 906, 720, 26, 293, 387, 308, 381, 735, 7, 53, 124, 172, 98, 11, 13, 9, 38, 23, 11, 1236, NULL, '2022-07-13 10:15:06', 1, 0, 27, 27, 0, 0, 0.29, 0),
(124, '', '', 'CR', 'interview', '2022-07-13 09:29:09', 1, 5, 1, 4, 2, 3, 5, 2, 5, 5, 1, 3, 5, 4, 3, 2, 4, 4, 1, '23', 4, '5', 1, 1, '0,5', 4, 5, 5, 5, 2, 2, 4, 4, 5, 5, 5, 5, 1, 1, 1, 5, 5, 5, 5, 2, 5, 4, 4, 5, 3, 5, 5, 5, 1, 2, 1, 3, 4, 5, 4, 5, 5, 4, 4, 5, 5, 5, 5, 5, 2, 1, 1, 2, 4, 4, 4, 2, 5, 5, 4, 4, 3, 2, 3, 1, 36, 61, 27, 5, 3, 26, 47, 92, 72, 58, 2, 109, 85, 150, 82, 100, 4, 36, 30, 66, 18, 4, 31, 3, 21, 15, 14, 1197, NULL, '2022-07-13 09:49:06', 1, 0, 27, 27, 0, 0, 0.99, 21),
(125, '', '', 'CR', 'interview', '2022-07-13 09:39:42', 1, 5, 1, 5, 3, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 1, '21', 1, '7', 1, 2, '0', 5, 5, 5, 5, 5, 5, 5, 5, 1, 1, 5, 5, 1, 1, 1, 1, 5, 5, 5, 5, 5, 5, 5, 5, 4, 4, 5, 5, 5, 5, 2, 2, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 3, 5, 1, 5, 1, 1, 5, 5, 5, 5, 3, 5, 3, 4, 5, 3, 3, 4, 2, 4, 10, 1, 2, 41, 14, 40, 33, 40, 2, 36, 34, 15, 50, 26, 3, 8, 13, 25, 4, 2, 8, 4, 6, 7, 14, 444, NULL, '2022-07-13 09:47:06', 1, 0, 27, 27, 0, 0, 2.02, 96),
(126, '', '', 'CR', 'interview', '2022-07-13 10:27:30', 1, 5, 1, 3, 3, 2, 4, 4, 5, 2, 2, 3, 5, 2, 5, 1, 3, 1, 2, '29', 1, '0,7', 1, 1, '0,7', 4, 4, 5, 5, 5, 5, 4, 5, 1, 1, 1, 4, 1, 1, 1, 4, 4, 5, 4, 5, 4, 4, 4, 4, 5, 5, 5, 2, 4, 1, 3, 2, 1, 1, 1, 4, 1, 1, 1, 1, 3, 2, 3, 2, 1, 3, 1, 1, 5, 5, 5, 4, 5, 5, 5, 5, 2, 1, 1, 1, 15, 41, 39, 4, 17, 101, 49, 66, 51, 36, 23, 182, 104, 233, 289, 106, 11, 45, 52, 74, 69, 7, 20, 13, 10, 43, 60, 1363, NULL, '2022-07-13 10:56:50', 1, 0, 27, 27, 0, 0, 0.63, 2),
(127, '', '', 'CR', 'interview', '2022-07-13 10:40:25', 1, 5, 2, 1, 2, 1, 5, 5, 5, 5, 3, 3, 4, 4, 2, 1, 4, 1, 1, '21', 1, '2', 1, 2, '1', 4, 5, 5, 5, 5, 5, 4, 4, 4, 4, 2, 4, 1, 1, 2, 3, 4, 5, 3, 4, 5, 4, 4, 4, 2, 4, 4, 4, 2, 2, 3, 4, 1, 4, 3, 4, 2, 4, 4, 4, 5, 4, 5, 5, 1, 5, 2, 1, 4, 5, 5, 5, 4, 4, 4, 5, 2, 3, 4, 3, 64, 7, 38, 4, 24, 70, 37, 32, 47, 86, 33, 151, 134, 92, 78, 91, 9, 30, 29, 34, 23, 9, 7, 5, 5, 8, 6, 1153, NULL, '2022-07-13 10:59:38', 1, 0, 27, 27, 0, 0, 0.95, 11),
(128, '', '', 'CR', 'interview', '2022-07-13 11:02:28', 1, 4, 4, 4, 4, 3, 3, 4, 4, 4, 4, 3, 4, 5, 4, 3, 4, 5, 1, '33', 2, '1', 1, 2, '0.5', 3, 4, 3, 4, 4, 4, 3, 4, 4, 4, 3, 4, 3, 3, 2, 3, 2, 3, 4, 3, 3, 4, 3, 4, 3, 4, 3, 2, 4, 4, 3, 4, 3, 4, 3, 4, 4, 3, 4, 4, 4, 2, 1, 3, 3, 2, 4, 3, 4, 4, 3, 2, 4, 3, 4, 3, 2, 3, 3, 4, 16, 8, 37, 1, 3, 7, 13, 7, 11, 7, 2, 12, 18, 26, 15, 16, 3, 17, 50, 5, 5, 3, 3, 4, 5, 3, 3, 269, NULL, '2022-07-13 11:07:30', 1, 0, 27, 27, 0, 0, 2.4, 134),
(131, '', '', 'CR', 'interview', '2022-07-13 12:33:37', 1, 4, 2, 5, 3, 2, 4, 3, 4, 3, 1, 1, 4, 3, 5, 3, 3, 2, 1, '19', 1, '3', 1, 2, '0', 5, 4, 5, 5, 5, 1, 2, 4, 1, 3, 5, 5, 1, 2, 1, 2, 5, 5, 5, 5, 5, 5, 5, 5, 2, 3, 2, 2, 2, 2, 1, 1, 3, 3, 5, 5, 1, 2, 1, 1, 3, 1, 2, 2, 1, 5, 4, 4, 5, 5, 4, 4, 2, 3, 5, 4, 3, 4, 4, 2, 10, 186, 41, 13, 17, 44, 41, 47, 56, 50, 14, 79, 46, 63, 71, 107, 5, 23, 24, 22, 55, 6, 11, 7, 6, 4, 12, 1060, NULL, '2022-07-13 12:51:17', 1, 0, 27, 27, 0, 0, 0.95, 6),
(132, '', '', 'CR', 'interview', '2022-07-13 12:33:37', 1, 4, 1, 2, 2, 4, 5, 4, 5, 4, 1, 4, 3, 5, 2, 2, 5, 2, 1, '19', 1, '1', 1, 2, '0', 5, 5, 5, 5, 5, 2, 5, 5, 2, 2, 5, 2, 3, 5, 5, 3, 5, 4, 4, 3, 5, 3, 5, 5, 2, 3, 5, 2, 2, 4, 4, 1, 2, 3, 5, 4, 1, 4, 5, 5, 5, 5, 4, 3, 2, 5, 4, 4, 4, 5, 2, 4, 4, 4, 2, 5, 3, 3, 5, 2, 74, 2776, 37, 11, 39, 130, 182, 114, 99, 128, 17, 10478, 202, 224, 193, 201, 14, 62, 25, 34, 34, 6, 25, 10, 8, 16, 10, 1754, NULL, '2022-07-13 16:46:06', 1, 0, 27, 27, 0, 0, 0.47, 0),
(135, '', '', 'CR', 'interview', '2022-07-13 15:16:33', 1, 5, 1, 3, 1, 2, 5, 5, 5, 4, 4, 1, 4, 3, 3, 1, 4, 2, 1, '30', 1, '3', 1, 2, '1', 5, 5, 5, 5, 4, 4, 5, 5, 5, 5, 5, 5, 1, 2, 1, 1, 5, 5, 5, 5, 4, 4, 5, 5, 2, 3, 4, 2, 2, 2, 1, 2, 3, 2, 3, 2, 1, 3, 4, 4, 5, 5, 5, 5, 1, 2, 1, 4, 4, 5, 4, 4, 2, 4, 3, 2, 3, 3, 4, 4, 61, 466, 49, 53, 36, 110, 105, 161, 104, 108, 31, 99, 93, 173, 129, 121, 11, 30, 35, 62, 41, 11, 14, 5, 22, 17, 13, 1564, NULL, '2022-07-13 15:52:33', 1, 0, 27, 27, 0, 0, 0.49, 0),
(136, '', '', 'CR', 'interview', '2022-07-13 17:11:26', 1, 5, 1, 4, 1, 2, 5, 5, 5, 4, 2, 1, 3, 3, 4, 1, 1, 1, 1, '29', 4, '2,5', 1, 2, '2,5', 5, 5, 4, 5, 5, 5, 5, 5, 2, 4, 5, 2, 1, 3, 1, 1, 5, 5, 2, 5, 4, 5, 5, 5, 3, 4, 2, 1, 2, 1, 1, 1, 2, 1, 4, 4, 2, 1, 4, 3, 5, 4, 4, 5, 1, 5, 3, 2, 4, 3, 1, 3, 4, 2, 3, 4, 2, 4, 2, 1, 84, 121, 124, 23, 52, 336, 135, 309, 187, 187, 45, 273, 220, 267, 229, 250, 8, 30, 67, 192, 49, 8, 59, 9, 27, 23, 36, 1336, NULL, '2022-07-13 18:07:16', 1, 0, 27, 27, 0, 0, 0.31, 0),
(145, '', '', 'CR', 'interview', '2022-07-14 14:38:54', 1, 4, 1, 4, 3, 3, 4, 4, 4, 2, 4, 2, 3, 3, 2, 2, 4, 1, 2, '26', 1, '0,5', 2, 2, '0,5', 4, 4, 5, 5, 4, 4, 4, 4, 2, 2, 2, 4, 2, 2, 3, 3, 4, 4, 4, 4, 4, 4, 4, 4, 3, 3, 4, 2, 3, 2, 3, 2, 3, 3, 4, 4, 1, 2, 4, 5, 4, 5, 3, 4, 3, 4, 4, 3, 4, 3, 4, 2, 4, 4, 4, 3, 3, 3, 4, 3, 28, 187, 48, 22, 38, 169, 99, 181, 85, 130, 24, 177, 111, 459, 176, 97, 13, 16, 34, 33, 36, 5, 50, 5, 21, 27, 11, 1627, NULL, '2022-07-14 15:16:56', 1, 0, 27, 27, 0, 0, 0.51, 1),
(146, '', '', 'CR', 'interview', '2022-07-14 18:59:04', 1, 5, 1, 2, 1, 4, 5, 5, 5, 5, 5, 1, 1, 1, 1, 5, 5, 5, 1, '32', 3, '0,5', 1, 2, '0,5', 5, 5, 5, 5, 5, 1, 5, 5, 1, 1, 5, 1, 1, 1, 1, 1, 5, 5, 5, 5, 2, 3, 5, 5, 3, 5, 5, 1, 1, 1, 1, 4, 1, 1, 5, 1, 1, 3, 1, 5, 5, 5, 5, 4, 1, 1, 1, 4, 5, 5, 3, 5, 5, 5, 5, 2, 4, 5, 5, 1, 10, 23, 52, 29, 10, 179, 49, 74, 59, 65, 14, 111, 96, 298, 147, 85, 12, 18, 47, 23, 44, 9, 8, 7, 21, 9, 19, 1518, NULL, '2022-07-14 19:24:23', 1, 0, 27, 27, 0, 0, 0.74, 4),
(147, '', '', 'CR', 'interview', '2022-07-15 01:27:46', 1, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 1, '29', 4, '5', 1, 2, '5', 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 5, 5, 5, 5, 1, 1, 2, 1, 5, 5, 5, 2, 4, 4, 2, 4, 5, 4, 3, 4, 10, 71, 51, 8, 21, 35, 59, 99, 68, 72, 11, 25, 10, 11, 9, 7, 2, 4, 4, 5, 4, 1, 3, 2, 4, 3, 2, 601, NULL, '2022-07-15 01:37:48', 1, 0, 27, 27, 0, 0, 2.03, 106),
(149, '', '', 'CR', 'interview', '2022-07-15 12:38:29', 1, 3, 4, 4, 5, 5, 2, 1, 4, 5, 1, 3, 4, 2, 4, 2, 1, 2, 1, '22', 1, '1', 1, 2, '1', 2, 4, 2, 4, 4, 2, 3, 2, 5, 3, 4, 2, 4, 1, 3, 4, 5, 4, 3, 2, 3, 2, 4, 5, 4, 2, 1, 3, 4, 1, 2, 4, 4, 3, 5, 4, 1, 2, 1, 4, 2, 4, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 4, 2, 5, 1, 4, 2, 1, 2, 4, 8, 33, 4, 13, 26, 16, 7, 8, 7, 2, 13, 13, 14, 13, 11, 3, 10, 6, 6, 6, 2, 3, 4, 3, 3, 2, 240, NULL, '2022-07-15 12:42:29', 1, 0, 27, 27, 0, 0, 2.56, 132),
(158, '', '', 'CR', 'interview', '2022-07-15 22:10:30', 1, 5, 4, 4, 3, 2, 5, 2, 4, 3, 2, 2, 4, 4, 4, 2, 3, 2, 2, '20', 1, '3', 2, 2, '0,5', 5, 4, 4, 4, 4, 5, 4, 4, 3, 3, 2, 4, 4, 3, 2, 4, 4, 4, 4, 4, 4, 4, 3, 3, 3, 3, 3, 2, 3, 3, 3, 3, 3, 4, 2, 3, 3, 3, 4, 4, 5, 4, 3, 3, 2, 3, 3, 3, 5, 4, 4, 4, 4, 4, 3, 3, 2, 4, 3, 3, 8, 262, 36, 17, 14, 83, 59, 48, 60, 44, 10, 91, 160, 194, 115, 111, 20, 35, 23, 43, 16, 4, 13, 7, 6, 7, 4, 1241, NULL, '2022-07-15 22:35:20', 1, 0, 27, 27, 0, 0, 0.85, 4),
(160, '', '', 'CR', 'interview', '2022-07-16 09:58:49', 1, 4, 4, 3, 3, 4, 5, 5, 5, 5, 5, 5, 5, 5, 5, 4, 5, 5, 2, '27', 4, '0,5', 1, 2, '0,5', 4, 4, 4, 5, 5, 5, 5, 5, 4, 4, 3, 5, 3, 4, 4, 4, 4, 5, 3, 5, 4, 4, 4, 4, 4, 4, 3, 4, 4, 4, 4, 4, 4, 4, 4, 5, 4, 4, 4, 4, 5, 4, 4, 5, 4, 3, 4, 5, 4, 3, 3, 5, 4, 3, 4, 5, 4, 4, 4, 5, 44, 70, 46, 4, 17, 148, 92, 138, 79, 147, 9, 119, 459, 159, 36, 54, 2, 22, 10, 17, 24, 2, 18, 5, 8, 4, 2, 1242, NULL, '2022-07-16 10:27:44', 1, 0, 27, 27, 0, 0, 1.05, 14),
(161, '', '', 'CR', 'interview', '2022-07-16 10:02:31', 1, 4, 2, 4, 3, 2, 3, 4, 4, 4, 3, 2, 4, 4, 4, 3, 3, 2, 2, '29', 4, '2', 1, 2, '1', 4, 4, 4, 4, 4, 4, 4, 5, 2, 2, 2, 1, 2, 2, 2, 2, 5, 5, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 3, 3, 3, 3, 4, 1, 3, 3, 3, 3, 4, 3, 3, 5, 2, 3, 4, 2, 4, 4, 4, 4, 4, 4, 4, 3, 4, 3, 3, 3, 36, 120, 24, 2, 23, 89, 54, 71, 86, 47, 20, 199, 78, 90, 58, 133, 44, 15, 15, 34, 44, 4, 33, 4, 6, 8, 4, 1301, NULL, '2022-07-16 10:24:52', 1, 0, 27, 27, 0, 0, 0.91, 9),
(163, '', '', 'CR', 'interview', '2022-07-16 13:01:47', 1, 5, 1, 5, 3, 4, 5, 5, 5, 5, 5, 5, 5, 4, 4, 3, 3, 3, 1, '30', 1, '2,5', 1, 2, '1', 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 3, 3, 1, 2, 5, 5, 5, 5, 5, 3, 5, 5, 3, 2, 5, 2, 2, 3, 2, 1, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 2, 4, 2, 5, 5, 5, 5, 5, 5, 5, 5, 4, 5, 5, 5, 3, 28, 292, 52, 24, 45, 120, 99, 169, 251, 151, 26, 132, 164, 231, 238, 575, 8, 20, 11, 39, 29, 7, 13, 6, 7, 12, 14, 1355, NULL, '2022-07-16 13:47:53', 1, 0, 27, 27, 0, 0, 0.56, 2),
(164, '', '', 'CR', 'interview', '2022-07-16 21:51:32', 1, 5, 1, 2, 2, 2, 5, 5, 5, 5, 5, 5, 5, 5, 5, 2, 5, 1, 1, '32', 4, '2', 1, 2, '0,5', 5, 5, 5, 5, 5, 3, 5, 5, 1, 1, 1, 2, 1, 1, 2, 3, 5, 5, 5, 5, 5, 5, 5, 5, 2, 4, 5, 2, 2, 3, 2, 2, 1, 4, 1, 1, 1, 3, 5, 5, 5, 5, 5, 5, 2, 2, 1, 4, 5, 5, 5, 5, 5, 5, 5, 5, 4, 5, 4, 2, 10, 48, 28, 18, 3, 58, 38, 47, 28, 37, 3, 67, 82, 89, 171, 72, 3, 15, 21, 45, 16, 5, 7, 4, 5, 15, 10, 945, NULL, '2022-07-16 22:07:20', 1, 0, 27, 27, 0, 0, 1.2, 21),
(167, '', '', 'CR', 'interview', '2022-07-17 16:26:10', 1, 4, 3, 4, 3, 4, 3, 4, 3, 4, 4, 3, 4, 4, 4, 4, 4, 4, 2, '19', 1, '1', 2, 2, '1', 3, 4, 4, 4, 3, 4, 3, 4, 4, 4, 3, 4, 5, 4, 4, 4, 5, 5, 5, 4, 5, 4, 4, 4, 4, 4, 3, 4, 4, 4, 4, 5, 4, 4, 4, 4, 5, 4, 4, 4, 3, 3, 2, 4, 4, 4, 4, 4, 5, 4, 5, 5, 4, 3, 4, 4, 3, 3, 4, 4, 11, 43, 32, 3, 8, 69, 33, 22, 36, 25, 2, 67, 38, 88, 45, 778, 2, 18, 16, 11, 21, 10, 8, 17, 14, 6, 6, 704, NULL, '2022-07-17 16:49:59', 1, 0, 27, 27, 0, 0, 1.34, 26),
(168, '', '', 'CR', 'interview', '2022-07-18 10:58:47', 1, 5, 1, 4, 1, 2, 5, 5, 5, 3, 1, 1, 3, 4, 4, 2, 4, 2, 2, '25', 3, '2', 1, 2, '1', 5, 5, 5, 5, 5, 5, 5, 5, 1, 5, 5, 4, 1, 2, 1, 1, 5, 5, 5, 5, 5, 5, 5, 5, 3, 5, 2, 2, 1, 2, 1, 1, 1, 3, 4, 5, 3, 4, 4, 5, 5, 5, 4, 5, 1, 1, 4, 4, 5, 5, 4, 5, 4, 4, 4, 5, 2, 1, 3, 1, 56, 92, 44, 20, 12, 85, 49, 82, 87, 42, 28, 94, 96, 154, 92, 127, 8, 17, 26, 38, 19, 5, 9, 4, 12, 6, 3, 1307, NULL, '2022-07-18 11:20:34', 1, 0, 27, 27, 0, 0, 0.82, 4),
(183, '', '', 'CR', 'interview', '2022-07-21 14:34:19', 1, 4, 3, 2, 1, 1, 4, 5, 4, 5, 5, 4, 3, 1, 2, 3, 3, 2, 2, '22', 1, '2', 1, 2, '2', 5, 4, 5, 5, 3, 3, 4, 4, 2, 3, 5, 5, 2, 3, 2, 4, 5, 5, 5, 4, 3, 3, 3, 4, 4, 4, 2, 4, 2, 3, 2, 3, 2, 3, 2, 3, 1, 3, 2, 3, 4, 5, 4, 5, 3, 2, 2, 4, 5, 3, 4, 5, 4, 4, 4, 4, 3, 3, 4, 4, 49, 39, 58, 32, 23, 68, 54, 47, 46, 41, 10, 95, 72, 67, 40, 44, 19, 28, 16, 23, 22, 9, 37, 20, 5, 5, 5, 959, NULL, '2022-07-21 14:50:33', 1, 0, 27, 27, 0, 0, 0.88, 5),
(186, '', '', 'CR', 'interview', '2022-07-21 22:08:31', 1, 4, 1, 4, 2, 4, 5, 4, 5, 1, 1, 1, 3, 3, 4, 4, 5, 4, 1, '19', 1, '2', 1, 2, '0', 5, 4, 5, 5, 4, 4, 4, 5, 1, 1, 5, 4, 1, 2, 2, 1, 2, 2, 4, 5, 2, 2, 4, 4, 3, 3, 4, 5, 1, 2, 1, 3, 4, 4, 5, 5, 4, 4, 4, 5, 5, 5, 5, 5, 3, 3, 2, 1, 5, 5, 5, 5, 4, 4, 5, 4, 4, 3, 4, 2, 8, 10, 40, 2, 25, 84, 59, 64, 92, 225, 5, 94, 92, 130, 115, 135, 11, 22, 23, 43, 26, 7, 28, 8, 23, 9, 17, 1215, NULL, '2022-07-21 22:31:48', 1, 0, 27, 27, 0, 0, 0.9, 10),
(190, '', '', 'CR', 'interview', '2022-07-23 09:26:06', 1, 4, 2, 5, 2, 2, 4, 4, 4, 5, 4, 4, 4, 4, 4, 1, 4, 4, 1, '36', 4, '6', 1, 2, '3', 5, 4, 5, 5, 5, 2, 5, 5, 1, 1, 1, 3, 1, 2, 2, 3, 5, 5, 4, 5, 5, 5, 5, 5, 1, 2, 5, 1, 2, 2, 2, 4, 2, 5, 4, 4, 4, 4, 4, 4, 4, 3, 2, 1, 1, 5, 1, 4, 4, 4, 5, 5, 4, 5, 4, 4, 3, 4, 4, 4, 27, 25, 29, 6, 13, 65, 40, 48, 44, 31, 17, 101, 97, 37, 102, 86, 4, 29, 24, 47, 11, 4, 12, 7, 16, 20, 15, 957, NULL, '2022-07-23 09:42:03', 1, 0, 27, 27, 0, 0, 0.94, 6),
(195, '', '', 'CR', 'interview', '2022-07-23 10:22:19', 1, 4, 1, 5, 1, 4, 5, 5, 5, 5, 5, 5, 4, 5, 5, 3, 5, 3, 1, '30', 4, '3', 1, 2, '0', 5, 5, 5, 5, 4, 5, 5, 5, 5, 5, 5, 5, 1, 5, 3, 4, 5, 5, 5, 5, 5, 5, 5, 5, 3, 4, 5, 2, 3, 4, 2, 2, 3, 3, 4, 5, 5, 5, 5, 5, 5, 5, 4, 5, 2, 4, 1, 2, 4, 5, 4, 4, 4, 5, 3, 4, 3, 4, 5, 4, 24, 26, 32, 4, 16, 113, 98, 117, 114, 93, 10, 269, 140, 148, 238, 78, 8, 39, 27, 68, 39, 6, 20, 14, 28, 31, 9, 1415, NULL, '2022-07-23 10:52:29', 1, 0, 27, 27, 0, 0, 0.61, 2),
(201, '', '', 'CR', 'interview', '2022-07-25 17:21:19', 1, 5, 1, 4, 2, 3, 4, 2, 4, 1, 1, 1, 5, 5, 5, 2, 2, 4, 1, '22', 1, '3', 1, 2, '3', 4, 4, 3, 1, 4, 4, 5, 5, 2, 3, 4, 2, 1, 2, 1, 3, 4, 4, 3, 4, 4, 5, 4, 4, 2, 4, 5, 1, 1, 1, 1, 1, 4, 1, 4, 3, 2, 4, 3, 3, 3, 4, 2, 3, 1, 2, 4, 4, 4, 3, 3, 3, 2, 4, 3, 5, 2, 4, 4, 1, 226, 168, 37, 20, 16, 132, 101, 74, 85, 88, 17, 108, 89, 124, 135, 248, 6, 23, 22, 49, 92, 6, 12, 3, 19, 16, 7, 1443, NULL, '2022-07-25 17:53:22', 1, 0, 27, 27, 0, 0, 0.64, 2),
(205, '', '', 'CR', 'interview', '2022-07-26 12:00:01', 1, 2, 1, 3, 2, 4, 5, 5, 5, 5, 5, 5, 5, 5, 5, 4, 4, 3, 1, '34', 4, '10', 2, 2, '0', 5, 5, 5, 5, 5, 5, 5, 5, 3, 3, 5, 5, 3, 3, 5, 5, 5, 5, 5, 4, 5, 5, 5, 5, 4, 5, 5, 4, 4, 5, 4, 4, 4, 4, 4, 4, 4, 4, 2, 3, 5, 5, 5, 4, 3, 3, 4, 3, 5, 4, 5, 4, 5, 5, 5, 4, 5, 5, 5, 4, 67, 192, 42, 24, 26, 62, 53, 36, 34, 32, 9, 94, 125, 239, 96, 201, 9, 14, 13, 10, 14, 7, 11, 13, 7, 11, 6, 1447, NULL, '2022-07-26 12:24:09', 1, 0, 27, 27, 0, 0, 0.89, 8),
(208, '', '', 'CR', 'interview', '2022-07-28 10:24:31', 1, 5, 4, 3, 4, 3, 5, 4, 5, 5, 4, 3, 3, 3, 3, 3, 4, 3, 1, '35', 4, '10', 1, 2, '0', 4, 4, 4, 4, 4, 4, 4, 4, 3, 4, 3, 4, 3, 3, 3, 3, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 3, 4, 3, 4, 3, 3, 3, 3, 4, 4, 3, 3, 4, 3, 3, 3, 4, 4, 4, 4, 4, 4, 4, 4, 3, 3, 3, 4, 7, 50, 37, 7, 18, 52, 50, 19, 15, 17, 7, 51, 34, 61, 21, 24, 3, 27, 8994, 22, 11, 4, 16, 22, 8, 10, 4, 600, NULL, '2022-07-28 13:04:23', 1, 0, 27, 27, 0, 0, 1.39, 27),
(211, '', '', 'CR', 'interview', '2022-07-29 09:07:08', 1, 5, 1, 2, 3, 4, 5, 4, 5, 2, 3, 2, 2, 3, 2, 2, 3, 3, 2, '29', 3, '1', 2, 2, '1', 4, 3, 2, 3, 3, 3, 3, 4, 2, 3, 2, 4, 2, 3, 3, 4, 3, 3, 2, 4, 3, 3, 3, 4, 4, 4, 2, 4, 3, 3, 3, 4, 2, 3, 2, 4, 3, 3, 3, 3, 5, 3, 3, 4, 1, 1, 3, 2, 4, 2, 3, 3, 4, 2, 3, 4, 2, 2, 3, 2, 18, 7, 78, 50, 33, 116, 60, 81, 30, 27, 45, 107, 49, 82, 56, 36, 21, 43, 19, 27, 33, 23, 32, 14, 12, 28, 27, 995, NULL, '2022-07-29 09:26:22', 1, 0, 27, 27, 0, 0, 0.8, 10),
(216, '', '', 'CR', 'interview', '2022-07-31 12:50:47', 1, 4, 4, 5, 2, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 3, 4, 2, 2, '35', 4, '15', 1, 1, '12', 4, 4, 4, 4, 4, 4, 4, 4, 2, 3, 2, 4, 4, 4, 4, 4, 4, 4, 2, 4, 4, 4, 4, 4, 4, 4, 4, 2, 2, 3, 2, 2, 2, 2, 2, 4, 4, 4, 4, 4, 4, 3, 3, 4, 2, 2, 2, 2, 4, 3, 4, 4, 4, 4, 4, 4, 4, 2, 2, 2, 30, 127, 44, 39, 16, 89, 72, 69, 75, 51, 21, 96, 211, 169, 111, 110, 8, 166, 34, 20, 46, 8, 15, 15, 8, 8, 8, 1520, NULL, '2022-07-31 13:18:34', 1, 0, 27, 27, 0, 0, 0.62, 0),
(217, '', '', 'CR', 'interview', '2022-07-31 22:59:57', 1, 3, 1, 5, 2, 4, 5, 5, 5, 1, 1, 1, 5, 5, 5, 1, 1, 1, 2, '30', 4, '6', 1, 1, '2', 4, 3, 5, 5, 3, 5, 5, 4, 4, 3, 4, 4, 2, 2, 2, 2, 5, 5, 5, 5, 5, 5, 2, 5, 2, 3, 4, 5, 2, 4, 2, 2, 4, 4, 4, 4, 3, 4, 4, 4, 3, 3, 5, 4, 1, 1, 3, 2, 4, 4, 4, 5, 3, 3, 4, 4, 3, 4, 4, 3, 15, 22, 39, 3, 8, 55, 38, 33, 52, 41, 5, 115, 62, 66, 85, 66, 4, 20, 13, 15, 11, 2, 39, 7, 12, 15, 8, 824, NULL, '2022-07-31 23:14:08', 1, 0, 27, 27, 0, 0, 1.18, 14),
(220, '', '', 'CR', 'interview', '2022-08-15 11:04:55', 1, 5, 1, 4, 2, 3, 5, 5, 5, 5, 5, 5, 1, 2, 1, 1, 4, 1, 2, '25', 3, '2', 1, 2, '2', 5, 5, 5, 5, 5, 5, 5, 5, 1, 2, 1, 2, 1, 2, 1, 1, 5, 5, 5, 5, 5, 5, 5, 5, 2, 3, 3, 1, 2, 2, 2, 1, 2, 3, 3, 3, 3, 2, 4, 4, 4, 4, 4, 5, 1, 3, 1, 2, 5, 5, 5, 5, 4, 5, 5, 5, 2, 1, 3, 3, 75, 105, 42, 27, 20, 107, 77, 71, 39, 43, 67, 140, 138, 94, 72, 145, 7, 26, 24, 33, 23, 7, 18, 7, 12, 9, 17, 1387, NULL, '2022-08-15 11:29:00', 1, 0, 27, 27, 0, 0, 0.66, 1);

-- Delete values from the pretest, in which the cpp-programming years were missing.
DELETE FROM t_data_readability_survey WHERE KV07_01 = '';

-- Update some values so that they are in a homogenous and usable format.

-- 3/4 -> 0.75
UPDATE t_data_readability_survey SET 
    KV04_01 = 0.75,
    KV07_01 = 0.75
WHERE 
    `CASE` = 61;

-- '1 Jahr' -> '1'
-- '2 Jahre ' -> '2'
UPDATE t_data_readability_survey SET
    KV04_01 = 2,
    KV07_01 = 1
WHERE `CASE`= 120;

-- '1,5' -> '1.5'
UPDATE t_data_readability_survey SET
    KV04_01 = REPLACE(KV04_01, ',', '.'),
    KV07_01 = REPLACE(KV07_01, ',', '.');
    
    
-- Interpreting some free-text answers, so that the data is usable.
-- 'Nicht sonderlich viel' -> 0 years of experience
UPDATE t_data_readability_survey SET 
    KV07_01 = 0
WHERE 
    `CASE` = 105;
-- 'Mittelmäßig' -> MEAN of our survey, which is assumed to be a good estimator for the mean of the population of the distribution.
UPDATE t_data_readability_survey SET 
    KV07_01 = 0
WHERE 
    `CASE` = 100;
UPDATE t_data_readability_survey SET 
    KV07_01 = (
    SELECT AVG(CAST(t_temporary.KV07_01 AS DECIMAL(10, 0)))
        FROM (
            SELECT CAST(t_data_readability_survey.KV07_01 AS DECIMAL(10, 0)) AS KV07_01, @rownum:=@rownum+1 AS `row_number`, @total_rows:=@rownum
            FROM t_data_readability_survey, (SELECT @rownum:=0) r
            WHERE t_data_readability_survey.KV07_01 IS NOT NULL
            ORDER BY CAST(t_data_readability_survey.KV07_01 AS DECIMAL(10, 0))
        ) AS t_temporary
    WHERE t_temporary.row_number IN ( FLOOR((@total_rows+1)/2), FLOOR((@total_rows+2)/2))
    )
WHERE 
    `CASE` = 100;

-- mySQL table for holding the control variables from the clean code survey.

CREATE TABLE t_control_variables_survey (
    case_id INT,
    interview_duration INT,
    gender BOOLEAN,
    age INT,
    advertised_at INT,
    years_of_programming DECIMAL(9, 2),
    familiar_with_oop BOOLEAN,
    familiar_with_cc BOOLEAN,
    years_of_cpp_programming DECIMAL(9, 2),
    
    PRIMARY KEY (case_id)
);

-- interview_duration describes the amount of seconds the participant needed to answer the questionnaire, so it has to be calculated by subtracting the datetime at the start of the interview from the datetime at the end of the interview, converting the results into seconds and summing them up.
INSERT INTO `t_control_variables_survey` (case_id, interview_duration, gender, age, advertised_at, years_of_programming, familiar_with_oop, familiar_with_cc, years_of_cpp_programming) SELECT `CASE`, TIMESTAMPDIFF(HOUR, STARTED, LASTDATA) * 3600 + TIMESTAMPDIFF(MINUTE, STARTED, LASTDATA) * 60 + TIMESTAMPDIFF(SECOND, STARTED, LASTDATA), KV01, KV02_01 - 1, KV03, KV04_01, KV05, KV06, KV07_01 FROM `t_data_readability_survey`;

-- since no one chose 'divers' as his*her during the survey, we transform it into a boolean variable where 1 means 'male' and 0 means 'female'. Originally, 0 meant 'divers', 1 meant 'male' and meant 2 'female'.
UPDATE t_control_variables_survey SET gender = 0 WHERE gender = 2;

-- change values for 'no' from the value 2 to the value 0. So 1 means 'yes' and 0 means 'no'.
UPDATE t_control_variables_survey SET familiar_with_oop = 0 WHERE familiar_with_oop = 2;
UPDATE t_control_variables_survey SET familiar_with_cc = 0 WHERE familiar_with_cc = 2;

-- subtract 1 from the advertised_at-variable, so it starts by the value 0 like the other values (and not by the value 1).
UPDATE t_control_variables_survey SET advertised_at = advertised_at - 1;

-- mySQL table for holding the data regarding the first code example

CREATE TABLE t_code_example1_survey (
    case_id INT NOT NULL,
    naming_vars_item1 INT,
    naming_vars_item2 INT,
    naming_vars_item3 INT,
    naming_vars_item4 INT,
    vars_score DECIMAL(9, 2),
    naming_methods_item1 INT,
    naming_methods_item2 INT,
    naming_methods_item3 INT,
    naming_methods_score DECIMAL(9, 2),
    formatting_methods_item INT,
    clearness_methods_item INT,
    complexity_methods_item1 INT,
    complexity_methods_item2 INT,
    complexity_methods_item3 INT,
    complexity_methods_score DECIMAL(9, 2),
    methods_score DECIMAL(9, 2),
    naming_class_item INT,
    complexity_class_item INT,
    clearness_class_item INT,
    class_score DECIMAL(9, 2),
    overall_clearness_item INT,
    total_score DECIMAL(9, 2),
    code_example INT,
    
    PRIMARY KEY(case_id)
);

INSERT INTO `t_code_example1_survey` (case_id, naming_vars_item1, naming_vars_item2, naming_vars_item3, naming_vars_item4, naming_methods_item1, naming_methods_item2, naming_methods_item3, formatting_methods_item, clearness_methods_item, complexity_methods_item1, complexity_methods_item2, complexity_methods_item3, naming_class_item, complexity_class_item, clearness_class_item, overall_clearness_item, code_example) SELECT `CASE`, VA03, VA04, VA05, VA06, ME03, ME04, ME05, ME06, ME07, ME08, ME09, ME10, KL03, KL04, KL05, GE03, 1 FROM `t_data_readability_survey`;

-- the values for the items (or the normalized scores) are summed up and divided by the total possible score (or by the number of summands) to get a normalized parameter between 0 and 1.

UPDATE t_code_example1_survey SET vars_score = (naming_vars_item1 + naming_vars_item2 + naming_vars_item3 + naming_vars_item4) / 2.0;

UPDATE t_code_example1_survey SET naming_methods_score = (naming_methods_item1 + naming_methods_item2 + naming_methods_item3) / 1.5;

UPDATE t_code_example1_survey SET complexity_methods_score = (complexity_methods_item1 + complexity_methods_item2 + complexity_methods_item3) / 1.5;

UPDATE t_code_example1_survey SET methods_score = (formatting_methods_item + clearness_methods_item + naming_methods_score + complexity_methods_score) / 3.0;

UPDATE t_code_example1_survey SET class_score = (naming_class_item + complexity_class_item + clearness_class_item) / 1.5;

UPDATE t_code_example1_survey SET total_score = (vars_score + methods_score + class_score + (overall_clearness_item * 2.0)) / 4.0;

-- mySQL table for holding the data regarding the second code example

CREATE TABLE t_code_example2_survey (
    case_id INT NOT NULL,
    naming_vars_item1 INT,
    naming_vars_item2 INT,
    naming_vars_item3 INT,
    naming_vars_item4 INT,
    vars_score DECIMAL(9, 2),
    naming_methods_item1 INT,
    naming_methods_item2 INT,
    naming_methods_item3 INT,
    naming_methods_score DECIMAL(9, 2),
    formatting_methods_item INT,
    clearness_methods_item INT,
    complexity_methods_item1 INT,
    complexity_methods_item2 INT,
    complexity_methods_item3 INT,
    complexity_methods_score DECIMAL(9, 2),
    methods_score DECIMAL(9, 2),
    naming_class_item INT,
    complexity_class_item INT,
    clearness_class_item INT,
    class_score DECIMAL(9, 2),
    overall_clearness_item INT,
    total_score DECIMAL(9, 2),
    code_example INT,
    
    PRIMARY KEY(case_id)
);

INSERT INTO `t_code_example2_survey` (case_id, naming_vars_item1, naming_vars_item2, naming_vars_item3, naming_vars_item4, naming_methods_item1, naming_methods_item2, naming_methods_item3, formatting_methods_item, clearness_methods_item, complexity_methods_item1, complexity_methods_item2, complexity_methods_item3, naming_class_item, complexity_class_item, clearness_class_item, overall_clearness_item, code_example) SELECT `CASE`, VA08, VA09, VA10, VA11, ME12, ME13, ME14, ME15, ME16, ME17, ME18, ME19, KL07, KL08, KL09, GE05, 2 FROM `t_data_readability_survey`;

UPDATE t_code_example2_survey SET vars_score = (naming_vars_item1 + naming_vars_item2 + naming_vars_item3 + naming_vars_item4) / 2.0;

UPDATE t_code_example2_survey SET naming_methods_score = (naming_methods_item1 + naming_methods_item2 + naming_methods_item3) / 1.5;

UPDATE t_code_example2_survey SET complexity_methods_score = (complexity_methods_item1 + complexity_methods_item2 + complexity_methods_item3) / 1.5;

UPDATE t_code_example2_survey SET methods_score = (formatting_methods_item + clearness_methods_item + naming_methods_score + complexity_methods_score) / 3.0;

UPDATE t_code_example2_survey SET class_score = (naming_class_item + complexity_class_item + clearness_class_item) / 1.5;

UPDATE t_code_example2_survey SET total_score = (vars_score + methods_score + class_score + (overall_clearness_item * 2.0)) / 4.0;

-- mySQL table for holding the data regarding the third code example

CREATE TABLE t_code_example3_survey (
    case_id INT NOT NULL,
    naming_vars_item1 INT,
    naming_vars_item2 INT,
    naming_vars_item3 INT,
    naming_vars_item4 INT,
    vars_score DECIMAL(9, 2),
    naming_methods_item1 INT,
    naming_methods_item2 INT,
    naming_methods_item3 INT,
    naming_methods_score DECIMAL(9, 2),
    formatting_methods_item INT,
    clearness_methods_item INT,
    complexity_methods_item1 INT,
    complexity_methods_item2 INT,
    complexity_methods_item3 INT,
    complexity_methods_score DECIMAL(9, 2),
    methods_score DECIMAL(9, 2),
    naming_class_item INT,
    complexity_class_item INT,
    clearness_class_item INT,
    class_score DECIMAL(9, 2),
    overall_clearness_item INT,
    total_score DECIMAL(9, 2),
    code_example INT,
    
    PRIMARY KEY(case_id)
);

-- since the code-example is not a class, it gets NULL-values for the class-items.
INSERT INTO `t_code_example3_survey` (case_id, naming_vars_item1, naming_vars_item2, naming_vars_item3, naming_vars_item4, naming_methods_item1, naming_methods_item2, naming_methods_item3, formatting_methods_item, clearness_methods_item, complexity_methods_item1, complexity_methods_item2, complexity_methods_item3, naming_class_item, complexity_class_item, clearness_class_item, overall_clearness_item, code_example) SELECT `CASE`, VA13, VA14, VA15, VA16, ME21, ME22, ME23, ME24, ME25, ME26, ME27, ME28, KL11, KL12, KL13, GE07, 3 FROM `t_data_readability_survey`;

UPDATE t_code_example3_survey SET vars_score = (naming_vars_item1 + naming_vars_item2 + naming_vars_item3 + naming_vars_item4) / 2.0;

UPDATE t_code_example3_survey SET naming_methods_score = (naming_methods_item1 + naming_methods_item2 + naming_methods_item3) / 1.5;

UPDATE t_code_example3_survey SET complexity_methods_score = (complexity_methods_item1 + complexity_methods_item2 + complexity_methods_item3) / 1.5;

UPDATE t_code_example3_survey SET methods_score = (formatting_methods_item + clearness_methods_item + naming_methods_score + complexity_methods_score) / 3.0;

UPDATE t_code_example3_survey SET class_score = (naming_class_item + complexity_class_item + clearness_class_item) / 1.5;

UPDATE t_code_example3_survey SET total_score = (vars_score + methods_score + class_score + (overall_clearness_item * 2.0)) / 4.0;

-- mySQL table for holding the data regarding the fourth code example

CREATE TABLE t_code_example4_survey (
    case_id INT NOT NULL,
    naming_vars_item1 INT,
    naming_vars_item2 INT,
    naming_vars_item3 INT,
    naming_vars_item4 INT,
    vars_score DECIMAL(9, 2),
    naming_methods_item1 INT,
    naming_methods_item2 INT,
    naming_methods_item3 INT,
    naming_methods_score DECIMAL(9, 2),
    formatting_methods_item INT,
    clearness_methods_item INT,
    complexity_methods_item1 INT,
    complexity_methods_item2 INT,
    complexity_methods_item3 INT,
    complexity_methods_score DECIMAL(9, 2),
    methods_score DECIMAL(9, 2),
    naming_class_item INT,
    complexity_class_item INT,
    clearness_class_item INT,
    class_score DECIMAL(9, 2),
    overall_clearness_item INT,
    total_score DECIMAL(9, 2),
    code_example INT,
    
    PRIMARY KEY(case_id)
);

INSERT INTO `t_code_example4_survey` (case_id, naming_vars_item1, naming_vars_item2, naming_vars_item3, naming_vars_item4, naming_methods_item1, naming_methods_item2, naming_methods_item3, formatting_methods_item, clearness_methods_item, complexity_methods_item1, complexity_methods_item2, complexity_methods_item3, overall_clearness_item, code_example) SELECT `CASE`, VA18, VA19, VA20, VA21, ME30, ME31, ME32, ME33, ME34, ME35, ME36, ME37, GE09, 4 FROM `t_data_readability_survey`;

UPDATE t_code_example4_survey SET vars_score = (naming_vars_item1 + naming_vars_item2 + naming_vars_item3 + naming_vars_item4) / 2.0;

UPDATE t_code_example4_survey SET naming_methods_score = (naming_methods_item1 + naming_methods_item2 + naming_methods_item3) / 1.5;

UPDATE t_code_example4_survey SET complexity_methods_score = (complexity_methods_item1 + complexity_methods_item2 + complexity_methods_item3) / 1.5;

UPDATE t_code_example4_survey SET methods_score = (formatting_methods_item + clearness_methods_item + naming_methods_score + complexity_methods_score) / 3.0;

UPDATE t_code_example4_survey SET total_score = (vars_score + methods_score + (overall_clearness_item * 2.0)) / 3.0;

-- mySQL table for holding the data regarding the fifth code example

CREATE TABLE t_code_example5_survey (
    case_id INT NOT NULL,
    naming_vars_item1 INT,
    naming_vars_item2 INT,
    naming_vars_item3 INT,
    naming_vars_item4 INT,
    vars_score DECIMAL(9, 2),
    naming_methods_item1 INT,
    naming_methods_item2 INT,
    naming_methods_item3 INT,
    naming_methods_score DECIMAL(9, 2),
    formatting_methods_item INT,
    clearness_methods_item INT,
    complexity_methods_item1 INT,
    complexity_methods_item2 INT,
    complexity_methods_item3 INT,
    complexity_methods_score DECIMAL(9, 2),
    methods_score DECIMAL(9, 2),
    naming_class_item INT,
    complexity_class_item INT,
    clearness_class_item INT,
    class_score DECIMAL(9, 2),
    overall_clearness_item INT,
    total_score DECIMAL(9, 2),
    code_example INT,
    
    PRIMARY KEY(case_id)
);

INSERT INTO `t_code_example5_survey` (case_id, naming_vars_item1, naming_vars_item2, naming_vars_item3, naming_vars_item4, naming_methods_item1, naming_methods_item2, naming_methods_item3, formatting_methods_item, clearness_methods_item, complexity_methods_item1, complexity_methods_item2, complexity_methods_item3, naming_class_item, complexity_class_item, clearness_class_item, overall_clearness_item, code_example) SELECT `CASE`, VA23, VA24, VA25, VA26, ME39, ME40, ME41, ME42, ME43, ME44, ME45, ME46, KL15, KL16, KL17, GE11, 5 FROM `t_data_readability_survey`;

UPDATE t_code_example5_survey SET vars_score = (naming_vars_item1 + naming_vars_item2 + naming_vars_item3 + naming_vars_item4) / 2.0;

UPDATE t_code_example5_survey SET naming_methods_score = (naming_methods_item1 + naming_methods_item2 + naming_methods_item3) / 1.5;

UPDATE t_code_example5_survey SET complexity_methods_score = (complexity_methods_item1 + complexity_methods_item2 + complexity_methods_item3) / 1.5;

UPDATE t_code_example5_survey SET methods_score = (formatting_methods_item + clearness_methods_item + naming_methods_score + complexity_methods_score) / 3.0;

UPDATE t_code_example5_survey SET class_score = (naming_class_item + complexity_class_item + clearness_class_item) / 1.5;

UPDATE t_code_example5_survey SET total_score = (vars_score + methods_score + class_score + (overall_clearness_item * 2.0)) / 4.0;

-- mySQL table for holding the data regarding the clean code app analyzer tool

CREATE TABLE t_cc_app_analysis (
    vars_score_analyzer DECIMAL(9, 2),
    methods_score_analyzer DECIMAL(9, 2),
    class_score_analyzer DECIMAL(9, 2),
    global_score_analyzer DECIMAL(9, 2),
    total_score_analyzer DECIMAL(9, 2),
    code_example INT NOT NULL,
    
    PRIMARY KEY(code_example)
);

INSERT INTO `t_cc_app_analysis` (`vars_score_analyzer`, `methods_score_analyzer`, `class_score_analyzer`, `global_score_analyzer`, `total_score_analyzer`, `code_example`) VALUES
(8.636363636363637, 8.333333333333334, 10.0, 8.333333333333334, 8.825757575757576, 1),
(9.84126984126984, 4.375, 9.230769230769232, 10.0, 8.361759768009769, 2),
(8.53211009174312, 8.963855421686747, 9.55056179775281, 9.473684210526315, 9.130052880427247, 3),
(8.015873015873016, 3.25, 10.0, 8.333333333333334, 7.399801587301587, 4),
(8.813559322033898, 8.823529411764707, 9.591836734693878, 6.666666666666666, 8.473898033789787, 5);

-- mySQL table for holding the data regarding the final clean-code-score_survey table

CREATE TABLE t_cc_scores_final (
    id INT NOT NULL AUTO_INCREMENT,
    case_id INT NOT NULL,
    interview_duration INT,
    gender BOOLEAN,
    age INT,
    advertised_at INT,
    years_of_programming DECIMAL(9, 2),
    familiar_with_oop BOOLEAN,
    familiar_with_cc BOOLEAN,
    years_of_cpp_programming DECIMAL(9, 2),
    vars_score_survey DECIMAL(9, 2),
    methods_score_survey DECIMAL(9, 2),
    class_score_survey DECIMAL(9, 2),
    general_score_survey DECIMAL(9, 2),
    total_score_survey DECIMAL(9, 2),
    vars_score_analyzer DECIMAL(9, 2),
    methods_score_analyzer DECIMAL(9, 2),
    class_score_analyzer DECIMAL(9, 2),
    global_score_analyzer DECIMAL(9, 2),
    total_score_analyzer DECIMAL(9, 2),
    code_example INT,
    
    PRIMARY KEY(id)
);

INSERT INTO t_cc_scores_final (case_id, vars_score_survey, methods_score_survey, class_score_survey, general_score_survey, total_score_survey, code_example) SELECT case_id, vars_score, methods_score, class_score, (overall_clearness_item * 2.0), total_score, code_example FROM t_code_example1_survey;

INSERT INTO t_cc_scores_final (case_id, vars_score_survey, methods_score_survey, class_score_survey, general_score_survey, total_score_survey, code_example) SELECT case_id, vars_score, methods_score, class_score, (overall_clearness_item * 2.0), total_score, code_example FROM t_code_example2_survey;

INSERT INTO t_cc_scores_final (case_id, vars_score_survey, methods_score_survey, class_score_survey, general_score_survey, total_score_survey, code_example) SELECT case_id, vars_score, methods_Score, class_score, (overall_clearness_item * 2.0), total_score, code_example FROM t_code_example3_survey;

INSERT INTO t_cc_scores_final (case_id, vars_score_survey, methods_score_survey, class_score_survey, general_score_survey, total_score_survey, code_example) SELECT case_id, vars_score, methods_score, class_score, (overall_clearness_item * 2.0), total_score, code_example FROM t_code_example4_survey;

INSERT INTO t_cc_scores_final (case_id, vars_score_survey, methods_score_survey, class_score_survey, general_score_survey, total_score_survey, code_example) SELECT case_id, vars_score, methods_score, class_score, (overall_clearness_item * 2.0), total_score, code_example FROM t_code_example5_survey;

UPDATE t_cc_scores_final AS cc_scores
    INNER JOIN t_control_variables_survey cv ON cc_scores.case_id = cv.case_id
    SET cc_scores.interview_duration = cv.interview_duration,
    cc_scores.gender = cv.gender,
    cc_scores.age = cv.age,
    cc_scores.advertised_at = cv.advertised_at,
    cc_scores.years_of_programming = cv.years_of_programming,
    cc_scores.familiar_with_oop = cv.familiar_with_oop,
    cc_scores.familiar_with_cc = cv.familiar_with_cc,
    cc_scores.years_of_cpp_programming = cv.years_of_cpp_programming;
    
UPDATE t_cc_scores_final AS cc_scores
    INNER JOIN t_cc_app_analysis aa ON cc_scores.code_example = aa.code_example
    SET cc_scores.vars_score_analyzer = aa.vars_score_analyzer,
    cc_scores.vars_score_analyzer = aa.vars_score_analyzer,
    cc_scores.methods_score_analyzer = aa.methods_score_analyzer,
    cc_scores.class_score_analyzer = aa.class_score_analyzer,
    cc_scores.global_score_analyzer = aa.global_score_analyzer,
    cc_scores.total_score_analyzer = aa.total_score_analyzer;