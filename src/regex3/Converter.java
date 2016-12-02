/*
 *  
 */
package regex3;

import java.util.PriorityQueue;
import java.util.Stack;
import regex2.NFANode;
import regex2.NondeterministicFA;

/**
 *
 * @author Billy
 */
public class Converter {
    
    private InputType[] inputs;
    private NondeterministicFA fa;
    
    public Converter(String regex) {
        inputs = InputType.parseString(regex);
        fa = new NondeterministicFA();
        
        NFANode start = fa.createNode(false);
        NFANode subGraphEnd = createInternal(0, inputs.length, start);
        
        NFANode acceptor = fa.createNode(true);
        subGraphEnd.set(NFANode.NULL, acceptor);
    }
    
    public NondeterministicFA getNFA() { return fa; }
    
    /**
     * Generates the subgraph based on the sequence of symbols between st and en 
     * of inputs
     * @return The end node of the subgraph
     */
    private NFANode createInternal(int st, int en, NFANode begin) {
        Stack<NFANode> mergerStack = new Stack();
        Stack<Integer> matcherStack = new Stack();
        
        NFANode cur=begin, end=begin;
        int i=st;
                       
        boolean success;
        int cc;
        
        while(i<en) {
            switch(inputs[i].type) {
                case InputType.TYPE_CHARACTER:
                    /* Create a new node pair with edge inputs[i].chr between them */
                    
                    // create a node reachable via epsilon
                    cur = end;
                    end = fa.createNode(false);
                    cur.set(NFANode.NULL, end);
                    
                    // create a node reachable via inputs[i].chr
                    cur = end;
                    end = fa.createNode(false);
                    cur.set(inputs[i].chr, end);
                    i++;
                    break;
                   
                case InputType.TYPE_OPTIONAL:
                    /* Create an epsilon edge from cur to end */
                    cur.set(NFANode.NULL, end);
                    i++;
                    break;
                case InputType.TYPE_PLUS:
                    /* Create an epsilon edge from end to cur */
                    end.set(NFANode.NULL, cur);
                    i++;
                    break;
                case InputType.TYPE_STAR:
                    /* Create a bidirectional epsilon edge between end and cur */
                    cur.set(NFANode.NULL, end);
                    end.set(NFANode.NULL, cur);
                    i++;
                    break;
                    
                case InputType.TYPE_PIPE:
                    /* Save end into a merger stack, start new branch */
                    mergerStack.push(end);
                    cur = begin;
                    end = begin;
                    i++;
                    break;
                    
                    
                case InputType.TYPE_OPEN_BRACKET:
                    /* Create a pair of nodes, with the required edges for character class*/
                    
                    // create a node reachable via epsilon
                    cur = end;
                    end = fa.createNode(false);
                    cur.set(NFANode.NULL, end);
                    
                    // create a node reachable via character class
                    cur = end;
                    end = fa.createNode(false);

                    success = false;
                    
                    for(cc=i+1; cc<inputs.length;cc++) {
                        if(inputs[cc].type == InputType.TYPE_CLOSE_BRACKET) {
                            success=true;
                            break;
                        }
                    }
                    
                    if(!success) 
                        throw new RuntimeException("Cannot find closing bracket for character class.");
                    
                    generateEdges(cur, end, i+1, cc);
                    i = cc+1;
                    break;
                    
                    
                case InputType.TYPE_OPEN_PAREN:
                    /* Invoke createInternal on this regex group */
                    
                    // create a node reachable via epsilon
                    cur = end;
                    end = fa.createNode(false);
                    cur.set(NFANode.NULL, end);
                    
                    cur = end;
                    
                    // determine closing parentheses
                    matcherStack.clear();
                    
                    success = false;
                    for(cc=i;cc<inputs.length;cc++) {
                        if(inputs[cc].type == InputType.TYPE_OPEN_PAREN) 
                            matcherStack.add(0);
                        else if(inputs[cc].type==InputType.TYPE_CLOSE_PAREN && 
                                !matcherStack.isEmpty()) {

                            matcherStack.pop();
                            if(matcherStack.isEmpty()) {
                                // we can now extract the group
                                end = createInternal(i+1, cc, cur);
                                
                                i = cc+1;
                                
                                success=true;
                                break;
                            }
                        }
                    }
                    
                    if(!success) {
                        throw new RuntimeException("Unbalanced parentheses.");
                    }
                    break;
            }
        }
        
        // put the last end node into merger stack to simplify things
        mergerStack.push(end);
        
        end = fa.createNode(false);
        
        // get every node in merger queue to epsilon-link to end
        while(!mergerStack.isEmpty()) {
            NFANode node = mergerStack.pop();
            node.set(NFANode.NULL, end);
        }
        
        return end;
    }
    
    private void generateEdges(NFANode start, NFANode end, int st, int en) {
        int i = st;
        char cur;
        char lastChar = 0;
        boolean ranged = false;
        
        while(i<en) {
            cur = inputs[i].chr;
            if(ranged) {
                for(; lastChar <= cur; lastChar++) 
                    start.set(lastChar, end);
                ranged = false;
            }
            else if(cur == '-') {
                ranged = true;
            }
            else {
                start.set(cur, end);
                lastChar = cur;
            }
            i++;
        }
    }

}
