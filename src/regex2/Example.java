
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
                node4 = fa.createNode(true),
                node5 = fa.createNode(false), 
                node6 = fa.createNode(false),
                node7 = fa.createNode(false);
        
        node0.set('a', node1);
        node0.set('a', node5);
        
        node1.set('b', node2);
        node1.set(NFANode.NULL, node4);
        
        node2.set('a', node3);
        
        node3.set(NFANode.NULL, node1);
        node3.set(NFANode.NULL, node4);
        
        node5.set('c', node7);
        node5.set('c', node6);
        
        node6.set('e', node4);
        node7.set('d', node4);
        
        Scanner sc = new Scanner(System.in);
        
        System.out.print("String to test against 'a(ba)*|ac(e|d)' regex: ");
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
