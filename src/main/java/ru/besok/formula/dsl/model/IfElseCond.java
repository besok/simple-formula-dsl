package ru.besok.formula.dsl.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.besok.formula.dsl.DSLBindingException;
import ru.besok.formula.dsl.DSLVisitorException;
import ru.besok.formula.dsl.Visitor;


/**
 * Class for conditional.
 * It consists of :
 * a boolean operation = @see {@link BoolOperation}
 * left operand and right operand :
 * <ul>
 * <li>@see {@link Expression})</li>
 * <li>@see {@link Variable})</li>
 * <li>@see {@link Digit})</li>
 * </ul>
 *
 * Example : (['a' qt 10])
 *
 * @author Boris Zhguchev
 *
 * */
@Data
@EqualsAndHashCode(callSuper = false)
public class IfElseCond extends Item {

  private Item l;
  private Item r;
  private BoolOperation op;

  public IfElseCond(Item l, Item r, BoolOperation op) {
    this.l = l;
    this.r = r;
    this.op = op;
  }

  public IfElseCond(Item l, BoolOperation op, Item r) {
    this.l = l;
    this.r = r;
    this.op = op;
  }

  @Override
  public void accept(Visitor visitor) throws DSLBindingException, DSLVisitorException {
    visitor.visitIfElseCond(this);
  }
}
