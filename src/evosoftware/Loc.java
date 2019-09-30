package evosoftware;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public class Loc {

	private static FileWriter writer;
	private static BufferedReader reader;

	public static void main(String[] args) throws IOException {
//		List<String> allLines = Files
//				.readAllLines(Paths.get("C:/Users/Public/eclipse-workspace/evosoftware/src/evosoftware/Loc.java"));
//		int[] a = calcLocMetodos(allLines);
//		System.out.println(a[0] + " " + a[1]);
		System.out.println("Mês, LOC, Classe, Métodos, Classe Deus, Método Deus");
		writer = new FileWriter(new File("evolucao.csv"), Charset.forName("ISO-8859-1"));
		writer.write("Mês, LOC, Classe, Métodos, Classe Deus, Método Deus\n");
		getNumArquivos(new File("/Users/Sérgio/Downloads/Dataset"), null);
		writer.close();

	}

	static int[] calcLocMetodos(List<String> allLines) throws IOException {
		int loc = 0;
		int metodos = 0;
		int classes = 0;
		int classeDeus = 0;
		int metodoDeus = 0;
		ArrayList<String> listaClasses = new ArrayList<String>();
		ArrayList<String> listaMetodos = new ArrayList<String>();
		ArrayList<String> listaMetodosDeus = new ArrayList<String>();
		ArrayList<String> listaClassesDeus = new ArrayList<String>();
		ArrayList<String> linhasClasse = new ArrayList<String>();
		ArrayList<String> linhasMetodo = new ArrayList<String>();
//		ArrayList<String> proxLinhaMetodo = new ArrayList<String>();
		List<String> nextLines = new ArrayList<>(allLines);

		int blocoComment = 0;
		for (String line : allLines) {
			if (isComentadoB(line) == true) {
				blocoComment++;
			}
			if (isComentadoC(line) == true) {
				blocoComment--;
			}
			if (blocoComment == 0 && !isComentado(line) && !line.isEmpty()) {
				loc++;
				if (Pattern.compile("class \\w+").matcher(line).find() && !listaClasses.contains(line)) {
					listaClasses.add(line);
					classes++;
					int chaves = 0;
					int locClasse = 0;
					int blocoCommentC = 0;
					for (String lineC : nextLines) {
						boolean locAdicionado = false;
						boolean blocoRecemFechado = false;
						if (isComentadoB(lineC) == true) {
							blocoCommentC++;
						}
						if (isComentadoC(lineC) == true) {
							blocoCommentC--;
							blocoRecemFechado = true;
						}
						if (blocoCommentC == 0 && chaves != 0 && !lineC.isEmpty() && !isComentado(lineC)
								&& !lineC.equals(line)) {
							locClasse++;
							linhasClasse.add(lineC);
							locAdicionado = true;
						}
						if (lineC.contains("{") && chaves != 0 && blocoCommentC == 0) {
							chaves++;
							linhasClasse.add(lineC);
							if (!locAdicionado) {
								locClasse++;
							}
						}
						if (lineC.contains("}") && chaves != 0 && blocoCommentC == 0 && !blocoRecemFechado) {
							chaves--;
							linhasClasse.add(lineC);
							if (!locAdicionado) {
								locClasse++;
							}
						}
						if (lineC.equals(line)) {
							chaves++;
						}

					}

					if (locClasse >= 800) {
						classeDeus++;
						listaClassesDeus.add(line);
					}

				} else if (Pattern.compile("\\w+ \\w+\\(.*?\\)").matcher(line).find() && !line.contains("new ")
						&& !line.contains("return ")) {
					listaMetodos.add(line);
					metodos++;
					int chaves = 0;
					int locMetodo = 0;
					int blocoCommentM = 0;
					for (String lineM : nextLines) {
						boolean locAdicionadoM = false;
						boolean blocoRecemFechadoM = false;
						if (isComentadoB(lineM) == true) {
							blocoCommentM++;
						}
						if (isComentadoC(lineM) == true) {
							blocoCommentM--;
							blocoRecemFechadoM = true;
						}
						if (blocoCommentM == 0 && chaves != 0 && !lineM.isEmpty() && !isComentado(lineM)
								&& !lineM.equals(line)) {
							locMetodo++;
							linhasMetodo.add(lineM);
							locAdicionadoM = true;
						}
						if (lineM.contains("{") && chaves != 0 && blocoCommentM == 0) {
							chaves++;
							linhasMetodo.add(lineM);
							if (!locAdicionadoM) {
								locMetodo++;
							}
						}
						if (lineM.contains("}") && chaves != 0 && blocoCommentM == 0 && !blocoRecemFechadoM) {
							chaves--;
							linhasMetodo.add(lineM);
							if (!locAdicionadoM) {
								locMetodo++;
							}
						}
						if (lineM.equals(line)) {
							chaves++;
						}

					}

					if (locMetodo >= 127) {
						metodoDeus++;
						listaMetodosDeus.add(line);
					}
				}
			}
			nextLines.remove(line);
		}
//		System.out.println(metodos);
//		System.out.println(listaClasses);
//		System.out.println(listaMetodos);
//		System.out.println(linhasClasse);
//		System.out.println(listaMetodosDeus);
//		System.out.println(listaClassesDeus);
		return new int[] { loc, classes, metodos, classeDeus, metodoDeus };

//		System.out.println("Linhas de Codigo: " + loc + " Numero de Metodos: " + metodos + " Número de Classes: "
//				+ getNumArquivos(new File("/Users/Sérgio/Downloads/Dataset")));
	}

	static int calcularInfoClasse() {
		return 0;

	}

	static boolean isComentado(String line) {
		line = line.trim();
		return line.startsWith("//") || line.startsWith("/*") || line.startsWith(" *") || line.startsWith("*")
				|| line.endsWith("*/");
	}

	static boolean isComentadoB(String line) {
		line = line.trim();
		return line.startsWith("/*");
	}

	static boolean isComentadoC(String line) {
		line = line.trim();
		return line.endsWith("*/") || line.startsWith("*/") || line.contains("*/");
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
		if (Objects.nonNull(dirResults)) {
			System.out.println(dir + "," + dirResults[0] + "," + dirResults[1] + "," + dirResults[2] + ","
					+ dirResults[3] + "," + dirResults[4]);
			writer.write(dir + "," + dirResults[0] + "," + dirResults[1] + "," + dirResults[2] + "," + dirResults[3]
					+ "," + dirResults[4] + "\n");
		}
		return c;
	}

	private static int[] somaResultados(int[] results, int[] dirResults) {
		if (Objects.nonNull(dirResults)) {
			dirResults[0] += results[0];
			dirResults[1] += results[1];
			dirResults[2] += results[2];
			dirResults[3] += results[3];
			dirResults[4] += results[4];
		} else {
			dirResults = results;
		}
		return dirResults;
	}
}