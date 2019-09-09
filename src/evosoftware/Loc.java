package evosoftware;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
public class Loc {
	
	private static FileWriter writer;
	
	public static void main(String[] args) throws IOException {
//		List<String> allLines = Files
//				.readAllLines(Paths.get("C:/Users/Public/eclipse-workspace/evosoftware/src/evosoftware/Loc.java"));
//		int[] a = calcLocMetodos(allLines);
//		System.out.println(a[0] + " " + a[1]);
		System.out.println("Mês, LOC, Métodos, Classes");
		writer = new FileWriter(new File("evolucao.csv"), Charset.forName("ISO-8859-1"));
		writer.write("Mês, LOC, Métodos, Classes\n");
		getNumArquivos(new File("/Users/Sérgio/Downloads/Dataset"), null);
		writer.close();
	}
	
	static int[] calcLocMetodos(List<String> allLines) 
	{
		int loc = 0;
		int metodos = 0;
		int classes = 0;
		for (String line : allLines) {
			if (!line.isEmpty()) {
				loc++;
				if (!isComentado(line) && Pattern.compile("class \\w+").matcher(line).find()) {
					classes++;
				} else if (!isComentado(line) && Pattern.compile("\\w+ \\w+\\(.*?\\)").matcher(line).find() && !line.contains("new ") && !line.contains("return ")) {
//					System.out.println(line);
					metodos++;
				}
			}
		}
//		System.out.println(metodos);
		return new int[] { loc, metodos, classes };
//		System.out.println("Linhas de Codigo: " + loc + " Numero de Metodos: " + metodos + " Número de Classes: "
//				+ getNumArquivos(new File("/Users/Sérgio/Downloads/Dataset")));
	}
	
	static boolean isComentado(String line) {
		line = line.trim();
		return line.startsWith("//") || line.startsWith("/*");
	}
	
	public static int getNumArquivos(File arquivo, String dir) throws IOException {
		File[] arquivos = arquivo.listFiles();
		if (Objects.isNull(dir))
			Arrays.asList(arquivos).sort((a, b) -> Integer.parseInt(a.getName()) - Integer.parseInt(b.getName()));
		int c = 0;
		int[] dirResults = null;
		for (File f : arquivos) {
			if (f.isDirectory()) {
				c += getNumArquivos(f, f.getName());
			} else if (f.isFile()) {
				try {
					int[] results = calcLocMetodos(Files.readAllLines(f.toPath()));
					dirResults = somaResultados(results, dirResults);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				c++;
			}
		}
		if(Objects.nonNull(dirResults)) {
			System.out.println(dir + "," + dirResults[0] + "," + dirResults[1] + "," + dirResults[2]);
			writer.write(dir + "," + dirResults[0] + "," + dirResults[1] + "," + dirResults[2] + "\n");
		}
		return c;
	}
	
	private static int[] somaResultados(int[] results, int[] dirResults) {
		if(Objects.nonNull(dirResults)) {			
			dirResults[0] += results[0];
			dirResults[1] += results[1];
			dirResults[2] += results[2];
		}
		else {
			dirResults = results;
		}
		return dirResults;
	}
	protected void metodoFantasmaTest2() {
	}
	protected void metodoFantasmaTest3() {
	}
	protected void metodoFantasmaTest4() {
	}
}