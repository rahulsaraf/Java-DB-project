import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

/**
 * @author Rahul
 *
 */
public class MyDatabase {

	//private static final String PATH = "D:/UTDallasStudy/Coursework/Summer_15/Database_Design/Project2/";
	private static final String PATH = "";
	/**
	 * @param args
	 */

	// Assign byte variables using Decimal notation
	final static byte double_blind_mask = 8; // binary 0000 1000
	final static byte controlled_study_mask = 4; // binary 0000 0100
	final static byte govt_funded_mask = 2; // binary 0000 0010
	final static byte fda_approved_mask = 1; // binary 0000 0001

	static Scanner scan;
	static OutputStream out;

	static TreeMap<Integer, ArrayList<Integer>> idIndex = new TreeMap<Integer, ArrayList<Integer>>();
	static TreeMap<String, ArrayList<Integer>> companyIndex = new TreeMap<String, ArrayList<Integer>>();
	static TreeMap<String, ArrayList<Integer>> drugIndex = new TreeMap<String, ArrayList<Integer>>();
	static TreeMap<Short, ArrayList<Integer>> trialsIndex = new TreeMap<Short, ArrayList<Integer>>();
	static TreeMap<Short, ArrayList<Integer>> patientsIndex = new TreeMap<Short, ArrayList<Integer>>();
	static TreeMap<Short, ArrayList<Integer>> dosageIndex = new TreeMap<Short, ArrayList<Integer>>();
	static TreeMap<Float, ArrayList<Integer>> readingIndex = new TreeMap<Float, ArrayList<Integer>>();
	static TreeMap<Boolean, ArrayList<Integer>> double_blindIndex = new TreeMap<Boolean, ArrayList<Integer>>();
	static TreeMap<Boolean, ArrayList<Integer>> controlled_studyIndex = new TreeMap<Boolean, ArrayList<Integer>>();
	static TreeMap<Boolean, ArrayList<Integer>> govt_fundedIndex = new TreeMap<Boolean, ArrayList<Integer>>();
	static TreeMap<Boolean, ArrayList<Integer>> fda_approvedIndex = new TreeMap<Boolean, ArrayList<Integer>>();

	static String[] headerArray = {"id","company","drug","trials","patients","dosage","reading","double_blind","controlled_study","govt_funded","fda_approved"};
	
	
	static String idname = "id.ndx";
	static String companyname = "company.ndx";
	static String drugname = "drug.ndx";
	static String trialsname = "trials.ndx";
	static String patientsname = "patients.ndx";
	static String dosagename = "dosage.ndx";
	static String readingname = "reading.ndx";
	static String double_blindname = "double_blind.ndx";
	static String controlled_studyname = "controlled_study.ndx";
	static String govt_fundedname = "govt_funded.ndx";
	static String fda_approvedname = "fda_approved.ndx";
	static boolean result = true;
	
	
	public static void main(String[] args) throws IOException {
		System.out.println("Welcome to Database Console");
		File file = new File(PATH + "PHARMA_TRIALS_1000B.csv");
		scan = new Scanner(file);
		RandomAccessFile out = new RandomAccessFile(PATH + "data.db", "rw");
		Scanner input = new Scanner(System.in);
		while(true){
			System.out.println("Please enter an option for ");
					System.out.println("1. Load Database ");
					System.out.println("2. Query Database - ");
					System.out.println("3. To Exit - ");
			    System.out.println("=========================================================");
			    System.out.println("Selected option = ");
			int option = input.nextInt();
			if(option == 1){
				result = createDBAndIndexFile(out);
				System.out.println("");
				System.out.println(".db file and .ndx files created successfully!!!");
			}else if(option == 2){
				if(result){
				System.out.println("Below are the columns to query : - ");
				System.out.println("");
				for (int i = 0; i < headerArray.length; i++) {
					System.out.println(" " + headerArray[i]);
				}
				System.out.println("Below are avalaible operators - ");
				System.out.println("=, <, >, >=, <=, !=");			
				
				System.out.println("=====================Query================================");
				System.out.println("Select a column - ");
				String selectedColumn = input.next();
				System.out.println("Select an operator - ");
				String searchOperator = input.next();
				System.out.println("Select a value - ");
				String searchValue = input.next();
				System.out.println("======================Query Result=========================");
				try {
					boolean tr = true;
					for (int i = 0; i < headerArray.length; i++) {
						if(selectedColumn.equals(headerArray[i])){
							tr = true;
						}else{
							tr = false;
						}
					}
					if(tr){
						queryDatabase(selectedColumn, searchOperator, searchValue);						
					}else{
						System.out.println(selectedColumn
								+ " Column is not supported or invalid");
					}
				} catch (Exception e) {
					//System.out.println("something wrong happened");
					//e.printStackTrace();
				} finally {
					//input.close();
				}
				System.out.println("===========================================================");
				}else{
					System.out.println("Please select option 1 first and then select option 2");
				}
			}else if(option == 3){
				System.out.println("System is exiting..!!");
				System.exit(0);
			}else{
				System.out.println("Invalid Option");
			}	
		}
	}

	private static void queryDatabase(String selectedColumn,
			String searchOperator, String searchValue)
			throws NumberFormatException, IOException {
		File indexFile = new File(PATH + selectedColumn + ".ndx");
		scan = new Scanner(indexFile);
		double doubleVal = 0;
		int intVal = 0;
		String strVal = "";
		boolean booleanVal = false;
		String record = "";

		if (selectedColumn.equals("id") || selectedColumn.equals("trials")
				|| selectedColumn.equals("patients")
				|| selectedColumn.equals("dosage_mg")
				|| selectedColumn.equals("reading")) {

			switch (searchOperator) {
			case ">":
				while ((record = scan.nextLine()) != null) {
					String[] rows = record.split("  ");
					for (int i = 1; i < rows.length; i++) {
						if (selectedColumn.equals("reading")) {
							doubleVal = Double.valueOf(searchValue);
							if (Double.valueOf(rows[0]) > doubleVal) {
								readFile(rows[i]);
							}
						} else {
							intVal = Integer.valueOf(searchValue);
							if (Integer.valueOf(rows[0]) > intVal) {
								readFile(rows[i]);
							}
						}
					}
				}
				break;
			case "<":
				while ((record = scan.nextLine()) != null) {
					String[] rows = record.split("  ");
					for (int i = 1; i < rows.length; i++) {
						if (selectedColumn.equals("reading")) {
							doubleVal = Double.valueOf(searchValue);
							if (Double.valueOf(rows[0]) < doubleVal) {
								readFile(rows[i]);
							}
						} else {
							intVal = Integer.valueOf(searchValue);
							if (Integer.valueOf(rows[0]) < intVal) {
								readFile(rows[i]);
							}
						}
					}
				}
				break;
			case ">=":
				while ((record = scan.nextLine()) != null) {
					String[] rows = record.split("  ");
					for (int i = 1; i < rows.length; i++) {
						if (selectedColumn.equals("reading")) {
							doubleVal = Double.valueOf(searchValue);
							if (Double.valueOf(rows[0]) >= doubleVal) {
								readFile(rows[i]);
							}
						} else {
							intVal = Integer.valueOf(searchValue);
							if (Integer.valueOf(rows[0]) >= intVal) {
								readFile(rows[i]);
							}
						}
					}
				}
				break;
			case "<=":
				while ((record = scan.nextLine()) != null) {
					String[] rows = record.split("  ");
					for (int i = 1; i < rows.length; i++) {
						if (selectedColumn.equals("reading")) {
							doubleVal = Double.valueOf(searchValue);
							if (Double.valueOf(rows[0]) <= doubleVal) {
								readFile(rows[i]);
							}
						} else {
							intVal = Integer.valueOf(searchValue);
							if (Integer.valueOf(rows[0]) <= intVal) {
								readFile(rows[i]);
							}
						}
					}
				}
				break;
			case "=":
				while ((record = scan.nextLine()) != null) {
					String[] rows = record.split("  ");
					for (int i = 1; i < rows.length; i++) {
						if (selectedColumn.equals("reading")) {
							doubleVal = Double.valueOf(searchValue);
							if (Double.valueOf(rows[0]) == doubleVal) {
								readFile(rows[i]);
							}
						} else {
							intVal = Integer.valueOf(searchValue);
							if (Integer.valueOf(rows[0]) == intVal) {
								readFile(rows[i]);
							}
						}
					}
				}
				break;
			case "!=":
				while ((record = scan.nextLine()) != null) {
					String[] rows = record.split("  ");
					for (int i = 1; i < rows.length; i++) {
						if (selectedColumn.equals("reading")) {
							doubleVal = Double.valueOf(searchValue);
							if (Double.valueOf(rows[0]) != doubleVal) {
								readFile(rows[i]);
							}
						} else {
							intVal = Integer.valueOf(searchValue);
							if (Integer.valueOf(rows[0]) != intVal) {
								readFile(rows[i]);
							}
						}
					}
				}
				break;
			default:
				System.out.println(searchOperator
						+ " Operator is not supported or invalid");
				break;
			}
		} else if (selectedColumn.equals("company")
				|| selectedColumn.equals("drug_id")) {

			switch (searchOperator) {
			case ">":
				while ((record = scan.nextLine()) != null) {
					String[] rows = record.split("  ");
					for (int i = 1; i < rows.length; i++) {
						strVal = searchValue;
						if (rows[0].compareTo(strVal) > 0) {
							readFile(rows[i]);
						}
					}
				}
				break;
			case "<":
				while ((record = scan.nextLine()) != null) {
					String[] rows = record.split("  ");
					for (int i = 1; i < rows.length; i++) {
						strVal = searchValue;
						if (rows[0].compareTo(strVal) < 0) {
							readFile(rows[i]);
						}
					}
				}
				break;
			case ">=":
				while ((record = scan.nextLine()) != null) {
					String[] rows = record.split("  ");
					for (int i = 1; i < rows.length; i++) {
						strVal = searchValue;
						if (rows[0].compareTo(strVal) >= 0) {
							readFile(rows[i]);
						}
					}
				}
				break;
			case "<=":
				while ((record = scan.nextLine()) != null) {
					String[] rows = record.split("  ");
					for (int i = 1; i < rows.length; i++) {
						strVal = searchValue;
						if (rows[0].compareTo(strVal) <= 0) {
							readFile(rows[i]);
						}
					}
				}
				break;
			case "=":
				while ((record = scan.nextLine()) != null) {
					String[] rows = record.split("  ");
					for (int i = 1; i < rows.length; i++) {
						strVal = searchValue;
						if (rows[0].equals(strVal)) {
							readFile(rows[i]);
						}
					}
				}
				break;
			case "!=":
				while ((record = scan.nextLine()) != null) {
					String[] rows = record.split("  ");
					for (int i = 1; i < rows.length; i++) {
						strVal = searchValue;
						if (!(rows[0].equals(strVal))) {
							readFile(rows[i]);
						}
					}
				}
				break;
			default:
				System.out.println(searchOperator
						+ " Operator is not supported or invalid");
				break;
			}
		} else if (selectedColumn.equals("double_blind")
				|| selectedColumn.equals("controlled_study")
				|| selectedColumn.equals("govt_funded")
				|| selectedColumn.equals("fda_approved")) {
			switch (searchOperator) {
			case "=":
				while ((record = scan.nextLine()) != null) {
					String[] rows = record.split("  ");
					booleanVal = Boolean.valueOf(searchValue);
					if (booleanVal == Boolean.valueOf(rows[0])) {
						for (int i = 1; i < rows.length; i++) {
							readFile(rows[i]);
						}
					}
				}
				break;
			case "!=":
				while ((record = scan.nextLine()) != null) {
					String[] rows = record.split("  ");
					booleanVal = Boolean.valueOf(searchValue);
					if (booleanVal != Boolean.valueOf(rows[0])) {
						for (int i = 1; i < rows.length; i++) {
							readFile(rows[i]);
						}
					}
				}
				break;
			default:
				System.out.println(searchOperator
						+ " Operator is not supported or invalid");
				break;
			}
		}else{
			System.out.println(selectedColumn
					+ " Column is not supported or invalid");
		}
	}

	private static void readFile(String string) throws NumberFormatException,
			IOException {
		RandomAccessFile db = new RandomAccessFile(PATH + "data.db", "r");
		db.seek(Long.valueOf(string));
		byte[] companyBytes = new byte[4];
		db.read(companyBytes, 0, 4);
		System.out.print(toInt(companyBytes) + " | ");
		byte strLen = db.readByte();
		companyBytes = new byte[(int) strLen];
		db.read(companyBytes);
		System.out.print(toStr(companyBytes) + " | ");
		companyBytes = new byte[6];
		db.read(companyBytes);
		System.out.print(toStr(companyBytes) + " | ");

		for (int i = 1; i <= 3; i++) {
			System.out.print(db.readShort() + " | ");
		}
		System.out.print(db.readFloat() + " | ");
		byte onlyByte = db.readByte();
		System.out.print(((onlyByte & double_blind_mask) > 0) ? "true | "
				: "false|");
		System.out.print(((onlyByte & controlled_study_mask) > 0) ? "true | "
				: "false|");
		System.out.print(((onlyByte & govt_funded_mask) > 0) ? "true | "
				: "false|");
		System.out.println(((onlyByte & fda_approved_mask) > 0) ? "true"
				: "false");
		db.close();
	}

	public static int toInt(byte[] bytes) {
		return bytes[0] << 24 | (bytes[1] & 0xFF) << 16
				| (bytes[2] & 0xFF) << 8 | (bytes[3] & 0xFF);
	}

	public static String toStr(byte[] bytes) {
		StringBuilder b = new StringBuilder();
		for (byte x : bytes) {
			b.append((char) x);
		}

		return b.toString();
	}

	private static boolean createDBAndIndexFile(RandomAccessFile out)
			throws IOException {
		String line = scan.nextLine();

		try {
			while ((line = scan.nextLine()) != null) {
				String[] columnVal = line
						.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
				int pointer = Long.valueOf(out.getFilePointer()).intValue();

				// for ID
				Integer id = Integer.valueOf(columnVal[0]);
				out.writeInt(id);

				if (idIndex.containsKey(id)) {
					idIndex.get(id).add(pointer);
				} else {
					ArrayList<Integer> x = new ArrayList<Integer>();
					x.add(pointer);
					idIndex.put(id, x);
				}

				// for company
				String company = columnVal[1].replace("\"", "");
				int length = company.length();
				out.writeByte(length);
				out.writeBytes(company);

				if (companyIndex.containsKey(company)) {
					companyIndex.get(company).add(pointer);
				} else {
					ArrayList<Integer> x = new ArrayList<Integer>();
					x.add(pointer);
					companyIndex.put(company, x);
				}

				// for drug_id

				String drug_id = columnVal[2];
				out.writeBytes(drug_id);
				if (drugIndex.containsKey(drug_id)) {
					drugIndex.get(drug_id).add(pointer);
				} else {
					ArrayList<Integer> x = new ArrayList<Integer>();
					x.add(pointer);
					drugIndex.put(drug_id, x);
				}

				// for trials
				Short trials = Short.valueOf(columnVal[3]);
				out.writeShort(trials);

				if (trialsIndex.containsKey(trials)) {
					trialsIndex.get(trials).add(pointer);
				} else {
					ArrayList<Integer> x = new ArrayList<Integer>();
					x.add(pointer);
					trialsIndex.put(trials, x);
				}

				// for patients
				Short patients = Short.valueOf(columnVal[4]);
				out.writeShort(Short.valueOf(columnVal[4]));
				if (patientsIndex.containsKey(patients)) {
					patientsIndex.get(patients).add(pointer);
				} else {
					ArrayList<Integer> x = new ArrayList<Integer>();
					x.add(pointer);
					patientsIndex.put(patients, x);
				}
				// for dosage_mg
				Short dosage = Short.valueOf(columnVal[5]);
				out.writeShort(dosage);
				if (dosageIndex.containsKey(dosage)) {
					dosageIndex.get(dosage).add(pointer);
				} else {
					ArrayList<Integer> x = new ArrayList<Integer>();
					x.add(pointer);
					dosageIndex.put(dosage, x);
				}
				// for reading
				Float reading = Float.valueOf(columnVal[6]);
				out.writeFloat(reading);
				if (readingIndex.containsKey(reading)) {
					readingIndex.get(reading).add(pointer);
				} else {
					ArrayList<Integer> x = new ArrayList<Integer>();
					x.add(pointer);
					readingIndex.put(reading, x);
				}

				// for all boolean variables
				byte commonByte = 0x00;
				boolean double_blind = Boolean.valueOf(columnVal[7]);
				if (double_blindIndex.containsKey(double_blind)) {
					double_blindIndex.get(double_blind).add(pointer);
				} else {
					ArrayList<Integer> x = new ArrayList<Integer>();
					x.add(pointer);
					double_blindIndex.put(double_blind, x);
				}
				if (double_blind) {
					commonByte = (byte) (commonByte | double_blind_mask);
				}

				boolean controlled_study = Boolean.valueOf(columnVal[8]);
				if (controlled_studyIndex.containsKey(controlled_study)) {
					controlled_studyIndex.get(controlled_study).add(pointer);
				} else {
					ArrayList<Integer> x = new ArrayList<Integer>();
					x.add(pointer);
					controlled_studyIndex.put(controlled_study, x);
				}
				if (controlled_study) {
					commonByte = (byte) (commonByte | controlled_study_mask);
				}

				boolean govt_funded = Boolean.valueOf(columnVal[9]);
				if (govt_fundedIndex.containsKey(govt_funded)) {
					govt_fundedIndex.get(govt_funded).add(pointer);
				} else {
					ArrayList<Integer> x = new ArrayList<Integer>();
					x.add(pointer);
					govt_fundedIndex.put(govt_funded, x);
				}
				if (govt_funded) {
					commonByte = (byte) (commonByte | govt_funded_mask);
				}

				boolean fda_approved = Boolean.valueOf(columnVal[9]);
				if (fda_approvedIndex.containsKey(fda_approved)) {
					fda_approvedIndex.get(fda_approved).add(pointer);
				} else {
					ArrayList<Integer> x = new ArrayList<Integer>();
					x.add(pointer);
					fda_approvedIndex.put(fda_approved, x);
				}
				if (fda_approved) {
					commonByte = (byte) (commonByte | fda_approved_mask);
				}

				out.writeByte((int) commonByte);

				pointer += 4;
				pointer += length + 1;
				pointer += 6;
				pointer += 2;
				pointer += 2;
				pointer += 2;
				pointer += 4;
				pointer += 1;
				
			}
		} catch (Exception e) {
//			e.printStackTrace();
		} finally {
			out.close();
		}
		result = true;
		createIndexFiles(idname);
		createIndexFiles(companyname);
		createIndexFiles(drugname);
		createIndexFiles(trialsname);
		createIndexFiles(patientsname);
		createIndexFiles(dosagename);
		createIndexFiles(readingname);
		createIndexFiles(double_blindname);
		createIndexFiles(controlled_studyname);
		createIndexFiles(govt_fundedname);
		createIndexFiles(fda_approvedname);
		return result;
	}

	private static void createIndexFiles(String indexFileName)
			throws IOException {

		File indexFile = new File(PATH + indexFileName);
		BufferedWriter out = new BufferedWriter(new FileWriter(indexFile));
		String data = "";
		switch (indexFileName) {
		case "id.ndx":
			Set<Integer> idSet = idIndex.keySet();
			Iterator<Integer> iterator0 = idSet.iterator();
			while (iterator0.hasNext()) {
				Integer id = (Integer) iterator0.next();
				data = data + id;
				ArrayList<Integer> idList = idIndex.get(id);
				for (int i = 0; i < idList.size(); i++) {
					data = data + "  " + idList.get(i);
				}
				data = data + "\n";
			}
			break;

		case "company.ndx":
			Set<String> companySet = companyIndex.keySet();
			Iterator<String> iterator1 = companySet.iterator();
			while (iterator1.hasNext()) {
				String company = (String) iterator1.next();
				data = data + company;
				ArrayList<Integer> idList = companyIndex.get(company);
				for (int i = 0; i < idList.size(); i++) {
					data = data + "  " + idList.get(i);
				}
				data = data + "\n";
			}
			break;

		case "drug.ndx":
			Set<String> drugSet = drugIndex.keySet();
			Iterator<String> iterator2 = drugSet.iterator();
			while (iterator2.hasNext()) {
				String drug = (String) iterator2.next();
				data = data + drug;
				ArrayList<Integer> idList = drugIndex.get(drug);
				for (int i = 0; i < idList.size(); i++) {
					data = data + "  " + idList.get(i);
				}
				data = data + "\n";
			}
			break;

		case "trials.ndx":
			Set<Short> trialsSet = trialsIndex.keySet();
			Iterator<Short> iterator3 = trialsSet.iterator();
			while (iterator3.hasNext()) {
				Short trials = (Short) iterator3.next();
				data = data + trials;
				ArrayList<Integer> idList = trialsIndex.get(trials);
				for (int i = 0; i < idList.size(); i++) {
					data = data + "  " + idList.get(i);
				}
				data = data + "\n";
			}
			break;

		case "patients.ndx":
			Set<Short> patientsSet = patientsIndex.keySet();
			Iterator<Short> iterator4 = patientsSet.iterator();
			while (iterator4.hasNext()) {
				Short patients = (Short) iterator4.next();
				data = data + patients;
				ArrayList<Integer> idList = patientsIndex.get(patients);
				for (int i = 0; i < idList.size(); i++) {
					data = data + "  " + idList.get(i);
				}
				data = data + "\n";
			}
			break;

		case "dosage.ndx":
			Set<Short> dosageSet = dosageIndex.keySet();
			Iterator<Short> iterator5 = dosageSet.iterator();
			while (iterator5.hasNext()) {
				Short dosage = (Short) iterator5.next();
				data = data + dosage;
				ArrayList<Integer> idList = dosageIndex.get(dosage);
				for (int i = 0; i < idList.size(); i++) {
					data = data + "  " + idList.get(i);
				}
				data = data + "\n";
			}
			break;

		case "reading.ndx":
			Set<Float> readingSet = readingIndex.keySet();
			Iterator<Float> iterator6 = readingSet.iterator();
			while (iterator6.hasNext()) {
				Float reading = (Float) iterator6.next();
				data = data + reading;
				ArrayList<Integer> idList = readingIndex.get(reading);
				for (int i = 0; i < idList.size(); i++) {
					data = data + "  " + idList.get(i);
				}
				data = data + "\n";
			}
			break;

		case "double_blind.ndx":
			Set<Boolean> double_blindSet = double_blindIndex.keySet();
			Iterator<Boolean> iterator7 = double_blindSet.iterator();
			while (iterator7.hasNext()) {
				Boolean double_blind = (Boolean) iterator7.next();
				data = data + double_blind;
				ArrayList<Integer> idList = double_blindIndex.get(double_blind);
				for (int i = 0; i < idList.size(); i++) {
					data = data + "  " + idList.get(i);
				}
				data = data + "\n";
			}
			break;

		case "controlled_study.ndx":
			Set<Boolean> controlled_studySet = controlled_studyIndex.keySet();
			Iterator<Boolean> iterator8 = controlled_studySet.iterator();
			while (iterator8.hasNext()) {
				Boolean controlled_study = (Boolean) iterator8.next();
				data = data + controlled_study;
				ArrayList<Integer> idList = controlled_studyIndex
						.get(controlled_study);
				for (int i = 0; i < idList.size(); i++) {
					data = data + "  " + idList.get(i);
				}
				data = data + "\n";
			}
			break;

		case "fda_approved.ndx":
			Set<Boolean> fda_approvedSet = fda_approvedIndex.keySet();
			Iterator<Boolean> iterator9 = fda_approvedSet.iterator();
			while (iterator9.hasNext()) {
				Boolean fda_approved = (Boolean) iterator9.next();
				data = data + fda_approved;
				ArrayList<Integer> idList = fda_approvedIndex.get(fda_approved);
				for (int i = 0; i < idList.size(); i++) {
					data = data + "  " + idList.get(i);
				}
				data = data + "\n";
			}
			break;

		case "govt_funded.ndx":
			Set<Boolean> govt_fundedSet = govt_fundedIndex.keySet();
			Iterator<Boolean> iterator10 = govt_fundedSet.iterator();
			while (iterator10.hasNext()) {
				Boolean govt_funded = (Boolean) iterator10.next();
				data = data + govt_funded;
				ArrayList<Integer> idList = govt_fundedIndex.get(govt_funded);
				for (int i = 0; i < idList.size(); i++) {
					data = data + "  " + idList.get(i);
				}
				data = data + "\n";
			}
			break;

		default:
			break;
		}
		out.write(data);
		out.close();
	}
}