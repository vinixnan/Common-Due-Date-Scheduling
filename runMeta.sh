#!/bin/bash
generation=500
allcross="0.9"
allmuta="0.1"

allcrossType="OnePointCrossover TwoPointCrossover"
allmutaType="SwapMutation BitFlipMutationBackward BitFlipMutationFoward"

allcrossType="OnePointCrossover"
allmutaType="SwapMutation"

rm -f "run.txt"
for crossType in $allcrossType
do
    for mutaType in $allmutaType
    do
        for cross in $allcross
        do
            for muta in $allmuta
            do
                saida="result/saida_"$generation"_"$cross"_"$muta"_"$crossType"_"$mutaType
                erros="result/erros_"$generation"_"$cross"_"$muta"_"$crossType"_"$mutaType
                echo "java -Xms512m -Xmx512m -cp dist/Common-Due-Date-Scheduling.jar br.usp.lti.cdds.MainCompleteMH $generation $cross $muta $crossType $mutaType > $saida 2> $erros" >> "run.txt"
            done
        done
    done
done

cat "run.txt" | xargs -I CMD -P 4 bash -c CMD &
wait
