import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Servicio simple para consultar la API de ExchangeRate-API (v6)
 * Documentación: https://www.exchangerate-api.com/docs/overview
 *
 * Realiza peticiones al endpoint /pair/{base}/{target}
 * Ejemplo: https://v6.exchangerate-api.com/v6/API_KEY/pair/USD/CLP
 */
public class ExchangeRateService {

    // Clave provista por el usuario. Para uso educativo. Considera rotarla si la publicas.
    private final String API_KEY;

    // Endpoint base para conversión de pares
    private static final String API_URL_TEMPLATE = "https://v6.exchangerate-api.com/v6/%s/pair/%s/%s";

    // Expresión regular para extraer el campo conversion_rate del JSON de respuesta
    private static final Pattern RATE_PATTERN = Pattern.compile("\"conversion_rate\"\\s*:\\s*([0-9]+(?:\\.[0-9]+)?)");

    public ExchangeRateService(String apiKey) {
        this.API_KEY = apiKey;
    }

    /**
     * Obtiene la tasa de conversión entre dos códigos ISO 4217 (por ejemplo: USD, CLP, BRL, COP).
     * @param fromCurrency Moneda origen (ISO 4217)
     * @param toCurrency   Moneda destino (ISO 4217)
     * @return tasa de conversión (multiplicador) desde fromCurrency hacia toCurrency
     * @throws IOException si ocurre un error de red o de parsing
     */
    public double getConversionRate(String fromCurrency, String toCurrency) throws IOException {
        String urlStr = String.format(API_URL_TEMPLATE, API_KEY, sanitize(fromCurrency), sanitize(toCurrency));
        HttpURLConnection conn = null;
        try {
            URL url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(15000);

            int status = conn.getResponseCode();
            InputStream is = (status >= 200 && status < 300) ? conn.getInputStream() : conn.getErrorStream();
            String body = readAll(is);

            if (status < 200 || status >= 300) {
                throw new IOException("Error HTTP " + status + " desde API: " + body);
            }

            Double rate = parseConversionRate(body);
            if (rate == null) {
                throw new IOException("No se pudo extraer conversion_rate del JSON de respuesta.");
            }
            return rate;
        } finally {
            if (conn != null) conn.disconnect();
        }
    }

    private static String sanitize(String s) {
        return s == null ? "" : s.trim().toUpperCase();
    }

    private static String readAll(InputStream is) throws IOException {
        if (is == null) return "";
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        }
    }

    private static Double parseConversionRate(String json) {
        Matcher m = RATE_PATTERN.matcher(json);
        if (m.find()) {
            try {
                return Double.parseDouble(m.group(1));
            } catch (NumberFormatException ignored) {}
        }
        return null;
    }
}
