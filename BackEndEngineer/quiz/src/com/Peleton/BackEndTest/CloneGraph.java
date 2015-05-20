package com.LeetCode.Algorithms;

public class CloneGraph {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	/*
	public UndirectedGraphNode cloneGraph(UndirectedGraphNode node) {
        if(node == null){
            return null;
        }
        //HashMap and Queue
        Queue<UndirectedGraphNode> queue = new LinkedList<UndirectedGraphNode>();
        Map<UndirectedGraphNode,UndirectedGraphNode> map = new HashMap<UndirectedGraphNode,UndirectedGraphNode>();
        queue.add(node);
        UndirectedGraphNode cloneNode = new UndirectedGraphNode(node.label);
        map.put(node,cloneNode);
        while(!queue.isEmpty()){
            UndirectedGraphNode currNode = queue.poll();
            List<UndirectedGraphNode> neighbors = currNode.neighbors;
            //loop on each neighbor
            for(UndirectedGraphNode neighbor: neighbors){
                //If map do not contain neighbor node create a node and its clone
                if(!map.containsKey(neighbor)){
                    UndirectedGraphNode copy = new UndirectedGraphNode(neighbor.label);
                    map.put(neighbor, copy);
                    map.get(currNode).neighbors.add(copy);
                    queue.add(neighbor);
                }
                //else, update neighbors list
                else{
                    map.get(currNode).neighbors.add(map.get(neighbor));
                }
            }
        }
        return cloneNode;
    }*/
}
