package ru.bazis.fias.utis;

import java.util.stream.Collector;
import java.util.stream.Collectors;

public class Collections {

  public static <T> Collector<T, ?, T> toSingleton() {
    return Collectors.collectingAndThen(
        Collectors.toList(),
        list -> {
          if (list.size() != 1) {
            throw new IllegalStateException(
                "|============| FAIL TO FETCH ADDRESS - THERE ARE MORE THAN ONE IN COLLECTION: SIZE: "+ list.size());
          }
          return list.get(0);
        }
    );
  }


}
