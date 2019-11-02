package ru.bazis.fias.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.bazis.fias.model.Address;

public interface RepositoryService {

  Page<Address> getByGuid(String guid, Pageable pageable);

  Page<Address> getByName(String name, Pageable unPaged);

}

