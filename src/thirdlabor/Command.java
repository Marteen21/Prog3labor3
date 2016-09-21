package thirdlabor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;

public class Command {
	protected static File wd = new File(System.getProperty("user.dir"));

	protected static void exit(String[] cmd) {
		System.out.println("Exiting...");
		System.exit(0);
	}

	protected static void reclist(File file, String indent) {
		for (File f : file.listFiles()) {
			try {
				System.out.println(indent + f.getCanonicalPath());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (f.isDirectory())
				reclist(f, indent + "  ");
		}
	}

	protected static void pwd(String[] cmd) {
		try {
			System.out.println(wd.getCanonicalPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected static void cd(String[] cmd) {
		if (cmd.length < 2) {
			try {
				throw new IOException("No such file or directory!");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (cmd[1].equals("..")) {
			wd = wd.getParentFile();
		} else if (Arrays.asList(wd.list()).contains(cmd[1])) {
			wd = new File(wd, cmd[1]);
		} else {
			try {
				throw new IOException("No such file or directory!");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		pwd(cmd);
	}

	protected static void ls(String[] cmd) {
		for (File file : wd.listFiles()) {
			try {
				System.out.print(file.getCanonicalPath());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (cmd.length > 1 && cmd[1].equals("-l")) {
				System.out.print(" " + file.length());
				if (file.isDirectory()) {
					System.out.print(" " + "D");
				} else {
					System.out.print(" " + "F");
				}
			}
			System.out.println();
		}
	}

	protected static void rm(String[] cmd) throws Exception {
		if (cmd.length < 2 || !Arrays.asList(wd.list()).contains(cmd[1])) {
			throw new IOException("No such file or directory!");
		} else {
			File f = new File(wd, cmd[1]);
			if (!f.delete())
				throw new IOException("Can't delete!");
		}
	}

	protected static void mkdir(String[] cmd) throws Exception {
		if (cmd.length < 2) {
			throw new IOException("Can't create the directory!");
		} else {
			File f = new File(wd, cmd[1]);
			if (!f.mkdir()) {
				throw new IOException("Can't create the directory!");
			}
		}
	}

	protected static void cp(String[] cmd) throws Exception {
		if (cmd.length < 3 || !Arrays.asList(wd.list()).contains(cmd[1])) {
			throw new IOException("No such file or directory!");
		} else {
			Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(cmd[2])));
			Reader reader = new BufferedReader(new InputStreamReader(new FileInputStream(cmd[1])));
			while (reader.ready()) {
				writer.write(reader.read());
			}
			writer.close();
			reader.close();
		}
	}

	protected static void mv(String[] cmd) throws IOException {
		if (cmd.length < 3 || !Arrays.asList(wd.list()).contains(cmd[1]))
			throw new IOException("No such file or directory!");
		else {
			File f = new File(wd, cmd[1]);
			if (!f.renameTo(new File(wd, cmd[2]))) {
				throw new IOException("The task failed.");
			}
		}
	}

	protected static void cat(String[] cmd) throws IOException {
		if (cmd.length < 2 || !Arrays.asList(wd.list()).contains(cmd[1])) {
			throw new IOException("No such file or directory!");
		} else {
			BufferedReader br = new BufferedReader(new FileReader(new File(wd, cmd[1])));
			while (br.ready()) {
				System.out.println(br.readLine());
			}
			br.close();
		}
	}

	protected static void wc(String[] cmd) throws IOException {
		int sumChars = 0, sumWords = 0, sumLines = 0;
		if (cmd.length < 2 || !Arrays.asList(wd.list()).contains(cmd[1])) {
			throw new IOException("No such file or directory!");
		} else {
			BufferedReader br = new BufferedReader(new FileReader(new File(wd, cmd[1])));

			while (br.ready()) {
				String s = br.readLine();
				sumChars += s.replace(" ", "").length();
				sumWords += s.split(" ").length;
				sumLines++;
			}
			br.close();

			System.out.println("Number of lines: " + sumLines);
			System.out.println("Number of words: " + sumWords);
			System.out.println("Number of characters: " + sumChars);
		}
	}

	protected static void length(String[] cmd) throws IOException {
		if (cmd.length < 2 || !Arrays.asList(wd.list()).contains(cmd[1])) {
			throw new IOException("No such file or directory!");
		} else {
			System.out.println("Length: " + new File(wd, cmd[1]).length());
		}
	}

	protected static void tail(String[] cmd) throws IOException {
		int n = 0;
		File f = null;
		if (cmd.length < 4 && cmd.length != 2) {
			throw new IOException("No such file or directory!");
		} else if (cmd.length == 2 && Arrays.asList(wd.list()).contains(cmd[1])) {
			n = 10;
			f = new File(wd, cmd[1]);
		} else if (Arrays.asList(wd.list()).contains(cmd[3])) {
			if (cmd[1].equals("-n")) {
				try {
					n = Integer.parseInt(cmd[2]);
				} catch (NumberFormatException e) {
					n = 10;
				}
			} else {
				n = 10;
			}
		}
		if (f.equals(null)) {
			throw new IOException("No such file or directory!");
		}
		LinkedList<String> ls = new LinkedList<String>();
		BufferedReader br = new BufferedReader(new FileReader(f));
		while (br.ready()) {
			ls.add(br.readLine());
		}
		br.close();

		for (int i = ls.size() - n; i < ls.size(); i++) {
			System.out.println(ls.get(i));
		}
	}

	protected static void grep(String[] cmd) throws IOException {
		if (cmd.length < 3 || !Arrays.asList(wd.list()).contains(cmd[2])) {
			throw new IOException("No such file or directory!");
		} else {
			BufferedReader br = new BufferedReader(new FileReader(new File(wd, cmd[2])));
			while (br.ready()) {
				String string = br.readLine();
				if (string.matches(cmd[1])) {
					System.out.println(string);
				}
			}
			br.close();
		}
	}

	public static void main(String[] args) {
		boolean running = true;
		System.out.println("Type command...");
		while (running) {
			Scanner scan = new Scanner(System.in);
			String s = scan.nextLine();
			String[] cmd = s.split(" ");
			if (cmd[0].equals("exit")) {
				exit(cmd);
			} else if (cmd[0].equals("reclist")) {
				reclist(wd, " ");
			} else if (cmd[0].equals("pwd")) {
				pwd(cmd);
			} else if (cmd[0].equals("cd")) {
				cd(cmd);
			} else if (cmd[0].equals("ls")) {
				ls(cmd);
			} else if (cmd[0].equals("rm")) {
				try {
					rm(cmd);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (cmd[0].equals("mkdir")) {
				try {
					mkdir(cmd);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (cmd[0].equals("cp")) {
				try {
					cp(cmd);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (cmd[0].equals("mv")) {
				try {
					mv(cmd);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (cmd[0].equals("cat")) {
				try {
					cat(cmd);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (cmd[0].equals("wc")) {
				try {
					wc(cmd);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (cmd[0].equals("length")) {
				try {
					length(cmd);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (cmd[0].equals("tail")) {
				try {
					tail(cmd);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (cmd[0].equals("grep")) {
				try {
					grep(cmd);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				System.out.println("Unknown command");
			}

		}
	}

}
