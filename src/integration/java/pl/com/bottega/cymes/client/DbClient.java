package pl.com.bottega.cymes.client;

import org.hibernate.Metamodel;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import javax.persistence.EntityManager;
import javax.persistence.Table;
import javax.persistence.metamodel.EntityType;

@Component
public class DbClient {

    @Autowired
    private EntityManager em;

    @Autowired
    private TransactionTemplate transactionTemplate;

    public void clean() {
        transactionTemplate.execute((props) -> {
            Session session = em.unwrap(Session.class);
            Metamodel hibernateMetadata = session.getSessionFactory().getMetamodel();
            session.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();
            hibernateMetadata.getEntities().stream()
                    .map(this::getTableName)
                    .forEach(tableName -> session.createNativeQuery("TRUNCATE TABLE " + tableName)
                            .executeUpdate()
                    );
            session.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();
            return null;
        });
    }

    private String getTableName(EntityType<?> entityType) {
        Table table = entityType.getJavaType().getAnnotation(Table.class);
        return table == null ? entityType.getName() : table.name();
    }
}
