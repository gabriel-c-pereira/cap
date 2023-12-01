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
            System.out.println("A criação do Chrome Driver  apresentou uma falha. Motivo:" + e.getStackTrace().toString());
        }

        ChromeDriver chromeDriver = configurarChromeDriver(chromeOptions);

        return chromeDriver;
    }
    
    private static Optional<String> verificaMatchingDrivers(String driversDirectory, String chromeVersion) {
        /* Cria uma instância da classe File com o diretório especificado */
        File directory = new File(driversDirectory);
        /* Obtém a lista de arquivos e diretórios dentro do diretório raiz */
        File[] files = directory.listFiles();
         
        /* Verifica se a lista de arquivos não é nula */
        if (files != null) {
            /* Itera sobre cada arquivo/diretório na lista */
            for (File file : files) {
                /* Verifica se é um diretório e se o nome começa com a versão do Chrome */
                if (file.isDirectory() && file.getName().startsWith(chromeVersion)) {
                    /* Retorna um objeto Optional com o caminho absoluto do diretório */
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
        
        // Define os dois caminhos possíveis para o chrome.exe
        String caminho1 = "C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe";
        String caminho2 = "C:\\Program Files (x86)\\Google\\Chrome\\Application\\chrome.exe";

        // Cria objetos File para cada caminho
        File file1 = new File(caminho1);
        File file2 = new File(caminho2);

        // Verifica se algum dos arquivos existe e é um arquivo (não um diretório)
        if (file1.exists() && file1.isFile()) {
            // Usa o caminho1 para executar o comando do PowerShell
            processBuilder = new ProcessBuilder("C:\\Windows\\System32\\WindowsPowerShell\\v1.0\\powershell.exe",
                        "(Get-Item '" + caminho1 + "').VersionInfo.ProductVersion");
            // O resto do código é igual ao seu método original
        } else if (file2.exists() && file2.isFile()) {
            // Usa o caminho2 para executar o comando do PowerShell
            processBuilder = new ProcessBuilder("C:\\Windows\\System32\\WindowsPowerShell\\v1.0\\powershell.exe",
                        "(Get-Item '" + caminho2 + "').VersionInfo.ProductVersion");
            
        }
        
        /* cria uma instância de ProcessBuilder, uma classe que permite criar e controlar processos */
        /* já definindo o comando e os argumentos que serão executados pelo processo */
        //ProcessBuilder processBuilder = new ProcessBuilder("C:\\Windows\\System32\\WindowsPowerShell\\v1.0\\powershell.exe",
          //      "(Get-Item 'C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe').VersionInfo.ProductVersion");
        
        /* Redireciona o erro para a saída padrão */
        processBuilder.redirectErrorStream(true);

        /*  inicia o processo e obtém um objeto Process, que representa o processo em execução */
        Process processo = processBuilder.start();

        /* cria um objeto da classe BufferedReader usando o construtor que recebe um objeto da classe InputStreamReader, que por sua vez recebe o fluxo de entrada do objeto processo */
        BufferedReader stdInput = new BufferedReader(new InputStreamReader(processo.getInputStream()));
        /* declara uma variável do tipo String chamada s e inicializa com null */
        String retorno = null;
        /* declara outra variável do tipo String chamada versao e inicializa com null */
        String versao = null;

        /* inicia um laço while que lê uma linha do objeto stdInput e atribui á variável s, e repete enquanto s não for null */
        while ((retorno = stdInput.readLine()) != null) {
            /* verifica se a variável s contém a palavra "version" */
            if (!retorno.isBlank()) {
                /*
                 * cria um objeto da classe Pattern usando o método estático compile e passando uma String que representa um padrão regex que
                 * corresponde a um ou mais dígitos seguidos por um ponto, repetido uma vez, e terminado por um ou mais dígitos
                 */
                Pattern pattern = Pattern.compile("(\\d+)\\.(\\d+)\\.\\d+\\.\\d+");
                /* cria um objeto da classe Matcher usando o método matcher do objeto pattern e passando a variável s como argumento */
                Matcher matcher = pattern.matcher(retorno);
                /* verifica se o objeto matcher encontrou uma correspondência na variável s */
                if (matcher.find()) {
                    /* atribui a  variável versao a String que corresponde à  primeira correspondência encontrada pelo objeto matcher */
                    versao = matcher.group(1) + "." + matcher.group(2);
                }

                // essa linha está comentada, mas se fosse executada, criaria outra variável do tipo String chamada versao e atribuiria a ela o terceiro
                // elemento do array resultante de dividir a variável s em partes separadas por um ou mais espaços em branco */
                // String versao = s.split("\\s+")[2];
                
                /* finaliza o processo de consulta para evitar que fique executando na estação/servidor */
                processo.destroy();

                /* retorna a variável versao como resultado do método getChromeVersion */
                return versao;
            }
        }

        /* finaliza o processo de consulta para evitar que fique executando na estação/servidor */
        processo.destroy();

        /* retorna null como resultado do método getChromeVersion se não encontrar a versão */
        return "2.44";
    }
}
