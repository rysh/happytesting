package com.rysh.happytesting;

import static com.rysh.happytesting._02ReadableJunit.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.contrib.theories.Theories;
import org.junit.contrib.theories.Theory;
import org.junit.runner.RunWith;

import com.pholser.junit.quickcheck.ForAll;
import com.pholser.junit.quickcheck.generator.InRange;

@RunWith(Theories.class)
public class _05QuickCheck {
    @Theory
    public void quicktest(@ForAll @InRange(min = "0", max = "31") Integer sizeOfA, 
                          @ForAll @InRange(min = "0", max = "31") Integer sizeOfB) {

        List<Integer> actual = resultAsIds(1, sizeOfA, sizeOfA + sizeOfB);
        Map<Boolean, List<Integer>> collect = actual.stream().collect(Collectors.partitioningBy(n -> n <= sizeOfA));
        List<Integer> valuesOfAinResult = collect.get(true);
        List<Integer> valuesOfBinResult = collect.get(false);
        String params = String.format(" normal: %s / %s hr: %s / %s", valuesOfAinResult.size(), sizeOfA, valuesOfBinResult.size(), sizeOfB);

        assertThat("合計数" + params, actual.size(), is((sizeOfA + sizeOfB >= 30) ? 30 : sizeOfA + sizeOfB));
        if (sizeOfA + sizeOfB >= 30) {
            if (sizeOfA <= 20) {
                assertThat("listAが少ない場合のlistAの数" + params, valuesOfAinResult.size(), is(sizeOfA));
                assertThat("listAが少ない場合のlistBの数" + params, valuesOfBinResult.size(), is(30 - sizeOfA));
            } else if (sizeOfB <= 10) {
                assertThat("listBが少ない場合のlistAの数" + params, valuesOfAinResult.size(), is(30 - sizeOfB));
                assertThat("listBが少ない場合のlistBの数" + params, valuesOfBinResult.size(), is(sizeOfB));
            } else {
                assertThat("共に閾値以上の件数がある場合のlistAの数" + params, valuesOfAinResult.size(), is(20));
                assertThat("共に閾値以上の件数がある場合のlistBの数" + params, valuesOfBinResult.size(), is(10));
            }
        } else {
            assertThat("合計が３０件未満の場合のlistAの数" + params, valuesOfAinResult.size(), is(sizeOfA));
            assertThat("合計が３０件未満の場合のlistBの数" + params, valuesOfBinResult.size(), is(sizeOfB));
        }
    }
}
