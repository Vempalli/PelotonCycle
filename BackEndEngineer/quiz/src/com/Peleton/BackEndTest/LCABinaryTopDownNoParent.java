package com.LeetCode.Algorithms;

public class LCABinaryTopDownNoParent {

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
		
		n3.left = n6;
		n3.right = n7;
		
		n4.left = n8;
		
		//LCA with root pointer but no parent given
		TreeNode lca = LCA(n1, n5,n7);
		System.out.println(lca.value);
		
		//LCA Bottom Up Approach
		TreeNode lcaBU = LCABottomUP(n1,n5,n7);
		System.out.println(lcaBU.value);
	}
	/*O(n) all cases
	 */
	private static TreeNode LCABottomUP(TreeNode root, TreeNode one, TreeNode two) {
		if(root == null) return null;
		if(root == one || root == two) return root;
		TreeNode left = LCABottomUP(root.left, one, two);
		TreeNode right = LCABottomUP(root.right, one, two);
		if(left != null && right != null){
			return root;
		}
		return (left!=null)?left:right;
	}
	/*The worst case the complexity could go up to O(n2) with degenerate tree
	 * Given root of the tree - Top down approach
	 * @param root
	 * @param one
	 * @param two
	 * @return
	 */
	private static TreeNode LCA(TreeNode root, TreeNode one, TreeNode two) {
		if(root == one || root == two){
			return root;
		}
		int countMatches = getCountMatch(root.left,one,two);
		//this count is one if only one of nodes exist in the left subtree so obviously other node will be on right and
		//current node is our required answer
		if(countMatches == 1){
			return root;
		}
		//If both are present in left then we have to get lowest common one so dig deep in to left
		if(countMatches == 2){
			return LCA(root.left,one,two);
		}
		else{
			//It means both nodes are present in right side. Dig deep on right
			return LCA(root.right,one,two);
		}
	}
	private static int getCountMatch(TreeNode root, TreeNode one, TreeNode two) {
		if(root == null){
			return 0;
		}
		int match = getCountMatch(root.left, one, two)+getCountMatch(root.right, one, two);
		if(root == one || root == two){
			return 1 + match;
		}
		else{
			return match;
		}
	}
	
	
}
