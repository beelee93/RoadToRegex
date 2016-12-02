package regex2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;
import regex1.DFANode;
import regex1.DeterministicFA;
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

    public NondeterministicFA() {
        currentState = new HashSet();
        nodeList = new ArrayList();
    }

    @Override
    public boolean nextInput(char a) {
        HashSet tempSet;

        // get the reachable nodes given this input and current state set of nodes
        reachable.clear();
        for (NFANode n : currentState) {
            tempSet = n.get(a);
            if (tempSet == null) {
                continue;
            }
            reachable.addAll(tempSet);
        }

        currentState.clear();
        if (reachable.isEmpty()) {
            // no reachable nodes with this input
            return false; // don't consume character
        } else {
            // we put the closure of all reachables into currentState
            for (NFANode n : reachable) {
                currentState.addAll(n.getClosure());
            }

            // consume character
            tentativeConsumedChars += a;

            if (isAccepting()) {
                consumedChars = tentativeConsumedChars;
            }

            return true;
        }
    }

    @Override
    public boolean isAccepting() {
        for (NFANode n : currentState) {
            if (n.isAccepting()) {
                return true;
            }
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
        currentState.clear();
        currentState.addAll(nodeList.get(0).getClosure());

    }

    public NFANode createNode(boolean accepting) {
        NFANode n = new NFANode(nodeList.size(), accepting);
        nodeList.add(n);

        return n;
    }

    /**
     * Constructs a Deterministic Finite Automaton from this NFA graph
     * @return 
     */
    public DeterministicFA convertToDeterministic() {
        HashSet<NFANode> curSet, tempSet, nextSet;
        HashSet<Character> charSet = new HashSet();
        Stack<Integer> stack = new Stack();
        HashMap<Integer, HashSet<NFANode>> definedNodes = new HashMap();

        DeterministicFA result = new DeterministicFA();
        DFANode curNode, nextNode;
        
        // construct initial node
        curSet = nodeList.get(0).getClosureNonStatic();
        curNode = result.createNode(isSetAccepting(curSet));
        
        definedNodes.put(curNode.getNodeId(), curSet);
        stack.push(curNode.getNodeId());
        
        while(!stack.isEmpty()) {
            int curId = stack.pop(); // get the node to process
            curNode = result.getNode(curId);
            
            // get the set of NFA nodes represented
            // by this DFA node id
            curSet = definedNodes.get(curId); 
                        
            // populate character set acceptable by this set of nodes
            // except NULL terminator
            populateCharSet(charSet, curSet);
            
            // iterate each character
            for(Character chr : charSet) {
                tempSet = new HashSet();
                
                // iterate each node in current set
                for(NFANode node : curSet) {
                    // get the reachable nodes
                    nextSet = node.get(chr);
                    if(nextSet != null) {
                        tempSet.addAll(closure(nextSet));
                    }
                }
                
                // does this set of node exist already?
                // if so, get the reference to it
                nextNode = null;
                for(HashMap.Entry<Integer, HashSet<NFANode>> entry : 
                        definedNodes.entrySet()) {
                    if(entry.getValue().equals(tempSet)) {
                        nextNode = result.getNode(entry.getKey());
                        break;
                    }
                }
                
                if(nextNode == null) {
                    // no such node set was found, we create one
                    nextNode = result.createNode(isSetAccepting(tempSet));
                    definedNodes.put(nextNode.getNodeId(), tempSet);
                    
                    // a new node, we should process it
                    stack.push(nextNode.getNodeId());
                }            
                
                // connect curNode to nextNode via char chr
                curNode.set(chr, nextNode);
            }
        }
        return result;
    }
    
    private boolean isSetAccepting(HashSet<NFANode> nodes) {
        for(NFANode n : nodes)
            if(n.isAccepting())
                return true;
        return false;
    }
    
    private void populateCharSet(HashSet<Character> charSet, HashSet<NFANode> nodeSet) {
        charSet.clear();
        for(NFANode n : nodeSet) {
           charSet.addAll(n.map.keySet());
           charSet.remove(NFANode.NULL);
        }
    }
    
    private HashSet<NFANode> closure(HashSet<NFANode> nodeSet) {
        HashSet<NFANode> set = new HashSet();
        for(NFANode node: nodeSet) {
            set.addAll(node.getClosure());
        }
        return set;
    }
    
    public int getNodeCount() {
        return nodeList.size();
    }
}
