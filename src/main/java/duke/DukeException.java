package duke;

public class DukeException extends Exception {
    public DukeException(String s) {
        super(s);
        System.out.println(Global.PATTERNLINE);
    }
}
