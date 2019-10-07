package parser;

import scanner.Token;

public class ParserException extends Exception {
    public Token token;

    public ParserException(Token t) { token = t; }

    public String toString() {
        return "Error at token: " + token.toString();
    }
}