package ru.besok.formula.dsl.model;

import ru.besok.formula.dsl.DSLBindingException;
import ru.besok.formula.dsl.DSLVisitorException;
import ru.besok.formula.dsl.Visitor;
/**
 * Base component for implementing structure for ru.gpb.als.source.generator.dsl.
 *
 *
 * @author Boris Zhguchev
 * */
public abstract class Item {

  public abstract void accept(Visitor visitor) throws DSLBindingException, DSLVisitorException;

}
