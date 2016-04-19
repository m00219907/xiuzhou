package com.jsycloud.rs.xiuzhou;


public class CommonTools {

    public static boolean newVersion(String first, String second) {
        String[] firsts = first.split("\\.");
        String[] seconds = second.split("\\.");
        int firstToNum = Integer.parseInt(firsts[0]) * 100 + Integer.parseInt(firsts[1]) * 10 + Integer.parseInt(firsts[2]);
        int secondToNum = Integer.parseInt(seconds[0]) * 100 + Integer.parseInt(seconds[1]) * 10 + Integer.parseInt(seconds[2]);
        if(firstToNum > secondToNum){
            return true;
        }
        return false;
    }
}
