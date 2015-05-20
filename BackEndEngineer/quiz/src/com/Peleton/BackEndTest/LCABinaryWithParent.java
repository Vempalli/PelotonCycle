package com.LeetCode.Algorithms;

import java.util.ArrayList;
import java.util.List;


public class LCABinaryWithParent {
	private static class TreeNode {
		TreeNode left, right, parent;
		int value;
		//constructor
		public TreeNode(int value) {
			this.left = null;
			this.right = null;
			this.parent = null;
			this.value = value;
		}

	}
	public static void main(String[] args) {
		TreeNode n1 = new TreeNode(1);
		TreeNode n2 = new TreeNode(2);
		TreeNode n3 = new TreeNode(3);
		TreeNode n4 = new TreeNode(4);
		TreeNode n5 = new TreeNode(5);
		TreeNode n6 = new TreeNode(6);
		TreeNode n7 = new TreeNode(7);
		TreeNode n8 = new TreeNode(8);
		
		n1.left = n2;
		n1.right = n3;
		
		n2.left = n4;
		n2.right = n5;
		n2.parent = n1;
		
		n3.left = n6;
		n3.right = n7;
		n3.parent = n1;
		
		n4.left = n8;
		n4.parent = n2;
		
		n5.parent = n2;
		n6.parent = n3;
		n7.parent = n3;
		n8.parent = n4;
		
		TreeNode lca = lcaWithSpace(n1, n4, n3);
		System.out.println(lca.value);
		TreeNode lca1 = LCAWithOutExtraSpace(n4,n3);
		System.out.println(lca1.value);
		
	}
	
	//store visited nodes in a list and go up with parent pointer. 
	//Added space of O(h). Time complexity is also O(h)
	//h is height of tree
	private static TreeNode lcaWithSpace(TreeNode root, TreeNode one, TreeNode two) {
		List<TreeNode> visited = new ArrayList<TreeNode>();
		while(one!=null || two!=null){
			if(one!=null){
				if(visited.contains(one)){
					return one;
				}
				else{
					visited.add(one);
					one = one.parent;
				}
			}
			if(two!=null){
				if(visited.contains(two)){
					return two;
				}
				else{
					visited.add(two);
					two = two.parent;
				}
			}
		}
		return null;
	}
	
	//Approach avoiding additional space

	//Methods that gives height of node from root
	public static int getHeight(TreeNode node){
		int height = 0;
		while(node!=null){
			height++;
			node = node.parent;
		}
		return height;
	}
	
	// Get LCA by using heights function above. Get difference of heights.
	// Traverse deeper node up by difference of heights. From this level check both nodes if equal return them else go up..
	public static TreeNode LCAWithOutExtraSpace(TreeNode one, TreeNode two){
		int height1 = getHeight(one);
		int height2 = getHeight(two);
		if(height2>height1){
			//swap heights
			int temp = height1;
			height1 = height2;
			height2 = temp;
			//swap nodes
			TreeNode tempNode = one;
			one = two;
			two = tempNode;
		}
		int difference = height1 - height2;
		for(int i = 0; i < difference; i++){
			one = one.parent;
		}
		while(one!=null && two!=null){
			if(one==two) return one;
			one = one.parent;
			two = two.parent;
		}
		return null;
	}
}
