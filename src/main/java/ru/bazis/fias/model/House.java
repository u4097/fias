package ru.bazis.fias.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "houses", type = "_doc")
@Data
@TypeAlias("_doc")
public class House {

  @Id
  private String id;

  // ID записи
  @Field(name = "house_id", type = FieldType.Keyword)
  private String houseId;

  // HOUSE GUID
  @Field(name = "house_guid", type = FieldType.Keyword)
  private String houseGuid;

  // GUID родительского объекта
  @Field(name = "ao_guid", type = FieldType.Keyword)
  private String streetFiasId;

  // FIAS: Код региона (Субъекта РФ)
  @Field(name = "region_code", type = FieldType.Keyword)
  private String regionCode;

  // Почтовый индекс
  @Field(name = "postal_code", type = FieldType.Keyword)
  private String postalCode;

  // Номер дома
  @Field(name = "house_num", type = FieldType.Keyword)
  private String houseNumber;

  //  Корпус дома
  @Field(name = "build_num", type = FieldType.Keyword)
  private String buildNumber;

  //  Номер строения
  @Field(name = "str_num", type = FieldType.Keyword)
  private String strNumber;

  //  Кадастровый номер
  @Field(name = "cad_num", type = FieldType.Keyword)
  private String cadastrNumber;

  // Дата создания
  @Field(name = "start_date", type = FieldType.Date)
  private String startDate;

  // Дата окончания действия
  @Field(name = "end_date", type = FieldType.Date)
  private String endDate;

  // Дата обновления
  @Field(name = "update_date", type = FieldType.Date)
  private String updateDate;

  // KLADR code
  @Field(name = "counter", type = FieldType.Keyword)
  private String counter;

  //OKATO
  @Field(name = "okato", type = FieldType.Keyword)
  private String okato;

  //OKTMO
  @Field(name = "oktmo", type = FieldType.Keyword)
  private String oktmo;

  //IFNS FL
  @Field(name = "ifns_fl", type = FieldType.Keyword)
  private String ifnsFl;

  //IFNS UL
  @Field(name = "ifns_ul", type = FieldType.Keyword)
  private String ifnsUl;

  //TERR IFNS UL
  @Field(name = "terr_ifns_ul", type = FieldType.Keyword)
  private String terrIfnsUl;

  //TERR IFNS FL
  @Field(name = "terr_ifns_fl", type = FieldType.Keyword)
  private String terrIfnsFl;

  //ES STATUS
  @Field(name = "est_status", type = FieldType.Byte)
  private Byte est_status;


}
