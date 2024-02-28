Das Projekt "cppCompiler" wurde im Rahmen meiner Masterarbeit an der Technischen Universität Chemnitz realisiert.
Es ist kein fertiger C++-Compiler, sondern beinhaltet ein statisches Clean-Code-Analyse-Tool.
Dabei bezieht sich der umgesetzte Parser lediglich auf ein Subset von C++.
Dabei wird Java-Code verwendet, um C++-Code aus einer lokalen Datei zu scannen. Nach dem Scannen ist der Code in sogenannte Tokens (lexikalische Einheiten) aufgeteilt.
Aus diesen Tokens wird während des Parsens ein Abstrakter Syntaxbaum (AST).
Während des Parsing-Vorgangs werden Clean-Code-Heuristiken aus dem Clean-Code-Buch von Robert C. Martin geprüft. Eine Ad-Hoc-Matrix wird aufgestellt.
Zusätzlich werden Refactoring-Hinweise gesammelt und in der Konsole nach dem Durchlaufen des Programms ausgegeben.
Für die Bewertung des Eingangscodes werden Werte für die vier Entitäten (Variablen, Methoden/Funktionen, Klassen, globaler Raum) in eine CSV-Datei geschrieben.
Zusätzlich wird ein Gesamtscore aus den Werten für die vier Entitäten berechnet. Diese Werte werden für den Review-Prozess des Refactoring-Tools benötigt.
Für das Review wurde ein checklistenbasiertes Code-Review mit 60 teilnehmenden Programmierenden durchgeführt, denen Items zu 5 C++-Codes gezeigt wurden.
Die Ergebnisse wurden in eine MySQL-Tabelle gespeichert. Anschließend wurden für die vier Entitäten (Variablen, Funktionen/Methoden, Klassen, Gesamt) Werte zwischen 0 und 10 berechnet.
Mithilfe von R und der linearen Regression konnten statistisch signifikante Einflüsse der unabhängigen Variablen (jeweils die vier Werte aus der Umfrage) auf die
abhängige Variable (Daten aus dem Analyse-Tool) gefunden werden.

Verwendung: Obwohl das Tool fehleranfällig ist und nicht mit allen Codebeispielen funktioniert, können Sie es gerne ausprobieren.
Es wurde mit den fünf Code-Beispielen aus der Studie, aber auch mit einigen weiteren Code-Beispielen aus den Fächern Grundlagen
der Informatik und Informatik II an der TU Chemnitz getestet.
Die Main-Funktion befindet sich unter src/compiler/TestCompiler.java. In der Main-Funktion muss der Pfad der Testdatei eingetragen werden.
Sollte das Tool abstürzen, schauen Sie sich das Verhalten des Tools im Debugger an. Der Fehler befindet sich höchstwahrscheinlich im Parse-Vorgang bei der Erstellung des ASTs.

