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
import java.util.concurrent.atomic.AtomicInteger;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.stereotype.Component;
import ru.bazis.fias.model.Address;
import ru.bazis.fias.model.House;
import ru.bazis.fias.query.AddressRequest;

@Component
public class RequestResolver implements GraphQLQueryResolver {

  @Autowired
  RepositoryService repositoryService;

  private final String MOSKOW_CITY = "москва";
  private final String SANKT_PETERBURG_CITY = "санкт-питербург";
  private final String SEVASTOPOL_CITY = "севастополь";
  private final String BAIKONUR_CITY = "байконур";


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
   * Нормализация адресов
   */
  public List<String> getExpand(String address) {
    final String MORDOVIA_REGION = "13";
    final int START_PAGE = 0;
    final int MAX_PAGE_SIZE = 10_000;
    AtomicInteger counter = new AtomicInteger();

    Page<Address> streets = repositoryService
        .getAllStreetByRegion(MORDOVIA_REGION, PageRequest.of(START_PAGE, MAX_PAGE_SIZE));

    streets.getContent().stream().forEach(
        it -> {

          it.setStreetName(it.getName());
          it.setStreetType(it.getType().toLowerCase());

          // L4: Находим город по parentFiasId
          Page<Address> cityPageable = repositoryService.getByFiasId(it.getParentFiasId());
          Address cityAddress = cityPageable.getContent().get(0);
          it.setSettlement(cityAddress.getName());
          if (cityAddress.getCentStatus() != null && !cityAddress.getCentStatus().isEmpty()) {
            switch (cityAddress.getCentStatus()) {
              case "0":
                it.setSettlementCentStatus("не определен");
                break;
              case "1":
                it.setSettlementCentStatus("центр района");
                break;
              case "2":
                it.setSettlementCentStatus("центр региона");
                break;
              case "3":
                it.setSettlementCentStatus("центр района и региона");
                break;
              case "4":
                it.setSettlementCentStatus("центральный район региона");
                break;
              default:
                it.setSettlementCentStatus("не определен");
            }

          }

          // L1: Находим Регион
          Page<Address> cityParent = repositoryService.getByFiasId(cityAddress.getParentFiasId());
          Address cityParentsAddress = cityParent.getContent().get(0);
          it.setDistrict(cityParentsAddress.getName());
          it.setDistrictType(cityParentsAddress.getType().toLowerCase());

          // L8: Находим дома
          List<House> houseList = getStreetHouses(it.getFiasId(),
              PageRequest.of(START_PAGE, MAX_PAGE_SIZE));
          if (!houseList.isEmpty()) {
            it.setHouses(houseList);
          }

          String fullStreetAddr = "";

/*
          if (!it.getDistrict().isEmpty()) {
            fullStreetAddr += it.getDistrict().toLowerCase();
          }
*/
          if (!it.getSettlement().isEmpty()) {
            fullStreetAddr += " " + it.getSettlement().toLowerCase();
          }
          if (!it.getName().isEmpty()) {
            fullStreetAddr += " " + it.getName().toLowerCase();
          }

          it.setStreet_address_suggest(fullStreetAddr);
          // save
          IndexQuery indexQuery = new IndexQueryBuilder()
              .withId(it.getId())
              .withObject(it)
              .build();

          repositoryService.getOperations().index(indexQuery);
          System.out.println(
              " REC: " + counter.getAndIncrement() + "    ======================================");
        }
    );

    List<String> expandedList = new ArrayList<>();
    for (int i = 0; i < 500; i++) {
      expandedList.add(
          streets.getContent().get(i).getDistrict().toLowerCase() + " " +
              streets.getContent().get(i).getSettlement().toLowerCase() + " " +
              streets.getContent().get(i).getType().toLowerCase() + " " +
              streets.getContent().get(i).getName().toLowerCase()
      );
    }

    return expandedList;
  }


  public List<Address> getAddress(String unparsedAddress) throws JSONException {

    return getByStreetFull(unparsedAddress);
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

/*
    System.out.printf(unparsedAddress);
    String[] query = unparsedAddress.split("\\s");
    String houseNumber = query[query.length - 1];
    Page<Address> addresses = repositoryService.getByStreetFull(unparsedAddress);
    if (addresses.getContent().isEmpty()) {
      return Collections.emptyList();
    }
    List<Address> addressList = addresses.toList();

    // Save street fiasId
    String fiasId = addressList.get(0).getFiasId();
    if (!houseNumber.isEmpty()) {
      List<House> houseList = getHouses(houseNumber, fiasId);
      if (!houseList.isEmpty()) {
//        a.setHouses(houseList);
      }

    }
*/

//    return addressList;
  }

  private List<Address> templateRequest(String requestString) throws IOException {
/*
    val searchSourceBuilder = new SearchSourceBuilder();
    val searchRequest = new SearchRequest("fias_addr_suggest");
    val client = new RestHighLevelClient(
        RestClient.builder(new HttpHost("127.0.0.1", 9200, "http")));

    SuggestionBuilder termSuggestionBuilder =
        SuggestBuilders.termSuggestion("street_address_suggest").text(request);
    SuggestBuilder suggestBuilder = new SuggestBuilder();
    suggestBuilder.addSuggestion("suggest_city", termSuggestionBuilder);
    searchSourceBuilder.suggest(suggestBuilder);
*/

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
/*
    HighlightBuilder highlightBuilder = new HighlightBuilder();
    HighlightBuilder.Field highlightTitle =
        new HighlightBuilder.Field("formal_name");
    highlightBuilder.field(highlightTitle);
    searchSourceBuilder.highlighter(highlightBuilder);
*/

/*
    searchRequest.source(searchSourceBuilder);
    SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
    List<Map<String, Object>> mapList = new ArrayList<>();
    Gson gson = new Gson();

    response.getHits().forEach(it -> {
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
*/






