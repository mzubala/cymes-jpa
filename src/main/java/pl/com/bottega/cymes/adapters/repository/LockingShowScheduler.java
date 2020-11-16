package pl.com.bottega.cymes.adapters.repository;

import lombok.AllArgsConstructor;
import org.springframework.transaction.support.TransactionTemplate;
import pl.com.bottega.cymes.domain.model.commands.ScheduleShowCommand;
import pl.com.bottega.cymes.domain.ports.ShowScheduler;

import javax.persistence.EntityManager;
import javax.persistence.OptimisticLockException;

import static javax.persistence.LockModeType.OPTIMISTIC_FORCE_INCREMENT;

@AllArgsConstructor
public class LockingShowScheduler implements ShowScheduler {

    private ShowScheduler decorated;

    private EntityManager entityManager;

    private TransactionTemplate transactionTemplate;

    @Override
    public void schedule(ScheduleShowCommand command) throws CinemaHallOccupiedException {
        boolean retry = false;
        do {
            try {
                transactionTemplate.execute((params) -> {
                    entityManager.find(CinemaHallEntity.class, new CinemaHallPK(command.getCinemaHallId()), OPTIMISTIC_FORCE_INCREMENT);
                    decorated.schedule(command);
                    return null;
                });
            } catch (OptimisticLockException optimisticLockException) {
                retry = true;
            }
        } while (retry);
    }
}
