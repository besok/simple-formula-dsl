package ru.besok.formula.dsl.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.besok.formula.dsl.DSLBindingException;
import ru.besok.formula.dsl.model.Item;
import ru.besok.formula.dsl.DSLVisitorException;
import ru.besok.formula.dsl.Visitor;

/**
 * Class for implementing else construction.
 * Example : (else Expression or Variable or Digit);
 * @author Boris Zhguchev
 * */

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Else extends Item {

  /**the body for else cosntruction => else 10; => 10 is a body construction.*/
  private Item body;




  @Override
  public void accept(Visitor visitor) throws DSLBindingException, DSLVisitorException {
    visitor.visitElse(this);
  }
}
