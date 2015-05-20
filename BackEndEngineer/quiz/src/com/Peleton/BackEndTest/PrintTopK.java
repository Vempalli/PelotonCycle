package com.LeetCode.Algorithms;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;

public class PrintTopK {

	public static void main(String[] args) {
		
		List<Integer> output = new ArrayList<Integer>();
		int[] input = {0,2,5,6,7,6,5,5,7,9,6,6,2,5,2,5};
		int k = 4;
		output = getTopKElements(input,k);
		for(int x: output){
			System.out.println(x);
		}
	}

	private static List<Integer> getTopKElements(int[] input, int k) {
		List<Integer> output = new ArrayList<Integer>();
		//Insert each element in to hash map with number as key and no.of times repeated as value
		HashMap<Integer,Integer> countMap = new HashMap<Integer,Integer>();
		//O(n)
		countMap = insertIntoMap(input);
		//Now insert the <key,Value> pair in to a priority queue considering priority on value field
		//Priority Queue needs Comparator implementation -- A Max Heap
		PriorityQueue<Entry<Integer,Integer>> queue = new PriorityQueue<Entry<Integer,Integer>>(k, new myOwnPQComperator());
		//O(KlogM) -- M is no. of Unique items
		queue.addAll(countMap.entrySet());
		//Depending on value of K poll 'k' times on Priority Queue
		//O(k)
		for(int i = 0; i < k ; i++){
			output.add(queue.poll().getKey());
		}
		return output;
	}

	private static HashMap<Integer, Integer> insertIntoMap(int[] input) {
		HashMap<Integer,Integer> countMap = new HashMap<Integer,Integer>();
		for(int index = 0; index < input.length; index++){
			int key = input[index];
			if(countMap.containsKey(key)){
				int value = countMap.get(key);
				countMap.put(key, value+1);
			}
			else{
				countMap.put(key, 1);
			}
		}
		return countMap;
	}
	
	private static class myOwnPQComperator implements Comparator<Map.Entry<Integer,Integer>>{

		@Override
		public int compare(Entry<Integer, Integer> entry1,Entry<Integer, Integer> entry2) {
			if(entry1.getValue() > entry2.getValue()){
				return -1;
			}
			else if(entry1.getValue() < entry2.getValue()){
				return 1;
			}
			else{
				return 0;
			}
		}
		
	}
}
