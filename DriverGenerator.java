package bergs.Cap.Capuaajm.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerDriverService;
import org.openqa.selenium.ie.InternetExplorerOptions;


public class DriverGenerator {
    public static InternetExplorerDriver criarIEDriver() {
        return criarIEDriver(null);
    }
    
    public static InternetExplorerDriver criarIEDriver(InternetExplorerOptions ieOptions) {
        System.setProperty("webdriver.ie.driver", Paths.get("\\\\n712", "pxh", "Soft", "BTH", "BIN", "selenium", "drivers", "IEDriverServer.exe").toString());
        
        InternetExplorerDriverService ieService = InternetExplorerDriverService.createDefaultService();
        InternetExplorerOptions ieOptionsDefault = new InternetExplorerOptions();
        ieOptionsDefault.requireWindowFocus();
        ieOptionsDefault.destructivelyEnsureCleanSession();
        ieOptionsDefault.setCapability("unexpectedAlertBehaviour", "ignore");
        ieOptionsDefault.setCapability("ignoreZoomSetting", true);
        ieOptionsDefault.setCapability("acceptSslCerts", "true");
        ieOptionsDefault.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
        ieOptionsDefault.setCapability("ignoreProtectedModeSettings", true);
        ieOptionsDefault.setCapability("requireWindowFocus", "false");
        
        if(ieOptions != null) {
            ieOptionsDefault.merge(ieOptions);
        }
        

        return new InternetExplorerDriver(ieService, ieOptionsDefault);
    }
    
    public static ChromeDriver getChromeDriver(ChromeOptions chromeOptions) {
        final String diretorioDrivers = Paths.get("\\\\n712", "pxh", "Soft", "BTH", "BIN", "selenium", "drivers", "chromedriver").toString();
        
        try {
        Optional<String> matchingDrivers = Optional.ofNullable(null);
        String chromeDriverPath = null;
        
            matchingDrivers = verificaMatchingDrivers(diretorioDrivers, getVersaoChromeLocal());
            if (matchingDrivers.isPresent()) {
                chromeDriverPath = matchingDrivers.get().concat("\\chromedriver.exe");
            } else {
                chromeDriverPath = diretorioDrivers.concat("\\2.44\\chromedriver.exe");
            }
            System.setProperty("webdriver.chrome.driver", chromeDriverPath);

        } catch (Exception e) {
            System.out.println("A cria��o do Chrome Driver  apresentou uma falha. Motivo:" + e.getStackTrace().toString());
        }

        ChromeDriver chromeDriver = configurarChromeDriver(chromeOptions);

        return chromeDriver;
    }
    
    private static Optional<String> verificaMatchingDrivers(String driversDirectory, String chromeVersion) {
        /* Cria uma inst�ncia da classe File com o diret�rio especificado */
        File directory = new File(driversDirectory);
        /* Obt�m a lista de arquivos e diret�rios dentro do diret�rio raiz */
        File[] files = directory.listFiles();
         
        /* Verifica se a lista de arquivos n�o � nula */
        if (files != null) {
            /* Itera sobre cada arquivo/diret�rio na lista */
            for (File file : files) {
                /* Verifica se � um diret�rio e se o nome come�a com a vers�o do Chrome */
                if (file.isDirectory() && file.getName().startsWith(chromeVersion)) {
                    /* Retorna um objeto Optional com o caminho absoluto do diret�rio */
                    return Optional.of(file.getAbsolutePath());
                }
            }
        }
        /* Retorna um objeto Optional vazio se nenhum driver correspondente for encontrado */
        return Optional.empty();
    }
    
    private static ChromeDriver configurarChromeDriver(ChromeOptions options) {
        /* Configura o path do Chrome instalado localmente no ambiente de testes web */
        setarPathChrome(options);

        return new ChromeDriver(options);
    }
    
    private static void setarPathChrome(ChromeOptions options) {
        final String pathChrome64bits = Paths.get("C:", "Program Files", "Google", "Chrome", "Application", "chrome.exe").toString();
        final String pathChrome32bits = Paths.get("C:", "Program Files (x86)", "Google", "Chrome", "Application", "chrome.exe").toString();

        File arquivoChrome32bits = new File(pathChrome32bits);

        if (arquivoChrome32bits.exists()) {
            options.setBinary(pathChrome32bits);
        } else {
            options.setBinary(pathChrome64bits);
        }
    }
    
    private static String getVersaoChromeLocal() throws IOException {
        ProcessBuilder processBuilder = null;
        
        // Define os dois caminhos poss�veis para o chrome.exe
        String caminho1 = "C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe";
        String caminho2 = "C:\\Program Files (x86)\\Google\\Chrome\\Application\\chrome.exe";

        // Cria objetos File para cada caminho
        File file1 = new File(caminho1);
        File file2 = new File(caminho2);

        // Verifica se algum dos arquivos existe e � um arquivo (n�o um diret�rio)
        if (file1.exists() && file1.isFile()) {
            // Usa o caminho1 para executar o comando do PowerShell
            processBuilder = new ProcessBuilder("C:\\Windows\\System32\\WindowsPowerShell\\v1.0\\powershell.exe",
                        "(Get-Item '" + caminho1 + "').VersionInfo.ProductVersion");
            // O resto do c�digo � igual ao seu m�todo original
        } else if (file2.exists() && file2.isFile()) {
            // Usa o caminho2 para executar o comando do PowerShell
            processBuilder = new ProcessBuilder("C:\\Windows\\System32\\WindowsPowerShell\\v1.0\\powershell.exe",
                        "(Get-Item '" + caminho2 + "').VersionInfo.ProductVersion");
            
        }
        
        /* cria uma inst�ncia de ProcessBuilder, uma classe que permite criar e controlar processos */
        /* j� definindo o comando e os argumentos que ser�o executados pelo processo */
        //ProcessBuilder processBuilder = new ProcessBuilder("C:\\Windows\\System32\\WindowsPowerShell\\v1.0\\powershell.exe",
          //      "(Get-Item 'C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe').VersionInfo.ProductVersion");
        
        /* Redireciona o erro para a sa�da padr�o */
        processBuilder.redirectErrorStream(true);

        /*  inicia o processo e obt�m um objeto Process, que representa o processo em execu��o */
        Process processo = processBuilder.start();

        /* cria um objeto da classe BufferedReader usando o construtor que recebe um objeto da classe InputStreamReader, que por sua vez recebe o fluxo de entrada do objeto processo */
        BufferedReader stdInput = new BufferedReader(new InputStreamReader(processo.getInputStream()));
        /* declara uma vari�vel do tipo String chamada s e inicializa com null */
        String retorno = null;
        /* declara outra vari�vel do tipo String chamada versao e inicializa com null */
        String versao = null;

        /* inicia um la�o while que l� uma linha do objeto stdInput e atribui �vari�vel s, e repete enquanto s n�o for null */
        while ((retorno = stdInput.readLine()) != null) {
            /* verifica se a vari�vel s cont�m a palavra "version" */
            if (!retorno.isBlank()) {
                /*
                 * cria um objeto da classe Pattern usando o m�todo est�tico compile e passando uma String que representa um padr�o regex que
                 * corresponde a um ou mais d�gitos seguidos por um ponto, repetido uma vez, e terminado por um ou mais d�gitos
                 */
                Pattern pattern = Pattern.compile("(\\d+)\\.(\\d+)\\.\\d+\\.\\d+");
                /* cria um objeto da classe Matcher usando o m�todo matcher do objeto pattern e passando a vari�vel s como argumento */
                Matcher matcher = pattern.matcher(retorno);
                /* verifica se o objeto matcher encontrou uma correspond�ncia na vari�vel s */
                if (matcher.find()) {
                    /* atribui a� vari�vel versao a String que corresponde � primeira correspond�ncia encontrada pelo objeto matcher */
                    versao = matcher.group(1) + "." + matcher.group(2);
                }

                // essa linha est� comentada, mas se fosse executada, criaria outra vari�vel do tipo String chamada versao e atribuiria a ela o terceiro
                // elemento do array resultante de dividir a vari�vel s em partes separadas por um ou mais espa�os em branco */
                // String versao = s.split("\\s+")[2];
                
                /* finaliza o processo de consulta para evitar que fique executando na esta��o/servidor */
                processo.destroy();

                /* retorna a vari�vel versao como resultado do m�todo getChromeVersion */
                return versao;
            }
        }

        /* finaliza o processo de consulta para evitar que fique executando na esta��o/servidor */
        processo.destroy();

        /* retorna null como resultado do m�todo getChromeVersion se n�o encontrar a vers�o */
        return "2.44";
    }
}
