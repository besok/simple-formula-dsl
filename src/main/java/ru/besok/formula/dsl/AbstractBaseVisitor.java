package ru.besok.formula.dsl;

import ru.besok.formula.dsl.model.Expression;
import ru.besok.formula.dsl.model.Item;
import ru.besok.formula.dsl.model.Variable;
import ru.besok.formula.dsl.model.Else;
import ru.besok.formula.dsl.model.If;
import ru.besok.formula.dsl.model.IfElseCond;
import ru.besok.formula.dsl.model.IfElseIf;

import java.util.Map;
import java.util.Set;


/**
 * Base implementing for visitor pattern.
 * @see Visitor
 * @param <T> the type of the result result for calculation(double for numbers or string for printing)
 *
 * @author Boris Zhguchev
 *
 * */
public abstract class AbstractBaseVisitor<T> implements Visitor {

  protected Context<T>.Manager ctxMan;
  protected Formatter<T> formatter;

  public void bind(String key, T val) {
    ctxMan.bind(key, val);
  }

  public void bindMap(Map<String, T> map) {
    ctxMan.bindMap(map);
  }

  public Set<String> bindings() {
    return ctxMan.bindings();
  }

  public AbstractBaseVisitor(Context<T> context, Formatter<T> formatter) {
    this.ctxMan = context.ctxBuild();
    this.formatter = formatter;
  }

  @Override
  public void visit(Item item) throws DSLBindingException, DSLVisitorException {
    item.accept(this);
  }


  @Override
  public void visitElse(Else elseSt) throws DSLBindingException, DSLVisitorException {
    elseSt.getBody().accept(this);
  }


  @Override
  public void visitIf(If ifSt) throws DSLBindingException, DSLVisitorException {
    ifSt.getHead().accept(this);
    ifSt.getBody().accept(this);
  }


  @Override
  public void visitIfElseIf(IfElseIf ifElseIf) throws DSLBindingException, DSLVisitorException {
    ifElseIf.getIfExp().accept(this);
    if (!ctxMan.popCond())
      ifElseIf.getElseExp().accept(this);

  }


  @Override
  public void visitVariable(Variable variable) throws DSLBindingException {
    ctxMan.push(ctxMan.getBind(variable.toString()));
  }

  @Override
  public void visitExpression(Expression expr) throws DSLBindingException, DSLVisitorException {
    expr.getL().accept(this);
    expr.getR().accept(this);
    T r = ctxMan.pop();
    T l = ctxMan.pop();
    T res = formatter.visitOperation(expr.getOp(), l, r);
    ctxMan.push(res);
  }
  @Override
  public void visitIfElseCond(IfElseCond ifElseCond) throws DSLBindingException, DSLVisitorException {
    visit(ifElseCond.getL());
    visit(ifElseCond.getR());
    T rS = ctxMan.pop();
    T lS = ctxMan.pop();

    ctxMan.pushCond(formatter.visitBoolOperation(ifElseCond.getOp(),lS,rS));
  }

}
