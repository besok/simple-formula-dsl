package ru.besok.formula.dsl;

import org.parboiled.Parboiled;
import org.parboiled.parserunners.ReportingParseRunner;
import org.parboiled.support.ParsingResult;
import ru.besok.formula.dsl.model.Item;

import java.util.Map;
import java.util.Set;

import static org.parboiled.errors.ErrorUtils.printParseError;

/**
 * Major class for processing formula formula.
 * It has a fluent interface.
 * Example:
 * Handler
 *  .calculateHandler()
 *  .formula("('a'*'b');")
 *  .addBind("a",100)
 *  .addBind("b",10)
 *  .handle();
 *
 * @param <T> the type of the result result for calculation(double for numbers or string for printing)
 * */

public class Handler<T> {
  public Handler(AbstractBaseVisitor<T> visitor) {
    this.visitor = visitor;
  }


  private AbstractBaseVisitor<T> visitor;
  private Item rootItem;
  private String formula;

  /**
   * find all bindings for creating bind map. @see {@link FindBindingsBaseVisitor}
   * @throws DSLVisitorException - for visitor ex
   * @throws DSLBindingException - for binding ex
   *
   * @return bindings
   * */
  public Set<String> bindings() throws DSLBindingException, DSLVisitorException {
    assertInputNotNull();
    visitor.ctxMan.bindingFinder().visit(rootItem);
    return visitor.bindings();
  }

  /**
   * Input formula. It has a structure for parsing with a @see {@link FormulaParser}
   * @throws DSLParseException - parse ex
   * @throws DSLVisitorException - vistor ex
   * @param input -formula
   * @return Handler
   * */
  public Handler<T> formula(String input) throws DSLVisitorException, DSLParseException {
    this.formula = input;
    this.rootItem = parseFormula();
    return this;
  }

  /**
   * add bind key val
   * @param key = binding name - ('a' + 5) - a
   * @param v = result replaces a binding name for processing a formula.
   * @return Handler
   * */
  public Handler<T> addBind(String key, T v) {
    visitor.bind(key, v);
    return this;
  }

  /**
   * add map with pairs like @see {@link #addBind(String, Object)}
   * @param bindMap - bindingMap
   * @return Handler
   * */
  public Handler<T> addBindMap(Map<String, T> bindMap) {
    visitor.bindMap(bindMap);
    return this;
  }

  /**
   * parsing and then prccessing.
   * @throws DSLParseException -ex
   * @throws DSLVisitorException -ex
   * @throws DSLBindingException -ex
   *
   * @return FormulaResult
   *
   * @author Boris Zhguchev
   *
   * */
  public FormulaResult<T> handle() throws DSLParseException, DSLVisitorException, DSLBindingException {
    visitor.visit(rootItem);

    FormulaResult<T> formulaResult = new FormulaResult<>();
    formulaResult.setFormula(this.formula);
    formulaResult.setResult(visitor.ctxMan.result());
    return formulaResult;
  }

  private Item parseFormula() throws DSLParseException, DSLVisitorException {
    assertInputNotNull();
    FormulaParser parser = Parboiled.createParser(FormulaParser.class);
    ParsingResult<Object> result = new ReportingParseRunner<>(parser.Formula()).run(formula);
    Object r = result.resultValue;

    if (!result.parseErrors.isEmpty()) {
      String error = printParseError(result.parseErrors.get(0));
      throw new DSLParseException(
          "The formula result[" + formula + "] does not follow the right structure. " +
              " The error description: " + error);
    } else {
      return (Item) r;
    }

  }

  private void assertInputNotNull() throws DSLVisitorException {
    if(formula == null )
      throw new DSLVisitorException(
          "Input formula hasn't been set. Please put Input formula to Handler by invoking the formula()");
  }


}