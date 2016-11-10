package regex1;

import java.util.Scanner;

/**
 *
 * @author Billy
 */
public class Example {
    public static void main(String[] args) {
        DeterministicFA fa = new DeterministicFA();
        DFANode node0 = fa.createNode(false),
                node1 = fa.createNode(true),
                node2 = fa.createNode(false);
        
        node0.set('a', node1);
        node1.set('b', node2);
        node2.set('a', node1);
        
        Scanner sc = new Scanner(System.in);
        
        System.out.print("String to test against 'a(ba)*' regex: ");
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
