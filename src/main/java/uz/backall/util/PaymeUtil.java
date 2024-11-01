package uz.backall.util;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import uz.backall.cardOperation.card.CardNotFoundException;

@Slf4j
public class PaymeUtil {

  public static String createCard(String cardNumber, String expireDate, Long id, String authToken) {
    HttpURLConnection connection = null;
    try {
      String url = "https://checkout.test.paycom.uz/api";
      connection = (HttpURLConnection) new URL(url).openConnection();

      connection.setRequestMethod("POST");
      connection.setRequestProperty("X-auth", authToken);
      connection.setRequestProperty("Content-Type", "application/json");
      connection.setDoOutput(true);

      String jsonInputString = String.format(
        "{\"id\": %d, \"method\": \"cards.create\", \"params\": {\"card\": {\"number\": \"%s\", \"expire\": \"%s\"}, \"save\": false}}",
        id, cardNumber, expireDate
      );

      try (DataOutputStream dos = new DataOutputStream(connection.getOutputStream())) {
        dos.writeBytes(jsonInputString);
        dos.flush();
      }

      int responseCode = connection.getResponseCode();
      if (responseCode == HttpURLConnection.HTTP_OK) {
        StringBuilder response = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
          String inputLine;
          while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
          }
        }
        return extractToken(response.toString());
      } else {
        log.error("PAYME ISHLAMAYABTI MAZGIOS::::::: createCard()" + responseCode);
        throw new PaymeServerErrorException("PAYME_ERROR");
      }
    } catch (IOException e) {
      log.error("PAYME ISHLAMAYABTI MAZGIOS::::::: createCard()", e);
      throw new PaymeServerErrorException("PAYME_ERROR");
    } finally {
      if (connection != null) {
        connection.disconnect();
      }
    }
  }

  private static String extractToken(String jsonResponse) {
    JSONObject jsonObject = new JSONObject(jsonResponse);
    JSONObject result = jsonObject.optJSONObject("result"); // Using optJSONObject to avoid NullPointerException

    if (result != null && result.has("card")) {
      JSONObject card = result.getJSONObject("card");
      if (card.has("token")) {
        return card.getString("token");
      } else {
        throw new CardNotFoundException("NOT_FOUND");
      }
    } else {
      throw new CardNotFoundException("NOT_FOUND");
    }
  }

  public static String getVerifyCode(String token, Long id, String authToken) {
    try {
      String url = "https://checkout.test.paycom.uz/api";
      HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();

      connection.setRequestMethod("POST");
      connection.setRequestProperty("X-auth", authToken);
      connection.setRequestProperty("Content-Type", "application/json");
      connection.setDoOutput(true);

      String jsonInputString = String.format(
        "{\"id\": %d, \"method\": \"cards.get_verify_code\", \"params\": {\"token\": \"%s\"}}",
        id, token
      );

      try (DataOutputStream dos = new DataOutputStream(connection.getOutputStream())) {
        dos.writeBytes(jsonInputString);
        dos.flush();
      }

      int responseCode = connection.getResponseCode();
      StringBuilder response = new StringBuilder();
      try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
          response.append(inputLine);
        }
      }

      connection.disconnect();

      return extractPhoneNumber(response.toString());
    } catch (IOException e) {
      log.error("PAYME ISHLAMAYABTI MAZGIOS::::::: getVerifyCode()", e);
      throw new PaymeServerErrorException("PAYME_ERROR");
    }
  }

  private static String extractPhoneNumber(String jsonResponse) {
    JSONObject jsonObject = new JSONObject(jsonResponse);

    if (jsonObject.has("result") && jsonObject.getJSONObject("result").has("phone")) {
      return jsonObject.getJSONObject("result").getString("phone");
    }

    throw new CardNotFoundException("NOT_FOUND");
  }


  public static Boolean verifyCard(String token, String code, int id, String authToken) {
    try {
      String url = "https://checkout.test.paycom.uz/api";
      HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();

      connection.setRequestMethod("POST");
      connection.setRequestProperty("X-auth", authToken);
      connection.setRequestProperty("Content-Type", "application/json");
      connection.setDoOutput(true);

      String jsonInputString = String.format(
        "{\"id\": %d, \"method\": \"cards.verify\", \"params\": {\"token\": \"%s\", \"code\": \"%s\"}}",
        id, token, code
      );

      try (DataOutputStream dos = new DataOutputStream(connection.getOutputStream())) {
        dos.writeBytes(jsonInputString);
        dos.flush();
      }

      int responseCode = connection.getResponseCode();
      StringBuilder response = new StringBuilder();
      try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
          response.append(inputLine);
        }
      }

      connection.disconnect();

      return isVerifySuccessful(response.toString());
    } catch (IOException e) {
      log.error("PAYME ISHLAMAYABTI MAZGIOS::::::: verifyCard()", e);
      throw new PaymeServerErrorException("PAYME_ERROR");
    }
  }

  private static boolean isVerifySuccessful(String jsonResponse) {
    JSONObject jsonObject = new JSONObject(jsonResponse);

    if (jsonObject.has("result") && jsonObject.getJSONObject("result").has("card")) {
      return jsonObject.getJSONObject("result").getJSONObject("card").getBoolean("verify");
    }

    return false;
  }


  public static String createReceipt(int amount, int orderId, String title, int price, int count, String code, int vatPercent, String packageCode) {
    HttpURLConnection connection = null;
    try {
      String url = "https://checkout.test.paycom.uz/api";
      connection = (HttpURLConnection) new URL(url).openConnection();

      connection.setRequestMethod("POST");
      connection.setRequestProperty("X-auth", "5e730e8e0b852a417aa49ceb:ZPDODSiTYKuX0jyO7Kl2to4rQbNwG08jbghj");
      connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
      connection.setDoOutput(true);

      // Build JSON input string dynamically
      JSONObject jsonParams = new JSONObject();
      jsonParams.put("amount", amount);
      jsonParams.put("order_id", orderId);

      JSONObject item = new JSONObject();
      item.put("title", title);
      item.put("price", price);
      item.put("count", count);
      item.put("code", code);
      item.put("vat_percent", vatPercent);
      item.put("package_code", packageCode);

      JSONArray items = new JSONArray();
      items.put(item);

      JSONObject detail = new JSONObject();
      detail.put("receipt_type", 0);
      detail.put("items", items);

      jsonParams.put("detail", detail);

      JSONObject jsonInput = new JSONObject();
      jsonInput.put("id", 4);
      jsonInput.put("method", "receipts.create");
      jsonInput.put("params", jsonParams);

      // Write JSON data to output stream
      try (OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), "UTF-8")) {
        writer.write(jsonInput.toString());
        writer.flush();
      }

      int responseCode = connection.getResponseCode();
      StringBuilder response = new StringBuilder();

      // Read the response
      try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"))) {
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
          response.append(inputLine);
        }
      }

      System.out.println("Response Code: " + responseCode);
      System.out.println("Response: " + response.toString());

      return extractId(response.toString());

    } catch (IOException e) {
      System.err.println("PAYME ISHLAMAYABTI MAZGIOS::::::: createReceipt()" + e);
      throw new PaymeServerErrorException("PAYME_ERROR");
    } finally {
      if (connection != null) {
        connection.disconnect();
      }
    }
  }

  public static String extractId(String jsonResponse) {
    try {
      JSONObject jsonObject = new JSONObject(jsonResponse);
      return jsonObject.getJSONObject("result").getJSONObject("receipt").getString("_id");
    } catch (Exception e) {
      System.err.println("PAYME ISHLAMAYABTI MAZGIOS::::::: extractId()" + e);
      throw new PaymeServerErrorException("PAYME_ERROR");
    }
  }

  public static String payReceipt(String receiptId, String token) {
    HttpURLConnection connection = null;
    try {
      String url = "https://checkout.test.paycom.uz/api";
      connection = (HttpURLConnection) new URL(url).openConnection();

      connection.setRequestMethod("POST");
      connection.setRequestProperty("X-auth", "5e730e8e0b852a417aa49ceb:ZPDODSiTYKuX0jyO7Kl2to4rQbNwG08jbghj");
      connection.setRequestProperty("Content-Type", "application/json");
      connection.setDoOutput(true);

      JSONObject jsonParams = new JSONObject();
      jsonParams.put("id", 123);
      jsonParams.put("method", "receipts.pay");

      JSONObject params = new JSONObject();
      params.put("id", receiptId);
      params.put("token", token);
      jsonParams.put("params", params);

      try (DataOutputStream dos = new DataOutputStream(connection.getOutputStream())) {
        dos.writeBytes(jsonParams.toString());
        dos.flush();
      }

      int responseCode = connection.getResponseCode();
      StringBuilder response = new StringBuilder();
      try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
          response.append(inputLine);
        }
      }

      System.out.println("Response Code: " + responseCode);
      System.out.println("Response: " + response.toString());

      return extractPayerPhone(response.toString());
    } catch (IOException e) {
      System.err.println("PAYME ISHLAMAYABTI MAZGIOS::::::: payReceipt()" + e);
      throw new PaymeServerErrorException("PAYME_ERROR");
    } finally {
      if (connection != null) {
        connection.disconnect();
      }
    }
  }

  private static String extractPayerPhone(String jsonResponse) {
    try {
      JSONObject jsonObject = new JSONObject(jsonResponse);
      return jsonObject
        .getJSONObject("result")
        .getJSONObject("receipt")
        .getJSONObject("payer")
        .getString("phone");
    } catch (Exception e) {
      System.err.println("PAYME ISHLAMAYABTI MAZGIOS::::::: extractPayerPhone()" + e);
      throw new PaymeServerErrorException("PAYME_ERROR");
    }
  }
}