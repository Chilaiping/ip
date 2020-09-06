package duke;

public abstract class Task {
    protected String description;
    protected boolean isDone;

    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    public String getStatusIcon() {
        return (isDone ? "\u2713" : "\u2718"); //return tick or X symbols
    }

    public void markAsDone() {
        this.isDone = true;
    }

    //public String toString() {
    abstract public String toString(); /*{
        //return this.toString() ;
        return ("[" + this.getStatusIcon() + "] " +  this.description );
    }*/

    public void printTask() {
        //System.out.println("[" + this.getStatusIcon() + "] " +  this.description ) ;
        System.out.println(this.toString());
    }
}