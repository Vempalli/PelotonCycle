package com.LeetCode.Algorithms;

public class FindDuplicates {

	public static void main(String[] args) {
		int[] input = {1,2,2,2,2};
		int[] input1 = {1,2,3,3,5};
		//findDuplicates(input);
		findXor(input1);
	}

	private static void findXor(int[] input1) {
		int duplicateNumber = 0;
		for(int i = 0; i < input1.length; i++){
			if(input1[Math.abs(input1[i] - 1)] < 0){
				duplicateNumber = (Math.abs(input1[i]));
				break;
			}
			else{
				input1[Math.abs(input1[i]) - 1] = -1 * input1[Math.abs(input1[i]) - 1];
			}
		}
		System.out.println(duplicateNumber);
		//XOR of 1 to N
		int allXOR = 1;
		for(int i = 2; i <= input1.length; i++){
			allXOR = allXOR ^ i;
		}
		//allXOR XOR arrayElements
		int xor2 = input1[0];
		for(int i = 1 ; i <input1.length; i++){
			xor2 = xor2 ^ input1[i];
		}
		System.out.println(xor2);
		System.out.println(allXOR);
		System.out.println(xor2 ^ allXOR);
	}

	private static void findDuplicates(int[] input) {
		//1 to N range. Array Size N+1
		for(int i = 0; i < input.length; i++){
			if(input[Math.abs(input[i])] < 0){
				System.out.println(Math.abs(input[i]));
				continue;
			}
			else{
				input[Math.abs(input[i])] = -1 * input[Math.abs(input[i])];
			}
		}
	}

}
