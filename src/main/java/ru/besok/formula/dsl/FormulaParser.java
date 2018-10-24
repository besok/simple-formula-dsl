package ru.besok.formula.dsl;

import org.parboiled.BaseParser;
import org.parboiled.Rule;
import org.parboiled.annotations.BuildParseTree;
import org.parboiled.support.Var;
import ru.besok.formula.dsl.model.*;


/**
 * Simple AST parser for parsing limit formula.
 * It has few conditionals and it must be noted.
 *
 * <H3>Elements:</H3>
 * <ul>
 * <li>BooleanSign operators: (qt) (lt) !(not equal) =(equal). It is used in if else operators. </li>
 * <li>Operators : +(sum) -(minus) *(prod) \/(dic) %(perc). It is used in expressions.</li>
 * <li>Digits: one or several digital = 0 - 9 </li>
 * <li>Variables: one or several symbols from 'a' to 'z' or '_'. They can be divided with two items (.)  or (-). Examples: 'a','a.b','a-b','a_b.b_a' </li>
 * <li>Expressions: a group from elements has a special construct with an opportunity to have a recursion cycles.
 * Structure is (Symbol or Variable then Operation then Symbol or variable).Examples: (1+2),('a'+2),(('z'*10)*('b'*'x')) </li>
 * <li>If else operator: a group from elements has a special construct with an opportunity to have a one or more intermediate conditionals(tempopary disabled),
 * separating semicolons
 * Structure:
 * <ul>
 * <li>if[Variable or Digit or Expression then BooleanSign operator then Variable or Digit or Expression ] Variable or Digit or Expression  </li>
 * <li>[Temporary disabled] zero or more = elseIf[Variable or Digit or Expression then BooleanSign operator then Variable or Digit or Expression ] Variable or Digit or Expression  </li>
 * <li>else Variable or Digit or Expression  </li>
 * </ul>
 * Example: if['a' qt 0] 10 ; else 0 ;
 * </li>
 * <li>FormulaResult expression: it is either Variables or Digits or Expression or If else operator and a semicolon at the end.
 *
 * </ul>
 *
 * @author Boris Zhguchev
 *
 */
@BuildParseTree
public class FormulaParser extends BaseParser<Object> {


  Rule Formula() {
    return FirstOf(SimpleFormula(), IfElseFull());
  }


  Rule SimpleFormula() {
    return Sequence(FirstOf(Expression(), Variable(), Digit()), Space(), SemiColon());
  }

  // Basic

  Rule IfElseFull() {
    IfElseIf ifElseIf = new IfElseIf();
    Var<Item> s = new Var<>();
    Var<Item> f = new Var<>();
    return Sequence(
        IfElseStart(), s.set((Item) pop()),
        Space(),
//        ZeroOrMore(IfElseMid()),
        IfElseEnd(), f.set((Item) pop()),
        push(new IfElseIf(s.get(), f.get()))
    );
  }

  Rule IfElseStart() {
    Var<Item> h = new Var<>();
    Var<Item> b = new Var<>();
    return Sequence(
        If(), Space(),
        IfElseCond(), h.set((Item) pop()),
        Space(),
        IfElseBody(), b.set((Item) pop()),
        Space(), SemiColon(),
        push(new If(h.get(), b.get()))
    );
  }

  Rule IfElseEnd() {
    return Sequence(
        Else(), Space(),
        FirstOf(Variable(), Expression(), Digit()), push(new Else((Item) pop())),
        Space(), SemiColon());
  }

  // it's temporary disabled.
  Rule IfElseMid() {
    Var<Item> h = new Var<>();
    Var<Item> b = new Var<>();
    return Sequence(
        ElseIf(), Space(),
        IfElseCond(), h.set((Item) pop()),
        Space(),
        IfElseBody(), b.set((Item) pop()),
        Space(),
        push(new ElseIF(h.get(), b.get()))
    );
  }

  Rule IfElseCond() {

    Var<Item> l = new Var<>();
    Var<Item> r = new Var<>();
    Var<BoolOperation> op = new Var<>();

    return Sequence(
        SquareBracket(), Space(),
        FirstOf(Variable(), Digit(), Expression()), l.set((Item) pop()), Space(),
        BooleanSign(), op.set((BoolOperation) pop()), Space(),
        FirstOf(Variable(), Digit(), Expression()), r.set((Item) pop()), Space(),
        SquareBracket(),
        push(new IfElseCond(l.get(), r.get(), op.get()))
    );
  }

  Rule IfElseBody() {
    return FirstOf(Variable(), Expression(), Digit());
  }


  Rule Expression() {
    Var<Item> l = new Var<>();
    Var<Operation> o = new Var<>();
    Var<Item> r = new Var<>();
    return Sequence(
        Bracket(), Space(),
        FirstOf(Expression(), Variable(), Digit()), l.set((Item) pop()), Space(),
        OperationSign(), o.set((Operation) pop()), Space(),
        FirstOf(Digit(), Variable(), Expression()), r.set((Item) pop()), Space(),
        Bracket(),
        push(new Expression(l.get(), o.get(), r.get()))
    );
  }

  // numbers and variables
  Rule Digit() {
    Var<String> l = new Var<>();
    Var<String> r = new Var<>();
    return Sequence(
        Number(),l.set(match()),
        Optional(Dot(),Number()),r.set(match()),
        push(Digit.from(l.get()+ r.get()))
    );
  }

  Rule Variable() {

    Var<String> l = new Var<>();
    Var<String> d = new Var<>();

    Var<String> r = new Var<>();
    Var<String> nd = new Var<>();
    Var<String> e = new Var<>();

    return Sequence(
        Quote(),
        Symbol(), l.set(match()),
        Optional(Delimiter(), d.set(match())),
        Optional(Symbol(), r.set(match())),
        Optional(Delimiter(), nd.set(match())),
        Optional(Symbol(), e.set(match())),
        Quote(),
        push(Variable.from(l.get(), d.get(), r.get(), nd.get(), e.get()))
    );
  }
  Rule Number(){
    return Sequence(Space(),OneOrMore(CharRange('0', '9')),Space());
  }
  Rule Dot(){
    return Ch('.');
  }
  // operators
  Rule OperationSign() {
    return Sequence(
        Space(),
        AnyOf("+-*/%"), push(Operation.from(match())),
        Space());
  }

  Rule BooleanSign() {
    return Sequence(
        Space(),
        AnyOf("><!="), push(BoolOperation.from(match())),
        Space()
    );
  }

  // only symbols without being caught. It is used for delimiting only.
  Rule Bracket() {
    return AnyOf("()");
  }

  Rule Delimiter() {
    return AnyOf(".-");
  }

  Rule SquareBracket() {
    return AnyOf("[]");
  }

  Rule Symbol() {
    return OneOrMore(FirstOf(CharRange('a', 'z'),Ch('_')));
  }

  Rule Quote() {
    return Ch('\'');
  }


  Rule If() {
    return String("if");
  }

  Rule ElseIf() {
    return String("elseIf");
  }

  Rule Else() {
    return String("else");
  }

  Rule Space() {
    return ZeroOrMore(AnyOf(" \t\f"));
  }

  Rule SemiColon() {
    return String(";");
  }
}
