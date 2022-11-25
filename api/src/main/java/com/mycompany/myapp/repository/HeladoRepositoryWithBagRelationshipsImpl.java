package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Helado;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.annotations.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class HeladoRepositoryWithBagRelationshipsImpl implements HeladoRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Helado> fetchBagRelationships(Optional<Helado> helado) {
        return helado.map(this::fetchIngredientes);
    }

    @Override
    public Page<Helado> fetchBagRelationships(Page<Helado> helados) {
        return new PageImpl<>(fetchBagRelationships(helados.getContent()), helados.getPageable(), helados.getTotalElements());
    }

    @Override
    public List<Helado> fetchBagRelationships(List<Helado> helados) {
        return Optional.of(helados).map(this::fetchIngredientes).orElse(Collections.emptyList());
    }

    Helado fetchIngredientes(Helado result) {
        return entityManager
            .createQuery("select helado from Helado helado left join fetch helado.ingredientes where helado is :helado", Helado.class)
            .setParameter("helado", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Helado> fetchIngredientes(List<Helado> helados) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, helados.size()).forEach(index -> order.put(helados.get(index).getId(), index));
        List<Helado> result = entityManager
            .createQuery(
                "select distinct helado from Helado helado left join fetch helado.ingredientes where helado in :helados",
                Helado.class
            )
            .setParameter("helados", helados)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
