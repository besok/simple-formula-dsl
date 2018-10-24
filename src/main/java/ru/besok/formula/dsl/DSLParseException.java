package ru.besok.formula.dsl;



/**
 * This Exception describes issues for a wrong syntax.
 * Example: formula ('a' + b) - b has no quotas.
 *
 * @author Boris Zhguchev
 * */
public class DSLParseException extends Exception{
  public DSLParseException(String message) {
    super(message);
  }
}
