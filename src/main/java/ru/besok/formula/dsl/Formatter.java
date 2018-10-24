package ru.besok.formula.dsl;

import ru.besok.formula.dsl.model.Operation;
import ru.besok.formula.dsl.model.BoolOperation;
/**
 * Formatter is a class for processing operations: BooleanSign operations and  action operations.
 * Example :
 *  <ul>
 *  <li>[ 1 + 5 ] Operation(+) and the left and right operands.</li>
 *  <li>[ 'a' gt 100 ]  BooleanSign Operation(gt) and the left and right operands</li>
 * </ul>
 *
 *  @param <T> the type of the operands.
 *
 *
 *  @author Boris Zhguchev
 *
 * */
@SuppressWarnings("unchecked")
public interface Formatter<T> {

  /**
   * Proccessing for BooleanSign Operations
   *
   * @param boolOperation - the operation type. @see {@link BoolOperation.V}. It is the main component for proccessing this operation.
   * @param left - the left operand
   * @param right - the right operand
   * @return - boolean result for this action. Example 5 gt 4 = true
   */

  Boolean visitBoolOperation(BoolOperation boolOperation, T left, T right);
  /**
   * Proccessing for Operations(*, + , - ,% ... )
   *
   * @param operation - the operation type. @see {@link Operation.V}. It is base for proccess this operation.
   * @param left - the left operand
   * @param right - the right operand
   * @return - result for this action. Example: 5 + 4 = 9
   */
  T visitOperation(Operation operation, T left, T right);

  static<T> Formatter<T> createPrintFormatter(){
    return (Formatter<T>) new PrintFormatter();
  }
  static<T> Formatter<T> createCalculateFormatter(){
    return (Formatter<T>) new CalculateFormatter();
  }
}
