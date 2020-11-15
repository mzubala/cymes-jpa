package pl.com.bottega.cymes.adapters.repository;

import org.springframework.stereotype.Component;
import pl.com.bottega.cymes.domain.model.queries.ShowsQuery;
import pl.com.bottega.cymes.domain.ports.RepertoireBrowser;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Component
public class RepertoireBrowserAdapter implements RepertoireBrowser {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<SearchedShow> search(ShowsQuery query) {
        return List.of();
    }
}
