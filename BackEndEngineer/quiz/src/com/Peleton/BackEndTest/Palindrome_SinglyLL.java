package com.LeetCode.Algorithms;

public class Palindrome_SinglyLL {

	public static class ListNode {
		char val;
		ListNode next;

		ListNode(char x) {
			val = x;
		}
	}

	public static void main(String[] args) {
		ListNode node1 = new ListNode('R');
		ListNode node2 = new ListNode('A');
		ListNode node3 = new ListNode('C');
		ListNode node4 = new ListNode('E');
		ListNode node5 = new ListNode('C');
		ListNode node6 = new ListNode('A');
		ListNode node7 = new ListNode('R');
		node1.next = node2;
		node2.next = node3;
		node3.next = node4;
		node4.next = node5;
		node5.next = node6;
		node6.next = node7;
		node7.next = null;
		
		System.out.println(isPalindrome(node1));
		
	}

	private static boolean isPalindrome(ListNode head) {
		//Find mid point of LL
		ListNode fast = head;
		ListNode slow = head;
		//to terminate first list we need prev to slow ptr
		ListNode prev_to_slow = head;
		ListNode secondList = null;
		//store mid value in odd case
		ListNode midPoint = null;
		boolean result = false;
		if(head!=null && head.next!=null){
			while(fast!=null && fast.next!=null){
				fast = fast.next.next;
				prev_to_slow = slow;
				slow = slow.next;
			}
		}
		//if fast!=null then it is an odd numbered list
		if(fast!=null){
			midPoint = slow;
			slow = slow.next;
		}
		secondList = slow;
		prev_to_slow.next = null;
		//Reverse second half
		secondList = reverseList(secondList);
		//compare second half and first half
		result = compare(head,secondList);
		//construct back as actual list
		secondList = reverseList(secondList);
		if(midPoint!=null){
			prev_to_slow.next = midPoint;
			midPoint.next = secondList;
		}
		else{
			prev_to_slow.next = secondList;
		}
		return result;
	}

	private static boolean compare(ListNode head, ListNode secondList) {
		ListNode temp1 = head;
		ListNode temp2 = secondList;
		while(temp1!=null && temp2!= null){
			if(temp1.val != temp2.val){
				return false;
			}
			temp1 = temp1.next;
			temp2 = temp2.next;
		}
		if(temp1 == null && temp2 == null){
			return true;
		}
		return false;
	}

	private static ListNode reverseList(ListNode head) {
		ListNode prev = null;
		ListNode curr = head;
		ListNode next = null;
		while(curr!=null){
			next = curr.next;
			curr.next = prev;
			prev = curr;
			curr = next;
		}
		head = prev;
		return head;
	}
}
