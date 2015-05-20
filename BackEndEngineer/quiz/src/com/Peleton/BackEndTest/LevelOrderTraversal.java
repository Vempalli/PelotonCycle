package com.LeetCode.Algorithms;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class LevelOrderTraversal {
	public static class TreeNode {
		int value;
		TreeNode left, right;
		TreeNode(int x) {
			value = x;
		}
	}

	public static void main(String[] args) {
		List<List<Integer>> result = new ArrayList<List<Integer>>();
		TreeNode n1 = new TreeNode(-8);
		TreeNode n2 = new TreeNode(3);
		TreeNode n3 = new TreeNode(0);
		TreeNode n4 = new TreeNode(-8);
		TreeNode n5 = new TreeNode(-1);
		TreeNode n6 = new TreeNode(8);

		n1.left = n2;
		n1.right = n3;

		n2.left = n4;
		n2.right = null;
		
		n3.left = null;
		n3.right = null;
		
		n4.left = null;
		n4.right = n5;
		
		n5.left = null;
		n5.right = n6;
		
		result = levelOrder(n1);
		System.out.println(result);
	}

	private static List<List<Integer>> levelOrder(TreeNode root) {
		List<List<Integer>> mainList = new ArrayList<List<Integer>>();
		if (root == null){
			return new ArrayList<List<Integer>>();
		}
		Queue<TreeNode> q = new LinkedList<TreeNode>();
		TreeNode last = null;
		q.add(root);
		List<Integer> subList = new ArrayList<Integer>();
		subList.add(root.value);
		mainList.add(subList);
		TreeNode rightMost = root;
		int depth = 1;
		subList = new ArrayList<Integer>();
		while (!q.isEmpty()) {
			rightMost = rightMost==null?last:rightMost;
			TreeNode node = q.poll();
			if (node.left != null || node.right != null) {
				if (node.left != null) {
					q.add(node.left);
					last = node.left;
					subList.add(node.left.value);

				}
				if (node.right != null) {
					q.add(node.right);
					last = node.right;
					subList.add(node.right.value);

				}
				if (node == rightMost) {
					depth++;
					if (node.right == null && node.left == null) {
						rightMost = last;
					} else {
						rightMost = (node.right != null) ? node.right
								: node.left;
					}
					mainList.add(subList);
					subList = new ArrayList<Integer>();
				}
			}
			// This node does not have left or right child
			else {
				if (node == rightMost) {
					depth++;
					rightMost = (node.right != null) ? node.right : node.left;
					if (subList.size() > 0) {
						mainList.add(subList);
						subList = new ArrayList<Integer>();
					}
				}
			}
		}
		if(subList.size() > 0){
			mainList.add(subList);
		}
		return mainList;
	}

}
