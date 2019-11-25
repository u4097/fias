package ru.bazis.fias.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import java.util.List;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.CompletionField;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "address", type = "_doc")
@Data
public class Address {

  @Id
  @Field(name = "id", type = FieldType.Keyword)
  private String id;

  // GUID FIAS записи в базе
  @Field(name = "ao_id", type = FieldType.Keyword)
  @SerializedName("ao_id")
  private String recordId = "";

  // Нормализованное представление для полнотекстового поиска
  // Название региона + Название нас.пункта + Название Улицы
  @CompletionField
  private String street_address_suggest;

  // Название района (региона)
  @Field(name = "district", type = FieldType.Keyword)
  private String district = "";

  // Тип района (региона)
  @Field(name = "district_type", type = FieldType.Keyword)
  private String districtType = "";

  // Название нас. пункта
  @Field(name = "settlement", type = FieldType.Keyword)
  private String settlement = "";

  // Тип нас. пункта (региона)
  @Field(name = "settlement_type", type = FieldType.Keyword)
  private String settlementType = "";

  // Название улицы
  @Field(name = "street", type = FieldType.Keyword)
  @SerializedName("street")
  private String streetName = "";

  // Тип улицы
  @Field(name = "street_type", type = FieldType.Keyword)
  private String streetType = "";

  // GUID адресного объекта: FIAS ID
  @Field(name = "ao_guid", type = FieldType.Keyword)
  @SerializedName("ao_guid")
  private String fiasId = "";

  // КЛАДР ID
  @Field(name = "plain_code", type = FieldType.Keyword)
  @SerializedName("plain_code")
  private String kladrId = "";

  // FIAS ID родительского объекта
  @Field(name = "parent_guid", type = FieldType.Keyword)
  @SerializedName("parent_guid")
  private String parentFiasId = "";

  // Уровень адресного объекта ( 1 -регион, 3 - район, 4 - город, 5 - район города, 6 - нас. пункт, 7 - улица)
  @Field(name = "ao_level", type = FieldType.Integer)
  @SerializedName("ao_level")
  private Byte level;

  // Формальное наименование адресного объекта (для поиска)
  @Field(name = "formal_name", type = FieldType.Keyword)
  @SerializedName("formal_name")
  private String name = "";

  // Тип адресного объекта (г., ул., нас.п.)
  @Field(name = "short_name", type = FieldType.Keyword)
  @SerializedName("short_name")
  private String type = "";

  // Статус адреса на текущую дату (0 - не действующий, 1 - действущий)
  @Field(name = "live_status", type = FieldType.Integer)
  @SerializedName("live_status")
  private Byte liveStatus;

  // Статус актульности адреса по КЛАДР
  // (0 - актуальный, 1-51 - адрес не актуальный, 99 - адрес удален.)
  @Field(name = "curr_status", type = FieldType.Integer)
  @SerializedName("curr_status")
  private Byte kladrStatus;

  // Статус актульности адреса по ФИАС
  @Field(name = "act_status", type = FieldType.Keyword)
  @SerializedName("act_status")
  private Byte fiasStatus;

  // FIAS: Код региона (Субъекта РФ)
  @Field(name = "region_code", type = FieldType.Integer)
  @SerializedName("region_code")
  private String regionCode = "";

  // FIAS: Код города
  @Field(name = "city_code", type = FieldType.Integer)
  @SerializedName("city_code")
  private String cityCode = "";

  // FIAS: Код населенного пункта
  @Field(name = "place_code", type = FieldType.Integer)
  @SerializedName("place_code")
  private String placeCode = "";

  // FIAS: Код улицы
  @Field(name = "street_code", type = FieldType.Integer)
  @SerializedName("street_code")
  private String streetCode = "";

  // Дата создания записи адресного объекта
  @Field(name = "start_date", type = FieldType.Date)
  @SerializedName("start_date")
  private String startDate = "";

  // Дата обновления (создания) записи адресного объекта
  @Field(name = "update_date", type = FieldType.Date)
  @SerializedName("update_date")
  private String updateDate = "";

  // Дата окончания действия адресного объекта
  @Field(name = "end_date", type = FieldType.Date)
  @SerializedName("end_date")
  private String endDate = "";

  // Адреса домов (для улиц)
  @Field(name = "houses", type = FieldType.Nested)
  List<House> houses;

  // AutoCode Код автономной области
  @Field(name = "auto_code", type = FieldType.Integer)
  @SerializedName("auto_code")
  private String autoCode;

  @Field(name = "city_ar_code", type = FieldType.Integer)
  @SerializedName("city_ar_code")
  private String cityArCode;

  @Field(type = FieldType.Short)
  @SerializedName("area_code")
  private String areaCode;

  @Field(type = FieldType.Byte)
  @SerializedName("cent_status")
  private String centStatus;

  @Field(name = "code", type = FieldType.Keyword)
  @SerializedName("code")
  private String code;

  @Field(name = "div_type", type = FieldType.Integer)
  @SerializedName("div_type")
  private String divType;

  @Field(name = "norm_doc", type = FieldType.Keyword)
  @SerializedName("norm_doc")
  private String normDoc;

  @Field(name = "okato", type = FieldType.Keyword)
  @SerializedName("okato")
  private String okato;

  @Field(name = "oktmo", type = FieldType.Keyword)
  @SerializedName("oktmo")
  private String oktmo;

  @Field(name = "oper_status", type = FieldType.Integer)
  @SerializedName("oper_status")
  private String operStatus;

  @Field(name = "plan_code", type = FieldType.Integer)
  @SerializedName("plan_code")
  private String planCode;

  @Field(name = "postal_code", type = FieldType.Keyword)
  @SerializedName("postal_code")
  private String postalCode;

  @Field(name = "prev_id", type = FieldType.Keyword)
  @SerializedName("prev_id")
  private String prevId;

  @Field(name = "extr_code", type = FieldType.Integer)
  @SerializedName("extr_code")
  private String extrCode;

  @Field(name = "sub_ext_code", type = FieldType.Integer)
  @SerializedName("sub_ext_code")
  private String subExtCode;

  @Field(name = "terr_ifns_fl", type = FieldType.Keyword)
  @SerializedName("terr_ifns_fl")
  private String terrIfnsFl;

  @Field(name = "terr_ifns_ul", type = FieldType.Keyword)
  @SerializedName("terr_ifns_ul")
  private String terrIfnsUl;

  @Field(name = "ifns_fl", type = FieldType.Keyword)
  @SerializedName("ifns_fl")
  private String ifnsFl;

  @Field(name = "ifns_ul", type = FieldType.Keyword)
  @SerializedName("ifns_ul")
  private String ifnsUl;

  @JsonProperty("house_counts")
  private Integer houseCounts;

  public List<House> getHouses(String houseNum, String streetGuid) {
    return houses;
  }
}
