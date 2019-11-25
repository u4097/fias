package ru.bazis.fias.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import ru.bazis.fias.model.Address;
import ru.bazis.fias.model.House;

public interface RepositoryService {

  Optional<Address> getOneByRecordId(String fiasId);

  Page<Address> getByFiasId(String guid);

  Page<Address> getByName(String name);

  Page<Address> getByStreetFull(String name);

  Page<Address> getStreet(String name, String cityGuid, Pageable unPaged);

  Page<Address> getStreetByRegion(String name, String cityGuid);

  Page<Address> getAllStreetByRegion(String region, Pageable page);

  Page<Address> getAllStreet(Pageable page);

  ElasticsearchOperations getOperations();

  Page<Address> getAddressGuid(String name, Byte level, Pageable unpaged);

  Page<House> getHouse(String streetGuid, String houseNumber, Pageable pageable);

  Page<House> getStreetHouse(String houseNumber, Pageable pageable);

}

