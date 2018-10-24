package ru.besok.formula.dsl;


/**
 * This Exception describes issues for wrong bindings.
 * Example : formula ('a' + 'b') and 'a' has no binding result.
 *
 * @author Boris Zhguchev
 *
 * */
public class DSLBindingException extends Exception {
  public DSLBindingException(String message) {
    super(message);
  }
}
