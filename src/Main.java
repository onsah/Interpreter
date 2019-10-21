
import scanner.*;
import java.io.*;
import java.util.List;

import interpreter.Interpreter;
import interpreter.Value;
import parser.*;

class Main {
    
    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.out.println("Usage: java program [file-path]");
            return;
        }
        String path = args[0];
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = new BufferedReader(new FileReader(new File(path)));

        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
            sb.append('\n');
        }

        String source= sb.toString();
        Scanner scanner = new Scanner(source);
        List<Token> tokens = scanner.scan();

        Parser parser = new Parser(tokens);
        Interpreter interpreter = new Interpreter();
        try {
            Expr parsed = parser.parse();
            // System.out.println((new ExprPrinter()).convertString(parsed));
            Value val = interpreter.evaluate(parsed);
            System.out.println("Evaluated: " + val.toString());
        } catch (ParserException e) {
            System.out.println(e);
        }

        reader.close();
    }
}