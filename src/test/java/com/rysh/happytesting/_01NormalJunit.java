package com.rysh.happytesting;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.hamcrest.Matcher;
import org.junit.Test;

public class _01NormalJunit {

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

    // hamcrestに無いので。
    public static <T> org.hamcrest.Matcher<java.lang.Iterable<T>> hasAnyOfItems(@SuppressWarnings("unchecked") T... items) {

        List<Matcher<? super Iterable<T>>> all = new ArrayList<Matcher<? super Iterable<T>>>(items.length);
        for (T element : items) {
            all.add(org.hamcrest.core.IsCollectionContaining.<T> hasItem(element));
        }

        return anyOf(all);
    }
}
