package br.com.leandrofabriciosarmento.sinapi.extrator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;

import br.com.leandrofabriciosarmento.sinapi.extrator.model.Composicao;
import br.com.leandrofabriciosarmento.sinapi.extrator.model.Referencia;
import br.com.leandrofabriciosarmento.sinapi.extrator.model.SubComposicao;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;
import io.searchbox.core.Bulk;
import io.searchbox.core.Bulk.Builder;
import io.searchbox.core.Index;

public class Main {

    final static Logger logger = Logger.getLogger(Main.class);

    static String regexCodigoSINAPI = "\\d{4,5}/[0]{0,2}\\d{1,2}|\\d{4,5}";
    static String regexCodigoSINAPIAntigo = "\\d{1,}/[0]{1,2}\\d{1,2}";
    static String regexCodigoSINAPIComZeros = "/[0]{1,2}";

    static String regexCodigoSBC = "\\d{6}";

    static Pattern patternCodigoSINAPI = Pattern.compile(regexCodigoSINAPI);
    static Pattern patternCodigoSINAPIAntigo = Pattern.compile(regexCodigoSINAPIAntigo);
    static Pattern patternZerosSINAPI = Pattern.compile(regexCodigoSINAPIComZeros);

    static JestClient jestClient;

    public static void main(String[] args) throws JsonIOException, IOException {

	// Construct a new Jest client according to configuration via factory
	JestClientFactory factory = new JestClientFactory();
	// BasicCredentialsProvider credentialsProvider = new
	// BasicCredentialsProvider();
	// UsernamePasswordCredentials usernamePasswordCredentials = new
	// UsernamePasswordCredentials("user",
	// "humtntESDW03");

	// credentialsProvider.setCredentials(AuthScope.ANY,
	// usernamePasswordCredentials);

	factory.setHttpClientConfig(
		new HttpClientConfig.Builder("http://ec2-18-216-77-189.us-east-2.compute.amazonaws.com:9200")
			.multiThreaded(true).defaultCredentials("user", "humtntESDW03").build());
	//
	// factory.setHttpClientConfig(
	// new HttpClientConfig.Builder("http://localhost:9200")
	// .multiThreaded(true)
	// .build());

	jestClient = factory.getObject();

	List<Referencia> referencias = extrair(9, 2017);

    }

    private static final String url = "http://www.caixa.gov.br/Downloads/sinapi-a-partir-jul-2009-%s/SINAPI_ref_Insumos_Composicoes_%s.zip";

    private static List<Referencia> extrair(int mes, int ano) {

	List<Referencia> referencias = new ArrayList<>();

	String[] ufs = { "AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", "MA", "MT", "MS", "MG", "PA", "PB", "PR",
		"PE", "PI", "RJ", "RN", "RS", "RO", "RR", "SC", "SP", "SE", "TO" };

	// String[] ufs = { "AC" };

	String mesAno = String.format("%02d", mes) + ano;

	for (String uf : ufs) {

	    try {
		Referencia referencia = new Referencia();
		referencia.setDesoneracao("Desonerado");
		referencia.setUf(uf);
		referencia.setMes(String.format("%02d", mes));
		referencia.setAno(ano);
		referencia.setPeriodo(mesAno);
		extrair(referencia);
		referencias.add(referencia);

	    } catch (IOException e) {
		System.out.println("Falha ao processar o arquivo " + url);
	    }

	    try {
		Referencia referencia = new Referencia();
		referencia.setDesoneracao("NaoDesonerado");
		referencia.setUf(uf);
		referencia.setMes(String.format("%02d", mes));
		referencia.setAno(ano);
		referencia.setPeriodo(mesAno);
		extrair(referencia);
		referencias.add(referencia);
	    } catch (IOException e) {

		System.out.println("Falha ao processar o arquivo " + url);
	    }
	}

	return referencias;
    }

    private static void extrair(Referencia referencia) throws IOException {

	parseAnalitico(referencia);
	saveComposicoesInElasticSearch(referencia);

	// parseSintetico(referencia);
    }

    private static void saveComposicoesInElasticSearch(Referencia referencia) throws IOException {

	System.out.println("Salvando no ElasticSearch");
	Builder bulkIndexBuilder = new Bulk.Builder();
	for (Composicao composicao : referencia.getComposicaos()) {

	    composicao.setBanco("SINAPI");
	    composicao.setAno(referencia.getAno());
	    composicao.setMes(Integer.parseInt(referencia.getMes()));
	    composicao.setLocalidade(referencia.getUf());
	    composicao.setDesoneracao(referencia.getDesoneracao().startsWith("N") ? "N" : "S");
	    bulkIndexBuilder.addAction(new Index.Builder(composicao).index("precos").type("sinapi").build());
	}
	jestClient.execute(bulkIndexBuilder.build());
	System.out.println("Pronto!");

    }

    private static void saveSubcomposicoesInElasticSearch(List<SubComposicao> subComposicoes) throws IOException {

	Builder bulkIndexBuilder = new Bulk.Builder();
	for (SubComposicao composicao : subComposicoes) {
	    bulkIndexBuilder.addAction(new Index.Builder(composicao).index("precos").type("sinapi").build());
	}
	jestClient.execute(bulkIndexBuilder.build());

    }

    private static Composicao ultimaComposicaoEncontrada = null;

    public static void parseSintetico(Referencia referencia) throws IOException {

	String parametros = referencia.getUf() + "_" + referencia.getPeriodo() + "_" + referencia.getDesoneracao();

	String urlFormatada = String.format(url, referencia.getUf().toLowerCase(), parametros);
	String path = new File(".").getCanonicalPath();
	String folderTarget = path + "/target/";
	String toFile = folderTarget + parametros + ".zip";

	downloadFile(urlFormatada, toFile);
	unZipIt(toFile, folderTarget + parametros);

	String nomeArquivo = "SINAPI_Custo_Ref_Composicoes_Sintetico_" + referencia.getUf().toUpperCase() + "_"
		+ referencia.getAno() + "" + referencia.getMes() + "_" + referencia.getDesoneracao();

	String pathArquivoXLS = folderTarget + parametros + "/" + nomeArquivo + ".xls";

	System.out.println(pathArquivoXLS);

	HSSFWorkbook workbook;
	FileInputStream fileInputStream = null;

	try {
	    fileInputStream = new FileInputStream(pathArquivoXLS);
	    workbook = new HSSFWorkbook(fileInputStream);

	    HSSFSheet sheet = workbook.getSheetAt(0);

	    List<SubComposicao> subComposicoes = new ArrayList<>();

	    for (int rowIndex = 5; rowIndex <= sheet.getLastRowNum(); rowIndex++) {

		Row row = sheet.getRow(rowIndex);

		if (row == null) {
		    continue;
		}
		parceRowSintetico(row, referencia, subComposicoes);

	    }
	    workbook.close();

	} catch (IOException e) {
	    System.err.println(e.getMessage());
	} finally {
	    if (fileInputStream != null) {
		fileInputStream.close();
	    }
	}
    }

    public static void parseAnalitico(Referencia referencia) throws IOException {

	String parametros = referencia.getUf() + "_" + referencia.getPeriodo() + "_" + referencia.getDesoneracao();

	String urlFormatada = String.format(url, referencia.getUf().toLowerCase(), parametros);
	String path = new File(".").getCanonicalPath();
	String folderTarget = path + "/target/";
	String toFile = folderTarget + parametros + ".zip";

	downloadFile(urlFormatada, toFile);
	unZipIt(toFile, folderTarget + parametros);

	String nomeArquivo = "SINAPI_Custo_Ref_Composicoes_Analitico_" + referencia.getUf().toUpperCase() + "_"
		+ referencia.getAno() + "" + referencia.getMes() + "_" + referencia.getDesoneracao();

	String pathArquivoXLS = folderTarget + parametros + "/" + nomeArquivo + ".xls";

	System.out.println(pathArquivoXLS);

	HSSFWorkbook workbook;
	FileInputStream fileInputStream = null;

	try {
	    fileInputStream = new FileInputStream(pathArquivoXLS);
	    workbook = new HSSFWorkbook(fileInputStream);

	    HSSFSheet sheet = workbook.getSheetAt(0);

	    for (int rowIndex = 5; rowIndex <= sheet.getLastRowNum(); rowIndex++) {

		Row row = sheet.getRow(rowIndex);

		if (row == null) {
		    continue;
		}
		if (logger.isDebugEnabled()) {
		    //System.out.println((rowIndex + 1) + "\t");
		}
		parceRow(row, referencia);

	    }
	    workbook.close();
	    /*
	     * Gson gson = new Gson(); gson.toJson(referencia, new FileWriter(
	     * referencia.getUf() + "_" + referencia.getPeriodo() + "_" +
	     * referencia.getDesoneracao() + ".json"));
	     */
	} catch (IOException e) {
	    System.err.println(e.getMessage());
	} finally {
	    if (fileInputStream != null) {
		fileInputStream.close();
	    }
	}

	//System.out.println(referencia);
    }

    private static void parceRow(Row row, Referencia referencia) {

	Matcher matcher = patternCodigoSINAPI.matcher(getStringCellValue(row.getCell(6)));
	boolean linhaComposicao = matcher.find() && getStringCellValue(row.getCell(11)).isEmpty();

	matcher = patternCodigoSINAPI.matcher(getStringCellValue(row.getCell(12)));
	boolean linhaSubComposicao = matcher.find();

	if (linhaComposicao) {
	    ultimaComposicaoEncontrada = new Composicao();
	    referencia.addComposicaos(ultimaComposicaoEncontrada);
	    extrairLinhaComposicao(row, ultimaComposicaoEncontrada);
	} else if (linhaSubComposicao) {
	    SubComposicao subComposicao = new SubComposicao();
	    ultimaComposicaoEncontrada.addSubComposicaos(subComposicao);
	    extrairLinhaSubComposicao(row, subComposicao);
	}
    }

    private static void parceRowSintetico(Row row, Referencia referencia, List<SubComposicao> composicoes) {

	Matcher matcher = patternCodigoSINAPI.matcher(getStringCellValue(row.getCell(6)));
	boolean linhaSubComposicao = matcher.find();

	if (linhaSubComposicao) {
	    SubComposicao subComposicao = new SubComposicao();
	    subComposicao.setBanco("SINAPI");
	    subComposicao.setAno(referencia.getAno());
	    subComposicao.setMes(Integer.parseInt(referencia.getMes()));
	    subComposicao.setLocalidade(referencia.getUf());
	    subComposicao.setDesoneracao(referencia.getDesoneracao().startsWith("N") ? "N" : "S");
	    extrairLinhaComposicaoSintetico(row, subComposicao);
	    composicoes.add(subComposicao);
	}

    }

    private static void extrairLinhaComposicaoSintetico(Row row, SubComposicao subComposicao) {

	int lastCell = row.getLastCellNum();
	for (int cellIndex = 0; cellIndex < lastCell; cellIndex++) {
	    Cell cell = row.getCell(cellIndex);
	    String cellValue = getStringCellValue(cell);

	    // System.out.println("\t" + cellValue);

	    switch (cellIndex) {
	    case 6:
		subComposicao.setCodigoComposicao(cellValue);
		break;
	    case 7:
		subComposicao.setNomeComposicao(cellValue);
		break;
	    case 8:
		subComposicao.setUnidadeMedida(cellValue);
		break;
	    case 10:
		subComposicao.setPrecoUnitario(cellValue);
		break;
	    default:
		break;
	    }
	    // System.out.println("\t" + cellValue);

	}
	// System.out.println("\n");
    }

    private static void extrairLinhaSubComposicao(Row row, SubComposicao subComposicao) {

	int lastCell = row.getLastCellNum();
	for (int cellIndex = 0; cellIndex < lastCell; cellIndex++) {
	    Cell cell = row.getCell(cellIndex);
	    String cellValue = getStringCellValue(cell);

	    switch (cellIndex) {
	    case 11:
		subComposicao.setTipo(cellValue);
		break;
	    case 12:
		subComposicao.setCodigoComposicao(cellValue);
		break;
	    case 13:
		subComposicao.setNomeComposicao(cellValue);
		break;
	    case 14:
		subComposicao.setUnidadeMedida(cellValue);
		break;
	    case 16:
		subComposicao.setCoeficiente(cellValue);
		break;
	    case 17:
		subComposicao.setPrecoUnitario(cellValue);
		break;
	    case 18:
		subComposicao.setCustoTotal(cellValue);
		break;
	    default:
		break;
	    }
	    //System.out.println("\t" + cellValue);
	}
	//System.out.println("\n");
    }

    private static void extrairLinhaComposicao(Row row, Composicao composicao) {

	//System.out.println("***************************************************");

	int lastCell = row.getLastCellNum();
	for (int cellIndex = 0; cellIndex < lastCell; cellIndex++) {
	    Cell cell = row.getCell(cellIndex);
	    String cellValue = getStringCellValue(cell);

	    switch (cellIndex) {
	    case 0:
		composicao.setNomeClasse(cellValue);
		break;
	    case 1:
		composicao.setSiglaClasse(cellValue);
		break;
	    case 2:
		composicao.setNomeTipo(cellValue);
		break;
	    case 3:
		composicao.setSiglaTipo(cellValue);
		break;
	    case 4:
		composicao.setCodigoAgrupador(cellValue);
		break;
	    case 5:
		composicao.setNomeAgrupador(cellValue);
		break;
	    case 6:
		composicao.setCodigoComposicao(cellValue);
		break;
	    case 7:
		composicao.setNomeComposicao(cellValue);
		break;
	    case 8:
		composicao.setUnidadeMedida(cellValue);
		break;
	    case 9:
		composicao.setOrigemPreco(cellValue);
		break;
	    case 10:
		composicao.setCustoTotal(cellValue);
		break;
	    case 19:
		composicao.setCustoMaoDeObra(cellValue);
		break;
	    case 20:
		composicao.setPercentualMaoDeObra(cellValue);
		break;
	    case 21:
		composicao.setCustoMaterial(cellValue);
		break;
	    case 22:
		composicao.setPercentualMaterial(cellValue);
		break;
	    case 23:
		composicao.setCustoEquipamento(cellValue);
		break;
	    case 24:
		composicao.setPercentualEquipamento(cellValue);
		break;
	    case 25:
		composicao.setCustoServicoTerceiro(cellValue);
		break;
	    case 26:
		composicao.setPercentualServicoTerceiro(cellValue);
		break;
	    case 27:
		composicao.setCustoOutros(cellValue);
		break;
	    case 28:
		composicao.setPercentualOutros(cellValue);
		break;
	    case 29:
		composicao.setVinculo(cellValue);
		break;
	    default:
		break;
	    }
	    //System.out.println("\t" + cellValue);
	}
	//System.out.println("\n");
    }

    private static String getStringCellValue(Cell cell) {

	if (cell == null) {
	    return "";
	}

	if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {

	    return Double.toString(cell.getNumericCellValue());
	} else if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {

	    return cell.getCellFormula();
	} else if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {

	    return Boolean.toString(cell.getBooleanCellValue());
	} else {

	    return cell.getStringCellValue();
	}
    }

    private static void downloadFile(String urlFormatada, String toFile) {
	try {

	    File fileTarget = new File(toFile);
	    if (fileTarget.exists()) {
		return;
	    }

	    if (logger.isDebugEnabled()) {
		System.out.println("Baixando: " + urlFormatada);
		System.out.println("Destino: " + toFile);
	    }

	    CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
	    URL website = new URL(urlFormatada);
	    ReadableByteChannel rbc = Channels.newChannel(website.openStream());
	    FileOutputStream fos = new FileOutputStream(fileTarget);
	    fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
	    fos.close();
	    rbc.close();

	} catch (IOException e) {
	    System.out.println(e.getMessage());
	}
    }

    /**
     * Unzip it
     * 
     * @param zipFile
     *            input zip file
     * @param output
     *            zip file output folder
     * @throws IOException
     */
    public static void unZipIt(String zipFile, String outputFolder) throws IOException {

	FileOutputStream fos = null;
	FileInputStream fileInputStream = null;

	try {

	    byte[] buffer = new byte[1024];

	    // create output directory is not exists
	    File folder = new File(outputFolder);
	    if (!folder.exists()) {
		folder.mkdir();
	    } else {
		return;
	    }

	    fileInputStream = new FileInputStream(zipFile);
	    ZipInputStream zis = new ZipInputStream(fileInputStream);
	    ZipEntry ze = zis.getNextEntry();

	    while (ze != null) {

		String fileName = ze.getName();
		File newFile = new File(outputFolder + File.separator + fileName);

		if (logger.isDebugEnabled()) {
		    System.out.println("file unzip : " + newFile.getAbsoluteFile());
		}

		// create all non exists folders
		// else you will hit FileNotFoundException for compressed folder
		new File(newFile.getParent()).mkdirs();

		fos = new FileOutputStream(newFile);

		int len;
		while ((len = zis.read(buffer)) > 0) {
		    fos.write(buffer, 0, len);
		}

		fos.close();
		ze = zis.getNextEntry();
	    }

	    zis.closeEntry();
	    zis.close();

	    if (logger.isDebugEnabled()) {
		System.out.println("Done");
	    }

	} catch (IOException ex) {
	    System.out.println(ex.getMessage());
	} finally {
	    if (fos != null) {
		fos.close();
	    }
	    if (fileInputStream != null) {
		fileInputStream.close();
	    }
	}
    }

}
