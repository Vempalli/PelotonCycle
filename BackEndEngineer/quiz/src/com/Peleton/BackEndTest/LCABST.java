package com.LeetCode.Algorithms;

public class LCABST {
	private static class TreeNode {
		TreeNode left, right;
		int value;
		//constructor
		public TreeNode(int value) {
			this.left = null;
			this.right = null;
			this.value = value;
		}
	}
	public static void main(String[] args) {
		TreeNode n1 = new TreeNode(20);
		TreeNode n2 = new TreeNode(8);
		TreeNode n3 = new TreeNode(22);
		TreeNode n4 = new TreeNode(4);
		TreeNode n5 = new TreeNode(12);
		TreeNode n6 = new TreeNode(10);
		TreeNode n7 = new TreeNode(14);
		
		n1.left = n2;
		n1.right = n3;
		
		n2.left = n4;
		n2.right = n5;
		
		n5.left = n6;
		n5.right = n7;
		
		TreeNode lca = findLCABST(n1, n4, n7);
		System.out.println(lca.value);
	}
	//O(h)
	private static TreeNode findLCABST(TreeNode root, TreeNode one, TreeNode two) {
		while(root != null){
			if(root.value > one.value && root.value > two.value){
				root = root.left;
			}
			else if(root.value < one.value && root.value < two.value){
				root = root.right;
			}
			else{
				break;
			}
		}
		return root;
	}

}
