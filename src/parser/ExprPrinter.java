package parser;

import parser.Expr;

public class ExprPrinter implements Expr.Visitor<String> {

    public String convertString(Expr expr) {
        return expr.accept(this);
    }

    public String visitLiteral(Expr.Literal lit) {
        return lit.value.toString();
    }

    public String visitUnary(Expr.Unary un) {
        return "(" + un.operator.getLexeme() + " " + un.expr.accept(this) + ")";
    }

    public String visitBinary(Expr.Binary bin) {
        return "(" + bin.left.accept(this) + " " + 
            bin.operator.getLexeme() + " " + bin.right.accept(this) + ")";
    }

    public String visitGrouping(Expr.Grouping gr) {
        return "(" + gr.expr.accept(this) + ")";
    }
}