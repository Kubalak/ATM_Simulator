package manager;
import settings.Settings;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Objects;

/**
 * Klasa odpowiedzialna za główne okno aplikacji do edycji zapisanych danych do bankomatu.
 * Pozwala odczytywać pliki <i style="background:rgba(92,92,92,0.5);border-radius: 0.5em;">&nbsp;settings.xml&nbsp;</i> z dowolnego wybranego katalogu.
 * Umożliwia także stworzenie od zera pliku z danymi.
 * @author Jakub Jach
 * @version 1.0
 * @since 2021-05-10
 */

public class ManagerWindow extends JFrame implements ActionListener, ChangeListener{
    /**
     * Menu pliku.
     */
    JMenu MFile;
    /**
     * Pozycje z menu.
     */
    JMenuItem Open,Save,Exit;
    /**
     * Panel z kartami.
     */
    JTabbedPane Pane;
    /**
     * Panel do edycji ustawień okna aplikacji.
     */
    AppManager App;
    /**
     * Panel do edycji użytkowników.
     */
    UserManager Usr;
    /**
     * Panel do edycji kart.
     */
    CardManager Crd;
    /**
     * Panel do edycji portfela.
     */
    WalletManager Wlt;
    /**
     * Wybieranie folderu do otwarcia.
     */
    JFileChooser chooser;

    /**
     * Jedyny konstruktor klasy.
     * Tworzy okno i wszystkie elementy interfejsu.
     */
    ManagerWindow()
    {
        this.setTitle("Settings manager ");
        this.setIconImage(new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/textures/logo.png"))).getImage());
        this.setSize(400,400);
        this.setResizable(false);
        this.setLocation(Settings.posX,Settings.posY);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if(!Settings.users.isEmpty())
                {
               int result = JOptionPane.showConfirmDialog(null,"Do you want to save your work?","Confirm exit",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
               if(result == JOptionPane.YES_OPTION)
               {
                   if(Settings.path==null) {
                       if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                           Settings.path = chooser.getSelectedFile().getAbsolutePath();
                           Settings.path += Settings.path.contains("\\") ? "\\" : "/";
                           if(Settings.users.isEmpty())
                           {
                               JOptionPane.showMessageDialog(null,"Cannot save empty users!","Save error",JOptionPane.ERROR_MESSAGE);
                               return;
                           }
                           for(int i=0;i<Settings.users.size();i++)
                           {
                               if(Settings.users.get(i).getCards().isEmpty())
                               {
                                   JOptionPane.showMessageDialog(null,"Cannot save users without cards!","Save error",JOptionPane.ERROR_MESSAGE);
                                   return;
                               }
                           }
                           Settings.saveState();
                       }
                   }
                   else {
                       if(Settings.users.isEmpty())
                       {
                           System.exit(0);
                       }
                       for(int i=0;i<Settings.users.size();i++)
                       {
                           if(Settings.users.get(i).getCards().isEmpty())
                           {
                               JOptionPane.showMessageDialog(null,"Cannot save users without cards!","Save error",JOptionPane.ERROR_MESSAGE);
                               return;
                           }
                       }
                       Settings.saveState();
                   }
               }
                }
                System.exit(0);
            }
        });
        chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File("."));
        chooser.setDialogTitle("Folder to open");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        MFile = new JMenu("File");
        Open = new JMenuItem("Load");
        Open.addActionListener(this);
        Save = new JMenuItem("Save");
        Save.addActionListener(this);
        Exit = new JMenuItem("Exit");
        Exit.addActionListener(this);
        JMenuBar tmp = new JMenuBar();
        MFile.add(Open);
        MFile.add(Save);
        MFile.add(Exit);
        tmp.add(MFile);
        this.setJMenuBar(tmp);

        Pane = new JTabbedPane(JTabbedPane.TOP);
        App = new AppManager();
        App.setFocusable(false);
        Usr = new UserManager();
        Usr.setFocusable(false);
        Crd = new CardManager(Usr.selected);
        Crd.setFocusable(false);
        Wlt = new WalletManager(Usr.selected);
        Wlt.setFocusable(false);
        Pane.setFocusable(false);
        Pane.addTab("General",App);
        Pane.addTab("Users",Usr);
        Pane.addTab("Credit cards",Crd);
        Pane.addTab("Wallet",Wlt);
        Pane.addChangeListener(this);
        this.add(Pane);
        this.setVisible(true);
    }

    /**
     * Metoda odpowiedzialna za reagowanie na akcje użytkownika.
     * @param e <b style="color:#541704;">ActionEvent</b> - Akcja wykonana przez użytkownika np. naciśnięcie przycisku.
     */
    @Override
    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource()==Open)
        {
            if(!Settings.users.isEmpty())
            {
              int  result = JOptionPane.showConfirmDialog(this,"Are you sure you wan to proceed?\nAll the unsaved data will be lost!","Continue?",JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
              if(result == JOptionPane.YES_OPTION)Settings.users.clear();
              else return;
            }
            if(chooser.showOpenDialog(this)==JFileChooser.APPROVE_OPTION)
            {
                Settings.path = chooser.getSelectedFile().getAbsolutePath();
                Settings.path+=Settings.path.contains("\\")?"\\":"/";
                boolean isOK = Settings.loadSettings();
                if(!isOK)Settings.path = null;
                this.setTitle("Settings manager: Open "+(isOK?"Success":"Failed"));
                App.updateFields();
                Usr.updateFields();
                Wlt.updateFields();
                Crd.updateFields();
            }
        }
        else if(e.getSource() == Save)
        {
            if(Settings.path==null) {
                if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                    Settings.path = chooser.getSelectedFile().getAbsolutePath();
                    Settings.path += Settings.path.contains("\\") ? "\\" : "/";
                    if(Settings.users.isEmpty())
                    {
                        JOptionPane.showMessageDialog(this,"Cannot save empty users!","Save error",JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    for(int i=0;i<Settings.users.size();i++)
                    {
                        if(Settings.users.get(i).getCards().isEmpty())
                        {
                            JOptionPane.showMessageDialog(this,"Cannot save users without cards!","Save error",JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }
                    Settings.saveState();
                    this.setTitle("Settings manager: Save");
                }
            }
            else {
                if(Settings.users.isEmpty())
                {
                    JOptionPane.showMessageDialog(this,"Cannot save empty users  cards!","Save error",JOptionPane.ERROR_MESSAGE);
                    return;
                }
                for(int i=0;i<Settings.users.size();i++)
                {
                    if(Settings.users.get(i).getCards().isEmpty())
                    {
                        JOptionPane.showMessageDialog(this,"Cannot save users without cards!","Save error",JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
                Settings.saveState();
                this.setTitle("Settings manager: Save");
            }
        }
        else if(e.getSource() == Exit)
        {
            int result = JOptionPane.showConfirmDialog(this,"Do you want to save your work?\nAll the unsaved work will be lost!","Confirm exit",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
            if(result == JOptionPane.YES_OPTION)
            {
                Settings.saveState();
            }
            System.exit(0);
        }
    }

    /**
     * Metoda odpowiedzialna za akcję przy przełączeniu karty.
     * @param e <b style="color:#541704;">ChangeEvent</b> - Wydarzenie przełączania karty.
     */
    @Override
    public void stateChanged(ChangeEvent e)
    {
        if(Pane.getSelectedComponent() == App)App.updateFields();
        else if(Pane.getSelectedComponent() == Usr)Usr.updateFields();
        else if(Pane.getSelectedComponent() == Crd)Crd.updateFields();
        else Wlt.updateFields();
    }
}
