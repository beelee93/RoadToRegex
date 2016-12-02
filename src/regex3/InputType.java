/*
 *  
 */
package regex3;

import java.util.ArrayList;

/**
 *
 * @author Billy
 */
public class InputType {

    public static final int TYPE_CHARACTER = 0;
    public static final int TYPE_OPEN_PAREN = 1;
    public static final int TYPE_CLOSE_PAREN = 2;
    public static final int TYPE_OPEN_BRACKET = 3;
    public static final int TYPE_CLOSE_BRACKET = 4;
    public static final int TYPE_STAR = 5;
    public static final int TYPE_PLUS = 6;
    public static final int TYPE_OPTIONAL = 7;
    public static final int TYPE_PIPE = 8;

    public static final InputType OPEN_PAREN = new InputType(1, '(');
    public static final InputType CLOSE_PAREN = new InputType(2, ')');
    public static final InputType OPEN_BRACKET = new InputType(3, '[');
    public static final InputType CLOSE_BRACKET = new InputType(4, ']');
    public static final InputType STAR = new InputType(5, '*');
    public static final InputType PLUS = new InputType(6, '+');
    public static final InputType OPTIONAL = new InputType(7, '?');
    public static final InputType PIPE = new InputType(8, '|');

    public final int type;
    public final char chr;

    public InputType(int type, char c) {
        this.type = type;
        this.chr = c;
    }

    public static InputType[] parseString(String regex) {
        ArrayList<InputType> inputs = new ArrayList();

        int i = 0;
        boolean escaped = false;
        char cur;

        while (i < regex.length()) {
            cur = regex.charAt(i);
            if (cur == '\\' && !escaped) {
                escaped = true;
                i++;
                continue;
            }

            if (escaped) {
                switch (cur) {
                    case '(':
                    case ')':
                    case '+':
                    case '*':
                    case '\\':
                    case '?':
                    case '[':
                    case ']':
                    case '|':
                        inputs.add(new InputType(TYPE_CHARACTER, cur));
                        break;
                    case 'n':
                        inputs.add(new InputType(TYPE_CHARACTER, '\n'));
                        break;
                    case 't':
                        inputs.add(new InputType(TYPE_CHARACTER, '\t'));
                        break;
                    case 'r':
                        inputs.add(new InputType(TYPE_CHARACTER, '\r'));
                        break;
                    default:
                        throw new RuntimeException("Unsupported escape");

                }
                escaped = false;
            } else {
                switch (cur) {
                    case '(':
                        inputs.add(OPEN_PAREN);
                        break;
                    case ')':
                        inputs.add(CLOSE_PAREN);
                        break;
                    case '+':
                        inputs.add(PLUS);
                        break;
                    case '?':
                        inputs.add(OPTIONAL);
                        break;
                    case '[':
                        inputs.add(OPEN_BRACKET);
                        break;
                    case ']':
                        inputs.add(CLOSE_BRACKET);
                        break;
                    case '|':
                        inputs.add(PIPE);
                        break;
                    case '*':
                        inputs.add(STAR);
                        break;
                    default:
                        inputs.add(new InputType(TYPE_CHARACTER, cur));
                        break;
                }
            }
            i++;
        }
        
        return inputs.toArray(new InputType[inputs.size()]);
    }
    
    @Override
    public String toString() {
        return type + ":" + chr;
    }
}
