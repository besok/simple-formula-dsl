package ru.besok.formula.dsl;



import ru.besok.formula.dsl.model.Item;
import ru.besok.formula.dsl.model.Variable;

import java.math.BigDecimal;
import java.util.*;
import java.util.logging.Logger;


/**
 * It is inner class for storing values got from visitors and bindings.
 *
 * @param <T> the type of the result result and bindings.
 *
 * @author Boris Zhguchev
 *
 * */
public class Context<T> {
  private final static Logger log = Logger.getLogger(Context.class.getName());

  /**
   * for variables. @see {@link Variable}
   * */
  private Map<String, T> bindings;
  /**
   * The stack for storing values from inner structure @see {@link Item } and it's childs.
   * */
  private Deque<T> valueStack;
  /**
   * The stack for storing values from conditionals.
   * */
  private Deque<Boolean> conditionalStack;

  /**
   * Visitor for finding bind keys. @see {@link FindBindingsBaseVisitor}
   * */
  private FindBindingsBaseVisitor<T> bindingsVisitor =
      new FindBindingsBaseVisitor<>(this, null);

  Manager ctxBuild() {
    return new Manager();
  }

  private Context(Map<String, T> bindings) {
    this(bindings, new ArrayDeque<>(), new ArrayDeque<>());
  }

  private Context(Map<String, T> bindings, ArrayDeque<T> valueStack, ArrayDeque<Boolean> cstack) {
    this.bindings = bindings;
    this.valueStack = valueStack;
    this.conditionalStack= cstack;
  }


  /**Manager for serving Context.*/
  public class Manager {

    public void push(T v) {
      log.fine(" push[{"+v+"}] to ValueStack");
      valueStack.push(v);
    }

    public T pop() {
      T pop = valueStack.pop();
      log.fine(" pop[{"+pop+"}] to ValueStack");
      return pop;
    }

    public void pushCond(Boolean b) {
      log.info(" push[{"+b+"}] to ValueStack");
      conditionalStack.push(b);
    }

    public Boolean popCond() throws DSLVisitorException {
      if(conditionalStack.size() == 0){
        throw new DSLVisitorException(" The conditional Stack has no variables");
      }
      return conditionalStack.pop();
    }


    public T result() throws DSLVisitorException {
      if (valueStack.size() < 1) {
        throw new DSLVisitorException("The context stack has no values. Something goes wrong.");
      }
      T res = valueStack.pop();
      valueStack.clear();
      return res;
    }


    public Set<String> bindings() {
      return bindings.keySet();
    }

    public FindBindingsBaseVisitor<T> bindingFinder() {
      return bindingsVisitor;
    }

    public T getBind(String key) throws DSLBindingException {
      T t = bindings.get(key);
      if (t == null) {
        throw new DSLBindingException(String.format("The bind param[%s] is null. This param must be binded to some result", key));
      }
      log.fine(" unbind key[{"+key+"}] to value[{"+t+"}]");
      return t;
    }

    public void bind(String key, T val) {
      log.fine(" bind key[{"+key+"}] to value[{"+val+"}]");
      bindings.put(key, val);
    }

    public void bindMap(Map<String, T> map) {
      log.fine("bind {}" + map);
      bindings.putAll(map);
    }

    public void bindInit(String var) {
      bindings.putIfAbsent(var, null);
    }


  }

  public static class Factory{

    /**
     * Context for printing
     * @return Context
     * */
    public static Context<String> createPrintContext() {
      log.fine(" Dslcontext<String> with empty bindings has been initialized");
      return new Context<>(new HashMap<>());
    }

    /**
     * Context for calculation. In that case all bind key will be replaced to doubles values.
     * @return Context
     **/
    public static Context<BigDecimal> createCalcContext() {
      log.fine(" Dslcontext<Double> with empty bindings has been initialized");
      return new Context<>(new HashMap<>());
    }

  }
}

