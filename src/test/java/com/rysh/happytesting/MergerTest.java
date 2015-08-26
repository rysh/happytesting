package com.rysh.happytesting;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.hamcrest.Matcher;
import org.junit.Ignore;
import org.junit.Test;

public class MergerTest {

    @Test
    public void 普通のJunitテスト() {
        int start = 1;
        int thredshold = 30;
        int max = 40;
        List<Integer> listA = makeList(start, thredshold);
        List<Integer> listB = makeList(thredshold + 1, max);
        List<Integer> actual = new SampleLogic(listA, listB).merge(30, 20, 10).stream().collect(Collectors.toList());
        assertThat(actual.size(), is(30));
        assertThat(actual, hasItems(1, 20, 31, 40));
        assertThat(actual, not(hasAnyOfItems(21, 30)));
    }

    static List<Integer> makeList(int from, int to) {
        List<Integer> results = new ArrayList<>();
        for (int i = to; i >= from; i--) {
            results.add(i);
        }
        Collections.sort(results);
        return results;
    }

    @Test
    public void Readable_Junitテスト() {
        checkContainingOrNot("A:1~30,B:31~40", resultAsIds(1, 30, 40), hasItems(1, 20, 31, 40), not(hasAnyOfItems(21, 30)));
        checkContainingOrNot("A:1~20,B:21~35", resultAsIds(1, 20, 30), hasItems(1, 20, 30), not(hasAnyOfItems(31)));
        // たくさん続く
    }

    private void checkContainingOrNot(String message, List<Integer> actual, Matcher<Iterable<Integer>> hasItems,
            Matcher<Iterable<Integer>> not) {
        assertThat(message, actual, hasItems);
        assertThat(message, actual, not);
    }

    /**
     * example: 1, 10 , 40 -> listA (1~10) + listB (11~40) -> merged
     */
    static List<Integer> resultAsIds(int start, int thredshold, int max) {
        List<Integer> listA = makeList(start, thredshold);
        List<Integer> listB = makeList(thredshold + 1, max);
        return new SampleLogic(listA, listB).merge(30, 20, 10).stream().collect(Collectors.toList());
    }

    @Test
    public void ランダムテスト() {
        for (int i = 0; i < 1000; i++) {
            int sizeOfA = (int) (Math.random() * 30 + 1);
            int sizeOfB = (int) (Math.random() * 30 + 1);
            
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

    @Test
    public void 総当たりテスト() {
        IntStream.range(0, 32).forEach(sizeOfA -> IntStream.range(1, 32).forEach(sizeOfB -> {
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
        }));
    }

    @Ignore
    @Test
    public void listAが多いケース() {
        // ランダムテストで十分なので実行不要(デバッグしたいときに使えるように残している)
        List<Integer> actual = resultAsIds(1, 30, 40);
        assertThat("1, 30, 40", actual.size(), is(30));
        assertThat("1, 30, 40", actual, hasItems(1, 20, 31, 40));
        assertThat("1, 30, 40", actual, not(hasAnyOfItems(21, 30)));
    }

    // hamcrestに無いので。
    public static <T> org.hamcrest.Matcher<java.lang.Iterable<T>> hasAnyOfItems(@SuppressWarnings("unchecked") T... items) {

        List<Matcher<? super Iterable<T>>> all = new ArrayList<Matcher<? super Iterable<T>>>(items.length);
        for (T element : items) {
            all.add(org.hamcrest.core.IsCollectionContaining.<T> hasItem(element));
        }

        return anyOf(all);
    }
}
