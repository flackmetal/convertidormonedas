# Conversor de Monedas (Java + ExchangeRate-API)

Aplicación de consola en Java que convierte entre distintas monedas usando la API de ExchangeRate-API.

**Opciones incluidas**  
1. USD → CLP  
2. CLP → USD  
3. USD → BRL  
4. BRL → USD  
5. USD → COP  
6. COP → USD  
7. Convertir otra moneda (ingresando códigos ISO 4217 manualmente)

> Requiere conexión a Internet y Java 8+.

## Cómo ejecutar

### 1) Compilar
```bash
cd src
javac *.java
```

### 2) Ejecutar
```bash
java CurrencyConverter
```

## Configuración de la API

El proyecto ya está configurado con la clave proporcionada por ti:

```
d7dd7826079dafbe1fe25ff2
```

> Nota: Esta clave está embebida en el código para simplificar. Si planeas publicar este proyecto, te recomiendo usar variables de entorno o un archivo de configuración para evitar exponerla.

## ¿Cómo funciona?

- El programa consulta el endpoint `/pair/{base}/{target}` de ExchangeRate-API, por ejemplo:  
  `https://v6.exchangerate-api.com/v6/TU_API_KEY/pair/USD/CLP`
- Del JSON de respuesta extrae el campo `conversion_rate` y calcula el resultado según el monto ingresado.

## Solución de problemas

- **HTTP 4xx/5xx**: Verifica que la clave de API sea válida y que tengas conexión a Internet.
- **Formato de números**: Usa punto `.` como separador decimal.
- **Monedas**: Asegúrate de escribir códigos ISO 4217 válidos (ej: USD, CLP, BRL, COP).

## Licencia

MIT (haz lo que quieras, sin garantía).
