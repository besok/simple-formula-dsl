package ru.besok.formula.dsl.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.besok.formula.dsl.DSLBindingException;
import ru.besok.formula.dsl.DSLVisitorException;
import ru.besok.formula.dsl.Visitor;


/**
 * Class for implementing if construction.
 * Example : (if['a' qt 10] 'b')
 *
 * @author Boris Zhguchev
 *
 * */

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class If extends Item {

  /**
   * Conditional => if['a' > 'b'] @see {@link IfElseCond}
   * */
  private Item head;
  /**
   * Body => 10 => if['a' > 'b'] 10;
   * */
  private Item body;




  @Override
  public void accept(Visitor visitor) throws DSLBindingException, DSLVisitorException {
    visitor.visitIf(this);
  }
}
