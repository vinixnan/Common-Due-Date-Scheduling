#!/bin/bash
problems="10 20 50 100 200 500 1000"

rm -f "run.txt"
for problem in $problems
do
    saida="saida_"$problem".csv"
    serros="serros_"$problem".log"	
    echo "java -Xms512m -Xmx512m -cp dist/Common-Due-Date-Scheduling.jar br.usp.lti.cdds.Main $problem > $saida 2> $serros" >> "run.txt"
  
done

cat "run.txt" | xargs -I CMD -P 4 bash -c CMD &
wait
