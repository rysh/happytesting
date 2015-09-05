package com.rysh.happytesting;

import static com.rysh.happytesting._01NormalJunit.*;
import static com.rysh.happytesting._02ReadableJunit.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.contrib.theories.DataPoints;
import org.junit.contrib.theories.Theories;
import org.junit.contrib.theories.Theory;
import org.junit.runner.RunWith;

@RunWith(Theories.class)
public class _03Paramaterized {

    public static class Fixture {
        private final String label;
        private final int start;
        private final int threshold;
        private final int end;
        private final List<Integer> includes;
        private final List<Integer> excludes;

        public Fixture(String label, int inputStart, int inputThreshold, int inputEnd, List<Integer> includes, List<Integer> excludes) {
            this.label = label;
            this.start = inputStart;
            this.threshold = inputThreshold;
            this.end = inputEnd;
            this.includes = includes;
            this.excludes = excludes;

        }
    }

    @DataPoints
    public static Fixture[] getParameters() {
        return new Fixture[] {
            new Fixture("A:1~30,B:31~40", 1, 30, 40, Arrays.asList(1, 20, 31, 40), Arrays.asList(21, 30)),
            new Fixture("A:1~20,B:21~35", 1, 20, 30, Arrays.asList(1, 20, 30), Arrays.asList(31))
        };
    }

    @Theory
    public void checkContainingOrNot(Fixture f) {
        assertThat(f.label, resultAsIds(f.start, f.threshold, f.end), hasItems(f.includes.toArray(new Integer[0])));
        assertThat(f.label, resultAsIds(f.start, f.threshold, f.end), not(hasAnyOfItems(f.excludes.toArray(new Integer[0]))));
    }

}
