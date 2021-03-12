//Jacqueline Jou
//TA: Aidan Thaler
//AssassinManager creates a game of Assassin where players are constantly stalking their opponent
//until only one player is left who is the winner.
import java.util.*;

public class AssassinManager {
    private AssassinNode killFront;
    private AssassinNode graveFront;

    /*
     *Constructs an AssassinManager of the given people in the same order. Assumes the inputted
     names are non-empty, non-null, and unique. Does not save the inputted list of names
     *@throws - IllegalArgumentException if passed in an empty list
     *@param names - a List of names of people still alive in the Assassins game
     */
    public AssassinManager(List<String> names) {
        if (names.size() == 0) {
            throw new IllegalArgumentException();
        }
        graveFront = null;
        killFront = new AssassinNode(names.get(0));
        AssassinNode current = killFront;
        for (int i = 1; i < names.size(); i++) {
            current.next = new AssassinNode(names.get(i));
            current = current.next;
        }
    }

    /*
     *Prints out who is currently trying to kill whom, in the form of "    X is stalking Y" where
     *X is the name before Y in the kill list and the first name is stalked by the last name
     *in the list. If only one person is left, they are stalking themselves.
     */
    public void printKillRing() {
        String firstName = killFront.name;
        AssassinNode current = killFront;
        while (current.next != null) {
            System.out.println("    " + current.name + " is stalking " + current.next.name);
            current = current.next;
        }
        System.out.println("    " + current.name + " is stalking " + firstName);
    }

    /*
     *Prints out the names in the graveyard, those who died in the game in the front
     *"    X was killed by Y" and printed so that the most recent kill is printed first
     */
    public void printGraveyard() {
        AssassinNode current = graveFront;
        while (current != null) {
            System.out.println("    " + current.name + " was killed by " + current.killer);
            current = current.next;
        }
    }

    /*
     *Returns whether or not the given name (case insensitive is in the given ring)
     *@param name - the name of the person that is trying to be identified in the given ring
     *@param front - the first name in the given ring
     *@return - true if name is in the given ring and false if not
     */
    private boolean contains(String name, AssassinNode front) {
        AssassinNode current = front;
        while (current != null) {
            if (current.name.equalsIgnoreCase(name)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }
    /*
     *Returns whether or not the given name (case insensitive) is in the kill ring
     *@param name - the name of the person that is trying to be identified in the kill ring
     *@return - true if name is in kill ring and false if not
     */
    public boolean killRingContains(String name) {
        return contains(name, killFront);
    }

    /*
     *Returns whether or not the given name (case insensitive) is in the graveyard
     *@param name - the name of the person that is trying to be identified in the graveyard
     *@return - true if name is in graveyard and false if not
     */
    public boolean graveyardContains(String name) {
        return contains(name, graveFront);
    }

    /*
     *Returns whether or not the game is over
     *@return - true if game is over (one person left in kill ring) and false if not
     */
    public boolean gameOver() {
        return killFront.next == null;
    }

    /*
     *Returns the name of the winner of the game
     *@return - the winner's name (last person in the kill ring) or null if game hasn't ended
     */
    public String winner() {
        if (gameOver()) {
            return killFront.name;
        }
        return null;
    }

    /*
     *When player is killed, moves their name (case insentitive) from the kill ring to the front
     *of the graveyard, recording the name of their killer. Order of kill ring stays the same minus
     *the killed name. If first person in order is killed, then our kill ring will shift forward.
     *@throws - IllegalStateException if the game is over (has precedence over other exceptions)
     *@throws - IllegalArgumentException if the given name is not in the kill ring
     *@param name - the name of the person (case insensitive) that is trying to be identified in 
                    the graveyard
     */
    public void kill(String name) {
        if (gameOver()) {
            throw new IllegalStateException();
        }
        else if (!killRingContains(name)) {
            throw new IllegalArgumentException();
        }
        AssassinNode current = killFront;
        AssassinNode graveCurrent = current;
        if (killFront.name.equalsIgnoreCase(name)) {   //first name in kill ring case
            killFront = current.next;
            while (current.next != null) {
                current = current.next;
            }               
        }
        else { //middle & end names in kill ring cases
            while (!current.next.name.equalsIgnoreCase(name)) {
                current = current.next;
            }
            graveCurrent = current.next;
            current.next = current.next.next;
        }
        graveCurrent.next = graveFront;
        graveFront = graveCurrent;
        graveFront.killer = current.name;
    }
}
