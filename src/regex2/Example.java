
package regex2;

import java.util.Scanner;

/**
 *
 * @author Billy
 */
public class Example {
    public static void main(String[] args) {
        NondeterministicFA fa = new NondeterministicFA();
        NFANode node0 = fa.createNode(false), 
                node1 = fa.createNode(false),
                node2 = fa.createNode(false),
                node3 = fa.createNode(false),
                node4 = fa.createNode(true);
        
        node0.set('a', node1);
        node0.set('a', node2);
        node0.set(NFANode.NULL, node3);
        
        node1.set('b', node3);
        
        node2.set('c', node3);
        
        node3.set(NFANode.NULL, node0);
        node3.set(NFANode.NULL, node4);
        
        fa.reset();
        
        Scanner sc = new Scanner(System.in);
        
        System.out.print("String to test against '(ab|ac)*' regex: ");
        String line = sc.nextLine().trim();
        
        int i;
        for(i=0;i<line.length();i++) {
            if(!fa.nextInput(line.charAt(i)))
                break;
        }
        
        if(fa.isAccepting())
            System.out.println("Accepted");
        else
            System.out.println("Invalid");
        
        System.out.println("Consumed characters: " + fa.getConsumedCharacters());
    }
}
