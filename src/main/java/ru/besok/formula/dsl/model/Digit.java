package ru.besok.formula.dsl.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.besok.formula.dsl.Visitor;

import java.math.BigDecimal;

/**
 *
 * Class for wraping digitals
 *
 * @author Boris Zhguchev
 *
 * */

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)

public class Digit extends Item {
  private BigDecimal val;


  public static Digit from(String v) {
    String vRemovedSpace= v.replace(" ", "");
    return new Digit(new BigDecimal(vRemovedSpace));
  }

  public String toString() {
    return String.format(" Digit[%s] ", val.toString());
  }

  @Override
  public void accept(Visitor visitor) {
    visitor.visitDigit(this);
  }
}
