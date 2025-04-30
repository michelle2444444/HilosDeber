import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ventanaCarrera());
    }
}

class Hilo implements Runnable {
    private Thread t;
    private final String nombre;
    private final JLabel personaje;
    private final JLabel pfinal;
    private static int lugar = 1;

    public Hilo(String nombre, JLabel personaje, JLabel puntofinal) {
        this.nombre = nombre;
        this.personaje = personaje;
        this.pfinal = puntofinal;

        t = new Thread(this);
        t.start();
    }

    @Override
    public void run() {
        int retardo = (int) (Math.random() * 15) + 1;
        try {
            personaje.setVisible(true);
            pfinal.setVisible(false);

            for (int i = 50; i <= 400; i += 2) {
                personaje.setLocation(i, personaje.getY());
                Thread.sleep(retardo);
            }

            personaje.setVisible(false);
            pfinal.setVisible(true);

            synchronized (Hilo.class) {
                pfinal.setText(nombre + " ha llegado a la posición " + lugar);
                lugar++;
            }

        } catch (Exception e) {
            System.out.println("Error en hilo " + nombre + ": " + e.getMessage());
        }
    }

    public static void reiniciarLugar() {
        lugar = 1;
    }
}

class ventanaCarrera extends JFrame {
    public ventanaCarrera() {
        super("Carrera de personajes");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(null);

        JLabel gato = crearCorredor(panel, "src/Img/corredor4.gif", 50);
        JLabel estrella = crearCorredor(panel, "src/Img/corredor5.gif", 100);
        JLabel tails = crearCorredor(panel, "src/Img/corredor6.gif", 150);

        JLabel gato_pos = crearLabelResultado(panel, 50);
        JLabel estrella_pos = crearLabelResultado(panel, 100);
        JLabel tails_pos = crearLabelResultado(panel, 150);

        JButton iniciar_Carrera = new JButton("Iniciar carrera");
        iniciar_Carrera.setBounds(150, 200, 150, 50);
        iniciar_Carrera.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Hilo.reiniciarLugar();
                new Hilo("Gato", gato, gato_pos);
                new Hilo("Estrella", estrella, estrella_pos);
                new Hilo("Tails", tails, tails_pos);
            }
        });

        panel.add(iniciar_Carrera);
        add(panel);
        setVisible(true);
    }

    private JLabel crearCorredor(JPanel panel, String path, int y) {
        ImageIcon img = new ImageIcon(new ImageIcon(path).getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT));
        JLabel lbl = new JLabel(img);
        lbl.setBounds(50, y, 50, 50);
        panel.add(lbl);
        return lbl;
    }

    private JLabel crearLabelResultado(JPanel panel, int y) {
        JLabel lbl = new JLabel();
        lbl.setBounds(200, y, 250, 50);
        panel.add(lbl);
        return lbl;
    }
}