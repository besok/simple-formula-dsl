package ru.besok.formula.dsl.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.besok.formula.dsl.DSLBindingException;
import ru.besok.formula.dsl.DSLVisitorException;
import ru.besok.formula.dsl.Visitor;


/**
 * Class for wrapping expressions:
 * Example: ('a' + ('b' + ('c' *10)))
 * It can have recursion stack.
 *
 * @see Variable
 * @see Digit
 * @see Operation
 *
 * @author Boris Zhguchev
 * */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Expression extends Item {

  private Item l;
  private Item r;
  private Operation op;

  public Expression(Item l, Operation op, Item r) {
    this.l = l;
    this.r =  r;
    this.op = op;
  }
  @Override
  public void accept(Visitor visitor) throws DSLBindingException, DSLVisitorException {
    visitor.visitExpression(this);
  }
}
