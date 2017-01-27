List of used design patterns:
* Factory. `CommandFactory`
* Singleton. `CommandFactory`
* Strategy. `QuoteString` with implementations: `WeakQuoteString, StrongQuoteString, ComplexQuoteString`
* Command `Command` with implementations: `CatCommand, GrepCommand, ...`
* Wrapper. `WeakQuoteString`
* Composition. `ComplexQuoteString`
* Interpreter `REPL`
* Null object `CommandFactory.DEFAULT_COMMAND_CLASS`
* Chain-of-responsibility `Executor`
* Proxy `CommandWithArguments`
