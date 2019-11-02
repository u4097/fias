package ru.bazis.fias.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import ru.bazis.fias.model.Address;

@Repository
public interface FiasRepository extends ElasticsearchRepository<Address, String> {


  Page<Address> findByGuidAndActualityStatusAndLiveStatusAndCurrentStatus(String aoGuid,
      Byte actStatus, Byte liveStatus,
      Byte currStatus,
      Pageable pageable);

  Page<Address> findByNameAndActualityStatusAndLiveStatusAndCurrentStatus(String name,
      Byte actualStatus, Byte liveStatus,
      Byte currentStatus, Pageable pageable);

}
