package com.wei.common;

import java.util.PriorityQueue;

public class MedianFinder {

    private int count;
    private PriorityQueue<Integer> maxHeap;
    private PriorityQueue<Integer> minHeap;

    public MedianFinder() {
        count = 0;
        maxHeap = new PriorityQueue<>((x, y) -> y - x);
        minHeap = new PriorityQueue<>();
    }

    public void addNum(int num) {
        count ++;
        maxHeap.offer(num);
        minHeap.add(maxHeap.poll());
        if((count & 1) != 0) {
            maxHeap.add(minHeap.poll());
        }
    }

    public double findMedian() {
        if((count & 1) == 0) {
            return (double)(maxHeap.peek()+minHeap.peek())/2;
        } else {
            return maxHeap.peek();
        }
    }

}
