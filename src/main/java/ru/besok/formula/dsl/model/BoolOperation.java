package ru.besok.formula.dsl.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import static ru.besok.formula.dsl.model.BoolOperation.V.*;

/**
 * Boolean operation. @see {@link V}
 * @author Boris Zhguchev
 * */

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class BoolOperation extends Action {
  private V val;


  public static BoolOperation from(Object op) {
    if (op instanceof String)
      switch ((String) op) {
        case "<":
          return new BoolOperation(Lt);
        case ">":
          return new BoolOperation(Qt);
        case "=":
          return new BoolOperation(Eq);
        case "!":
          return new BoolOperation(NotEq);
        default:
          return new BoolOperation(Eq);
      }

    return new BoolOperation(Eq);
  }



  public enum V {
    Qt, Lt, Eq, NotEq;
  }

}
