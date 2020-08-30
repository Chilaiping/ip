import java.util.ArrayList;
import java.util.Scanner;

class Global {
    static String PATTERNLINE = "___________________________________________________________________________";
}

enum Action {
    Done,
    List,
    AddTodo,
    AddDeadlines,
    AddEvents,
}

public class Duke {

    public static void main(String[] args) {

        ArrayList<Task> tasks = new ArrayList<>();

        String logo = " ____        _        \n"
                + "|  _ \\ _   _| | _____ \n"
                + "| | | | | | | |/ / _ \\\n"
                + "| |_| | |_| |   <  __/\n"
                + "|____/ \\__,_|_|\\_\\___|\n";
        System.out.println("Hello from\n" + logo);

        greetNote("welcome");

        Scanner in = new Scanner(System.in);

        while(in.hasNextLine()){
            String input = in.nextLine();

            if(!(input.toLowerCase()).equals("bye")) {
                processInput(input, tasks);
            } else {
                greetNote("bye");
                return;
            }
        }
    }

    private static void greetNote(String hi_bye) {

        String welcome = Global.PATTERNLINE
                        + "\n     Hello! I'm Duke\n"
                        + "     What can I do for you?\n"
                        + Global.PATTERNLINE + "\n";

        String farewell = Global.PATTERNLINE
                        + "\n   Bye. Hope to see you again soon!\n"
                        + Global.PATTERNLINE;

        if(hi_bye.equals("welcome")) {
            System.out.println(welcome);
        } else {
            System.out.println(farewell);
        }
    }
    private static void processInput(String input, ArrayList<Task> tasks){

        String command = input.toLowerCase();
        Action action;
        int count = tasks.size();

        try {
            action = validateCommand(command);
        } catch (Exception g) {
            System.out.println(g + "\n" + Global.PATTERNLINE);
            return;
        }

        try {
            switch(action) {
            case List:
                printList(tasks);
                break;

            case Done:
                markDone(command, tasks);
                break;

            case AddTodo:
                addTodo(tasks, input, count);
                break;

            case AddDeadlines:
                addDeadlines(tasks, input, count);
                break;

            case AddEvents:
                addEvents(tasks, input, count);
                break;

            default:
                System.out.println(Global.PATTERNLINE + "\nYou have entered invalid input. Please re-input or \n" +
                        "Enter bye to terminate the program.\n" + Global.PATTERNLINE);

            }
        } catch (Exception m) {
            System.out.println(m + Global.PATTERNLINE);
        }
    }
    private static void replyLine(ArrayList<Task> tasks, String input, int count) {
        System.out.print(Global.PATTERNLINE + "\nGot it. I've added this task: \n\t" );
        tasks.get(count).printTask();
        count += 1;
        System.out.println("Now you have " + count + " tasks in the list.\n" + Global.PATTERNLINE );
    }
    private static void printList(ArrayList<Task> tasks) throws DukeException {
        if(tasks.size() == 0) {
            throw new DukeException("Your List is Empty\n");
        }
        System.out.println(Global.PATTERNLINE);
        for(int i = 1; i <= tasks.size(); i++) {
            System.out.print(i + ". ");
            tasks.get(i-1).printTask();
        }
        System.out.println(Global.PATTERNLINE);
    }
    private static void markDone(String command, ArrayList<Task> tasks) {
        int index = 0;

        // If user input done1 instead of done 1
        if(command.contains(" ")) {
            index = Integer.parseInt(command.substring(5));
        } else {
            index = Integer.parseInt(command.substring(4));
        }

        try {
            tasks.get(index-1).markAsDone();
            System.out.println(Global.PATTERNLINE + "\nNice! I've marked this task as done:");
            tasks.get(index-1).printTask();
            System.out.println(Global.PATTERNLINE);

        } catch (IndexOutOfBoundsException e) {
            System.out.println(Global.PATTERNLINE + "\nFriend, You do not have so much task in the list\n"
                    + Global.PATTERNLINE);
        }

    }

    private static void addTodo(ArrayList<Task> tasks, String input, int count) throws DukeException{
        if (input.length() < 6) {
            throw new DukeException("☹ OOPS!!! The description of a Todo cannot be empty.\n" +
                    "Please re-input or enter bye to terminate the program\n");
        }
        tasks.add(new ToDos(input.substring(5)));
        replyLine(tasks, input, count);
    }
    private static void addDeadlines(ArrayList<Task> tasks, String input, int count) throws DukeException{
        if(!input.contains(" ")) {
            throw new DukeException("☹ OOPS!!! The description of a Deadlines cannot be empty.\n " +
                    "Please re-input or enter bye to terminate the program\n");
        } else if(input.length()-1 == input.indexOf(" ")) {
            throw new DukeException("☹ OOPS!!! The description of a Deadlines cannot be empty.\n " +
                    "Please re-input or enter bye to terminate the program\n");
        }

        int index = input.indexOf("/by");
        if(index == -1) {
            throw new DukeException("You have input your Schedule or Wrong format\nPlease include -> /by when\n");
        }
        String schedule = input.substring(index + 4);
        String taskName = input.substring(input.indexOf(" ")+1, index);
        tasks.add(new Deadlines(taskName, schedule));
        replyLine(tasks, input, count);
    }
    private static void addEvents(ArrayList<Task> tasks, String input, int count) throws DukeException{
        if(!input.contains(" ")) {
            throw new DukeException("☹ OOPS!!! The description of a Events cannot be empty.\n " +
                    "Please re-input or enter bye to terminate the program\n");
        } else if(input.length()-1 == input.indexOf(" ")) {
            throw new DukeException("☹ OOPS!!! The description of a Events cannot be empty.\n " +
                    "Please re-input or enter bye to terminate the program\n");
        }

        int index = input.indexOf("/at");
        String schedule, taskName;

        if(index == -1) {
           // System.out.println("You have not input your Schedule or Wrong format. \nPlease include -> /at when");
            throw new DukeException("You have not input your Schedule or Wrong format. \nPlease include -> /at when\n");
        }
        schedule = input.substring(index + 4);
        taskName = input.substring(input.indexOf(" ")+1, index);
        tasks.add(new Events(taskName, schedule));
        replyLine(tasks, input, count);
    }

    static Action validateCommand(String command) throws DukeException {
        if(command.startsWith("list")) {
            return  Action.List;
        } else if (command.length()> 4 && command.substring(0, 4).equals("done")) {
            return Action.Done;
        } else if (command.length()> 3 && command.substring(0, 4).equals("todo")) {
            return Action.AddTodo;
        } else if (command.length()> 7 && command.substring(0, 8).equals("deadline")) {
            return Action.AddDeadlines;
        } else if (command.length()> 4 && command.substring(0, 5).equals("event")) {
            return Action.AddEvents;
        } else {
            throw new DukeException("☹ OOPS!!! I'm sorry, but I don't know what that means :-(\n" +
                    "Please re-input or enter bye to terminate the program");
        }
    }

}
