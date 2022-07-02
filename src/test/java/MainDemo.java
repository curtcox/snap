import java.io.*;
import java.util.*;

final class MainDemo {

    public static void main(String[] args) throws IOException {
        //java -jar target/snap-1.0-SNAPSHOT.jar
        InputStream in = new ProcessBuilder("java", "-jar", "target/snap-1.0-SNAPSHOT.jar").start().getInputStream();
        Scanner input = new Scanner(in);
        while (input.hasNextLine()) {
            System.out.println(input.nextLine());
        }
    }

}
