package com.rysh.happytesting;

import static com.rysh.happytesting._01NormalJunit.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.List;
import java.util.stream.Collectors;

import org.hamcrest.Matcher;
import org.junit.Test;

public class _02ReadableJunit {

    @Test
    public void Readable_Junitテスト() {
        checkContainingOrNot("A:1~30,B:31~40", resultAsIds(1, 30, 40), hasItems(1, 20, 31, 40), not(hasAnyOfItems(21, 30)));
        checkContainingOrNot("A:1~20,B:21~35", resultAsIds(1, 20, 30), hasItems(1, 20, 30), not(hasAnyOfItems(31)));
        // たくさん続く
    }

    /**
     * example: 1, 10 , 40 -> listA (1~10) + listB (11~40) -> merged
     */
    static List<Integer> resultAsIds(int start, int thredshold, int max) {
        List<Integer> listA = makeList(start, thredshold);
        List<Integer> listB = makeList(thredshold + 1, max);
        return new SampleLogic(listA, listB).merge(30, 20, 10).stream().collect(Collectors.toList());
    }

    private void checkContainingOrNot(String message, List<Integer> actual, Matcher<Iterable<Integer>> hasItems,
            Matcher<Iterable<Integer>> not) {
        assertThat(message, actual, hasItems);
        assertThat(message, actual, not);
    }
}
