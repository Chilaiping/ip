package ui;

import duke.DukeException;
import duke.Task;
import duke.ToDos;
import duke.Deadlines;
import duke.Events;
import duke.Global;
import duke.Action;

import java.util.ArrayList;

public class processCommand {

    public static void processInput(String input, ArrayList<Task> tasks) {

        String command = input.toLowerCase();
        Action action;
        int count = tasks.size();

        try {
            action = validateCommand(command);
        } catch (Exception g) {
            ui.errInvalidInput(g);
            return;
        }

        try {
            switch (action) {
            case List:
                ui.printList(tasks);
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

            case Delete:
                deleteTask(tasks,input);
                break;

            default:
                System.out.println(Global.PATTERNLINE + "\nYou have entered invalid input. Please re-input or \n" +
                        "Enter bye to terminate the program.\n" + Global.PATTERNLINE);

            }
        } catch (Exception m) {
            System.out.println(m + Global.PATTERNLINE);
        }
    }

    static Action validateCommand (String command) throws DukeException {
        if(command.startsWith("list")) {
            return  Action.List;
        } else if (command.startsWith("delete")) {
            return Action.Delete;
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
            ui.replyMarkedDone(tasks, index);
        } catch (IndexOutOfBoundsException e) {
            ui.errIndexOutOfBoundsException();
        }

    }
    private static void deleteTask (ArrayList<Task> tasks, String command) {
        int index = 0;
        if(command.contains(" ")) {
            index = Integer.parseInt(command.substring(7));
        } else {
            index = Integer.parseInt(command.substring(6));
        }

        try {
            ui.replyDeleteTask(tasks, index);
            tasks.remove(index-1);
        } catch (IndexOutOfBoundsException e) {
            ui.errIndexOutOfBoundsException();
        }
    }

    private static void addTodo(ArrayList<Task> tasks, String input, int count) throws DukeException{
        if (input.length() < 6) {
            throw new DukeException("☹ OOPS!!! The description of a Todo cannot be empty.\n" +
                    "Please re-input or enter bye to terminate the program\n");
        } else {
            tasks.add(new ToDos(input.substring(5)));
            ui.replyLine(tasks, input, count);
        }
    }
    private static void addDeadlines(ArrayList<Task> tasks, String input, int count) throws DukeException{
        String errorEmpty = "☹ OOPS!!! The description of a Deadlines cannot be empty.\n " +
                "Please re-input or enter bye to terminate the program\n";
        String errorMissing = "You have input your Schedule or Wrong format\nPlease include -> /by when\n";

        if(!input.contains(" ")) {
            throw new DukeException(errorEmpty);
        } else if(input.length()-1 == input.indexOf(" ")) {
            throw new DukeException(errorEmpty);
        } else if(!input.contains("/by")) {
            throw new DukeException(errorMissing);
        } else {
            int index = input.indexOf("/by");
            String schedule = input.substring(index + 4);
            String taskName = input.substring(input.indexOf(" ")+1, index);
            tasks.add(new Deadlines(taskName, schedule));
            ui.replyLine(tasks, input, count);
        }

    }
    private static void addEvents(ArrayList<Task> tasks, String input, int count) throws DukeException{
        String errorEmpty = "☹ OOPS!!! The description of a Events cannot be empty.\n " +
                "Please re-input or enter bye to terminate the program\n";
        String errorMissing = "You have not input your Schedule or Wrong format. \nPlease include -> /at when\n";
        String schedule, taskName;

        if(!input.contains(" ")) {
            throw new DukeException(errorEmpty);
        } else if(input.length()-1 == input.indexOf(" ")) {
            throw new DukeException(errorEmpty);
        } else if(!input.contains("/at")) {
            throw new DukeException(errorMissing);
        } else {
            int index = input.indexOf("/at");
            schedule = input.substring(index + 4);
            taskName = input.substring(input.indexOf(" ")+1, index);
            tasks.add(new Events(taskName, schedule));
            ui.replyLine(tasks, input, count);
        }
    }



}
