# Road To Regex

### Introduction
This project is more for my own self learning. What better way to learn about Regex than to code an engine for it? 
There are three components in this project:

1. DFA (optimised and unoptimised) 
2. NFA (convertible to DFA)
3. Regex-to-NFA Converter 

The project was made using NetBeans IDE 8.1, using JDK 1.8

### Compilation
Run this command in the same folder as this readme:
```
mkdir output
javac -d ./output ./src/regex*/*.java
```

### Running examples
To run the examples, use the following command in the same folder as this readme
```
java -cp ./output regex<example number>.Example
```

Suppose you want to run example 1, then you would type
```
java -cp ./output regex1.Example
```
