#!/bin/bash
problems="10 20 50 100 200 500 1000"

rm -f "run.txt"
for problem in $problems
do
    saidaConstruction="saida_"$problem"_const.csv"
    serrosConstruction="serros_"$problem"_const.log"
    saidaLocal="saida_"$problem"_local.csv"
    serrosLocal="serros_"$problem"_local.log"
    echo "java -Xms512m -Xmx512m -cp dist/Common-Due-Date-Scheduling.jar br.usp.lti.cdds.Main $problem 1 > $saidaConstruction 2> $serrosConstruction" >> "run.txt"
    echo "java -Xms512m -Xmx512m -cp dist/Common-Due-Date-Scheduling.jar br.usp.lti.cdds.Main $problem 2 > $saidaLocal 2> $serrosLocal" >> "run.txt"
done

cat "run.txt" | xargs -I CMD -P 4 bash -c CMD &
wait
