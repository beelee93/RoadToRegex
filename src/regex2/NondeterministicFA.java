
package regex2;

import java.util.ArrayList;
import java.util.HashSet;
import regex1.FiniteAutomaton;

/**
 *
 * @author Billy
 */
public class NondeterministicFA implements FiniteAutomaton {

    private static final HashSet<NFANode> reachable = new HashSet();
    
    private ArrayList<NFANode> nodeList;
    private HashSet<NFANode> currentState;
    private String consumedChars = "", tentativeConsumedChars = "";
    private boolean atStart = true;
    
    public NondeterministicFA() {
        currentState = new HashSet();
        nodeList = new ArrayList();
    }
    
    @Override
    public boolean nextInput(char a) {
        HashSet tempSet;
        
        if(atStart) {
            currentState.addAll(nodeList.get(0).getClosure());
            atStart = false;
        }
        
        // get the reachable nodes given this input and current state set of nodes
        reachable.clear();
        for(NFANode n : currentState) {
            tempSet = n.get(a);
            if(tempSet == null)
                continue;
            reachable.addAll(tempSet);
        }
        
        currentState.clear();
        if(reachable.isEmpty()) {
            // no reachable nodes with this input
            return false; // don't consume character
        }
        else {
            // we put the closure of all reachables into currentState
            for(NFANode n : reachable) 
                currentState.addAll(n.getClosure());
            
            // consume character
            tentativeConsumedChars += a;
            
            if(isAccepting())
                consumedChars = tentativeConsumedChars;
            
            return true;
        }
    }

    @Override
    public boolean isAccepting() {
        for(NFANode n : currentState) {
            if(n.isAccepting())
                return true;
        }
        return false;
    }

    @Override
    public String getConsumedCharacters() {
        return consumedChars;
    }

    @Override
    public void reset() {
        consumedChars = "";
        atStart = true;
        currentState.clear();
    }
    
    public NFANode createNode(boolean accepting) {
        NFANode n = new NFANode(nodeList.size(), accepting);
        nodeList.add(n);
        
        return n;
    }
    
}
