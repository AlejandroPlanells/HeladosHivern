package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Helado;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface HeladoRepositoryWithBagRelationships {
    Optional<Helado> fetchBagRelationships(Optional<Helado> helado);

    List<Helado> fetchBagRelationships(List<Helado> helados);

    Page<Helado> fetchBagRelationships(Page<Helado> helados);
}
