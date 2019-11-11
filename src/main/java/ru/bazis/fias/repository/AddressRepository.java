package ru.bazis.fias.repository;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import ru.bazis.fias.model.Address;

@Repository
public interface AddressRepository extends ElasticsearchRepository<Address, String> {


  Optional<Address> findByRecordId(String recordId);

  /**
   *  Search by: FiasId
   */
  Page<Address> findByFiasIdAndFiasStatusAndLiveStatusAndKladrStatus(String aoGuid,
      Byte actStatus, Byte liveStatus,
      Byte currStatus,
      Pageable pageable);

  /**
   *  Search by: Name
   */
  Page<Address> findByNameAndFiasStatusAndLiveStatusAndKladrStatus(String name,
      Byte actualStatus, Byte liveStatus,
      Byte currentStatus, Pageable pageable);

/*
  */
/**
   *  Search by: Street by Full address text (Region City Street House)
   *//*

  Page<Address> findByNameAndFiasStatusAndLiveStatusAndKladrStatus(String name,
      Byte actualStatus, Byte liveStatus,
      Byte currentStatus, Pageable pageable);
*/


  /**
   *  Search by: Name and Level
   */
  Page<Address> findByNameAndLevelAndFiasStatusAndLiveStatusAndKladrStatus(String name, Byte level,
      Byte actualStatus, Byte liveStatus,
      Byte currentStatus, Pageable pageable);

  /**
   *  Search by: Name and  ParentFiasId
   */
  Page<Address> findByNameAndParentFiasIdAndFiasStatusAndLiveStatusAndKladrStatus(String name, String guid,
      Byte fiasStatus, Byte liveStatus,
      Byte kladrStatus, Pageable pageable);


  /**
   *  Search by: Name and Region
   */
  Page<Address> findByNameAndRegionCodeAndFiasStatusAndLiveStatusAndKladrStatus(String name, String region,
      Byte fiasStatus, Byte liveStatus,
      Byte kladrStatus, Pageable pageable);

  /**
   *  Search by: Level and Region
   */
  Page<Address> findByLevelAndRegionCodeAndFiasStatusAndLiveStatusAndKladrStatus(Byte level, String region,
      Byte fiasStatus, Byte liveStatus,
      Byte kladrStatus, Pageable pageable);

}
