package ru.besok.formula.dsl.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.besok.formula.dsl.DSLBindingException;
import ru.besok.formula.dsl.DSLVisitorException;
import ru.besok.formula.dsl.Visitor;

import java.util.ArrayList;
import java.util.List;



/**
 * Class for implementing if else construction.
 * Example :
 * if['a' qt 10] 'b';
 * else 'c';
 *
 * @see If
 * @see Else
 *
 * @author Boris Zhguchev
 * */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class IfElseIf extends Item {


  public IfElseIf(Item ifExp, Item elseExp) {
    this.ifExp = ifExp;
    this.elseExp = elseExp;
  }

  private Item ifExp;
  private Item elseExp;
  private List<Item> elseIfList = new ArrayList<>();

  @Override
  public void accept(Visitor visitor) throws DSLVisitorException, DSLBindingException {
    visitor.visitIfElseIf(this);
  }
}
