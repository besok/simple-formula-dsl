### Description:
Custom dsl compiler for inner tasks. It consists of dsl parser, parsing model, dsl calculator and visitors implementing 
some behaviors. 

### Full example:
``` if[(('a'+'b')*('b'-'a')) > (('b'+'a')*('a'-'b'))] ('b' -'a') ; else ('a' - 'b'); ``` 
### Syntax
  
* BooleanSign operators: >(qt) <(lt) !(not equal) =(equal). It is used in conditionals.
* Operators : +(sum) -(minus) *(prod) /(dic) %(perc). It is used in expressions.
* Digits: one or several digital = 0 - 9 
* Variables: one or several symbols from 'a' to 'z' or '_'. 
   * They can be divided with three items (.)  or (-). Examples: 'a','a.b','a-b','a_b.b_a', 'a.b-c'
* Expressions: a group of elements having a special construct (can have a recursion).
    * Expression structure (Symbol or Variable or expession then Operation then Symbol or variable or expressioin).
    * Examples: (1+2),('a'+2),(('z'*10)+('b'-'x')),((((((((1+1)+1)+1)+1)+1)+1)+1)+1) 
* If else operator: a group of elements having a special construct with an opportunity to have a one or more intermediate conditionals(tempopary disabled),separating semicolons
* Structure:
    * if[Expression then BooleanSign operator then Expression ] Expression ; else  Expression semicolon ;
    * Example: if['a' > 0] 10 ; else 0 ;*
    
* FormulaResult expression: it is either Variables or Digits or Expression or If else operator and a semicolon at the end.
 
### Usage Example 
  
``` 
            // common example  
            
  String formula ="if['a' > 'b'] ('a'*'b'); else ('a'*'a');"; 
  
         BigDecimal r = HandlerFactory
             .calculateHandler()
             .formula(formula)
             .addBind("a",BigDecimal.valueOf(10)) // .addBindMap(mapWithKeyIsParamAndValIsValue)
             .addBind("b",BigDecimal.valueOf(5))
             .handle()
             .getResult();         
             
 Assert.assertEquals(BigDecimal.valueOf(50), r); 

             
            // get binding keys 
            
        Set<String> bindings = HandlerFactory
             .printHandler()
             .formula(formula)
             .bindings();

```