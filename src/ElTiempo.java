import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import okhttp3.*;
import com.google.gson.*;
import java.io.IOException;


public class ElTiempo {

    private static final String API_KEY = "TU_API_KEY";
    private static final OkHttpClient httpClient = new OkHttpClient();
    private static final Gson gson = new Gson();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("App del Tiempo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Panel principal
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Buscador de ciudades
        JTextField searchField = new JTextField();
        panel.add(searchField);

        // Desplegable de ciudades
        JComboBox<String> cityList = new JComboBox<>(new String[] {"Madrid", "Barcelona", "Valencia"});
        panel.add(cityList);

        // Botones
        JButton exitButton = new JButton("Salir");
        JButton weatherButton = new JButton("Saber el tiempo");

        exitButton.addActionListener(e -> System.exit(0));
        weatherButton.addActionListener(e -> showWeather((String) cityList.getSelectedItem()));

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(exitButton);
        buttonPanel.add(weatherButton);
        panel.add(buttonPanel);

        frame.getContentPane().add(panel);
        frame.pack();
        frame.setVisible(true);
    }

    private static void showWeather(String city) {
        Request request = new Request.Builder()
                .url("http://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + API_KEY)
                .build();

        try {
            Response response = httpClient.newCall(request).execute();
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            WeatherData weatherData = gson.fromJson(response.body().charStream(), WeatherData.class);
            JOptionPane.showMessageDialog(null, "Temperatura: " + weatherData.main.temp);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al obtener el tiempo.");
        }
    }

    class WeatherData {
        Main main;

        class Main {
            double temp;
        }
    }
}

