package regex1;

/**
 * Common methods in an FA
 * @author Billy
 */
public interface FiniteAutomaton {
     /**
     * Provides the next input character from stream into this DFA. 
     * @param a
     * @return true if there is a state change. false otherwise.
     */
    boolean nextInput(char a);
    
     /**
     * Checks if the DFA is in an accepting state
     * @return 
     */
    public boolean isAccepting();
    
    /**
     * Gets the string of characters that have caused state 
     * changes up to the latest accepting state.
     * @return 
     */
    public String getConsumedCharacters();
    
    /**
     * Sets this FA back to start state.
     */
    void reset();
}
