package ru.bazis.fias.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.bazis.fias.model.Address;
import ru.bazis.fias.repository.FiasRepository;

@Service
public class RepositoryServiceImpl implements RepositoryService {

  private final FiasRepository fiasRepository;

  private static final Byte CURRENT_STATUS = 0;
  private static final Byte LIVE_STATUS = 1;
  private static final Byte ACTUALITY_STATUS = 1;

  @Autowired
  public RepositoryServiceImpl(FiasRepository fiasRepository) {
    this.fiasRepository = fiasRepository;
  }

  @Override
  public Page<Address> getByGuid(String guid,
      Pageable pageable) {
    return fiasRepository
        .findByGuidAndActualityStatusAndLiveStatusAndCurrentStatus(guid, ACTUALITY_STATUS,
            LIVE_STATUS, CURRENT_STATUS, pageable);
  }

  @Override
  public Page<Address> getByName(String name, Pageable unPaged) {
    return fiasRepository
        .findByNameAndActualityStatusAndLiveStatusAndCurrentStatus(name, ACTUALITY_STATUS,
            LIVE_STATUS, CURRENT_STATUS, unPaged);
  }

}
