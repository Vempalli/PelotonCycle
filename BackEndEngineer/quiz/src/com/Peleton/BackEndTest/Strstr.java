package com.LeetCode.Algorithms;

/*
Implement strStr().
Returns the index of the first occurrence of needle in haystack, 
or -1 if needle is not part of haystack.
 */
public class Strstr {

	public static void main(String[] args) {
		String bigString = "mississippi";
		String smallString = "";
		int index = strStr(bigString,smallString);
		System.out.println(index);
	}
	//O(nm) - n length of bigString m length of small string
	private static int strStr(String bigString, String smallString) {
		for(int index = 0; ; index++){
			//for(int subIndex = 0; subIndex< needle.length() + 1; subIndex++){
			for(int subIndex = 0; ; subIndex++){
				if(subIndex == smallString.length()) return index;
				if(index+subIndex == bigString.length()) return -1;
				if(smallString.charAt(subIndex)!= bigString.charAt(index+subIndex)) break;
			}
		}
	}

}
