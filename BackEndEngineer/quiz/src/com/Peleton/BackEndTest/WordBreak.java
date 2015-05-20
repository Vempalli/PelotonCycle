package com.LeetCode.Algorithms;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class WordBreak {
	public static Map<String,String> memoized = new HashMap<String,String>();
	public static void main(String[] args) {
		Set<String> dictionary = new HashSet<String>();
		dictionary.add("aaaa");
		dictionary.add("aaa");
		String input = "aaaaaaa";
		// String output = divideWordBasic2Segments(input,dictionary);
		// System.out.println(output);
		// String output1 = divideWordGeneralSolution(input, dictionary);
		// System.out.println(output1);
		 String output1 = divideWordMemoization(input, dictionary);
	        if(output1!=null){
	        	System.out.println("true");
	        }
			else{
				System.out.println("false");
			}
		
	}

	private static String divideWordBasic2Segments(String input, Set<String> dictionary) {
		int length = input.length();
		for (int i = 1; i < length; i++) {
			String prefix = input.substring(0, i);
			if (dictionary.contains(prefix)) {
				String suffix = input.substring(i, length);
				if (dictionary.contains(suffix)) {
					return prefix + " " + suffix;
				}
			}
		}
		return null;
	}

	// Worst case O(2^n) for inputs like aaaaaab and dictionary contains
	// [a,aa,aaa,aaaa,...]
	private static String divideWordGeneralSolution(String input, Set<String> dictionary) {
		if (dictionary.contains(input)) {
			return input;
		}
		int length = input.length();
		for (int i = 1; i < length; i++) {
			String prefix = input.substring(0, i);
			if (dictionary.contains(prefix)) {
				String suffix = input.substring(i, length);
				String divideSuffix = divideWordGeneralSolution(suffix,
						dictionary);
				if (divideSuffix != null) {
					return prefix + " " + divideSuffix;
				}
			}
		}
		return null;
	}
	//worst case O(n^2) -- point to consider substring is O(n) from Java version 7
	private static String divideWordMemoization(String input,Set<String> dictionary) {
		if (dictionary.contains(input)) {
			return input;
		}
		if(memoized.containsKey(input)){
			return memoized.get(input);
		}
		int length = input.length();
		for (int i = 1; i < length; i++) {
			String prefix = input.substring(0, i);
			if (dictionary.contains(prefix)) {
				String suffix = input.substring(i, length);
				String divideSuffix = divideWordMemoization(suffix,	dictionary);
				if (divideSuffix != null) {
					return prefix + " " + divideSuffix;
				}
			}
		}
		memoized.put(input, null);
		return null;
	}
	
	//programcreek
	//O(str.length * dict Size)
	public boolean wordBreak(String input, Set<String> dict) {
        boolean[] t = new boolean[input.length()+1];
        //set first to be true, why?
        //Because we need initial state
        t[0] = true; 
        
        for(int i=0; i<input.length(); i++){
            //should continue from match position
            if(!t[i]) 
                continue;
            for(String a: dict){
                int len = a.length();
                int end = i + len;
                if(end > input.length())
                    continue;
 
                if(t[end]) continue;
 
                if(input.substring(i, end).equals(a)){
                    t[end] = true;
                }
            }
        }
        return t[input.length()];
    }
}