package parser;

import scanner.Token;
import parser.Expr;
import java.util.List;

public abstract class Stmt {

    public interface Visitor<T> {
        T visitDeclaration(Declaration declStmt);
        T visitPrint(Print print);
        T visitBlock(Block block);
    }

    public abstract <T> T accept(Visitor<T> visitor);

    public static class Declaration extends Stmt {
        public final String name;
        public final Expr value;

        public Declaration(String n, Expr v) { name = n; value = v; }

        public <T> T accept(Visitor<T> v) { return v.visitDeclaration(this); }
    }

    public static class Print extends Stmt {
        public final Expr value;

        public Print(Expr v) { value = v; }

        public <T> T accept(Visitor<T> v) { return v.visitPrint(this); }
    }

    public static class Block extends Stmt {
        public final List<Stmt> stmts;

        public Block(List<Stmt> s) { stmts = s; }

        public <T> T accept(Visitor<T> v) { return v.visitBlock(this); }
    }
}