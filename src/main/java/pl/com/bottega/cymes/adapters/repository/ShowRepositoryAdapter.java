package pl.com.bottega.cymes.adapters.repository;

import org.springframework.stereotype.Component;
import pl.com.bottega.cymes.domain.model.Show;
import pl.com.bottega.cymes.domain.ports.ShowRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Component
public class ShowRepositoryAdapter implements ShowRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void save(Show show) {

    }
}
