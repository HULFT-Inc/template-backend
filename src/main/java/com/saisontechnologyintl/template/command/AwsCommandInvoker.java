/*
 * Copyright (c) 2025 Saison Technology International
 * https://saison-technology-intl.com/
 * All rights reserved.
 */
package com.saisontechnologyintl.template.command;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AwsCommandInvoker {

  private final List<AwsCommand> commandHistory = new ArrayList<>();

  public Map<String, Object> executeCommand(AwsCommand command) {
    Map<String, Object> result = command.execute();
    commandHistory.add(command);
    return result;
  }

  public void undoLastCommand() {
    if (!commandHistory.isEmpty()) {
      AwsCommand lastCommand = commandHistory.remove(commandHistory.size() - 1);
      lastCommand.undo();
    }
  }
}
