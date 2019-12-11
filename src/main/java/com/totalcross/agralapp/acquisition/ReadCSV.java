import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ReadCSV {

	public static Acquisitor read (String path) {
		// open file input stream
		BufferedReader reader = new BufferedReader(new FileReader(path));

		// read file line by line
		String line = null;
		Scanner scanner = null;
		int index = 0;
		Acquisitor acquisitor = new Acquisitor();

		while ((line = reader.readLine()) != null) {
			scanner = new Scanner(line);
			scanner.useDelimiter(",");
			while (scanner.hasNext()) {
				String data = scanner.next();
				if (index == 0)
					acquisitor.addX(Double.parseDouble(data));
				else if (index == 1)
					acquisitor.addY(Double.parseDouble(data));
				else
					System.out.println("Invalid data: " + data);
				index++;
			}
			index = 0;
		}
		
		//close reader
		reader.close();
		return acquisitor;
	}
}