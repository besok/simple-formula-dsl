package ru.besok.formula.dsl;


/**
 * This Exception describes issues for a stack and structure.
 *
 * @author Boris Zhguchev
 * */
public class DSLVisitorException extends Exception {
  public DSLVisitorException(String message) {
    super(message);
  }
}
