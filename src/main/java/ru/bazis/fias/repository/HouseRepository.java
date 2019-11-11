package ru.bazis.fias.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import ru.bazis.fias.model.House;

@Repository
public interface HouseRepository extends ElasticsearchRepository<House, String> {

  Page<House> findByStreetFiasIdAndHouseNumberAndEndDateAfter(String aoGuid,
      String houseNumber,
      String dateNow,
      Pageable pageable);


  Page<House> findByStreetFiasIdAndEndDateAfter(String aoGuid,
      String dateNow,
      Pageable pageable);

}
