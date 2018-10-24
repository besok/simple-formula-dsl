package ru.besok.formula.dsl;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * Data class for parsed and processed formula. It consists of formula formula and processed result.
 *
 * @author Boris Zhguchev
 * */
@Data
public class FormulaResult<T> {

  private String formula;
  private T result;

}
