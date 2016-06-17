package com.jaspersoft.android.sdk.widget.report.v2;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tom Koptel
 * @since 2.6
 */
class CommandRegistry {
    private final List<Command> store = new ArrayList<>(10);

    public void register(Command command) {
        store.add(command);
    }

    public void cancelAll() {
        for (Command command : store) {
            command.cancel();
        }
    }
}
