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
   * Search by: FiasId
   */
  Page<Address> findByAoGUIDAndActStatusAndLiveStatusAndCurrentStatus(String aoGUID,
      Integer actStatus, Integer liveStatus, Integer currentStatus, Pageable pageable);

  /**
   * Search by: Name
   */
  Page<Address> findByNameAndActStatusAndLiveStatusAndCurrentStatus(String name, Integer actStatus,
      Integer liveStatus, Integer currentStatus, Pageable pageable);

  /*
   */
/**
 *  Search by: Street by Full address text (Region City Street House)
 *//*

  Page<Address> findByNameAndActStatusAndLiveStatusAndCurrentStatus(String name,
      Byte actualStatus, Byte liveStatus,
      Byte currentStatus, Pageable pageable);
*/


  /**
   * Search by: Name and AoLevel
   */
  Page<Address> findByNameAndAoLevelAndActStatusAndLiveStatusAndCurrentStatus(String name,
      Byte level,
      Integer actualStatus, Integer liveStatus,
      Integer currentStatus, Pageable pageable);

  /**
   * Search by: Name and  ParentGUID
   */
  Page<Address> findByNameAndParentGUIDAndActStatusAndLiveStatusAndCurrentStatus(String name,
      String guid,
      Integer fiasStatus, Integer liveStatus,
      Integer kladrStatus, Pageable pageable);


  /**
   * Search by: Name and Region
   */
  Page<Address> findByNameAndRegionCodeAndActStatusAndLiveStatusAndCurrentStatus(String name,
      String region,
      Integer fiasStatus, Integer liveStatus,
      Integer kladrStatus, Pageable pageable);

  /**
   * Search by: AoLevel and Region
   */
  Page<Address> findByAoLevelAndRegionCodeAndActStatusAndLiveStatusAndCurrentStatus(Byte level,
      String region,
      Integer fiasStatus, Integer liveStatus,
      Integer kladrStatus, Pageable pageable);

  /**
   * Search by: AoLevel
   */
  Page<Address> findByAoLevelAndActStatusAndLiveStatusAndCurrentStatus(Byte level,
      Integer fiasStatus, Integer liveStatus,
      Integer kladrStatus, Pageable pageable);

}
