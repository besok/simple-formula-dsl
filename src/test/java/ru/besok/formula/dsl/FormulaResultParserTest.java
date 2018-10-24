package ru.besok.formula.dsl;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.parboiled.Parboiled;
import org.parboiled.Rule;
import org.parboiled.parserunners.ReportingParseRunner;
import org.parboiled.support.ParsingResult;
import ru.besok.formula.dsl.model.*;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import static org.parboiled.errors.ErrorUtils.*;

public class FormulaResultParserTest {
  private final static Logger log = Logger.getLogger(FormulaResultParserTest.class.getName());
  FormulaParser parser = Parboiled.createParser(FormulaParser.class);

  private <T> void checker(HashMap<String, T> params, Rule rule) {
    for (Map.Entry<String, T> p : params.entrySet()) {
      String key = p.getKey();
      T v = p.getValue();

      ParsingResult<Object> result = new ReportingParseRunner<>(rule).run(key);
//      log.info(printNodeTree(result));

      Object r = result.resultValue;

      String mes = "failed string = [" + key + "]";
      String res;

      if (!result.parseErrors.isEmpty()) {
        res = "failure";
        mes = mes + " desc: " + printParseError(result.parseErrors.get(0));
      } else {
        res = "success";
      }
      log.info("[{" + res + "}] {" + key + "} => {" + r + "} ");
      Assert.assertEquals(mes, v, r);
    }
  }


  @Test
  public void common() {
    HashMap<String, Item> params = new HashMap<>();

    params.put("10;", Digit.from("10"));
    params.put("'a' ; ", Variable.from("a"));
    params.put("10.1.1;", null);
    params.put("10.1;", Digit.from("10.1"));
    params.put("'a' + 5 ;", null);
    params.put("'a.b' ;", Variable.from("a", "b", "."));
    params.put("'a_b';", Variable.from("a_b"));
    params.put("('a'*10);", new Expression(Variable.from("a"), Operation.from("*"), Digit.from("10")));
    params.put("('a'*'b');", new Expression(Variable.from("a"), Operation.from("*"), Variable.from("b")));
    params.put("if['a'>10] ('a'*10); else ('a'*20); ",
        new IfElseIf(
            new If(
                new IfElseCond(
                    Variable.from("a"),
                    BoolOperation.from(">"),
                    Digit.from("10")),
                new Expression(
                    Variable.from("a"),
                    Operation.from("*"),
                    Digit.from("10"))),
            new Else(
                new Expression(
                    Variable.from("a"),
                    Operation.from("*"),
                    Digit.from("20")))
        ));


    params.put("('als.customer_additional_properties.value'*10);",
        new Expression(
            Variable.from("als",".","customer_additional_properties",".","value"),
            Operation.from("*"),
            Digit.from("10")));

    checker(params, parser.Formula());
  }


  @Test
  public void ifElseFull() {
    HashMap<String, IfElseIf> params = new HashMap<>();
    params.put("if['a'>1] 10 ; else 11 ;",
        new IfElseIf(
            new If(
                new IfElseCond(Variable.from("a"),
                    BoolOperation.from(">"),
                    Digit.from("1")),
                Digit.from("10")),
            new Else(Digit.from("11"))
        ));
    params.put("if['a'>'b'] 10 ; else 11 ;",
        new IfElseIf(
            new If(new IfElseCond(Variable.from("a"), BoolOperation.from(">"), Variable.from("b")),
                Digit.from("10")),
            new Else(Digit.from("11"))
        ));
    params.put("if['a'>1] 10  else 11 ;", null);
    params.put("if['a'>1] 10 ; else 11 ", null);
    params.put("if['a'>'b'] 10; else 11;",
        new IfElseIf(
            new If(new IfElseCond(Variable.from("a"), BoolOperation.from(">"), Variable.from("b")),
                Digit.from("10")),
            new Else(Digit.from("11"))
        ));
    params.put("if['a'>'b']10;else11;",
        new IfElseIf(
            new If(new IfElseCond(Variable.from("a"), BoolOperation.from(">"), Variable.from("b")),
                Digit.from("10")),
            new Else(Digit.from("11"))
        ));
    checker(params, parser.IfElseFull());
  }

  @Test
  public void ifElseStart() {
    HashMap<String, If> params = new HashMap<>();
    params.put("if['a'>'b'] 10 ;",
        new If(
            new IfElseCond(Variable.from("a"), BoolOperation.from(">"), Variable.from("b")),
            Digit.from("10"))
    );
    params.put("if['a'>'b'] ('a'*'b') ;",
        new If(
            new IfElseCond(Variable.from("a"), BoolOperation.from(">"), Variable.from("b")),
            new Expression(Variable.from("a"), Operation.from("*"), Variable.from("b")))
    );
    params.put("if['a'>'b'] ('a'*'b') ", null);
    params.put("if['a'>'b'] 10", null);
    params.put("if['a'*'b'] 10 ;", null);
    params.put("iF['a'*'b'] 10 ;", null);
    params.put("IF['a'<'b'] 10 ;", null);
    params.put("If['a'!'b'] 10 ;", null);

    checker(params, parser.IfElseStart());
  }

  @Test
  @Ignore
  public void ifElseMid() {
    HashMap<String, ElseIF> params = new HashMap<>();
    params.put("elseIf['b'>10] ('c'+10) ",
        new ElseIF(new IfElseCond(Digit.from("1"), BoolOperation.from(">"), Digit.from("2")), Digit.from("1")));


    checker(params, parser.IfElseMid());
  }

  @Test
  public void ifElseEnd() {
    HashMap<String, Else> params = new HashMap<>();
    params.put("else 'a' ; ", new Else(Variable.from("a")));
    params.put("else 10 ;", new Else(Digit.from("10")));
    params.put("else ('a'*10) ;", new Else(new Expression(Variable.from("a"), Operation.from("*"), Digit.from("10"))));
    params.put("else ('a'*'b') ;", new Else(new Expression(Variable.from("a"), Operation.from("*"), Variable.from("b"))));
    params.put("else () ;", null);
    params.put("eLse 10 ;", null);
    params.put("elSe 10;", null);
    params.put("else 'a'+5 ;", null);


    checker(params, parser.IfElseEnd());
  }

  @Test
  public void IfElseCond() {
    HashMap<String, IfElseCond> params = new HashMap<>();
    params.put("['a'>'b']", new IfElseCond(Variable.from("a"), BoolOperation.from(">"), Variable.from("b")));
    params.put("['a'<'b']", new IfElseCond(Variable.from("a"), BoolOperation.from("<"), Variable.from("b")));
    params.put("['a'!'b']", new IfElseCond(Variable.from("a"), BoolOperation.from("!"), Variable.from("b")));
    params.put("['a'='b']", new IfElseCond(Variable.from("a"), BoolOperation.from("="), Variable.from("b")));
    params.put("[10='b']", new IfElseCond(Digit.from("10"), BoolOperation.from("="), Variable.from("b")));
    params.put("[('b' * 'b') > ('b' * 'a')]",
        new IfElseCond(
            new Expression(Variable.from("b"), Operation.from("*"), Variable.from("b")),
            BoolOperation.from(">"),
            new Expression(Variable.from("b"), Operation.from("*"), Variable.from("a"))
        ));
    params.put("['b']", null);
    params.put("['b' * 'a' ]", null);
    params.put("['b' * a ]", null);
    params.put("[b * a ]", null);
    params.put("b > a", null);

    checker(params, parser.IfElseCond());
  }

  @Test
  public void Exp() {
    HashMap<String, Expression> params = new HashMap<>();
    params.put("(1+2)", new Expression(Digit.from("1"), Operation.from("+"), Digit.from("2")));
    params.put("('a'+(2*'c'))",
        new Expression(
            Variable.from("a"),
            Operation.from("+"),
            new Expression(Digit.from("2"), Operation.from("*"), Variable.from("c"))
        )
    );
    params.put("(2+'b')", new Expression(Digit.from("2"), Operation.from("+"), Variable.from("b")));
    params.put("('c.a'/'d_b')", new Expression(Variable.from("c", "a", "."), Operation.from("/"), Variable.from("d_b")));
    params.put("('a'*'c')", new Expression(Variable.from("a"), Operation.from("*"), Variable.from("c")));
    //hard exp + whitespaces test
    params.put("(  (   'a'*'b'  )*(  5  * ('e'+'k')) )",
        new Expression(
            new Expression(Variable.from("a"), Operation.from("*"), Variable.from("b")),
            Operation.from("*"),
            new Expression(Digit.from("5"), Operation.from("*"),
                new Expression(Variable.from("e"), Operation.from("+"), Variable.from("k")))
        ));
    checker(params, parser.Expression());
  }

  @Test
  public void Var() {
    HashMap<String, Variable> params = new HashMap<>();
    params.put("'a'", Variable.from("a"));
    params.put("'ab'", Variable.from("ab"));
    params.put("'ab.ab'", Variable.from("ab", "ab", "."));
    params.put("'ab_ab'", Variable.from("ab_ab"));
    params.put("'ab-ab'", Variable.from("ab", "ab", "-"));
    params.put("'A'", null);
    params.put("0", null);
    params.put(">", null);
    params.put("'a.b'.a", Variable.from("a", "b", "."));
    params.put("'a.b.c'", Variable.from("a", ".", "b", ".", "c"));
    params.put("'a.b-c'", Variable.from("a", ".", "b", "-", "c"));
    params.put("'a_b_c_d.a_b_c_d.c_d_d_c'", Variable.from("a_b_c_d", ".", "a_b_c_d",
        ".", "c_d_d_c"));
    params.put("'als.customer_additional_properties.value'", Variable.from("als", ".", "customer_additional_properties",
        ".", "value"));

    checker(params, parser.Variable());
  }

  @Test
  public void Digit() {
    HashMap<String, Digit> params = new HashMap<>();
    params.put("1", Digit.from("1"));
    params.put("10", Digit.from("10"));
    params.put("100", Digit.from("100"));
    params.put("0100", Digit.from("0100"));
    params.put("00100", Digit.from("00100"));
    params.put("1.1", Digit.from("1.1"));
    params.put("1a", Digit.from("1"));
    params.put("1.", Digit.from("1."));
    params.put("1 .", Digit.from("1 ."));
    params.put("1. ", Digit.from("1. "));
    params.put("1 . 10", Digit.from("1 . 10"));
    params.put("a", null);
    params.put("1.1.1", Digit.from("1.1")); //important to remember!
    params.put(".1", null);

    checker(params, parser.Digit());


  }

  @Test
  public void Bool() {
    HashMap<String, BoolOperation> params = new HashMap<>();
    params.put("<", BoolOperation.from("<"));
    params.put(">", BoolOperation.from(">"));
    params.put("=", BoolOperation.from("="));
    params.put("!", BoolOperation.from("!"));
    params.put("1", null);
    params.put("a", null);
    params.put("*", null);
    params.put("", null);
    checker(params, parser.BooleanSign());
  }

  @Test
  public void Op() {
    HashMap<String, Operation> params = new HashMap<>();
    params.put("+", Operation.from("+"));
    params.put("-", Operation.from("-"));
    params.put("*", Operation.from("*"));
    params.put("/", Operation.from("/"));
    params.put("%", Operation.from("%"));
    params.put(">", null);
    params.put("<", null);
    params.put("a*b", null);
    params.put("'a'", null);
    params.put("a", null);
    params.put("1", null);
    checker(params, parser.OperationSign());
  }


}