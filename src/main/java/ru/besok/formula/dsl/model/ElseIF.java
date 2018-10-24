package ru.besok.formula.dsl.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.besok.formula.dsl.model.Item;
import ru.besok.formula.dsl.Visitor;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Deprecated
public class ElseIF extends Item {

  private Item head;
  private Item body;




  @Override
  public void accept(Visitor visitor) {
      // no-op
  }
}
