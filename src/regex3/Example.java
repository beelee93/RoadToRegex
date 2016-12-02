/*
 *  
 */
package regex3;

import java.util.Scanner;
import regex1.DeterministicFA;
import regex2.NFANode;
import regex2.NondeterministicFA;

/**
 *
 * @author Billy
 */
public class Example {

    public static void main(String[] args) {
            
        Scanner sc = new Scanner(System.in);
        String regex;
        System.out.print("Input a regex expression: ");
        
        regex = sc.nextLine();
        
        // create DFA
        Converter converter = new Converter(regex);
        NondeterministicFA nfa = converter.getNFA();
        
        DeterministicFA fa = nfa.convertToDeterministic();

        while(true) {
            fa.reset();
            System.out.print("String to test against '" + regex + "' regex (##QUIT## to quit): ");
            String line = sc.nextLine().trim();

            if(line.equals("##QUIT##"))
                break;
            
            int i;
            for (i = 0; i < line.length(); i++) {
                if (!fa.nextInput(line.charAt(i))) {
                    break;
                }
            }

            if (fa.isAccepting()) {
                System.out.println("Accepted");
            } else {
                System.out.println("Invalid");
            }

            System.out.println("Consumed characters: " + fa.getConsumedCharacters());
        }
    }
}
