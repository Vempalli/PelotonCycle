package com.LeetCode.Algorithms;

public class MinimumEditDistance {

	public static void main(String[] args) {
		String word1 = "tuesday";
		String word2 = "thursday";
		int minEditDistance = minDistance(word1,word2);
		System.out.println(minEditDistance);
	}

	private static int minDistance(String word1, String word2) {
		int len1 = word1.length();
		int len2 = word2.length();
		int[][] table = new int[len1+1][len2+1];
		//First row populate from 0 to len1
		for(int i = 0; i < table[0].length; i++){
			table[0][i] = i;
		}
		//first column populate from o to len2
		for(int i = 0; i < table.length; i++){
			table[i][0] = i;
		}
		//Now populate with min value as per framed relation
		for(int i = 1; i <= len1; i++){
			for(int j = 1; j <= len2; j++){
				if(word1.charAt(i-1)==word2.charAt(j-1)){
					table[i][j] = table[i-1][j-1];
				}
				else{
					int min = Math.min(table[i-1][j-1]+1, table[i-1][j]+1);
					table[i][j] = Math.min(min, table[i][j-1]+1);
				}
			}
		}
		return table[len1][len2];
	}

}
