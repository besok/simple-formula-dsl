package ru.besok.formula.dsl;

import ru.besok.formula.dsl.model.Digit;


public class PrintVisitor extends AbstractBaseVisitor<String> {

  public PrintVisitor() {

    super(Context.Factory.createPrintContext(),
        Formatter.createPrintFormatter());
  }

  public PrintVisitor(Context<String> context, Formatter<String> formatter) {
    super(context, formatter);
  }





  @Override
  public void visitDigit(Digit digit) {
    ctxMan.push(digit.toString());
  }


}
