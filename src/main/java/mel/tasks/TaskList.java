package mel.tasks;

import mel.exceptions.MelException;
import mel.exceptions.ParseException;
import mel.exceptions.TaskException;
import mel.main.Mel;
import mel.utils.Storage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

/**
 * TaskList class that handles a
 * list of tasks from user input.
 */
public class TaskList {
    private final Mel mel;
    private final Storage storage;
    private final ArrayList<Task> tasks = new ArrayList<>();

    /**
     * Constructs a new TaskList to handle tasks.
     * @param mel Mel chatbot instance.
     * @param storage Storage instance handling save file.
     */
    public TaskList(Mel mel, Storage storage) {
        this.mel = mel;
        this.storage = storage;
    }

    /**
     * Handles task input to Mel chatbot.
     * @param input task input string.
     * @throws TaskException if input is of invalid format.
     * @throws TaskException if task input is invalid.
     */
    public void taskAction(String input) throws MelException, TaskException {
        if (Objects.equals(input, "list")) {
            printAll();
        } else if (input.split(" ", 2)[0].equals("mark")) {
            mark(input);
        } else if (input.split(" ", 2)[0].equals("delete")) {
            delete(input);
        } else {
            mel.println("Mel remembers...");
            add(input);
        }
    }

    /**
     * Creates new tasks from input.
     * @param str task input string.
     * @throws TaskException if input is of invalid format.
     * @throws TaskException if task input is invalid.
     */
    private void add(String str) throws MelException, TaskException {
        Task task;
        switch (str.split(" ", 2)[0]) {
        case "todo":
            task = new ToDo(str);
            break;
        case "deadline":
            task = new Deadline(str);
            break;
        case "event":
            task = new Event(str);
            break;
        default:
            throw new MelException("Mel is confused... " +
                    "Mel doesn't understand you :((");
        }
        tasks.add(task);
        updateTasks();

        mel.println("  " + task);
        mel.println("Mel counts " + tasks.size()
                + " stuffs memorized XD");
    }

    /**
     * Deletes tasks based on input.
     * @param str input string.
     */
    private void delete(String str) {
        String[] temp = str.split(" ");
        int idx = Integer.parseInt(temp[1]) - 1;
        try {
            mel.println("Mel helps you forget:\n"
                    + "  " + tasks.get(idx));
            tasks.remove(idx);
            updateTasks();
            mel.println("Mel counts " + tasks.size()
                    + " stuffs memorized XD");
        } catch (IndexOutOfBoundsException e) {
            mel.println("Mel's brain explodes in anger?! " +
                    "Mel recalls only " + tasks.size() + " things");
        }
    }

    /**
     * Handles marking of tasks' completion based on input.
     * @param str input string.
     */
    private void mark(String str) {
        String[] temp = str.split(" ");
        String m = temp[0];
        int idx = Integer.parseInt(temp[1]) - 1;
        try {
            if (Objects.equals(m, "mark")) {
                mel.println("Mel sees you completed your task!");
                tasks.get(idx).mark();
            } else {
                mel.println("Mel wonders how you undid your task...");
                tasks.get(idx).unmark();
            }
            updateTasks();
        } catch (IndexOutOfBoundsException e) {
            mel.println("Mel's brain explodes in anger?! " +
                    "Mel recalls only " + tasks.size() + " things");
        }
    }

    /**
     * Updates save file of task list using Storage.
     * @see Storage
     */
    private void updateTasks() {
        int i = 0;
        String[] s = new String[tasks.size()];
        for (Task t : tasks) {
            s[i] = t.toString();
            i++;
        }
        try {
            storage.updateTasks(s);
        } catch (IOException e) {
            mel.println("Mel ran into an error"
                    + " creating save file :(");
        }
    }

    /**
     * Prints list of tasks.
     */
    private void printAll() {
        if (tasks.isEmpty()) {
            mel.println("Mel remembers... nothing?!");
        } else {
            mel.println("Mel remembers all your stuff~");
            int i = 0;
            for (Task t : tasks) {
                mel.println(++i + "." + t);
            }
        }
    }
}
