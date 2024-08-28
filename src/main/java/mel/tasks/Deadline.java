package mel.tasks;

import mel.exceptions.ParseException;
import mel.exceptions.TaskException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Deadline extends Task {
    private final LocalDateTime by;

    public Deadline(String str) throws TaskException {
        try {
            String[] temp = str.split("/by ");
            task = temp[0].split(" ", 2)[1];
            by = super.parseDateTime(temp[1]);
        } catch (ArrayIndexOutOfBoundsException | ParseException e) {
            throw new TaskException("deadline <task> /by <date> <time>");
        }
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + "(by: "
                + by.format(DateTimeFormatter.ofPattern("d LLL uuuu h.mma")) + ")";
    }
}
