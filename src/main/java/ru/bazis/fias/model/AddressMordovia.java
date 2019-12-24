package ru.bazis.fias.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

//@Document(indexName = "mordovia", type = "_doc")
@Data
public class AddressMordovia {

  @Id
  @Field(name = "id", type = FieldType.Keyword)
  private String id;

  // GUID FIAS записи в базе
  @Field(name = "ao_id", type = FieldType.Keyword)
  private String recordId = "";


  // Индекс
  @Field(name = "street_address_suggest", type = FieldType.Text, analyzer = "autocomplete", searchAnalyzer = "stop_analyzer")
  private String street_address_suggest;

  // Название района (региона)
  @Field(name = "full_address", type = FieldType.Keyword)
  private String fullAddress = "";

  // Название района (региона)
  @Field(name = "district_full", type = FieldType.Keyword)
  private String districtFull = "";

  // Тип района (региона)
  @Field(name = "district_type", type = FieldType.Keyword)
  private String districtType = "";

  // Название района (региона)
  @Field(name = "district", type = FieldType.Keyword)
  private String district = "";

  // Название нас. пункта с типом
  @Field(name = "settlement_full", type = FieldType.Keyword)
  private String settlementFull = "";

  // Название нас. пункта
  @Field(name = "settlement", type = FieldType.Keyword)
  private String settlement = "";

  // Тип нас. пункта (региона)
  @Field(name = "settlement_type", type = FieldType.Keyword)
  private String settlementType = "";

  // Название улицы с типом
  @Field(name = "street_full", type = FieldType.Keyword)
  private String streetFull = "";

  // Название улицы
  @Field(name = "street", type = FieldType.Keyword)
  private String streetName = "";

  // Тип улицы
  @Field(name = "street_type", type = FieldType.Keyword)
  private String streetType = "";

  // Формальное наименование адресного объекта (для поиска)
  @Field(name = "formal_name", type = FieldType.Keyword)
  private String name = "";

  // Тип адресного объекта (г., ул., нас.п.)
  @Field(name = "short_name", type = FieldType.Keyword)
  private String type = "";

  // Тип адресного объекта (г., ул., нас.п.)
  @Field(name = "off_name", type = FieldType.Keyword)
  private String offName = "";

  // Статус актульности адреса по КЛАДР
  // (0 - актуальный, 1-51 - адрес не актуальный, 99 - адрес удален.)
  @Field(name = "curr_status", type = FieldType.Byte)
  private Byte currentStatus;

  // Статус адреса на текущую дату (0 - не действующий, 1 - действущий)
  @Field(name = "live_status", type = FieldType.Byte)
  private Byte liveStatus;

  // Статус актульности адреса по ФИАС
  @Field(name = "act_status", type = FieldType.Byte)
  private Byte actStatus;

  @Field(name = "cent_status", type = FieldType.Byte)
  private String centStatus;

  @Field(name = "oper_status", type = FieldType.Byte)
  private String operStatus;

  // GUID адресного объекта: FIAS ID
  @Field(name = "ao_guid", type = FieldType.Keyword)
  private String aoGUID = "";

  // FIAS ID родительского объекта
  @Field(name = "parent_guid", type = FieldType.Keyword)
  private String parentGUID = "";

  // Уровень адресного объекта ( 1 -регион, 3 - район, 4 - город, 5 - район города, 6 - нас. пункт, 7 - улица)
  @Field(name = "ao_level", type = FieldType.Short)
  private Byte aoLevel;

  @Field(name = "area_code", type = FieldType.Short)
  private String areaCode;

  // AutoCode Код автономной области
  @Field(name = "auto_code", type = FieldType.Short)
  private String autoCode;

  @Field(name = "city_ar_code", type = FieldType.Short)
  private String cityArCode;

  // FIAS: Код города
  @Field(name = "city_code", type = FieldType.Short)
  private String cityCode = "";

  // FIAS: Код улицы
  @Field(name = "street_code", type = FieldType.Short)
  private String streetCode = "";

  @Field(name = "extr_code", type = FieldType.Short)
  private String extrCode;

  @Field(name = "sub_ext_code", type = FieldType.Short)
  private String subExtCode;

  // FIAS: Код населенного пункта
  @Field(name = "place_code", type = FieldType.Short)
  private String placeCode = "";

  @Field(name = "postal_code", type = FieldType.Long)
  private String postalCode;

  // КЛАДР ID
  @Field(name = "plain_code", type = FieldType.Keyword)
  private String plainCode = "";


  // FIAS: Код региона (Субъекта РФ)
  @Field(name = "region_code", type = FieldType.Short)
  private String regionCode = "";


  // Дата создания записи адресного объекта
  @Field(name = "start_date", type = FieldType.Date)
  private String startDate = "";

  // Дата обновления (создания) записи адресного объекта
  @Field(name = "update_date", type = FieldType.Date)
  private String updateDate = "";

  // Дата окончания действия адресного объекта
  @Field(name = "end_date", type = FieldType.Date)
  private String endDate = "";

  // Адреса домов (для улиц)
  @Field(name = "houses", type = FieldType.Nested)
  List<House> houses;

  @Field(name = "code", type = FieldType.Keyword)
  private String code;

  @Field(name = "div_type", type = FieldType.Integer)
  private String divType;

  @Field(name = "norm_doc", type = FieldType.Keyword)
  private String normDoc;

//  @Field(name = "okato", type = FieldType.Long)
//  private Long okato;

  @Field(name = "oktmo", type = FieldType.Long)
  private String oktmo;

  @Field(name = "plan_code", type = FieldType.Short)
  private String planCode;

  @Field(name = "terr_ifns_fl", type = FieldType.Short)
  private String terrIfnsFl;

  @Field(name = "terr_ifns_ul", type = FieldType.Short)
  private String terrIfnsUl;

  @Field(name = "ifns_fl", type = FieldType.Short)
  private String ifnsFl;

  @Field(name = "ifns_ul", type = FieldType.Short)
  private String ifnsUl;

  @JsonProperty("house_counts")
  private Integer houseCounts;

  public List<House> getHouses(String houseNum, String streetGuid) {
    return houses;
  }
}
