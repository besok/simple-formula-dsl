package ru.besok.formula.dsl;

import java.math.BigDecimal;

/**
 * Fabric for creating Handlers with type.
 *
 * @author Boris Zhguchev
 *
 * */
public class HandlerFactory {
  public static Handler<String> printHandler(){
    return new Handler<>(new PrintVisitor());
  }
  public static Handler<BigDecimal> calculateHandler(){
    return new Handler<>(new CalculateVisitor());
  }
  public  static <T> Handler<T> customHandler(AbstractBaseVisitor<T> visitor){
    return new Handler<>(visitor);
  }

  private HandlerFactory() {
  }
}
