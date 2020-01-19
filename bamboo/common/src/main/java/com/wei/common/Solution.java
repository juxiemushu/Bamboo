package com.wei.common;


public class Solution {

    public boolean isPowerOfThree(int n) {
       if(n <= 0) {
           return false;
       }

       int max = 1162261467;
       if(max % n == 0){
           return true;
       }
       return false;
    }

}
