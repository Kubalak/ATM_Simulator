package manager;
import settings.Settings;
import user.Wallet;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Klasa odpowiedzialna za operację na portfelu użytkownika wybranego do edycji w karcie <i>Users</i>.
 * @author Jakub Jach
 * @version 1.0
 * @since 2021-05-12
 */

public class WalletManager extends JPanel implements ActionListener,Manager{
    /**
     * Pola tekstowe dla różnych nominałów.
     */
    private JTextField[] banknotes;
    /**
     * Lista rozwijana do odczytywania użytkownika, którego portfel edytujemy.
     */
    private JComboBox<Integer> userIndex;
    /**
     * Przyciski do zatwierdzenia i resetowania ustawień portfela.
     */
    private JButton apply,clear;
    /**
     * Nominały banknotów używanych w bankomacie.
     */
    private final String[] values = {"10","20","50","100","200","500"};

    /**
     * Wewnętrzna klasa do edycji zawartości portfela.
     */
    private static class SpecialWallet extends Wallet
    {
        /**
         * Konstruktor odziedziczony z klasy <b style="color:#541704;">Wallet</b>.
         * @param wallet <b style="color:#541704;">Wallet</b> - Portfel, na którego podstawie tworzymy obiekt.
         */
        public SpecialWallet(Wallet wallet)
        {
            super(wallet);
        }

        /**
         * Metoda służąca do wpisywania ilości banknotów do portfela.
         * @param what <b style="color:#0B5E03;">String</b> - Nominał do wpisania
         * @param amount <b style="color:#B45700;">int</b> - Ilość do wpisania.
         */
        public void adminSet(String what, int amount)
        {
            super.adminSet(what,amount);
        }
    }

    /**
     * Jedyny konstruktor klasy.
     * @param comboBox <b style="color:#541704;">JComboBox</b> - Lista rozwiajana do odczytania bieżącego użytkownika.
     */
    WalletManager(JComboBox<Integer> comboBox)
    {
        userIndex = comboBox;
        this.setLayout(null);
        apply = new JButton("Apply");
        clear = new JButton("Clear");
        banknotes = new JTextField[6];
        JLabel[] banknoteL = new JLabel[6];
        for(int i=0;i<6;i++)
        {
            banknotes[i] = new JTextField();
            banknoteL[i] = new JLabel(values[i]+":");
            banknotes[i].setEnabled(!Settings.users.isEmpty());
            banknoteL[i].setBounds(85,10+i*45,75,20);
            banknoteL[i].setHorizontalAlignment(SwingConstants.RIGHT);
            banknoteL[i].setHorizontalTextPosition(SwingConstants.RIGHT);
            banknotes[i].setBounds(banknoteL[i].getX()+banknoteL[i].getWidth()+5, banknoteL[i].getY(),100,20);
            this.add(banknoteL[i]);
            this.add(banknotes[i]);
        }
        apply.setFocusable(false);
        apply.setBounds(banknoteL[5].getX() + 30,banknoteL[5].getY()+banknoteL[5].getHeight()+10,80,30);
        apply.setEnabled(!Settings.users.isEmpty());
        clear.setFocusable(false);
        clear.setBounds(apply.getX()+apply.getWidth()+5,banknoteL[5].getY()+banknoteL[5].getHeight()+10,80,30);
        clear.setEnabled(!Settings.users.isEmpty());
        apply.addActionListener(this);
        clear.addActionListener(this);
        this.add(apply);
        this.add(clear);
    }
    /**
     * Metoda służąca do odświeżania pól w danym obiekcie implementującym interfejs.
     */
    @Override
    public void updateFields() {
        boolean enable = !Settings.users.isEmpty();

           for(int i=0;i<6;i++)
            {
                banknotes[i].setEnabled(enable);
                banknotes[i].setText(enable?String.valueOf(Settings.users.get(userIndex.getSelectedIndex()).getWallet().getAmount(values[i])):"");
            }
            apply.setEnabled(enable);
            clear.setEnabled(enable);
    }
    /**
     * Metoda odpowiedzialna za reagowanie na akcje użytkownika.
     * @param e <b style="color:#541704;">ActionEvent</b> - Akcja wykonana przez użytkownika np. naciśnięcie przycisku.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == apply)
        {
            int[] tests = {0,0,0,0,0,0};
            try {
                for (int i = 0; i < 6; i++) {
                    tests[i] = Integer.parseInt(banknotes[i].getText());
                }
                SpecialWallet wallet = new SpecialWallet(Settings.users.get(userIndex.getSelectedIndex()).getWallet());
                for(int i = 0;i< 6;i++)
                    wallet.adminSet(values[i],tests[i]);
                Settings.users.get(userIndex.getSelectedIndex()).setWallet(wallet);
            }catch(Exception exception)
            {
                JOptionPane.showMessageDialog(this,"Invalid arguments!\nException: "+exception.getMessage(),"Input error",JOptionPane.ERROR_MESSAGE);
            }
        }
        else
        {
            for(int i=0;i<6;i++)
            {
                banknotes[i].setText("");
            }
        }
    }
}
