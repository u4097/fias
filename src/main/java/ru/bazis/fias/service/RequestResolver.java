package ru.bazis.fias.service;

import static ru.bazis.fias.service.RepositoryServiceImpl.CITY_LEVEL;
import static ru.bazis.fias.service.RepositoryServiceImpl.REGION_LEVEL;
import static ru.bazis.fias.utis.Collections.toSingleton;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.val;
import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.bytes.BytesReference;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.script.mustache.SearchTemplateRequest;
import org.elasticsearch.script.mustache.SearchTemplateResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import ru.bazis.fias.model.Address;
import ru.bazis.fias.model.House;
import ru.bazis.fias.query.AddressRequest;

@Component
public class RequestResolver implements GraphQLQueryResolver {

  @Autowired
  RepositoryService repositoryService;


  /**
   * Find Address by FiasId
   */
  public List<Address> getAddressByFiasId(final String guid) {
    Page<Address> addresses = repositoryService
        .getByFiasId(guid);

    if (addresses.getTotalElements() == 0) {
      throw new IllegalStateException(
          "|============| FAIL TO GET: <ADDRESS BY GUID>: ( TOTAL ELEMENT = 0) GUID: " + guid);
    }
    return addresses.stream().collect(Collectors.toList());

  }

  /**
   * Find Address by  Name
   */
  public List<Address> getAddressByName(final String name) {
    Page<Address> addresses = repositoryService.getByName(name);
    if (addresses.getTotalElements() == 0) {
      throw new IllegalStateException(
          "|============| FAIL TO GET: <ADDRESS BY NAME>: ( TOTAL ELEMENT = 0) NAME: " + name);
    }
    return addresses.stream().collect(Collectors.toList());

  }

  // Сохраняем guid города

  /**
   * City by City name
   */
  public List<Address> getCity(AddressRequest addressRequest) {
    String name = addressRequest.getCity();

    Byte aoLevel = CITY_LEVEL;
    String MOSKOW_CITY = "москва";
    String SANKT_PETERBURG_CITY = "санкт-питербург";
    String SEVASTOPOL_CITY = "севастополь";
    String BAIKONUR_CITY = "байконур";
    if (name.trim().toLowerCase().equals(MOSKOW_CITY) |
        name.trim().toLowerCase().equals(SANKT_PETERBURG_CITY) |
        name.trim().toLowerCase().equals(SEVASTOPOL_CITY) |
        name.trim().toLowerCase().equals(BAIKONUR_CITY)
    ) {
      aoLevel = REGION_LEVEL;
    }
    Page<Address> addresses = repositoryService
        .getAddressGuid(name, aoLevel, Pageable.unpaged());
    if (addresses.getTotalElements() == 0) {

      System.out.println("CITY NOT FOUND: " + addressRequest.toString());
      return Collections.emptyList();
/*
      throw new IllegalStateException(
          "|============| CITY NOT FOUND BY GUID: ( TOTAL ELEMENT = 0) CITY NAME: " + name);
*/
    }
    return addresses.stream()
        .collect(Collectors.toList());
  }

  // Получаем адрес улицы (используя в качестве родительского GUID'a, GUID города

  /**
   * Street by parent GUID and Name
   */
  public List<Address> getStreet(String name, String parentFiasId) {

    Page<Address> addresses = repositoryService
        .getStreet(name, parentFiasId, Pageable.unpaged());

    if (addresses.getTotalElements() == 0) {
      return Collections.emptyList();
/*
      throw new IllegalStateException(
          "|============| FAIL TO GET STREET BY PARENT GUID: ( TOTAL ELEMENT = 0) STREET NAME: "
              + name + " PARENT GUID: " + parentFiasId);
*/
    }
    return addresses.stream()
        .filter(it -> it.getName().toLowerCase().matches(name.toLowerCase()))
        .collect(Collectors.toList());

  }

  // Получаем адрес дома (используя в качестве родительского GUID'a, GUID улицы

  /**
   * House by street GUID and House number
   */
  private List<House> getHouses(String houseNumber, String streetGuid) {
    Page<House> addresses = repositoryService
        .getHouse(houseNumber, streetGuid, Pageable.unpaged());

    if (addresses.getTotalElements() == 0) {
      return Collections.emptyList();
    }

    return addresses.stream().collect(Collectors.toList());
  }

  /**
   * House by street GUID
   */
  private List<House> getStreetHouses(String streetFiasId, Pageable pageable) {
    Page<House> addresses = repositoryService
        .getStreetHouse(streetFiasId, pageable);

    if (addresses.getTotalElements() == 0) {
      return Collections.emptyList();
    }

    return addresses.stream().collect(Collectors.toList());
  }


  private List<Address> getFullAddress(AddressRequest addressRequest) {
    //1.  GUID города
    List<Address> cityAddress = getCity(addressRequest);
    if (cityAddress.isEmpty()) {
      return Collections.emptyList();
    }
    //2.  адрес нас. пункта
    Address city = cityAddress.stream().collect(toSingleton());
    city.setSettlement(city.getName());

    List<Address> streetAddress = getStreet(addressRequest.getRoad(), city.getFiasId());
    if (streetAddress.isEmpty()) {
      return cityAddress;
    }

    //3.  адрес дома
    if (!addressRequest.getHouse_number().isEmpty()) {
      streetAddress.forEach(it -> {
        it.setStreetName(it.getName());
        it.setSettlement(city.getName());
        List<House> houseList = getHouses(addressRequest.getHouse_number(), it.getFiasId());
        if (!houseList.isEmpty()) {
          it.setHouses(houseList);
        }
      });
    }
    return streetAddress;
  }

  /**
   * Индексация адресов
   *
   * @return Кол-во проиндексированных документов
   *//*

  public AtomicInteger getIndex() {
    final String MORDOVIA_REGION = "13";
    final int START_PAGE = 0;
    final int MAX_PAGE_SIZE = 10_000;
    AtomicInteger counter = new AtomicInteger();
    AtomicInteger region = new AtomicInteger();

    for (int i = 1; i < 98; i++) {
      Page<Address> streets;
      System.out.println("Регион: " + region);
      try {
        streets = repositoryService
            .getAllStreetByRegion(String.valueOf(i), PageRequest.of(START_PAGE, MAX_PAGE_SIZE));

      } catch (Exception e) {
        continue;
      }

      streets.getContent().forEach(
          it -> {

            it.setStreetName(it.getName());
            it.setStreetType(it.getType().toLowerCase());

            // L4: Находим город по parentFiasId
            Page<Address> cityPageable = repositoryService.getByFiasId(it.getParentFiasId());
            Address cityAddress = cityPageable.getContent().get(0);
            it.setSettlement(cityAddress.getName());
            it.setSettlementType(cityAddress.getType().toLowerCase());

            // L1: Находим Регион
            Page<Address> cityParent = repositoryService.getByFiasId(cityAddress.getParentFiasId());
            Address cityParentsAddress = cityParent.getContent().get(0);
            it.setDistrict(cityParentsAddress.getName());
            it.setDistrictType(cityParentsAddress.getType().toLowerCase());

            // L8: Находим дома
            List<House> houseList = getStreetHouses(it.getFiasId(),
                PageRequest.of(START_PAGE, MAX_PAGE_SIZE));
            if (!houseList.isEmpty()) {
              it.setHouseCounts(houseList.size());
              it.setHouses(houseList);
            } else {
              it.setHouseCounts(0);
            }

            String fullStreetAddr = "";

            //Район
            if (!it.getDistrict().isEmpty()) {
              fullStreetAddr += it.getDistrict().toLowerCase();
            }
            //Город/нас.пункт
            if (!it.getSettlement().isEmpty()) {
              fullStreetAddr += " " + it.getSettlement().toLowerCase();
            }
            //Наименование
            if (!it.getName().isEmpty()) {
              fullStreetAddr += " " + it.getName().toLowerCase();
            }

            // Устанавливаем индекс
            it.setStreet_address_suggest(fullStreetAddr.trim());
            // save
            IndexQuery indexQuery = new IndexQueryBuilder()
                .withId(it.getId())
                .withObject(it)
                .build();

            repositoryService.getOperations().index(indexQuery);
            counter.getAndIncrement();
          }
      );

      System.out.println("Регион: " + region.getAndIncrement());

    }

    return counter;
  }
*/
  public List<Address> getAddress(String fiasId) throws JSONException {
    Page<Address> cityPageable = repositoryService.getByFiasId(fiasId);
    List<Address> addresses = cityPageable.getContent();
    return addresses;
  }

  @NotNull
  private List<Address> getByStreetFull(String unparsedAddress) {
    List<Address> list = new ArrayList<>();
    try {
      list = templateRequest(unparsedAddress);
    } catch (IOException e) {
      e.printStackTrace();
    }

    if (list.isEmpty()) {
      return Collections.emptyList();
    }
    return list;

  }

  private List<Address> templateRequest(String requestString) throws IOException {

    val client = new RestHighLevelClient(
        RestClient.builder(new HttpHost("127.0.0.1", 9200, "http")));

    SearchTemplateRequest request = new SearchTemplateRequest();
    request.setRequest(new SearchRequest("fias_addr_suggest"));

    request.setScriptType(ScriptType.STORED);
    request.setScript("suggest_search");

    request.setSimulate(true);

    Map<String, Object> params = new HashMap<>();
    params.put("field", "settlement");
    params.put("prefix", "cабаево");
    request.setScriptParams(params);

    SearchTemplateResponse response = client.searchTemplate(request, RequestOptions.DEFAULT);
    SearchResponse searchResponse = response.getResponse();

    SearchTemplateResponse renderResponse = client.searchTemplate(request, RequestOptions.DEFAULT);
    BytesReference source = renderResponse.getSource();

    List<Map<String, Object>> mapList = new ArrayList<>();
    searchResponse.getHits().forEach(it -> {
      it.getSourceAsMap();
      mapList.add(it.getSourceAsMap());
    });
    List<Address> addressList = new ArrayList<>();
    mapList.forEach(it -> {
      try {
        Optional<Address> address = repositoryService.getOneByRecordId(it.get("ao_id").toString());
        addressList.add(address.orElseGet(Address::new));
      } catch (Exception e) {
        System.out.println(it.toString());
      }
    });

    return addressList;
  }
}
