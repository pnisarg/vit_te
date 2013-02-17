/**
 * @author Nisarg
 * This project is basics of oprating system.(phase1)
 */
import java.io.*;

public class MasterMode {
	// main memory of size 400 bytes 100 x 4
	static char[][] MEMORY;
	// 4 byte general purpose register
	static char[] R;
	// 1 byte toggle register
	static boolean C;
	// 4 byte instruction register
	static char[] IR;
	// 2 byte instruction counter
	static short IC;
	// Input file
	private File inputFile;
	// Output file
	private File outputFile;
	// Reader for file
	static BufferedReader reader;
	// Writer for File
	static BufferedWriter writer;
	// keep track of memory location
	static int u, l;
	// object of SlaveMode
	static SlaveMode slave;

	// constructor for masterMode
	public MasterMode() {
		C = false;
		IC = 0;
		u = 0;
		l = 0;
		MEMORY = new char[100][4];
		R = new char[4];
		IR = new char[4];
		inputFile = new File("/Users/Nisarg/Documents/programs/Eclipse/"
				+ "osPhase1/osinput_phase1.txt");
		outputFile = new File("/Users/Nisarg/Documents/programs/Eclipse/"
				+ "osPhase1/newFile.txt");
		slave = new SlaveMode();
		try {
			reader = new BufferedReader(new FileReader(inputFile));
			writer = new BufferedWriter(new FileWriter(outputFile));
			outputFile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void flush() {
		C = false;
		IC = 0;
		u = 0;
		l = 0;
		MEMORY = new char[100][4];
		R = new char[4];
		IR = new char[4];
	}

	private void load(MasterMode master) {
		try {
			String data = "";
			char[] inst = new char[100];
			// reading the file line by line
			while ((data = reader.readLine()) != null) {
				// start reading from next line load instructions in MEMORY till
				// next line is $data
				if (data.startsWith("$AMJ")) {
					flush();
					while (!(data = reader.readLine()).startsWith("$DTA")) {

						inst = data.toCharArray();
						for (int i = 0; i < inst.length; i++) {
							MEMORY[u][l++] = inst[i];
							l = (l < 4) ? l : 0;
							u = (l == 0) ? u + 1 : u;
						}
					}
					STARTEXECUTION(master);
				}

			}

			writer.close();
			reader.close();
		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	/*
	 * This method initialize IC and calls slave mode to execute user program.
	 */

	private void STARTEXECUTION(MasterMode master) {
		IC = 0;
		try {
			slave.EXECUTEUSERPROGRAM(master);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void MOS(int SI, int mlocation) throws IOException {
		String data = "";

		// memory location

		char[] temp = new char[40];
		switch (SI) {
		// SI 1 : Read Interrupt
		case 1: {

			// start writing the data to memory
			if (!(data = reader.readLine()).startsWith("$END")) {
				l = 0;
				temp = data.toCharArray();
				for (int i = 0; i < temp.length; i++) {
					MEMORY[mlocation][l++] = temp[i];
					l = (l < 4) ? l : 0;
					mlocation = (l == 0) ? mlocation + 1 : mlocation;
				}
			}
			return;

		}
		// SI 2 : Write Interrupt
		case 2: {
			for (int i = 0; i < 10; i++) {
				writer.append(MEMORY[mlocation][0]);
				writer.append(MEMORY[mlocation][1]);
				writer.append(MEMORY[mlocation][2]);
				writer.append(MEMORY[mlocation++][3]);
			}
			writer.newLine();
			break;
		}
		// SI 3 : Terminate Interrupt
		case 3: {
			writer.newLine();
			writer.newLine();
			break;
		}

		}
		return;
	}

	public static void main(String[] args) {
		MasterMode master = new MasterMode();
		master.load(master);

	}
}
