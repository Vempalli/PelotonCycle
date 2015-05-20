package com.LeetCode.Algorithms;

public class InorderSuccessorBST {
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
		TreeNode n1 = new TreeNode(20);
		TreeNode n2 = new TreeNode(8);
		TreeNode n3 = new TreeNode(22);
		TreeNode n4 = new TreeNode(4);
		TreeNode n5 = new TreeNode(12);
		TreeNode n6 = new TreeNode(10);
		TreeNode n7 = new TreeNode(14);
		
		n1.left = n2;
		n1.right = n3;
		n1.parent = null;
		
		n2.left = n4;
		n2.right = n5;
		n2.parent = n1;
		
		n5.left = n6;
		n5.right = n7;
		n5.parent = n2;
		n3.parent = n1;
		n4.parent = n2;
		n6.parent = n5;
		n7.parent = n5;
		
		TreeNode node = inOrderSuccessor(n1,n2);
		TreeNode res = inOrderSuccessorWithParent(n1,n7);
		System.out.println(node.value);
		System.out.println(res.value);
	}
	//O(h)
	private static TreeNode inOrderSuccessorWithParent(TreeNode root, TreeNode node) {
		if(node.right!=null){
			return minimumNode(node.right);
		}
		else{
			//go up using parent pointer till the node is a left child of its parent
			TreeNode temp = node.parent;
			while(temp!=null && node == temp.right){
				node = temp;
				temp = temp.parent;
			}
			return temp;
		}
	}
	//O(h)
	private static TreeNode inOrderSuccessor(TreeNode root, TreeNode node) {
		//If node.right exists return minimum value in node.right tree
		TreeNode prev = null;
		if(node.right != null){
			return minimumNode(node.right);
		}
		else{
			//start from root if node.val is < than root.val traverse left else right
			while(root!=null){
				if(node.value < root.value){
					prev = root;
					root = root.left;
				}
				else if (node.value > root.value){
					root = root.right;
				}
				else break;
			}
			return prev;
		}
	}
	private static TreeNode minimumNode(TreeNode node) {
		TreeNode current = node;
		while(current.left!=null){
			current = current.left;
		}
		return current;
	}

}
