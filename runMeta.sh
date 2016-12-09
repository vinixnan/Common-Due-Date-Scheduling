#!/bin/bash
generation=1000
allcross="1.0"
allmuta="0.5 0.2"
max=2

allcrossType="PMXCrossover OnePointCrossover TwoPointCrossover"
allmutaType="SwapMutation BitFlipMutationBackward BitFlipMutationFoward SwapMutationInternalBefore SwapMutationInternalAfter"

#allcrossType="OnePointCrossover"
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
				for i in `seq 1 $max`
				do
					saida="result/saida_"$generation"_"$cross"_"$muta"_"$crossType"_"$mutaType"_"$i
					erros="result/erros_"$generation"_"$cross"_"$muta"_"$crossType"_"$mutaType"_"$i
					#echo $saida
					echo "java -Xms512m -Xmx512m -cp dist/Common-Due-Date-Scheduling.jar br.usp.lti.cdds.MainCompleteMH $generation $cross $muta $crossType $mutaType > $saida 2> $erros" >> "run.txt"
				done
            done
        done
    done
done

cat "run.txt" | xargs -I CMD -P 4 bash -c CMD &
wait
