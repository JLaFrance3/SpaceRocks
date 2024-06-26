/* 
 * Jean LaFrance
 * InputHolder
 * Class used to store user input for use in separate frame
 * Input types:
 *      '-' - No input
 *      'w' - Up
 *      's' - Down
 *      ' ' - Shoot
 */

 public class InputHolder {
    
    private char userInput;         //User input command
    private boolean isShooting;     //Shooting status based on spacebar input

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

    public void setShooting(boolean isShooting) {
        this.isShooting = isShooting;
    }

    public boolean isShooting() {
        return isShooting;
    }

}
