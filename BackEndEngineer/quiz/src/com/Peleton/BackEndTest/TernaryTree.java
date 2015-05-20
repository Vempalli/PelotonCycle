package com.LeetCode.Algorithms;

import java.util.Stack;

public class TernaryTree {

	public static void main(String[] args) {
		String expression = "a?b?c:d:e";
		Node root = convertTernaryToTree(expression);
		printTreeInorder(root);
	}

	private static void printTreeInorder(Node root) {
		if(root == null) return;
		printTreeInorder(root.left);
		System.out.println(root.variableName);
		printTreeInorder(root.right);
	}

	private static Node convertTernaryToTree(String expression) {
		//expression = expression.replaceAll("\\(", "");
		//expression = expression.replaceAll("\\)", "");
		char[] content = expression.toCharArray();
		//set root
		Node root = new Node();
		root.variableName = content[0];
		Node temp = root;
		//evaluate expression and set left right
		Stack<Node> stack = new Stack<Node>();
		for(int index = 1; index < content.length; index = index + 2){
			if(content[index] == '?'){
				//make this a left node
				temp.left = new Node();
				temp.left.variableName = content[index+1];
				stack.push(temp);
				temp = temp.left;
			}
			else if(content[index] == ':'){
				temp = stack.pop();
				temp.right = new Node();
				temp.right.variableName = content[index+1];
				temp = temp.right;
			}
		}
		return root;
	}

	public static class Node {
		Character variableName;
		Node left, right;
	}
}
