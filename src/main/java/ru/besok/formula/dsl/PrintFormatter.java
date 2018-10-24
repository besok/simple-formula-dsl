package ru.besok.formula.dsl;

import ru.besok.formula.dsl.model.Operation;
import ru.besok.formula.dsl.model.BoolOperation;

/**
 * Implement for Formatter @see {@link Formatter}
 *
 * @author Boris Zhguchev
 *
 * */
public class PrintFormatter implements Formatter<String> {
  @Override
  public Boolean visitBoolOperation(BoolOperation boolOperation, String left, String right) {
    BoolOperation.V val = boolOperation.getVal();
    StringBuilder sb = new StringBuilder("Conditional[ ");
    sb.append(left);
    switch (val){
      case Eq:sb.append(" = ");break;
      case Lt:sb.append(" < ");break;
      case Qt:sb.append(" > ");break;
      case NotEq:sb.append(" != ");break;
      default: sb.append(" x ");break;
    }
    return false;
  }

  @Override
  public String visitOperation(Operation operation, String left, String right) {
    Operation.V val = operation.getVal();
    StringBuilder sb = new StringBuilder("Expression[ ");
    sb.append(left);
    switch (val){
      case Pr: sb.append(" * ");break;
      case Pl: sb.append(" + ");break;
      case Per: sb.append(" % ");break;
      case Mn: sb.append(" - ");break;
      case Dv: sb.append(" / ");break;
      default: sb.append(" x ");break;
    }
    return sb.append(right).append(" ]").toString();
  }
}
