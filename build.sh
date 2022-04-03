#!/bin/bash
printf "compilar fuentes: "
javac src/*.java -cp "lib/*" -d tmp

printf "DONE.\nextraer dependencias: "
unzip -oqq "lib/*" -d tmp &> /dev/null

printf "DONE.\nempaquetar .jar: "
jar cfe IADesastres.jar Main -C tmp .

printf "DONE.\n\nIADesastres.jar generado\n"