package at.florian7843.chif17bot.lib;

import lombok.Getter;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;

public class CurrencyManager {

  @Getter
  private HashMap<String, String> currency = new HashMap<String, String>() {{
    this.put("AUD", "AUD");
    this.put("Lew", "BGN");
    this.put("Real", "BRL");
    this.put("CA-Dollar", "CAD");
    this.put("Fr", "CHF");
    this.put("Renminbi", "CNY");
    this.put("CZK", "CZK");
    this.put("DKK", "DKK");
    this.put("Pound", "GBP");
    this.put("HKD", "HKD");
    this.put("Kuna", "HRK");
    this.put("Forint", "HUF");
    this.put("Rupiah", "IDR");
    this.put("Schekel", "ILS");
    this.put("INR", "INR");
    this.put("Yen", "JPY");
    this.put("KRW", "KRW");
    this.put("MXN", "MXN");
    this.put("Ringgit", "MYR");
    this.put("NOK", "NOK");
    this.put("NZD", "NZD");
    this.put("PHP", "PHP");
    this.put("Zloty", "PLN");
    this.put("Leu", "RON");
    this.put("Rubel", "RUB");
    this.put("SEK", "SEK");
    this.put("SGD", "SGD");
    this.put("Baht", "THB");
    this.put("Lira", "TRY");
    this.put("US-Dollar", "USD");
    this.put("Rand", "ZAR");
    this.put("Euro", "EUR");
  }};

  public double getRate(String base, String to) {
    try {
      JSONObject json = new JSONObject(IOUtils.toString(new URL("http://api.fixer.io/latest?base=" + base), Charset.forName("UTF-8")));
      JSONObject rates = json.getJSONObject("rates");
      return rates.getDouble(to);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return -1.0;
  }

}
