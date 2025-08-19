import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Locale;
import java.util.Scanner;

/**
 * Conversor de Monedas por consola.
 * - USD <-> CLP
 * - USD <-> BRL
 * - USD <-> COP
 * -convertir cualquier par (ISO 4217).
 *
 *
 */
public class CurrencyConverter {

    // Clave entregada por el usuario.
    private static final String API_KEY = "d7dd7826079dafbe1fe25ff2";

    private final ExchangeRateService service;
    private final Scanner scanner;
    private final DecimalFormat df = new DecimalFormat("#,##0.####");

    public CurrencyConverter() {
        this.service = new ExchangeRateService(API_KEY);
        this.scanner = new Scanner(System.in).useLocale(Locale.US);
    }

    public static void main(String[] args) {
        CurrencyConverter app = new CurrencyConverter();
        app.run();
    }

    private void run() {
        System.out.println("=== Conversor de Monedas (ExchangeRate-API) ===");
        boolean exit = false;
        while (!exit) {
            printMenu();
            int option = readInt("Elige una opción: ");
            try {
                switch (option) {
                    case 1: convert("USD", "CLP"); break;
                    case 2: convert("CLP", "USD"); break;
                    case 3: convert("USD", "BRL"); break;
                    case 4: convert("BRL", "USD"); break;
                    case 5: convert("USD", "COP"); break;
                    case 6: convert("COP", "USD"); break;
                    case 7: convertCustom(); break;
                    case 0: exit = true; System.out.println("¡Hasta luego!"); break;
                    default: System.out.println("Opción inválida. Intenta nuevamente.");
                }
            } catch (IOException e) {
                System.out.println("Error al consultar la API: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Ocurrió un error: " + e.getMessage());
            }
            System.out.println();
        }
    }

    private void printMenu() {
        System.out.println("-----------------------------------------------");
        System.out.println("1) Dólar (USD) -> Peso Chileno (CLP)");
        System.out.println("2) Peso Chileno (CLP) -> Dólar (USD)");
        System.out.println("3) Dólar (USD) -> Real Brasileño (BRL)");
        System.out.println("4) Real Brasileño (BRL) -> Dólar (USD)");
        System.out.println("5) Dólar (USD) -> Peso Colombiano (COP)");
        System.out.println("6) Peso Colombiano (COP) -> Dólar (USD)");
        System.out.println("7) Convertir otra moneda (códigos ISO 4217)");
        System.out.println("0) Salir");
    }

    private void convert(String from, String to) throws IOException {
        double amount = readDouble("Monto en " + from + ": ");
        double rate = service.getConversionRate(from, to);
        double result = amount * rate;
        System.out.println(df.format(amount) + " " + from + " = " + df.format(result) + " " + to + " (tasa: " + df.format(rate) + ")");
    }

    private void convertCustom() throws IOException {
        System.out.print("Código moneda origen (ej: USD, CLP, BRL, COP): ");
        String from = scanner.next().trim().toUpperCase();
        System.out.print("Código moneda destino: ");
        String to = scanner.next().trim().toUpperCase();
        convert(from, to);
    }

    private int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Ingresa un número válido.");
            }
        }
    }

    private double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            try {
                return Double.parseDouble(line.replace(",", "."));
            } catch (NumberFormatException e) {
                System.out.println("Ingresa un número válido (usa punto para decimales).");
            }
        }
    }
}
