package scanner;

import scanner.Token.TokenType;
import java.util.ArrayList;
import java.util.List;

public class Scanner {

    private final String source;
    private final ArrayList<Token> tokens = new ArrayList<>();
    private int cursor = 0;
    private int line = 1;

    public Scanner(String s) {
        source = s;
    }

    public List<Token> scan() {
        if (tokens.isEmpty()) {
            Token last = null;
            do {
                last = scanToken();
                tokens.add(last);
            } while (last.getType() != TokenType.EOF);
        }
        return tokens;
    }

    private Token scanToken() {
        int start = cursor;
        while (true) {
            try {
                char c = advance();
                switch (c) {
                    case '"': 
                        return string(start + 1);
                    case '(':
                        return makeToken(TokenType.LEFT_PAREN, start, cursor);
                    case ')':
                        return makeToken(TokenType.RIGHT_PAREN, start, cursor);
                    case '+':
                        return makeToken(TokenType.PLUS, start, cursor);
                    case '-':
                        return makeToken(TokenType.MINUS, start, cursor);
                    case '*':
                        return makeToken(TokenType.STAR, start, cursor);
                    case '/':
                        return makeToken(TokenType.SLASH, start, cursor);
                    case '&':
                        return makeToken(TokenType.AND, start, cursor);
                    case '|':
                        return makeToken(TokenType.OR, start, cursor);
                    case ';':
                        return makeToken(TokenType.SC, start, cursor);
                    case '>':
                        if (match('=')) {
                            return makeToken(TokenType.GREATER_EQ, start, cursor);
                        } else {
                            return makeToken(TokenType.GREATER, start, cursor);
                        }
                    case '<':
                        if (match('=')) {
                            return makeToken(TokenType.LESS_EQ, start, cursor);
                        } else {
                            return makeToken(TokenType.LESS, start, cursor);
                        }
                    case '=':
                        if (match('=')) {
                            return makeToken(TokenType.EQUAL_EQUAL, start, cursor);
                        } else {
                            return makeToken(TokenType.EQUAL, start, cursor);
                        }
                    case '!':
                        if (match('=')) {
                            return makeToken(TokenType.NOT_EQ, start, cursor);
                        } else {
                            return makeToken(TokenType.NOT, start, cursor);
                        }
                    // Ignore
                    case '\n':
                        line += 1;
                    case '\t':
                    case '\r':
                    case ' ':
                        // escape characters are not included in tokens
                        start += 1;
                        break;
                    default:
                        if (isDigit(c)) {
                            return number(start);
                        } if (isAlpha(c)) {
                            return name(start);
                        } else {
                            throw new ScanException("Unexpected character: " + c, line);
                        }
                }
            } catch (ScanException e) {
                return makeToken(TokenType.EOF, start, cursor);
            }
        }
    }

    private Token string(int start) throws ScanException {
        while (!match('"')) {
            advance();
        }
        return makeToken(TokenType.STRING, start, cursor - 1);
    }

    private Token number(int start) throws ScanException {
        while (isDigit(peek())) {
            advance();
        }
        return makeToken(TokenType.NUMBER, start, cursor);
    }

    private Token name(int start) throws ScanException {
        while(isAlpha(peek())) {
            advance();
        }
        Token token = makeToken(TokenType.NAME, start, cursor);
        String lexeme = token.getLexeme();
        if (Token.KEYWORDS.containsKey(lexeme)) {
            token.setType(Token.KEYWORDS.get(lexeme));
        }
        return token;
    }

    /* Helper methods */

    private Token makeToken(TokenType type, int start, int end) {
        String lexeme = this.source.substring(start, end);
        return new Token(type, lexeme, this.line);
    }

    private char peek() {
        if (!isEof())
            return this.source.charAt(cursor);
        else
            return '\n';
    }

    private char advance() throws ScanException {
        if (!isEof()) {
            return this.source.charAt(cursor++);
        } else {
            throw new ScanException("Reached end of the file", line);
        }
    }

    private boolean match(char c) {
        if (this.source.charAt(cursor) == c) {
            cursor += 1;
            return true;
        } else {
            return false;
        }
    }

    // is end of file
    private boolean isEof() {
        return cursor >= source.length();
    }

    private static boolean isDigit(char c) {
        int value = (int) c;
        return value >= 48 && value <= 57; 
    }

    private static boolean isAlpha(char c) {
        int value = (int) c;
        return (value >= 65 && value <= 90) || 
            (value >= 97 && value <= 122);
    }

    private class ScanException extends Exception {
        
        public ScanException(String message, int line) {
            super("[line " + String.valueOf(line) + "] " + message);
        }
    }
}