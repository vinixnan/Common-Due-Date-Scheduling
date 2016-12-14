#!/bin/bash
generation=1000
allcross="1.0"
allmuta="0.5"
parcels="0.9"
max=8

allcrossType="PMXCrossover OnePointCrossover TwoPointCrossover"
allmutaType="SwapMutation BitFlipMutationBackward BitFlipMutationFoward SwapMutationInternalBefore SwapMutationInternalAfter"

allcrossType="PMXCrossover"
allmutaType="SwapMutation BitFlipMutation"
crossType="PMXCrossover"

rm -f "run.txt"
for parcel in $parcels
do
    for mutaType in $allmutaType
    do
        for cross in $allcross
        do
            for muta in $allmuta
            do
				for i in `seq 1 $max`
				do
					saida="result/saida_"$generation"_"$cross"_"$muta"_"$crossType"_"$mutaType"_"$parcel"_"$i
					erros="result/erros_"$generation"_"$cross"_"$muta"_"$crossType"_"$mutaType"_"$parcel"_"$i
					#echo $saida
					echo "java -Xms1024m -Xmx1024m -cp dist/Common-Due-Date-Scheduling.jar br.usp.lti.cdds.MainCompleteMH $generation $cross $muta $crossType $mutaType $parcel > $saida 2> $erros" >> "run.txt"
				done
            done
        done
    done
done

cat "run.txt" | xargs -I CMD -P 3 bash -c CMD &
wait
