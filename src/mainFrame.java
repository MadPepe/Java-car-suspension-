import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by mateuszstolowski and michattopka on 16.06.2017.
 */
public class mainFrame extends JFrame implements ActionListener{

    private final int       CZAS                = 60;

    private Toolkit         NARZEDZIA           = Toolkit.getDefaultToolkit();
    private Dimension       WYMIARY_OKNA        = NARZEDZIA.getScreenSize();
    private int             WYMIAR_OKNA_X       = (int)(0.8*WYMIARY_OKNA.getWidth());
    private int             WYMIAR_OKNA_Y       = (int)(0.8*WYMIARY_OKNA.getHeight());
    private String          []wyboryWykresow    = {"Vx, Vy, u od czasu", "X, Y, u od czasu"};
    private int             iterator;
    private double          K1;
    private double          K2;
    private double          B1;
    private double          M1;
    private double          M2;
    private double           H;
    private double          []Vx;
    private double          []Vy;
    private double          []X;
    private double          []Y;
    private double          []U;
    private int             sygnal;
    private double          okres;
    private int             amplituda;

    JFreeChart chart;

    XYSeriesCollection dane = new XYSeriesCollection();
    XYSeries seriaVx = new XYSeries("Vx");
    XYSeries seriaVy = new XYSeries("Vy");
    XYSeries seriaX = new XYSeries("X");
    XYSeries seriaY = new XYSeries("Y");
    XYSeries seriaU = new XYSeries("U");


    JRadioButton []radioButtons = new JRadioButton[3];
    JTextField []textFields = new JTextField[8];
    JComboBox comboBox = new JComboBox(wyboryWykresow);
    JButton przyciskSymulacji = new JButton("Symuluj");
    ButtonGroup radioGroup = new ButtonGroup();


    JPanel panelGlowny = new JPanel();
    GridBagConstraints wytyczne = new GridBagConstraints();


    /** METODY
     *
     */

    public static void main(String args[]){
        new mainFrame();
    }

    public void zrobInterfejs(){

        for(int i=0; i<8; i++)
        {
            if(i<3){
                radioButtons[i] = new JRadioButton();
                radioGroup.add(radioButtons[i]);
            }
            textFields[i] = new JTextField();
            textFields[i].setText("0");
            textFields[i].setColumns(4);

        }
        radioButtons[0].setText("Sygnał prostokątny");
        radioButtons[0].setSelected(true);
        radioButtons[1].setText("Sygnał trójkątny");
        radioButtons[2].setText("Sygnał sinusoidalny");

        wytyczne.anchor     = GridBagConstraints.NORTHWEST;
        wytyczne.weightx    = 1;
        wytyczne.weighty    = 1;
        wytyczne.gridheight = 1;
        wytyczne.gridwidth  = 2;

        ///////////////////////////////////////////////////Wybory sygnałów
        wytyczne.gridx      = 0;
        wytyczne.gridy      = 0;
        panelGlowny.add(radioButtons[0],wytyczne);

        wytyczne.gridx      = 0;
        wytyczne.gridy      = 1;
        panelGlowny.add(radioButtons[1],wytyczne);

        wytyczne.gridx      = 0;
        wytyczne.gridy      = 2;
        panelGlowny.add(radioButtons[2],wytyczne);




        wytyczne.gridwidth  = 1;

        ///////////////////////////////////////////////Tekst
        wytyczne.anchor = GridBagConstraints.EAST;

        wytyczne.gridx      = 0;
        wytyczne.gridy      = 3;
        panelGlowny.add(new JLabel("K1"),wytyczne);

        wytyczne.gridx      = 0;
        wytyczne.gridy      = 4;
        panelGlowny.add(new JLabel("K2"),wytyczne);

        wytyczne.gridx      = 0;
        wytyczne.gridy      = 5;
        panelGlowny.add(new JLabel("B1"),wytyczne);

        wytyczne.gridx      = 0;
        wytyczne.gridy      = 6;
        panelGlowny.add(new JLabel("M1"),wytyczne);

        wytyczne.gridx      = 0;
        wytyczne.gridy      = 7;
        panelGlowny.add(new JLabel("M2"),wytyczne);

        wytyczne.gridx      = 0;
        wytyczne.gridy      = 8;
        panelGlowny.add(new JLabel("H"),wytyczne);

        wytyczne.gridx      = 0;
        wytyczne.gridy      = 9;
        panelGlowny.add(new JLabel("Amplituda"),wytyczne);

        wytyczne.weighty    = 20;
        wytyczne.anchor     = GridBagConstraints.NORTHEAST;
        wytyczne.gridx      = 0;
        wytyczne.gridy      = 10;
        panelGlowny.add(new JLabel("Okres"),wytyczne);



        //////////////////////////////////////////////////////Obszary tekstowe
        wytyczne.anchor     = GridBagConstraints.WEST;
        wytyczne.weighty    = 1;

        wytyczne.gridx      = 1;
        wytyczne.gridy      = 3;
        panelGlowny.add(textFields[0],wytyczne);

        wytyczne.gridx      = 1;
        wytyczne.gridy      = 4;
        panelGlowny.add(textFields[1],wytyczne);

        wytyczne.gridx      = 1;
        wytyczne.gridy      = 5;
        panelGlowny.add(textFields[2],wytyczne);

        wytyczne.gridx      = 1;
        wytyczne.gridy      = 6;
        panelGlowny.add(textFields[3],wytyczne);

        wytyczne.gridx      = 1;
        wytyczne.gridy      = 7;
        panelGlowny.add(textFields[4],wytyczne);

        wytyczne.gridx      = 1;
        wytyczne.gridy      = 8;
        panelGlowny.add(textFields[5],wytyczne);

        wytyczne.gridx      = 1;
        wytyczne.gridy      = 9;
        panelGlowny.add(textFields[6],wytyczne);

        wytyczne.weighty    = 20;
        wytyczne.anchor     = GridBagConstraints.NORTHWEST;
        wytyczne.gridx      = 1;
        wytyczne.gridy      = 10;
        panelGlowny.add(textFields[7],wytyczne);



        /////////////////////////////////////////Wybór wykresów
        wytyczne.weighty    = 1;
        wytyczne.anchor     = GridBagConstraints.CENTER;
        wytyczne.gridx      = 2;
        wytyczne.gridy      = 0;
        panelGlowny.add(comboBox, wytyczne);

        ////////////////////////////////////////Przycisk symulacji
        wytyczne.fill       = GridBagConstraints.BOTH;
        wytyczne.gridheight = 5;
        wytyczne.gridx      = 2;
        wytyczne.gridy      = 1;
        przyciskSymulacji.addActionListener(this);
        panelGlowny.add(przyciskSymulacji,wytyczne);


        ////////////////////////////////////////Wykres
        wytyczne.gridx      = 3;
        wytyczne.gridy      = 0;
        wytyczne.gridwidth  = 10;
        wytyczne.gridheight = 11;
        this.rysujWykres();


    }

    public void obliczWartosc(){
        int wartosc = 0;
        double wzrost  = 0;

        for(int i=0; i<radioButtons.length; i++) if(radioButtons[i].isSelected()) sygnal = i;

        Vx      = new double[iterator+1];
        Vx[0]   = 0;

        Vy      = new double[iterator+1];
        Vy[0]   = 0;

        X       = new double[iterator+1];
        X[0]    = 0;

        Y       = new double[iterator+1];
        Y[0]    = 0;

        U       = new double[iterator+1];
        U[0]    = 0;

        for(int i=0; i<=iterator; i++) U[i] = 0;

        if(sygnal==0){
            for(int i =1; i<=iterator; i++){
                if(i%okres==0) wartosc++;
                if(wartosc%2==0) U[i]=amplituda;
                else U[i] = -amplituda;

                seriaU.add(i,U[i]);
            }
        }
        else if(sygnal == 1){
            wzrost = amplituda/okres;
            for(int i=1; i<=iterator; i++){

                if(U[i-1]==amplituda) wzrost = -wzrost;
                else if(U[i-1]==-amplituda) wzrost += (-2)*wzrost;

                U[i] = U[i-1]+wzrost;
                seriaU.add(i,U[i]);
            }

        }
        else if(sygnal == 2){
            for(int i=1; i<=iterator; i++) {
                U[i] = amplituda * Math.sin(((2*Math.PI)/okres)*i);
                seriaU.add(i, U[i]);
            }
        }

        for(int i=1; i<=iterator; i++){

            X[i]    = Vx[i-1]*H + X[i-1];

            Y[i]    = Vy[i-1]*H + Y[i-1];

            Vx[i]   = (H*((K1*Y[i-1]-K1*X[i-1]+B1*Vy[i-1]-B1*Vx[i-1])/M1)+Vx[i-1]);

            Vy[i]   = (H*((K1*X[i-1]-K1*Y[i-1]+B1*Vx[i-1]-B1*Vy[i-1]+K2*U[i]-K2*Y[i-1])/M2)+Vy[i-1]);

            seriaVx.add(i,Vx[i]);
            seriaVy.add(i,Vy[i]);
            seriaX.add(i,X[i]);
            seriaY.add(i,Y[i]);
        }
        System.out.println("K1: " + K1 + "\n" +
                           "K2: " + K2 +"\n" +
                            "B1: " + B1 +"\n" +
                            "H: " + H +"\n" +
                            "M1: " + M1 +"\n" +
                            "M2: " + M2 +"\n");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(sprawdzeniePoprawnosci()){
            seriaVx.clear();
            seriaVy.clear();
            seriaU.clear();
            seriaX.clear();
            seriaY.clear();

            setK1(Double.parseDouble(textFields[0].getText()));
            setK2(Double.parseDouble(textFields[1].getText()));
            setB1(Double.parseDouble(textFields[2].getText()));
            setM1(Double.parseDouble(textFields[3].getText()));
            setM2(Double.parseDouble(textFields[4].getText()));
            setH(Double.parseDouble(textFields[5].getText()));
            setAmplitude(Integer.parseInt(textFields[6].getText()));
            setPeriod(Double.parseDouble(textFields[7].getText()));
            setIterator();
            obliczWartosc();
            rysujWykres();
        }

    }

    public boolean sprawdzeniePoprawnosci(){
        for (int i=0; i<textFields.length; i++){
            if(     !textFields[0].getText().matches("([0-9]+\\.[1-9]|[0-9]+\\.[0-9][5-9]|[0-9]+)")               ||
                    !textFields[1].getText().matches("([0-9]+\\.[1-9]|[0-9]+\\.[0-9][5-9]|[0-9]+)")               ||
                    !textFields[2].getText().matches("([0-9]+\\.[1-9]|[0-9]+\\.[0-9][5-9]|[0-9]+)")               ||
                    !textFields[3].getText().matches("([0-9]+\\.[1-9]|[0-9]+\\.[0-9][5-9]|[1-9]|[1-9][0-9]+)")    ||
                    !textFields[4].getText().matches("([0-9]+\\.[1-9]|[0-9]+\\.[0-9][5-9]|[1-9]|[1-9][0-9]+)")    ||
                    !textFields[5].getText().matches("([0-9]+\\.[1-9]|[0-9]+\\.[0-9][1-9]|[1-9]|[1-9][0-9]+)")){

                JOptionPane.showMessageDialog(this,
                        "Podano nieprawidłową wartość",
                        "Błąd!",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        return true;
    }

    public void rysujWykres(){

        dane.removeAllSeries();
        if(comboBox.getSelectedItem() == "Vx, Vy, u od czasu"){
            dane.addSeries(seriaVx);
            dane.addSeries(seriaVy);
            dane.addSeries(seriaU);
        }
        else if(comboBox.getSelectedItem() == "X, Y, u od czasu") {
            dane.addSeries(seriaX);
            dane.addSeries(seriaY);
            dane.addSeries(seriaU);
        }
        chart = ChartFactory.createXYLineChart(null,"Czas","Wartośc", dane);

        panelGlowny.add(new ChartPanel(chart),wytyczne);
    }

    public void setK1(double wartosc){ K1 = wartosc; }
    public void setK2(double wartosc){ K2 = wartosc; }
    public void setH(double wartosc){  H  = wartosc; }
    public void setB1(double wartosc){ B1 = wartosc; }
    public void setM1(double wartosc){ M1 = wartosc; }
    public void setM2(double wartosc){ M2 = wartosc; }
    public void setAmplitude(int wartosc){ amplituda= wartosc; }
    public void setPeriod(double wartosc){ okres = wartosc; }
    public void setIterator(){  iterator = (int)(CZAS/H); }

    /** KONSTRUKTORY
     *
     */

    public mainFrame(){
        this.setSize(WYMIAR_OKNA_X, WYMIAR_OKNA_Y);
        this.setTitle("Projekt MMM");
        this.setResizable(false);
        panelGlowny.setLayout(new GridBagLayout());

        this.zrobInterfejs();

        this.add(panelGlowny);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }
}
