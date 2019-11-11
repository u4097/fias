package ru.bazis.fias;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import ru.bazis.fias.config.RestClientConfig;
import ru.bazis.fias.model.Address;
import ru.bazis.fias.service.RepositoryService;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = RestClientConfig.class)
public class RepositoryTest {


  @Autowired
  private RepositoryService repositoryService;


  @Test
  public void givenFiasService_whenPassSomeDocId_thenReturnAddress() {
    Page<Address> address = repositoryService
        .getByFiasId("bbd6b0fd-f09e-4a0a-8a85-670e0103427d");
    Assert.assertEquals(3L, address.getTotalElements());
  }

  @Test
  public void givenFiasService_whenPassFormalName_thenReturnAddress() {
    Page<Address> addr = repositoryService.getByName("Гагарина");
    Assert.assertTrue(addr.getTotalElements() > 0);
    Assert.assertEquals(addr.toList().get(0).getName(), "Гагарина");
  }

}
