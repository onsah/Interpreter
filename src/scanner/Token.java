package scanner;

import java.util.HashMap;

public class Token {

    private TokenType type;
    private String lexeme;
    private int line;

    public static HashMap<String, TokenType> KEYWORDS = new HashMap<>();

    // Setup keywords
    static {
        KEYWORDS.put("true", TokenType.TRUE);
        KEYWORDS.put("false", TokenType.FALSE);
        KEYWORDS.put("nil", TokenType.NIL);
        KEYWORDS.put("var", TokenType.VAR);
        KEYWORDS.put("print", TokenType.PRINT);
    }

    public Token(TokenType t, String l, int ln) {
        setType(t);
        setLexeme(l);
        setLine(ln);
    }

    public int getLine() {
		return line;
	}

	public void setLine(int line) {
		this.line = line;
	}

	public String getLexeme() {
        return lexeme;
    }

    public void setLexeme(String lexeme) {
        this.lexeme = lexeme;
    }

    public TokenType getType() {
        return type;
    }

    public void setType(TokenType type) {
        this.type = type;
    }

    public String toString() {
        return "[line " + String.valueOf(this.line) + "] " + type.toString() + ": " + lexeme;
    }

    public enum TokenType {
        // a variable name
        NAME,

        // Literals
        STRING,
        NUMBER,
        TRUE,
        FALSE,
        NIL,    // Represents absence of value

        // Operators
        PLUS,
        MINUS,
        STAR,
        SLASH,
        EQUAL, EQUAL_EQUAL,
        GREATER, GREATER_EQ,
        LESS, LESS_EQ,
        NOT, NOT_EQ,
        AND,
        OR,
        LEFT_PAREN,
        RIGHT_PAREN,

        // Keywords
        VAR,
        PRINT,

        // Other tokens
        SC,

        // End of file
        EOF,
    }
}