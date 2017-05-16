package com.jaspersoft.android.sdk.widget.report.renderer.command;

import java.util.ArrayList;

/**
 * Created by Анна on 11.07.2016.
 */
public class CommandsList extends ArrayList<Command> {

    public Command get(Class commandType) {
        for (Command command : this) {
            if (commandType.isInstance(command)) {
                return command;
            }
        }
        return null;
    }
}
