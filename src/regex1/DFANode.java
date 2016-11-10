
package regex1;

import java.util.HashMap;

/**
 * Represents a node in a DFA.
 * @author Billy
 */
public class DFANode {

    // maps a character to its next node
    private HashMap<Character, DFANode> map;
    
    private boolean accepting;
    private int nodeId;

    DFANode(int nodeId, boolean accepting) {
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
     * Gets the next node reachable with given input
     * @param a
     * @return null if no reachable node
     */
    public DFANode get(char a) {
        return map.get(a);
    }

    /**
     * Sets the next node reachable with given input
     * @param input
     * @param nextState 
     */
    public void set(char input, DFANode nextState) {
        if(input == 0) {
            throw new IllegalArgumentException("DFA does not support null-char transition.");
        } 
        map.put(input, nextState);
    }
    
    /**
     * Gets the node id of this node
     * @return 
     */
    public int getNodeId() {return nodeId;}
    
}
