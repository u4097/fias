package ru.bazis.fias.query;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import ru.bazis.fias.model.Address;
import ru.bazis.fias.service.RepositoryService;

@Component
public class GQResolver implements GraphQLQueryResolver {

  @Autowired
  RepositoryService repositoryService;


  public List<Address> getAddressByGuid(final String guid) {
    Page<Address> addrObjects = repositoryService
        .getByGuid(guid, Pageable.unpaged());
    if (addrObjects.getTotalElements() != 0) {
      return addrObjects.get().collect(Collectors.toList());
    } else {
      return null;
    }
  }

  public List<Address> getAddressByName(final String name) {
    Page<Address> addrObjects = repositoryService.getByName(name, Pageable.unpaged());
    if (addrObjects.getTotalElements() != 0) {
      return addrObjects.get().collect(Collectors.toList());
    } else {
      return null;
    }
  }


}
