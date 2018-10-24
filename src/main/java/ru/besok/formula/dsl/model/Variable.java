package ru.besok.formula.dsl.model;


import lombok.*;
import ru.besok.formula.dsl.DSLBindingException;
import ru.besok.formula.dsl.Visitor;

/**
 * Class for wrapping variable .
 *
 * @author Boris Zhguchev
 */

@Data
@EqualsAndHashCode(callSuper = false)
public class Variable extends Item {
  public Variable() {
  }

  public static Variable from(String l, String r, String d) {
    Variable v = new Variable();
    v.setLeftVal(l);
    v.setMidVal(r);
    v.setDelim(strToD(d));
    return v;
  }

  public static Variable from(String l, String d,String m, String nextD, String endV) {
    Variable v = new Variable();
    v.setLeftVal(l);
    v.setMidVal(m);
    v.setDelim(strToD(d));
    v.setNextDelim(strToD(nextD));
    v.setEndVal(endV);
    return v;
  }

  public static Variable from(String l) {
    Variable v = new Variable();
    v.setLeftVal(l);
    return v;
  }


  private String leftVal;
  private String midVal;
  private D delim;
  private D nextDelim;
  private String endVal;


  @Override
  public void accept(Visitor visitor) throws DSLBindingException {
    visitor.visitVariable(this);
  }


  public String toString() {
    return
        delim != null ?
            nextDelim != null ?
                String.format("%s%s%s%s%s", leftVal, delim.s, midVal, nextDelim.s, endVal) :
                String.format("%s%s%s", leftVal, delim.s, midVal) :
            leftVal;
  }

  private static D strToD(String d) {
    if (d == null)
      return null;
    switch (d) {
      case ".":
        return D.Dot;
      case "-":
        return D.Dash;
      default:
        return null;
    }
  }

  public enum D {
    Dot("."),  Dash("-");
    private String s;

    D(String s) {
      this.s = s;
    }

  }
}
