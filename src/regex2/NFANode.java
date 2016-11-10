package regex2;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Stack;

/**
 * Represents a node in an NFA graph.
 * @author Billy
 */
public class NFANode {
    HashMap<Character, HashSet<NFANode>> map;
    
    // static reference to help compute closure
    private static HashSet<NFANode> closureTemp = new HashSet();
    private static Stack<NFANode> closureStack = new Stack();
    
    public static final char NULL = 0;
    
    private boolean accepting;
    private int nodeId;

    NFANode(int nodeId, boolean accepting) {
        this.nodeId = nodeId;
        this.accepting = accepting;
        this.map = new HashMap();
    }
    
    
    /**
     * Is this node an accepting one?
     * @return 
     */
    public boolean isAccepting() {
        return accepting;
    }
        
    /**
     * Gets the node id of this node
     * @return 
     */
    public int getNodeId() {return nodeId;}
    
     /**
     * Gets the next nodes reachable with given input
     * @param a
     * @return null if no reachable nodes
     */
    public HashSet<NFANode> get(char a) {
        return map.get(a);
    }
    
    /**
     * Sets the next node reachable with given input
     * @param input
     * @param nextState 
     */
    public void set(char input, NFANode nextState) {
        HashSet<NFANode> nodes = map.get(input);
        if(nodes == null) {
            nodes = new HashSet();
            map.put(input, nodes);
        } 
        
        nodes.add(nextState);
    }
    
    /**
     * Two nodes are equal if they have the same id.
     * @param e
     * @return 
     */
    @Override
    public boolean equals(Object e) {
        if(e instanceof NFANode) {
            return ((NFANode)e).nodeId == this.nodeId;
        }
        return false;
    }
    
    
    /**
     * Gets the powerset closure of this node
     * @return A static object reference. Advised to copy elements over to another place.
     */
    public HashSet<NFANode> getClosure() {
        NFANode cur;
        HashSet<NFANode> getSet;
        
        closureTemp.clear();
        closureStack.clear();
        
        closureStack.push(this);
        
        while(!closureStack.isEmpty()) {
            cur = closureStack.pop();
            closureTemp.add(cur);
            
            // depth-wise traversal of graph
            getSet = cur.get(NULL);
            
            if(getSet != null) {
                for(NFANode n : getSet) {
                    if(!closureStack.contains(n))
                        closureStack.push(n);
                }
            }
        }
        
        return closureTemp;
        
    }
}
