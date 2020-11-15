package pl.com.bottega.cymes.domain.ports;

import pl.com.bottega.cymes.domain.model.commands.ScheduleShowCommand;

public interface ShowScheduler {
    void schedule(ScheduleShowCommand command);
}
