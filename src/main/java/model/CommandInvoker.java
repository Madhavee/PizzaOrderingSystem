package model;

import java.util.ArrayList;
import java.util.List;

public class CommandInvoker {
    private List<command> commandLog = new ArrayList<>();

    public void executeCommand(command command) {
        command.execute();
        commandLog.add(command);
    }

    public List<command> getCommandLog() {
        return commandLog;
    }
}
