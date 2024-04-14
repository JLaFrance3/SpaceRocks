/* 
 * Jean LaFrance
 * InputHolder
 * Class used to store user input for use in separate frame
 * Input types:
 *      ' ' - No input
 *      'w' - Up
 *      's' - Down
 *      'e' - Pause
 */

public class InputHolder {
    
    private char userInput;

    public InputHolder() {
        userInput = ' ';
    }

    public void setInput(char c) {
        userInput = c;
    }

    public void clear() {
        userInput = ' ';
    }

    public char getInput() {
        return userInput;
    }

}
