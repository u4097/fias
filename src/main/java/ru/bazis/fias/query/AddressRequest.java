package ru.bazis.fias.query;

import lombok.Data;
import org.springframework.boot.configurationprocessor.json.JSONObject;

@Data
public class AddressRequest {

  JSONObject json;
  String expanded;
  String postcode = "";
  String state = "";
  String city = "";
  String road = "";
  String house = "";
  String house_number = "";
  String unit = "";

}
