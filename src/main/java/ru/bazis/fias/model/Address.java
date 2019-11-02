package ru.bazis.fias.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "fias_klade", type = "_doc")
@Data
@TypeAlias("_doc")
public class Address {

  @Id
  private String id;

  // ID записи в базе
  @Field(name = "ao_id", type = FieldType.Keyword)
  private String aoid;

  // GUID адресного объекта
  @Field(name = "ao_guid", type = FieldType.Keyword)
  private String guid;

  // FIAS: Уровень адресного объекта ( 1 -регион, 3 - район, 4 - город, 5 - район города, 6 - нас. пункт, 7 - улица)
  @Field(name = "ao_level", type = FieldType.Byte)
  private Byte aoLevel;

  // Формальное наименование адресного объекта
  @Field(name = "formal_name", type = FieldType.Text)
  private String name;

  // Тип адресного объекта (г., ул., нас.п.)
  @Field(name = "short_name", type = FieldType.Text)
  private String type;

  // Статус адреса на текущую дату (0 - не действующий, 1 - действущий)
  @Field(name = "live_status", type = FieldType.Byte)
  private Byte liveStatus;

  // Статус актульности адреса по КЛАДР
  // (0 - актуальный, 1-51 - адрес не актуальный, 99 - адрес удален.)
  @Field(name = "curr_status", type = FieldType.Byte)
  private Byte currentStatus;

  // Статус актульности адреса по ФИАС
  @Field(name = "act_status", type = FieldType.Byte)
  private Byte actualityStatus;

  // FIAS: Код региона (Субъекта РФ)
  @Field(name = "region_code", type = FieldType.Byte)
  private String regionCode;

  // FIAS: Код города
  @Field(name = "city_code", type = FieldType.Short)
  private String cityCode;

  // FIAS: Код населенного пункта
  @Field(name = "place_code", type = FieldType.Short)
  private String placeCode;

  // FIAS: Код улицы
  @Field(name = "street_code", type = FieldType.Short)
  private String streetCode;

  // Дата создания адресного объекта
  @Field(name = "start_date", type = FieldType.Date)
  private String startDate;

  // Дата окончания действия адресного объекта
  @Field(name = "end_date", type = FieldType.Date)
  private String endDate;

  // Дата обновления (создания) адресного объекта
  @Field(name = "update_date", type = FieldType.Date)
  private String updateDate;

}
