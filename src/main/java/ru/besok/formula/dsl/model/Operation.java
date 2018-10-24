package ru.besok.formula.dsl.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import static ru.besok.formula.dsl.model.Operation.V.*;

/**
 * Operation. @see {@link V}
 *
 * @author Boris Zhguchev
 * */

@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class Operation extends Action{
  private V val;



  public static Operation from(String op) {
    switch (op) {
      case "+":
        return new Operation(Pl);
      case "-":
        return new Operation(Mn);
      case "*":
        return new Operation(Pr);
      case "/":
        return new Operation(Dv);
      case "%":
        return new Operation(Per);
      default:
        return new Operation(Pr);
    }
  }


  public enum V{
    Pl,Mn,Pr,Dv,Per
  }
}
