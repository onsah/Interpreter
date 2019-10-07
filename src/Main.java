
import scanner.*;
import java.io.*;

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

        int lineNum = 1;
        for (Token t: scanner.scan()) {
            if (t.getLine() > lineNum) {
                lineNum = t.getLine();
                System.out.println();
            }
            System.out.print(t);
            System.out.print(", ");            
        }
        System.out.println();
        reader.close();
    }
}