package ru.besok.formula.dsl;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.*;
import java.util.logging.Logger;

public class ResultHandlerTest {
  private final static Logger log = Logger.getLogger(ResultHandlerTest.class.getName());
  @Test(expected = DSLBindingException.class)
  public void withoutBindingsTest() throws Exception {

    String formula = "if[(('a'*'b')*('a'-'b'))=('a'*'a')] ('a' + 'b'); else ('b'*'a');";
    HandlerFactory
        .calculateHandler()
        .formula(formula)
        .handle();
  }

  @Test(expected = DSLParseException.class)
  public void wrongSyntax() throws Exception {

    String formula = "if(1022)";
    HandlerFactory
        .calculateHandler()
        .formula(formula)
        .handle();
  }

  @Test
  public void printHandler() throws Exception {

    String formula = "if[" + "(('a'*'b')*('a'-'b'))=(('a'*'a')-10)] ('a' + 'b'); else ('b'*'a');";

    String result =
        HandlerFactory
            .printHandler()
            .formula(formula)
            .addBind("a", "10.0")
            .addBind("b", "9.0")
            .handle()
            .getResult();
    Assert.assertEquals("Expression[ 9.0 * 10.0 ]", result);
  }

  @Test
  public void checkBindings() throws Exception {
    Set<String> vars =
        new HashSet<>(Arrays.asList("a,ab,b,a.b,a_b,a-b".split(",")));
    String formula =
        "if['a' > 'b'] ((('a.b'%'a-b')+5)*(('a_b'-'a.b')*('a-b'-'a_b'))); else ('b' +'ab');";

    Set<String> bindings = HandlerFactory
        .printHandler()
        .formula(formula)
        .bindings();

    Assert.assertEquals(bindings, vars);
  }

  @Test
  public void checkBindingsMap() throws Exception {
    Set<String> vars =
        new HashSet<>(Arrays.asList("a,ab,b,a.b,a_b,a-b".split(",")));
    Map<String, String> bindMap = new HashMap<>();

    for (String var : vars) {
      bindMap.put(var, var.toUpperCase());
    }

    String assertRes = "Expression[ Expression[ Expression[ A.B % A-B ] +  Digit[5]  ] * Expression[ Expression[ A_B - A.B ] * Expression[ A-B - A_B ] ] ]";

    String formula =
        "if['a' > 'b'] ('a-b'-'a_b'); " +
            "else ((('a.b'%'a-b')+5)*(('a_b'-'a.b')*('a-b'-'a_b')));";

    String r = HandlerFactory
        .printHandler()
        .formula(formula)
        .addBindMap(bindMap)
        .handle()
        .getResult();
    Assert.assertEquals(assertRes, r);
  }

  @Test
  public void succsessSyntax() throws Exception {


    String formula =
        "if['a' > 'b'] ('a'*'b'); " +
            "else ('a'*'a');";

    BigDecimal r = HandlerFactory
        .calculateHandler()
        .formula(formula)
        .addBind("a",BigDecimal.valueOf(10))
        .addBind("b",BigDecimal.valueOf(5))
        .handle().getResult();
    Assert.assertEquals(BigDecimal.valueOf(50), r);
  }
}