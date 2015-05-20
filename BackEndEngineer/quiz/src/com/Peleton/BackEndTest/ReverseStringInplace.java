package com.LeetCode.Algorithms;

public class ReverseStringInplace {
	
	public static void main(String[] args) {
		String input = "This is a sample string";
		char[] str = input.toCharArray();
		reverseString(str,0,input.length());
		for(int i = 0 ; i < str.length; i++){
			System.out.print(str[i]);
		}
		//System.out.println(input);
	}
	//Note: Covert in to char array and again convert back in to string involved if don't want this just return void and print char array in main fun
	//If don't want to use additional space for string buffer use this - O(n) and O(1) space
	private static String reverseString(char[] str, int begin, int end) {
		for(int i = 0; i < (end-begin)/2; i++){
			char temp = str[begin+i];
			str[begin+i] = str[end-i-1];
			str[end-i-1] = temp;
		}
		return new String(str);
	}
	

}
