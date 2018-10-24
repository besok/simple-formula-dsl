package ru.besok.formula.dsl;

import ru.besok.formula.dsl.model.Digit;
import ru.besok.formula.dsl.model.Expression;
import ru.besok.formula.dsl.model.Item;
import ru.besok.formula.dsl.model.Variable;
import ru.besok.formula.dsl.model.IfElseCond;
import ru.besok.formula.dsl.model.IfElseIf;

/**
 * Visitor for finding all binding key. It does nothing except builds bind keySet
 *
 * @author Boris Zhguchev
 *
 */
public class FindBindingsBaseVisitor<T> extends AbstractBaseVisitor<T> {


  public FindBindingsBaseVisitor(Context<T> context, Formatter<T> formatter) {
    super(context, formatter);
  }


  @Override
  public void visitIfElseIf(IfElseIf ifElseIf) throws DSLBindingException, DSLVisitorException {
    ifElseIf.getIfExp().accept(this);
    ifElseIf.getElseExp().accept(this);

  }

  @Override
  public void visitIfElseCond(IfElseCond ifElseCond) throws DSLBindingException, DSLVisitorException {
    Item l = ifElseCond.getL();
    Item r = ifElseCond.getR();

    l.accept(this);
    r.accept(this);
  }

  @Override
  public void visitDigit(Digit digit) {
    // NO-OP
  }


  @Override
  public void visitExpression(Expression exp) throws DSLBindingException, DSLVisitorException {
    Item l = exp.getL();
    Item r = exp.getR();

    l.accept(this);
    r.accept(this);
  }

  @Override
  public void visitVariable(Variable variable) {
    ctxMan.bindInit(variable.toString());
  }
}
