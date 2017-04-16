import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

public class FileIO {
	static String filename = "AvgPerformance.txt";

	public static HashMap<String, String> readFromFile() throws IOException {
		HashMap<String, String> fileData = new HashMap<>();

		BufferedReader br = new BufferedReader(new FileReader(filename));
		String line;

		while ((line = br.readLine()) != null) {
			String[] lineData = line.split(":");
			fileData.put(lineData[0], lineData[1]);
		}
		br.close();

		return fileData;
	}

	public static void writeToFile(HashMap<String, String> fileData) throws IOException {
		FileWriter fw = new FileWriter(new File(filename), false);
		String fileInput = new String();

		// System.out.println("File Data: " + fileData);

		Iterator it = fileData.entrySet().iterator();
		while (it.hasNext()) {
			// System.out.println("Inside iter hasNext");
			Entry pair = (Entry) it.next();
			// System.out.println("it next: " + pair);
			fileInput = fileInput.concat(pair.getKey() + ":" + pair.getValue());
			// System.out.println("File Input1: " + fileInput);
			// System.out.println(pair.getKey() + " = " + pair.getValue());
			it.remove(); // avoids a ConcurrentModificationException

			if (it.hasNext())
				fileInput = fileInput.concat("\n");
			// System.out.println("File Input2: " + fileInput);
		}

		// System.out.println("File Input: " + fileInput);
		fw.write(fileInput);
		fw.close();
	}

	public static HashMap<String, String> initialiseFileData(Set<String> players) {
		HashMap<String, String> fileData = new HashMap<>();
		fileData.put("Number of tournaments", "0");

		for (String p : players) {
			fileData.put(p, "0");
		}

		return fileData;
	}

	public static void updateFile(HashMap<String, Float> latestPlayerPoints) {
		HashMap<String, String> fileData = new HashMap<>();
		if (!new File(filename).exists()) {
			fileData = initialiseFileData(latestPlayerPoints.keySet());
		} else
			try {
				fileData = readFromFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		int numTournaments = Integer.parseInt(fileData.get("Number of tournaments"));

		Iterator it = latestPlayerPoints.entrySet().iterator();
		while (it.hasNext()) {
			Entry pair = (Entry) it.next();

			float oldAverage = Float.parseFloat(fileData.get(pair.getKey()));
			float newAverage = (oldAverage * numTournaments + (float) pair.getValue()) / (numTournaments + 1);

			fileData.put((String) pair.getKey(), String.valueOf(newAverage));
			it.remove(); // avoids a ConcurrentModificationException
		}

		fileData.put("Number of tournaments", String.valueOf(numTournaments + 1));

		try {
			writeToFile(fileData);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
