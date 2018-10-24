package ru.besok.formula.dsl;

import ru.besok.formula.dsl.model.Digit;
import ru.besok.formula.dsl.model.Expression;
import ru.besok.formula.dsl.model.Item;
import ru.besok.formula.dsl.model.Variable;
import ru.besok.formula.dsl.model.Else;
import ru.besok.formula.dsl.model.If;
import ru.besok.formula.dsl.model.IfElseCond;
import ru.besok.formula.dsl.model.IfElseIf;

/**
 * Visitor interface pattern for processing @see {@link Item}
 *
 * @author Boris Zhguchev
 *
 */


@SuppressWarnings("unchecked")
public interface Visitor {

  void visit(Item item) throws DSLBindingException, DSLVisitorException;

  void visitElse(Else elseSt) throws DSLBindingException, DSLVisitorException;

  void visitIf(If ifSt) throws DSLBindingException, DSLVisitorException;

  void visitIfElseIf(IfElseIf ifElseIf) throws DSLBindingException, DSLVisitorException;

  void visitVariable(Variable variable) throws DSLBindingException;

  void visitIfElseCond(IfElseCond ifElseCond) throws DSLBindingException, DSLVisitorException;

  void visitDigit(Digit digit);

  void visitExpression(Expression expr) throws DSLBindingException, DSLVisitorException;

}
