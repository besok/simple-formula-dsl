package ru.besok.formula.dsl;

import ru.besok.formula.dsl.model.Digit;

import java.math.BigDecimal;


/**
 * Visitor for processing doubles bindings.
 * @see AbstractBaseVisitor
 *
 * @author Boris Zhguchev
 *
 * */
public class CalculateVisitor extends AbstractBaseVisitor<BigDecimal> {


  public CalculateVisitor() {
    super(Context.Factory.createCalcContext(),Formatter.createCalculateFormatter());
  }

  public CalculateVisitor(Context<BigDecimal> context, Formatter<BigDecimal> formatter) {
    super(context, formatter);
  }


  @Override
  public void visitDigit(Digit digit) {
    ctxMan.push(digit.getVal());
  }


}
