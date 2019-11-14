package ru.bazis.fias.service;

import java.util.Optional;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Service;
import ru.bazis.fias.model.Address;
import ru.bazis.fias.model.House;
import ru.bazis.fias.repository.AddressRepository;
import ru.bazis.fias.repository.HouseRepository;

@Service
public class RepositoryServiceImpl implements RepositoryService {

  private final AddressRepository addressRepository;
  private final HouseRepository houseRepository;

  private ElasticsearchOperations operations;

  private static final Byte KLADR_STATUS = 0;
  private static final Byte LIVE_STATUS = 1;
  private static final Byte FIAS_STATUS = 1;
  static final Byte CITY_LEVEL = 4;
  static final Byte REGION_LEVEL = 1;
  private static final Byte STREET_LEVEL = 7;

  @Autowired
  public RepositoryServiceImpl(AddressRepository addressRepository,
      HouseRepository houseRepository, ElasticsearchOperations elasticsearchOperations
  ) {
    this.addressRepository = addressRepository;
    this.houseRepository = houseRepository;
    this.operations = elasticsearchOperations;
  }

  @Override
  public Optional<Address> getOneByRecordId(String fiasId) {
    return addressRepository.findByRecordId(fiasId);
  }


  @Override
  public Page<Address> getByFiasId(String guid) {
    return addressRepository
        .findByFiasIdAndFiasStatusAndLiveStatusAndKladrStatus(guid, FIAS_STATUS,
            LIVE_STATUS, KLADR_STATUS, Pageable.unpaged());
  }

  @Override
  public Page<Address> getByName(String name) {
    return addressRepository
        .findByNameAndFiasStatusAndLiveStatusAndKladrStatus(name, FIAS_STATUS,
            LIVE_STATUS, KLADR_STATUS, Pageable.unpaged());
  }

  @Override
  public Page<Address> getByStreetFull(String name) {
    return addressRepository
        .findByNameAndFiasStatusAndLiveStatusAndKladrStatus(name, FIAS_STATUS,
            LIVE_STATUS, KLADR_STATUS, Pageable.unpaged());
  }

  @Override
  public Page<Address> getStreet(String name, String cityGuid, Pageable unPaged) {
    return addressRepository
        .findByNameAndParentFiasIdAndFiasStatusAndLiveStatusAndKladrStatus(name, cityGuid,
            FIAS_STATUS,
            LIVE_STATUS, KLADR_STATUS, unPaged);
  }

  @Override
  public Page<Address> getStreetByRegion(String name, String region) {
    return addressRepository
        .findByNameAndRegionCodeAndFiasStatusAndLiveStatusAndKladrStatus(name, region,
            FIAS_STATUS,
            LIVE_STATUS, KLADR_STATUS, Pageable.unpaged());
  }

  @Override
  public Page<Address> getAllStreetByRegion(String region, Pageable page) {
    return addressRepository
        .findByLevelAndRegionCodeAndFiasStatusAndLiveStatusAndKladrStatus(STREET_LEVEL, region,
            FIAS_STATUS,
            LIVE_STATUS, KLADR_STATUS, page);
  }

  @Override
  public Page<House> getHouse(String houseNumber, String streetGuid, Pageable unPaged) {

    LocalDate date = new LocalDate();
    String dateNow = date.toString();
    return houseRepository
        .findByStreetFiasIdAndHouseNumberAndEndDateAfter(streetGuid, houseNumber, dateNow, unPaged);
  }

  @Override
  public Page<House> getStreetHouse(String streetGuid, Pageable pageable) {

    LocalDate date = new LocalDate();
    String dateNow = date.toString();
    return houseRepository
        .findByStreetFiasIdAndEndDateAfter(streetGuid, dateNow, pageable);
  }

  @Override
  public Page<Address> getAddressGuid(String name, Byte level, Pageable unpaged) {
    return addressRepository
        .findByNameAndLevelAndFiasStatusAndLiveStatusAndKladrStatus(name, level,
            FIAS_STATUS,
            LIVE_STATUS,
            KLADR_STATUS, unpaged);
  }

  public ElasticsearchOperations getOperations() {
    return operations;
  }

}
