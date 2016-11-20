package regex1;

import java.util.Scanner;

/**
 *
 * @author Billy
 */
public class Example {
    public static void main(String[] args) {
        DeterministicFA fa = new DeterministicFA();

// create the nodes
        DFANode node0 = fa.createNode(false),
                node1 = fa.createNode(true);

// Node0 -> Node1 on [1-9]
        for (int i = 1; i < 10; i++) {
            node0.set((char) (0x30 + i), node1);
        }

// Node1 -> Node1 on [0-9]
        for (int i = 0; i < 10; i++) {
            node1.set((char) (0x30 + i), node1);
        }

// Node1 -> Node0 on + or -
        node1.set('+', node0);
        node1.set('-', node0);
        
        // read input and verify
        Scanner sc = new Scanner(System.in);
        
        System.out.print("String to test against '[1-9][0-9]*([\\+-][1-9][0-9]*)*' regex: ");
        String line = sc.nextLine().trim();
        
        for(int i=0;i<line.length();i++) {
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
