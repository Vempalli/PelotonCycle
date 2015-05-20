package com.LeetCode.Algorithms;
/*
Given an array with n objects colored red, white or blue, sort them so that objects of the 
same color are adjacent, with the colors in the order red, white and blue.
Here, we will use the integers 0, 1, and 2 to represent the color red, white, and blue respectively.
Note:
You are not suppose to use the library's sort function for this problem.

Follow up:
A rather straight forward solution is a two-pass algorithm using counting sort.
First, iterate the array counting number of 0's, 1's, and 2's, 
then overwrite array with total number of 0's, then 1's and followed by 2's.
Could you come up with an one-pass algorithm using only constant space?
*/
public class SortColors {
	public static void main(String[] args) {
		int[] input = {0, 1, 1, 0, 1, 2, 1, 2, 0, 0, 0, 1};
		input = sort(input);
		for(int index = 0; index < input.length; index++){
			System.out.print(input[index]+ " ");
		}
	}

	private static int[] sort(int[] input) {
		int low = 0;
		int mid = 0;
		int high = input.length-1;
		while(mid <= high){
			switch (input[mid]) {
			case 0:
				//swap low and mid
				if(input[low]!=input[mid]){
					int temp = input[low];
					input[low] = input[mid];
					input[mid] = temp;
				}
				low++;
				mid++;
				break;
			case 1:
				mid++;
				break;
			case 2:
				//swap mid and high
				if(input[mid]!=input[high]){
					int temp = input[mid];
					input[mid] = input[high];
					input[high] = temp;
				}
				high--;
				break;
			}
		}
		return input;
	}

}
