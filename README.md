# DigdesSchool
Добрый день!
В данном проекте не реализованы пункты:
- Если в запросе есть наименование колонки, которой нет в таблице - выдать Exception. 
  Также если в сравнении участвует тип, который не поддерживается данным оператором выкинуть также Exception ( например ‘lastName’>10)
- Лишние пробелы игнорируются, но в значении ячейки учитываются. Например ‘age’>4  равносильно ‘age’ > 4
- Значения которые передаются на сравнение не могут быть null. А при записи значение колонки может быть null. Т.е. ‘age’>=null считаем, 
  что такого не может быть. А UPDATE VALUES ‘age’=null - может быть, в этом  случае значение из ячейки удаляется
- Если значение в ячейке например age пусто (null), а на вход передается условие типа ‘age’!=0 , а существующее значение age=null. 
  То запрос считается корректным, 0!=null
  
  
Метод UPDATE чувствителе к запросу, а имеено к 'VALUES' и 'where' должны передавать имеено в таком виде.
Метод UPDATE работает с любой выборкой доступной в методе SELECT
