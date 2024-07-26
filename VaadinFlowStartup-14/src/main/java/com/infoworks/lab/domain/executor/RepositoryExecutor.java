package com.infoworks.lab.domain.executor;

import com.infoworks.lab.components.rest.RestRepositoryExecutor;
import com.infoworks.lab.domain.models.SecureSearchQuery;
import com.infoworks.lab.domain.repository.AuthRepository;
import com.infoworks.lab.rest.models.pagination.Pagination;
import com.infoworks.lab.rest.models.pagination.SortOrder;
import com.infoworks.lab.rest.repository.RestRepository;
import com.it.soul.lab.sql.query.SQLSelectQuery;
import com.it.soul.lab.sql.query.models.Property;

import java.sql.SQLException;
import java.util.*;
import java.util.logging.Logger;

public class RepositoryExecutor extends RestRepositoryExecutor {

    protected final Logger LOG = Logger.getLogger(this.getClass().getSimpleName());

    public RepositoryExecutor(RestRepository repository) {
        super(repository);
    }

    @Override
    public <T> List<T> executeSelect(SQLSelectQuery sqlSelectQuery, Class<T> aClass, Map<String, String> map) throws SQLException, IllegalArgumentException, IllegalAccessException, InstantiationException {
        //
        try {
            int limit = sqlSelectQuery.getLimit() <= 0 ? 10 : sqlSelectQuery.getLimit();
            int offset = sqlSelectQuery.getOffset();
            int page = offset / limit;
            if (offset > getMaxCount()) return new ArrayList();
            LOG.info(String.format("Offset:%s, Limit:%s, Page:%s", offset, limit, page));
            if (sqlSelectQuery.getWhereExpression() == null) {
                List returned = getRepository().fetch(page, limit);
                return returned == null ? Collections.emptyList() : returned;
            } else {
                List<Property> searchProps = sqlSelectQuery.getWhereProperties().getProperties();
                SecureSearchQuery query = Pagination.createQuery(SecureSearchQuery.class, limit, SortOrder.DESC);
                query.setPage(page);
                query.setAuthorization(AuthRepository.parseToken());
                //Iterate Over Search-Properties:
                if (searchProps.size() > 0) {
                    for (Property prop : searchProps) {
                        if (Objects.isNull(prop.getValue())) continue;
                        //In SqlDataSource.java, we already add '%' around like term.
                        query.getPredicate()
                                .or(prop.getKey()).isLike(prop.getValue().toString());
                    }
                }
                //
                List returned = getRepository().search(query);
                return returned == null ? Collections.emptyList() : returned;
            }
        } catch (RuntimeException e) {
            throw new SQLException(e.fillInStackTrace());
        }
    }
}
