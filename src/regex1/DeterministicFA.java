package regex1;

import java.util.ArrayList;

/**
 * Class encapsulating the functions of a deterministic finite automaton
 * @author Billy
 */
public class DeterministicFA implements FiniteAutomaton {

    private ArrayList<DFANode> nodeList;
    private DFANode currentState;
    private String consumedChars, tentativeConsumedChars;
    
    public DeterministicFA() {
        nodeList = new ArrayList();
        currentState = null;
        consumedChars = tentativeConsumedChars = "";
    }
    
    @Override
    public boolean nextInput(char a) {        
        if(currentState == null)
            return false;
        
        // get the next state
        currentState = currentState.get(a);
        if(currentState == null) {
            return false; // don't consume character
        }
        else {
            tentativeConsumedChars += a;
            if(isAccepting())
                consumedChars = tentativeConsumedChars;
            return true;
        }
    }

    @Override
    public boolean isAccepting() {
        if(currentState == null)
            return false;
        return currentState.isAccepting();
    }
    
    @Override
    public String getConsumedCharacters() {
        return consumedChars;
    }
    
    /**
     * Creates a new DFA node and adds it to the FA. The first node created is the 
     * start node.
     * @param accepting Is this new node an accepting one?
     * @return The newly created node.
     */
    public DFANode createNode(boolean accepting) {
        DFANode n = new DFANode(nodeList.size(), accepting);
        nodeList.add(n);
        
        if(n.getNodeId() == 0 && currentState == null)
            currentState = n;
        return n;
    }
    
    @Override
    public void reset() {
        currentState = nodeList.get(0);
        consumedChars = tentativeConsumedChars = "";
    }
    
    public DFANode getNode(int id) {
        return nodeList.get(id);
    }
    
    public int getNodeCount() {
        return nodeList.size();
    }
}
