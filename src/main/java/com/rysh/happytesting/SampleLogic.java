package com.rysh.happytesting;

import java.util.ArrayList;
import java.util.List;

public class SampleLogic {

    private List<Integer> listA;
    private List<Integer> listB;

    public SampleLogic(List<Integer> listA, List<Integer> listB) {
        this.listA = listA;
        this.listB = listB;
    }

    public List<Integer> merge(int maxNewsItems, int threadsHoldA, int threadsHoldB) {
        if (listA.size() + listB.size() <= maxNewsItems) {
            return mergeLists(listA, listB);
        }
        if (listA.size() >= threadsHoldA && listB.size() >= threadsHoldB) {
            return mergeLists(listA.subList(0, threadsHoldA), listB.subList(0, threadsHoldB));
        }
        if (listA.size() <= threadsHoldA) {
            return mergeLists(listA, listB.subList(0, maxNewsItems - listA.size()));
        }

        // ここを通るのは listB.size() <= listBThreadsHold のケース
        return mergeLists(listA.subList(0, maxNewsItems - listB.size()), listB);

    }

    private List<Integer> mergeLists(List<Integer> listA, List<Integer> listB) {
        List<Integer> list = new ArrayList<>(listA);
        list.addAll(listB);
        return list;
    }

    public boolean hasOtherNews(int maxNewsItems) {
        return listA.size() + listB.size() > maxNewsItems;
    }

}
