package app;
import settings.Settings;
import user.Wallet;
import sound.Sound;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Objects;

/**
 * Klasa odpowiedzialna za główne okno aplikacji.
 * To ono wysyła sygnały do obiektu klasy <i>StateManager</i>.
 * Plik z danymi <i style="background:rgba(92,92,92,0.5);border-radius: 0.5em;">&nbsp;settings.xml&nbsp;</i> znajduje się w katalogu <i style="background:rgba(92,92,92,0.5);border-radius:0.5em;">&nbsp;userdata&nbsp;</i>.
 * @author Patryk Jaworski, Jakub Jach
 * @version 1.0
 * @since 2021-01-10
 */

public class Window extends JFrame implements ActionListener{
    /**
     * Przycisk używzany przez główne okno - klawisze numeryczne.
     */
   private final JButton keyNumber1, keyNumber2, keyNumber3, keyNumber4, keyNumber5, keyNumber6;
    /**
     * Przycisk używzany przez główne okno - klawisze numeryczne.
     */
   private final JButton keyNumber7, keyNumber8, keyNumber9, keyNumber0, keyNumber000;
    /**
     * Przycisk używzany przez główne okno - klawisze specjalne kalwisze na keypadzie.
     */
   private final JButton  keyEnter, keyDelete, keyClear,keyCancel,keyCardtestonly;
    /**
     * Przyciski używzany przez główne okno - operacje przekazywania gotówki.
     */
   private final JButton [] WalletOps;
   /**
   * Etykiety dla ilości banknotów w operacji na gotówce.
    */
   private final JLabel [] WLabels;
   /**
    * Pasek menu.
    */
   private final JMenuBar Menubar;
   /**
    * Menu.
    */
   private final JMenu Help,MUser;
    /**
     * Elementy menu.
     */
   private final JMenuItem About,Exit,SwitchUser,SwitchCard;
    /**
     * Klawisze boczne oraz przycisk do włożenia karty.
     */
   private final JButton keyLeft1,keyLeft2, keyLeft3, keyRight1,keyRight2,keyRight3;
   /**
    * Obiekt <i>(referencja)</i> klasy <i>StateManager</i>, która odpowiada za działanie wewnętrznego mechanizmu bankomatu.
    * Jedna z najważniejszych klas zaraz po <i>Window</i>.
    */
   private final StateManager State;
    /**
     * <i>"Portfele"</i> używane w operacji depozytu.
     */
   private final Wallet operational,temporary;
    /**
     * Wersja programu - domyślnie 1.0 a uzyskiwana z pliku <i style="background:rgba(92,92,92,0.5);border-radius:0.5em;">&nbsp;VERSION&nbsp;</i> dołączanego jako zasób.
     */
   private String version = "1.0";

    /**
     * Tablica przechowująca wszystkich użytkowników - <i style="background:rgba(92,92,92,0.5);border-radius:0.5em;">&nbsp;settings.xml&nbsp;</i> oraz zapisywana do niego podczas zamykania programu.
     */
private void updatePos()
{
    Settings.posX = this.getX();
    Settings.posY = this.getY();
}
    /**
     * Jedyny konstruktor klasy.
     * @param defaultPos <b style="color:#B45700;">boolean</b> Wskazuje na to, czg okno ma pojawić się w pozycji domyślnej (nie ustawionej), czy też tej wczytanej z pliku.
     */
    Window(boolean defaultPos)
    {
        ImageIcon icon = null;
        try {
           icon = new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/textures/logo.png")));
        }
        catch(NullPointerException exception)
        {
            System.out.println("Exception "+exception.getMessage());
        }
        this.setSize(800,900);
        this.setResizable(false);
        this.setLayout(new BorderLayout());
        if(icon!=null)this.setIconImage(icon.getImage());
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                updatePos();
                Settings.saveState();
                System.exit(0);
            }
        });
        Menubar = new JMenuBar();
        Menubar.setBounds(0,0,800,25);
        Help = new JMenu("Help");
        MUser = new JMenu("User");
        SwitchCard = new JMenuItem("Switch Card");
        SwitchUser = new JMenuItem("Switch User");
        About = new JMenuItem("About");
        Exit = new JMenuItem("Exit");
        Exit.addActionListener(this);
        About.addActionListener(this);
        SwitchUser.addActionListener(this);
        SwitchCard.addActionListener(this);
        Help.add(Exit);
        Help.add(About);
        MUser.add(SwitchUser);
        MUser.add(SwitchCard);
        Menubar.add(Help);
        Menubar.add(MUser);
        State = new StateManager(Settings.users.get(Settings.currentUser()));
        State.currency = Settings.currency;
        this.setTitle("ATM: "+Settings.users.get(Settings.currentUser()).Name +" "+Settings.users.get(Settings.currentUser()).Surname);
        if(!defaultPos)this.setLocation(Settings.posX,Settings.posY);
        operational = new Wallet(true);
        temporary = Settings.users.get(Settings.currentUser()).getWallet().copy();
        try
        {
            InputStreamReader stream = new InputStreamReader(this.getClass().getResourceAsStream("/VERSION"));
            BufferedReader reader = new BufferedReader(stream);
            version = reader.readLine();
        }
        catch (Exception e)
        {
            System.out.println("Cannot get VERSION file");
        }

        JPanel top = new JPanel();
        JLayeredPane left = new JLayeredPane();
        JLayeredPane right = new JLayeredPane();
        JLayeredPane bottom = new JLayeredPane();
        JPanel center = new JPanel();

        JLayeredPane keypad = new JLayeredPane();
try {
    JLabel keypadBackground = new JLabel(new ImageIcon(this.getClass().getResource("/textures/keypad.png")));
    keypadBackground.setBounds(0,0,400,350);
    keypad.add(keypadBackground, 0);

    JLabel topBackground = new JLabel(new ImageIcon(this.getClass().getResource("/textures/banner.png")));
    topBackground.setBounds(0,10,100,100);
    top.add(topBackground);
}
catch(Exception e)
{
    System.out.println(e.getMessage());
}

        bottom.setLayout(null);
        left.setLayout(null);
        right.setLayout(null);
        keypad.setLayout(null);
        center.setLayout(null);
        top.setBackground(new Color(0,106,67));
        left.setBackground(new Color(0xcfcfcf));
        right.setBackground(new Color(0xcfcfcf));
        bottom.setBackground(new Color(0xcfcfcf));

        top.setBounds(0,0,800,100);
        left.setPreferredSize(new Dimension(100,100));
        right.setPreferredSize(new Dimension(100,100));
        bottom.setPreferredSize(new Dimension(200,400));
        center.setBounds(0,0,584,329);
        keypad.setSize(400,350);
        keypad.setLocation(100,40);

        keyNumber1 = new JButton();
        keyNumber2 = new JButton();
        keyNumber3 = new JButton();
        keyNumber4 = new JButton();
        keyNumber5 = new JButton();
        keyNumber6 = new JButton();
        keyNumber7 = new JButton();
        keyNumber8 = new JButton();
        keyNumber9 = new JButton();
        keyNumber0 = new JButton();
        keyNumber000 = new JButton();


        WalletOps = new JButton[12];
        WLabels = new JLabel[12];
        for(int i=0;i<12;i++)
        {
            WalletOps[i] = new JButton();
            WalletOps[i].setBounds(600 + i%2*40,150 + 40 * (i / 2) ,30,30);
            WalletOps[i].setOpaque(false);
            WalletOps[i].setBackground(null);
            WalletOps[i].setBorder(null);
            try {
                WalletOps[i].setIcon(new ImageIcon(this.getClass().getResource((i % 2 == 0) ? "/textures/plus.png" : "/textures/minus.png")));
                WalletOps[i].setPressedIcon(new ImageIcon(this.getClass().getResource((i % 2 == 0) ? "/textures/plus_w.png" : "/textures/minus_w.png")));
            }
            catch(Exception e)
            {
                System.out.println(e.getMessage());
            }
            WalletOps[i].setFocusable(false);
            WalletOps[i].addActionListener(this);
            WalletOps[i].setVisible(false);

            WLabels[i] = new JLabel("x0");
            WLabels[i].setFont(new Font("Comic Sans",Font.BOLD,20));
            WLabels[i].setForeground(new Color(0x33ff7d));
            WLabels[i].setBounds(480 + i%2*170,150+ 40 * (i / 2) ,60+(i%2==0?40:0),30);
            WLabels[i].setHorizontalTextPosition(SwingConstants.RIGHT);
            WLabels[i].setHorizontalAlignment(SwingConstants.RIGHT);
            WLabels[i].setVisible(false);
        }

        WLabels[0].setText("10 x"+temporary.getAmount("10"));
        WLabels[2].setText("20 x"+temporary.getAmount("20"));
        WLabels[4].setText("50 x"+temporary.getAmount("50"));
        WLabels[6].setText("100 x"+temporary.getAmount("100"));
        WLabels[8].setText("200 x"+temporary.getAmount("200"));
        WLabels[10].setText("500 x"+temporary.getAmount("500"));

        keyEnter = new JButton();
        keyDelete = new JButton();
        keyClear = new JButton();
        keyCancel = new JButton();
        keyCardtestonly = new JButton();

       keyLeft1 = new JButton();
       keyLeft2 = new JButton();
       keyLeft3 = new JButton();

       keyRight1 = new JButton();
       keyRight2 = new JButton();
       keyRight3 = new JButton();


        keyNumber1.setBounds(20,60,50,50);
        keyNumber1.setBackground(null);
        keyNumber1.setContentAreaFilled(false);
        keyNumber1.setBorder(null);
        keyNumber1.setFocusable(false);
        keyNumber1.addActionListener(this);


        keyNumber2.setBounds(90,60,50,50);
        keyNumber2.setBackground(null);
        keyNumber2.setContentAreaFilled(false);
        keyNumber2.setBorder(null);
        keyNumber2.setFocusable(false);
        keyNumber2.addActionListener(this);

        keyNumber3.setBounds(160,60,50,50);
        keyNumber3.setBackground(null);
        keyNumber3.setContentAreaFilled(false);
        keyNumber3.setBorder(null);
        keyNumber3.setFocusable(false);
        keyNumber3.addActionListener(this);

        keyNumber4.setBounds(20,130,50,50);
        keyNumber4.setBackground(null);
        keyNumber4.setContentAreaFilled(false);
        keyNumber4.setBorder(null);
        keyNumber4.setFocusable(false);
        keyNumber4.addActionListener(this);

        keyNumber5.setBounds(90,130,50,50);
        keyNumber5.setBackground(null);
        keyNumber5.setContentAreaFilled(false);
        keyNumber5.setBorder(null);
        keyNumber5.setFocusable(false);
        keyNumber5.addActionListener(this);

        keyNumber6.setBounds(160,130,50,50);
        keyNumber6.setBackground(null);
        keyNumber6.setContentAreaFilled(false);
        keyNumber6.setBorder(null);
        keyNumber6.setFocusable(false);
        keyNumber6.addActionListener(this);

        keyNumber7.setBounds(20,200,50,50);
        keyNumber7.setBackground(null);
        keyNumber7.setContentAreaFilled(false);
        keyNumber7.setBorder(null);
        keyNumber7.setFocusable(false);
        keyNumber7.addActionListener(this);

        keyNumber8.setBounds(90,200,50,50);
        keyNumber8.setBackground(null);
        keyNumber8.setContentAreaFilled(false);
        keyNumber8.setBorder(null);
        keyNumber8.setFocusable(false);
        keyNumber8.addActionListener(this);

        keyNumber9.setBounds(160,200,50,50);
        keyNumber9.setBackground(null);
        keyNumber9.setContentAreaFilled(false);
        keyNumber9.setBorder(null);
        keyNumber9.setFocusable(false);
        keyNumber9.addActionListener(this);

        keyNumber0.setBounds(60,270,50,50);
        keyNumber0.setBackground(null);
        keyNumber0.setContentAreaFilled(false);
        keyNumber0.setBorder(null);
        keyNumber0.setFocusable(false);
        keyNumber0.addActionListener(this);

        keyNumber000.setBounds(128,270,80,50);
        keyNumber000.setBackground(null);
        keyNumber000.setContentAreaFilled(false);
        keyNumber000.setBorder(null);
        keyNumber000.setFocusable(false);
        keyNumber000.addActionListener(this);

        keyLeft1.setBounds(25,50,50,50);
        keyLeft1.setBackground(null);
        keyLeft1.setContentAreaFilled(false);
        keyLeft1.setBorder(null);
        keyLeft1.setFocusable(false);
        keyLeft1.addActionListener(this);

        keyLeft2.setBounds(25,160,50,50);
        keyLeft2.setBackground(null);
        keyLeft2.setContentAreaFilled(false);
        keyLeft2.setBorder(null);
        keyLeft2.setFocusable(false);
        keyLeft2.addActionListener(this);

        keyLeft3.setBounds(25,270,50,50);
        keyLeft3.setBackground(null);
        keyLeft3.setContentAreaFilled(false);
        keyLeft3.setBorder(null);
        keyLeft3.setFocusable(false);
        keyLeft3.addActionListener(this);

        keyRight1.setBounds(25,50,50,50);
        keyRight1.setBackground(null);
        keyRight1.setContentAreaFilled(false);
        keyRight1.setBorder(null);
        keyRight1.setFocusable(false);
        keyRight1.addActionListener(this);

        keyRight2.setBounds(25,160,50,50);
        keyRight2.setBackground(null);
        keyRight2.setContentAreaFilled(false);
        keyRight2.setBorder(null);
        keyRight2.setFocusable(false);
        keyRight2.addActionListener(this);

        keyRight3.setBounds(25,270,50,50);
        keyRight3.setBackground(null);
        keyRight3.setContentAreaFilled(false);
        keyRight3.setBorder(null);
        keyRight3.setFocusable(false);
        keyRight3.addActionListener(this);

        keyEnter.setBounds(250,60,100,50);
        keyEnter.setBackground(null);
        keyEnter.setContentAreaFilled(false);
        keyEnter.setBorder(null);
        keyEnter.setFocusable(false);
        keyEnter.addActionListener(this);

        keyDelete.setBounds(250,130,100,50);
        keyDelete.setBackground(null);
        keyDelete.setContentAreaFilled(false);
        keyDelete.setBorder(null);
        keyDelete.setFocusable(false);
        keyDelete.addActionListener(this);

        keyClear.setBounds(250,200,100,50);
        keyClear.setBackground(null);
        keyClear.setContentAreaFilled(false);
        keyClear.setBorder(null);
        keyClear.setFocusable(false);
        keyClear.addActionListener(this);

        keyCancel.setBounds(250,270,100,50);
        keyCancel.setBackground(null);
        keyCancel.setContentAreaFilled(false);
        keyCancel.setBorder(null);
        keyCancel.setFocusable(false);
        keyCancel.addActionListener(this);

        keyCardtestonly.setBounds(550,40,175,100);
        keyCardtestonly.setBackground(null);
        keyCardtestonly.setContentAreaFilled(false);
        keyCardtestonly.setBorder(null);
        keyCardtestonly.setFocusable(false);
        keyCardtestonly.addActionListener(this);

        try{
            keyNumber1.setIcon(new ImageIcon(this.getClass().getResource("/textures/1.png")));
            keyNumber1.setPressedIcon(new ImageIcon(this.getClass().getResource("/textures/1_w.png")));
            keyNumber2.setIcon(new ImageIcon(this.getClass().getResource("/textures/2.png")));
            keyNumber2.setPressedIcon(new ImageIcon(this.getClass().getResource("/textures/2_w.png")));
            keyNumber3.setIcon(new ImageIcon(this.getClass().getResource("/textures/3.png")));
            keyNumber3.setPressedIcon(new ImageIcon(this.getClass().getResource("/textures/3_w.png")));
            keyNumber4.setIcon(new ImageIcon(this.getClass().getResource("/textures/4.png")));
            keyNumber4.setPressedIcon(new ImageIcon(this.getClass().getResource("/textures/4_w.png")));
            keyNumber5.setIcon(new ImageIcon(this.getClass().getResource("/textures/5.png")));
            keyNumber5.setPressedIcon(new ImageIcon(this.getClass().getResource("/textures/5_w.png")));
            keyNumber6.setIcon(new ImageIcon(this.getClass().getResource("/textures/6.png")));
            keyNumber6.setPressedIcon(new ImageIcon(this.getClass().getResource("/textures/6_w.png")));
            keyNumber7.setIcon(new ImageIcon(this.getClass().getResource("/textures/7.png")));
            keyNumber7.setPressedIcon(new ImageIcon(this.getClass().getResource("/textures/7_w.png")));
            keyNumber8.setIcon(new ImageIcon(this.getClass().getResource("/textures/8.png")));
            keyNumber8.setPressedIcon(new ImageIcon(this.getClass().getResource("/textures/8_w.png")));
            keyNumber9.setIcon(new ImageIcon(this.getClass().getResource("/textures/9.png")));
            keyNumber9.setPressedIcon(new ImageIcon(this.getClass().getResource("/textures/9_w.png")));
            keyNumber0.setIcon(new ImageIcon(this.getClass().getResource("/textures/0.png")));
            keyNumber0.setPressedIcon(new ImageIcon(this.getClass().getResource("/textures/0_w.png")));
            keyNumber000.setIcon(new ImageIcon(this.getClass().getResource("/textures/000.png")));
            keyNumber000.setPressedIcon(new ImageIcon(this.getClass().getResource("/textures/000_w.png")));
            keyEnter.setIcon(new ImageIcon(this.getClass().getResource("/textures/enter.png")));
            keyEnter.setPressedIcon(new ImageIcon(this.getClass().getResource("/textures/enter_w.png")));
            keyDelete.setIcon(new ImageIcon(this.getClass().getResource("/textures/delete.png")));
            keyDelete.setPressedIcon(new ImageIcon(this.getClass().getResource("/textures/delete_w.png")));
            keyClear.setIcon(new ImageIcon(this.getClass().getResource("/textures/clear.png")));
            keyClear.setPressedIcon(new ImageIcon(this.getClass().getResource("/textures/clear_w.png")));
            keyCancel.setIcon(new ImageIcon(this.getClass().getResource("/textures/cancel_b.png")));
            keyCancel.setPressedIcon(new ImageIcon(this.getClass().getResource("/textures/cancel_b_w.png")));
            keyLeft1.setIcon(new ImageIcon(this.getClass().getResource("/textures/arrow_right.png")));
            keyLeft1.setPressedIcon(new ImageIcon(this.getClass().getResource("/textures/arrow_right_w.png")));
            keyLeft2.setIcon(new ImageIcon(this.getClass().getResource("/textures/arrow_right.png")));
            keyLeft2.setPressedIcon(new ImageIcon(this.getClass().getResource("/textures/arrow_right_w.png")));
            keyLeft3.setIcon(new ImageIcon(this.getClass().getResource("/textures/arrow_right.png")));
            keyLeft3.setPressedIcon(new ImageIcon(this.getClass().getResource("/textures/arrow_right_w.png")));
            keyRight1.setIcon(new ImageIcon(this.getClass().getResource("/textures/arrow_left.png")));
            keyRight1.setPressedIcon(new ImageIcon(this.getClass().getResource("/textures/arrow_left_w.png")));
            keyRight2.setIcon(new ImageIcon(this.getClass().getResource("/textures/arrow_left.png")));
            keyRight2.setPressedIcon(new ImageIcon(this.getClass().getResource("/textures/arrow_left_w.png")));
            keyRight3.setIcon(new ImageIcon(this.getClass().getResource("/textures/arrow_left.png")));
            keyRight3.setPressedIcon(new ImageIcon(this.getClass().getResource("/textures/arrow_left_w.png")));
            keyCardtestonly.setIcon(new ImageIcon(this.getClass().getResource("/textures/card_slot_a.png")));
            keyCardtestonly.setPressedIcon(new ImageIcon(this.getClass().getResource("/textures/card_slot_in.png")));
        }catch(NullPointerException e)
        {
            System.out.println(e);
        }

        JLabel bottompanel =  new JLabel(new ImageIcon(this.getClass().getResource("/textures/bottom.png")));
        bottompanel.setBounds(0,0,785,400);
        keypad.add(keyNumber1, 2, 1);
        keypad.add(keyNumber2, 2, 1);
        keypad.add(keyNumber3, 2, 1);
        keypad.add(keyNumber4, 2, 1);
        keypad.add(keyNumber5, 2, 1);
        keypad.add(keyNumber6, 2, 1);
        keypad.add(keyNumber7, 2, 1);
        keypad.add(keyNumber8, 2, 1);
        keypad.add(keyNumber9, 2, 1);
        keypad.add(keyNumber0, 2, 1);
        keypad.add(keyNumber000, 2, 1);
        keypad.add(keyEnter, 2, 1);
        keypad.add(keyDelete, 2, 1);
        keypad.add(keyClear, 2, 1);
        keypad.add(keyCancel, 2, 1);
        bottom.add(bottompanel,0);
        bottom.add(keypad,2,1);
        bottom.add(keyCardtestonly,2,1);
        for(int i=0;i<12;i++)
        {
            bottom.add(WalletOps[i],2,1);
            bottom.add(WLabels[i],2,1);
        }
        center.add(State);
        JLabel leftpanel = new JLabel(new ImageIcon(this.getClass().getResource("/textures/leftpanel.png")));
        JLabel rightpanel = new JLabel(new ImageIcon(this.getClass().getResource("/textures/rightpanel.png")));

        leftpanel.setBounds(0,0,100,350);
        rightpanel.setBounds(0,0,100,350);

        left.add(leftpanel,0);
        left.add(keyLeft1,2,1);
        left.add(keyLeft2,2,1);
        left.add(keyLeft3,2,1);

        right.add(rightpanel,0);
        right.add(keyRight1,2,1);
        right.add(keyRight2,2,1);
        right.add(keyRight3,2,1);

        this.setJMenuBar(Menubar);
        this.add(top, BorderLayout.NORTH);
        this.add(left, BorderLayout.WEST);
        this.add(right, BorderLayout.EAST);
        this.add(bottom, BorderLayout.SOUTH);
        this.add(center, BorderLayout.CENTER);
        this.repaint();
        this.setVisible(true);
        Sound background = new Sound();
        background.playSound("/sounds/dzwiek_bankomatu.wav");
        background.playBackgroundMusic("/sounds/szum.wav");


    }

    /**
     * Implementacja metod dla klasy <i>ActionListener</i>.
     * @param e <b style="color:#541704;">ActionEvent</b> - Akcja wykonana przez użytkownika np. naciśnięcie przycisku.
     */
    @Override
    public void actionPerformed(ActionEvent e)
    {
        Sound sounds_play = new Sound();
        if(e.getSource()==keyNumber0){
            State.sendSignal(0,operational);
            sounds_play.playSound("/sounds/dzwiek_klikania.wav");
        }
        else if(e.getSource()==keyNumber1){
                State.sendSignal(1, operational);
                sounds_play.playSound("/sounds/dzwiek_klikania.wav");
        }
        else if(e.getSource()==keyNumber2){
            State.sendSignal(2,operational);
            sounds_play.playSound("/sounds/dzwiek_klikania.wav");
        }
        else if(e.getSource()==keyNumber3){
            State.sendSignal(3,operational);
            sounds_play.playSound("/sounds/dzwiek_klikania.wav");
        }
        else if(e.getSource()==keyNumber4){
            State.sendSignal(4,operational);
            sounds_play.playSound("/sounds/dzwiek_klikania.wav");
        }
        else if(e.getSource()==keyNumber5){
            State.sendSignal(5,operational);
            sounds_play.playSound("/sounds/dzwiek_klikania.wav");
        }
        else if(e.getSource()==keyNumber6){
            State.sendSignal(6,operational);
            sounds_play.playSound("/sounds/dzwiek_klikania.wav");
        }
        else if(e.getSource()==keyNumber7){
            State.sendSignal(7,operational);
            sounds_play.playSound("/sounds/dzwiek_klikania.wav");
        }
        else if(e.getSource()==keyNumber8){
            State.sendSignal(8,operational);
            sounds_play.playSound("/sounds/dzwiek_klikania.wav");
        }
        else if(e.getSource()==keyNumber9){
            State.sendSignal(9,operational);
            sounds_play.playSound("/sounds/dzwiek_klikania.wav");
        }
        else if(e.getSource()==keyNumber000) {
            State.sendSignal(10,operational);
            sounds_play.playSound("/sounds/dzwiek_klikania.wav");
        }
        else if(e.getSource()==keyLeft1){
            State.sendSignal(-1,operational);
            sounds_play.playSound("/sounds/dzwiek_klikania.wav");
        }
        else if(e.getSource()==keyLeft2){
            State.sendSignal(-2,operational);
            sounds_play.playSound("/sounds/dzwiek_klikania.wav");
        }
        else if(e.getSource()==keyLeft3){
            State.sendSignal(-3,operational);
            sounds_play.playSound("/sounds/dzwiek_klikania.wav");
        }
        else if(e.getSource()==keyRight1){
            State.sendSignal(-4,operational);
            sounds_play.playSound("/sounds/dzwiek_klikania.wav");
        }
        else if(e.getSource()==keyRight2){
            State.sendSignal(-5,operational);
            sounds_play.playSound("/sounds/dzwiek_klikania.wav");
        }
        else if(e.getSource()==keyRight3){
            State.sendSignal(-6,operational);
            sounds_play.playSound("/sounds/dzwiek_klikania.wav");
        }
        else if(e.getSource()==keyEnter){
            State.sendSignal(-7,operational);
            sounds_play.playSound("/sounds/dzwiek_klikania.wav");
        }
        else if(e.getSource()==keyDelete){
            State.sendSignal(-8,operational);
            sounds_play.playSound("/sounds/dzwiek_klikania.wav");
        }
        else if(e.getSource()==keyClear) {
            State.sendSignal(-9,operational);
            sounds_play.playSound("/sounds/dzwiek_klikania.wav");
        }
        else if(e.getSource()==keyCancel){
            State.sendSignal(-10,operational);
            sounds_play.playSound("/sounds/wysuwanie_karty.wav");
        }
        else if(e.getSource()==keyCardtestonly)
        {
            sounds_play.playSound("/sounds/wsuwanie_karty.wav");
            if(!State.isCardInserted())
            {
                if(State.insertCard() == 0)
                    keyCardtestonly.setEnabled(false);
            }
            else keyCardtestonly.setEnabled(true);

            State.updateVisible();
        }
        else if(e.getSource()==About)
        {
            JOptionPane.showMessageDialog(null,"ATM simulator v"+version,"Info",JOptionPane.INFORMATION_MESSAGE);//Sugeruję tutaj wykorzystać <version> </version> z pliku pom.xml na przykład jak zrobiono https://www.baeldung.com/java-accessing-maven-properties
        }
        else if(e.getSource() == Exit)
        {
            Settings.saveState();
            System.exit(0);
        }
        else if(e.getSource()==SwitchUser)
        {
            if(State.getCurrentState().equals("IDLE"))
            {
                String[] options = new String[Settings.ANumberOfUsers()];
                for(int i = 0;i<Settings.ANumberOfUsers();i++)options[i] = Settings.users.get(i).Name+" "+Settings.users.get(i).Surname+" "+(i+1);
                String s = (String) JOptionPane.showInputDialog(this,"Select user:","User selection",JOptionPane.PLAIN_MESSAGE,null,options,options[Settings.currentUser()]);
                if(s!=null)
                {
                    try{
                        int tmp = Integer.parseInt(s.substring(s.lastIndexOf(" ")+1));
                        Settings.setCurrentUser(tmp - 1);
                        this.setTitle("ATM: "+Settings.users.get(Settings.currentUser()).Name+" "+Settings.users.get(Settings.currentUser()).Surname);
                        State.switchUser(Settings.users.get(Settings.currentUser()));
                        temporary.copy(Settings.users.get(Settings.currentUser()).getWallet());
                    }
                    catch(NullPointerException | NumberFormatException exception)
                    {
                        System.out.println("Exception: "+exception.getMessage());
                    }
                }
            }
            else
            {
                JOptionPane.showMessageDialog(this,"You cannot change the user now","Warning",JOptionPane.WARNING_MESSAGE);
            }
        }
        else if(e.getSource()==SwitchCard)
        {
            if(State.getCurrentState().equals("IDLE"))
            {
                String[] options = new String[Settings.users.get(Settings.currentUser()).getNumberOfCards()];
                for (int i=0;i<Settings.users.get(Settings.currentUser()).getNumberOfCards();i++)options[i] = "Card number "+(i+1);
                String s = (String)JOptionPane.showInputDialog(this,"Select card: ","Card selection",JOptionPane.PLAIN_MESSAGE,null,options,options[Settings.users.get(Settings.currentUser()).getCardIndex()]);
                if(s!=null)
                {
                    try{
                        int tmp = Integer.parseInt(s.substring(s.lastIndexOf(" ")+1));
                        Settings.users.get(Settings.currentUser()).switchCard(tmp - 1);
                    }
                    catch(NullPointerException | NumberFormatException exception)
                    {
                        System.out.println("Exception: "+exception.getMessage());
                    }
                }
            }
            else
            {
                JOptionPane.showMessageDialog(this,"You cannot change the card now","Warning",JOptionPane.WARNING_MESSAGE);
            }
        }

            String [] nominals={"10","20","50","100","200","500"};
            for(int i=0;i<12;i++)
            {
                if(State.getCurrentState().equals("INPUT"))
                {
                    WLabels[i].setVisible(true);
                    WalletOps[i].setVisible(true);
                }
                else
                {
                    WLabels[i].setVisible(false);
                    WalletOps[i].setVisible(false);
                }
                switch (i % 2) {
                    case 0 -> {
                        if (e.getSource() == WalletOps[i] && State.getCurrentState().equals("INPUT"))
                            operational.transfer(nominals[i / 2], temporary);
                        WLabels[i].setText(nominals[i / 2] + " x" + temporary.getAmount(nominals[i / 2]));
                        WLabels[i + 1].setText("x" + operational.getAmount(nominals[i / 2]));
                    }
                    case 1 -> {
                        if (e.getSource() == WalletOps[i] && State.getCurrentState().equals("INPUT"))
                            temporary.transfer(nominals[i / 2], operational);
                        WLabels[i - 1].setText(nominals[i / 2] + " x" + temporary.getAmount(nominals[i / 2]));
                        WLabels[i].setText("x" + operational.getAmount(nominals[i / 2]));
                    }
                }
            }

        if(!State.getCurrentState().equals("INPUT"))
        {
            temporary.copy(Settings.users.get(Settings.currentUser()).getWallet());
            operational.clear();
        }
        keyCardtestonly.setEnabled(!State.isCardInserted());
        if(State.hasChanged())
        {
            System.out.println("State changed from "+State.stateChange()[0]+" to "+State.stateChange()[1]);
        }
        this.repaint();
    }
}


