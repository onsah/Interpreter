package parser;

import scanner.Token;
import scanner.Token.TokenType;

import static scanner.Token.TokenType.*;
import java.util.List;

public class Parser {
    
    private List<Token> tokens;
    private int cursor = 0;

    public Parser(List<Token> t) { tokens = t; }

    public Expr parse() throws ParserException {
        return expr();   
    }

    private Expr expr() throws ParserException { return binary(); }

    private Expr binary() throws ParserException {
        Expr expr = unary();
        Token op = null;
        while ((op = match(PLUS, MINUS, STAR, SLASH, LESS, GREATER, AND, OR)) != null) {
            Expr right = unary();
            expr = new Expr.Binary(op, expr, right);
        }
        return expr;
    }

    private Expr unary() throws ParserException{
        Token op = match(MINUS, NOT);
        if (op != null) {
            Expr expr = unary();
            return new Expr.Unary(op, expr);
        } else {
            return grouping();
        }
    }

    private Expr grouping() throws ParserException {
        if (match(LEFT_PAREN) != null) {
            Expr expr = expr();
            Token t = match(RIGHT_PAREN);
            if (t == null)
                throw new ParserException(t);
            return new Expr.Grouping(expr);
        } else {
            return literal();
        }
    }

    private Expr literal() throws ParserException {
        Token tok = advance();
        switch (tok.getType()) {
            case NUMBER:
                return new Expr.Literal(Integer.valueOf(tok.getLexeme()));
            case STRING:
                return new Expr.Literal(tok.getLexeme());
            case TRUE:
                return new Expr.Literal(Boolean.valueOf(true));
            case FALSE:
                return new Expr.Literal(Boolean.valueOf(false));
            case NIL:
                return new Expr.Literal(null);
            default:
                throw new ParserException(tok);
        }
    }

    /**
     * Returns a token from the tokens list
     * @return next token
     */
    private Token advance() {
        return tokens.get(cursor++);
    }

    /**
     * If current token matches with `type` automatically advances the cursor.
     * @param type type to be matched
     * @return current token if matches, otherwise `null`
     */
    private Token match(TokenType type) {
        if (tokens.get(cursor).getType() == type) {
            return advance();
        } else {
            return null;
        }
    }

    /**
     * Variadic version of match if any of them matches it is successful
     * @param types types to be matched
     * @return current token if any of them matches, otherwise `null`
     */
    private Token match(TokenType... types) {
        for (TokenType type: types) {
            Token t = match(type);
            if (t != null) 
                return t;
        }
        return null;
    }
}