package ru.besok.formula.dsl;

import ru.besok.formula.dsl.model.Operation;
import ru.besok.formula.dsl.model.BoolOperation;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Formatter for processing doubles.
 *
 * @see Formatter
 *
 * @author Boris Zhguchev
 *
 */

public class CalculateFormatter implements Formatter<BigDecimal> {
  @Override
  public Boolean visitBoolOperation(BoolOperation boolOperation, BigDecimal left, BigDecimal right) {
    BoolOperation.V val = boolOperation.getVal();
    switch (val) {
      case Eq:
        return Objects.equals(left, right);
      case Lt:
        return left.compareTo(right) < 0;
      case Qt:
        return left.compareTo(right) > 0;
      case NotEq:
        return !Objects.equals(left, right);
      default:
        return false;
    }
  }

  @Override
  public BigDecimal visitOperation(Operation operation, BigDecimal left, BigDecimal right) {
    Operation.V val = operation.getVal();
    switch (val) {
      case Pr:
        return left.multiply(right);
      case Pl:
        return left.add(right);
      case Per:
        return left.multiply(right).divide(BigDecimal.valueOf(100));
      case Mn:
        return left.subtract(right);
      case Dv:
        return left.divide(right);
      default:
        return left;
    }
  }
}
