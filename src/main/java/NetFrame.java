import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Artemy on 16.03.2018.
 */
public class NetFrame extends JFrame{
    JPanel mainPanel = null;
    JPanel taskPanel = null;
    JPanel resultPanel = null;
    JLabel insertIpLabel;
    JLabel warningLabel = null;
    JLabel bitMasAddr = null;
    JPanel firstLine = null;
    JLabel hostAddrResult = null;
    JLabel subNetAddrResult = null;
    JLabel broadcastAddrResult = null;
    JFormattedTextField ipTextField = null;
    JButton calculateButton = null;
    JCheckBox countMaxSubNetsBox = null;
    JCheckBox broadCastAddr = null;
    JFormattedTextField maxSubNetTextfield = null;
    JFormattedTextField subNetTextField;
    JFormattedTextField hostsTextField;
    JFormattedTextField neededSubNetAddr;
    JFormattedTextField neededHostAddr;

    GridBagConstraints c = null;

    public NetFrame(boolean smth) {
        super("Netter");
        setSize(280, 400);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new FlowLayout(FlowLayout.LEFT));
        initMainPanel();
        initGbc();
        initInsertIpLabel();
        initIpTextField();
        initSecondLine();
        initThirdLine();
        initFourthLine();
        initTaskPanel();
        initFifthLine();
        initSixthLine();
        initCalculateButton();
        initResultPanel();
        setVisible(true);
    }

    private void initResultPanel() {
        resultPanel = new JPanel();
        resultPanel.setLayout(new GridBagLayout());
        resultPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Результат:"),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        resultPanel.setPreferredSize(new Dimension(260, 90));
       // resultPanel.setBounds(0, 0, 200, 90);
        JLabel label1 = new JLabel("адрес подсети:  ");
        JLabel label2 = new JLabel("адрес хоста  :");
        JLabel label3 = new JLabel("broadcast:  ");
        JLabel label4 = new JLabel("маска подсети:  ");
        subNetAddrResult = new JLabel();
        subNetTextField.setAlignmentX(JComponent.LEFT_ALIGNMENT);
        hostAddrResult = new JLabel();
        broadcastAddrResult = new JLabel();
        bitMasAddr = new JLabel();
        c.gridx = 0;
        c.gridy = 0;
        resultPanel.add(label1, c);
        c.gridy = 1;
        resultPanel.add(label2, c);
        c.gridy = 2;
        resultPanel.add(label3, c);
        c.gridy = 3;
        resultPanel.add(label4, c);
        c.gridx = 1;
        c.gridy = 0;
        resultPanel.add(subNetAddrResult, c);
        c.gridy = 1;
        resultPanel.add(hostAddrResult, c);
        c.gridy = 2;
        resultPanel.add(broadcastAddrResult, c);
        c.gridy = 3;
        resultPanel.add(bitMasAddr, c);
        this.add(resultPanel);
    }


    private void initCalculateButton() {
        calculateButton = new JButton("вычислить");
        warningLabel = new JLabel();
        warningLabel.setForeground(Color.RED);
        calculateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                warningLabel.setText("");
                String ipAndMask = null;
                try {
                    ipTextField.commitEdit();
                 //   ipTextField.resetKeyboardActions();
                    ipAndMask = ipTextField.getValue().toString();
                    //ipTextField.commitEdit();
                }catch (NullPointerException npe) {
                    warningLabel.setText("Введено некорректное значение IP адреса или маски!");
                    System.out.println("Error");    ///здесь проверка на некорректное згпснгтн
                    return;
                } catch (ParseException e1) {
                    System.out.println("e1 error");
                    e1.printStackTrace();
                }
                ipTextField.revalidate();
                ipTextField.repaint();
                System.out.println(ipAndMask);

                String ip = ipAndMask.replace(" ", "");
                NetAddress netAddress= Netter.checkInputValues(ip);

                if (netAddress == null) {
                    warningLabel.setText("Введите корректное значение IP или маски!");
                } else {
                    String hostsinEachSubNetStr = hostsTextField.getText();
                    int hostsinEachSubNet = Netter.isIntegerValue(hostsinEachSubNetStr);
                    int subNetNumber = 0;
                    if (countMaxSubNetsBox.isSelected()) {
                        if (hostsinEachSubNet == -1) {
                            warningLabel.setText("Введите корректное количество хостов");
                            return;
                        }
                    } else  {
                        String subNetNumberStr = subNetTextField.getText();
                        subNetNumber  = Netter.isIntegerValue(subNetNumberStr);
                        if (subNetNumberStr == null || subNetNumberStr.equals("") || subNetNumber == -1) {
                            warningLabel.setText("Введите верное значение кол-ва подсетей");
                            return;
                        }
                    }
                    String neededHostNumStr = neededHostAddr.getText();
                    int neededHostNum = Netter.isIntegerValue(neededHostNumStr);
                    if (!broadCastAddr.isSelected()) {
                        if (neededHostNum == -1) {
                            warningLabel.setText("Введите корректный номер нужного хоста");
                            return;
                        }
                    }

                    String neededSubNetNumStr = neededSubNetAddr.getText();
                    int neededSubNetNum = Netter.isIntegerValue(neededSubNetNumStr);
                    if (neededSubNetNum == -1) {
                        warningLabel.setText("Введите корректный номер нужной подсети");
                        return;
                    }
                    String originalIP = Netter.getBinaryIpAsString(netAddress.getIpAddress());
                    String originalNetAddr = originalIP.substring(0, netAddress.getMask());
                    int bitsForSubNet;
                    if (countMaxSubNetsBox.isSelected()) {
                        bitsForSubNet =  32 - netAddress.getMask() - Netter.getBitsForSubNet(hostsinEachSubNet + 2); // +2 для broadcast и собственно адресс сети
                    } else {
                        bitsForSubNet = Netter.getBitsForSubNet(subNetNumber);
                    }
                    int newMask = netAddress.getMask() + bitsForSubNet;

                    int subnetAddr = Integer.parseInt(originalIP.substring(netAddress.getMask(), newMask), 2) + neededSubNetNum;
                    String subnetAddrResult = Netter.padRight(originalNetAddr + Netter.padLeft(Integer.toBinaryString(subnetAddr), bitsForSubNet), 32);//адрес новой подсети
                    String broadcastSubNetAddr = Netter.getBroadcastAddr(originalNetAddr + Netter.padLeft(Integer.toBinaryString(subnetAddr), bitsForSubNet), 32);
                    String hostAddr = Netter.padLeft(Netter.getHostAddr(subnetAddrResult, neededHostNum), 32);
                    String result = Netter.getHumanReadableAddr(hostAddr) + "/" + newMask;
                    hostAddrResult.setText(result);
                    broadcastAddrResult.setText(Netter.getHumanReadableAddr(broadcastSubNetAddr) + "/" + newMask);
                    subNetAddrResult.setText(Netter.getHumanReadableAddr(subnetAddrResult) + "/" + newMask);
                    bitMasAddr.setText(Netter.getMask4Octets(newMask));
                }
            }
        });
        this.add(calculateButton);
        this.add(warningLabel);
    }

    private void initIpTextField() {
        AllowBlankMaskFormatter mf= null;
        try {
            mf = new AllowBlankMaskFormatter("###.###.###.###/##", "[0-9 .]{0,15}[\\/][0-9]{0,2}");
           // mf.setMask("###.###.###.###/##");
            //    mf.setMask("AAA.AAA.AAA.AAA/AA");
            //  mf.setValidCharacters("0123456789.");
            mf.setPlaceholderCharacter(' ');
            mf.setValueContainsLiteralCharacters(false);
            mf.setAllowBlankField(true);
        } catch (ParseException e){e.printStackTrace();}
        ipTextField = new JFormattedTextField(mf);
        ipTextField.setFocusLostBehavior(JFormattedTextField.PERSIST);
        c.gridy = 0;
        c.gridx = 1;
        ipTextField.setColumns(10);
        mainPanel.add(ipTextField, c);
    }

    private void initGbc() {
        c = new GridBagConstraints();
        c.anchor = GridBagConstraints.WEST;
    }

    private void initThirdLine() {
        JLabel label1 = new JLabel("В каждой сети ");
        hostsTextField = new JFormattedTextField();
        hostsTextField.setColumns(4);
        hostsTextField.setEnabled(false);
        JLabel label2 = new JLabel(" хостов");
        JPanel panel = new JPanel(new FlowLayout());
        c.gridx = 0;
        c.gridy = 3;
        mainPanel.add(label1, c);
        c.gridx = 1;
        panel.add(hostsTextField);
        panel.add(label2);
        mainPanel.add(panel, c);
    }

    private void initSixthLine(){
        JLabel label1 = new JLabel("хоста ");
        neededHostAddr = new JFormattedTextField();
        neededSubNetAddr = new JFormattedTextField();
        neededSubNetAddr.setColumns(5);
        JLabel label2 = new JLabel("подсети  ");
        JPanel panel1 = new JPanel(new FlowLayout());
        neededHostAddr.setColumns(5);
        panel1.add(neededHostAddr);
        panel1.add(label1);
        panel1.add(neededSubNetAddr);
        panel1.add(label2);
        c.gridx = 0;
        c.gridy = 1;
        taskPanel.add(panel1, c);
    }
    private void initFifthLine() {
        broadCastAddr = new JCheckBox("широковещательный адрес");
        broadCastAddr.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (broadCastAddr.isSelected()) {
                    neededHostAddr.setEnabled(false);
                    neededHostAddr.setText("");
                } else {
                    neededHostAddr.setEnabled(true);
                }
            }
        });
        c.gridx = 0;
        c.gridy = 0;
        taskPanel.add(broadCastAddr, c);
    }

    private void initFourthLine() {
        JLabel label1 = new JLabel("Разбить на ", JLabel.LEFT);
        subNetTextField = new JFormattedTextField();
        subNetTextField.setColumns(4);
        JLabel label2 = new JLabel(" подсетей");
        JPanel panel = new JPanel(new FlowLayout());
        c.gridx = 0;
        c.gridy = 4;
        mainPanel.add(label1, c);
        c.gridx = 1;
        panel.add(subNetTextField);
        panel.add(label2);
        mainPanel.add(panel, c);
    }

    private void initSecondLine() {
        countMaxSubNetsBox = new JCheckBox("Макс.подсетей");
        countMaxSubNetsBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (countMaxSubNetsBox.isSelected()) {
                   // maxSubNetTextfield.setEnabled(true);
                    hostsTextField.setEnabled(true);
                    subNetTextField.setEnabled(false);
                    subNetTextField.setText("");
                } else {
                    hostsTextField.setEnabled(false);
                   // maxSubNetTextfield.setEnabled(false);
                    hostsTextField.setText("");
                  //  maxSubNetTextfield.setText("");
                    subNetTextField.setEnabled(true);
                }
            }
        });
        countMaxSubNetsBox.setHorizontalAlignment(JCheckBox.LEFT);
        countMaxSubNetsBox.setAlignmentX(JCheckBox.LEFT_ALIGNMENT);
        c.gridx = 0;
        c.gridy = 1;
        mainPanel.add(countMaxSubNetsBox, c);
      //  c.gridx = 1;
       // mainPanel.add(maxSubNetTextfield, c);
    }

    private void initMainPanel() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Параметры сети:"),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        this.add(mainPanel);
    }

    private void initTaskPanel() {
        taskPanel = new JPanel();
        taskPanel.setLayout(new GridBagLayout());
        taskPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Требуется найти:"),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        this.add(taskPanel);
    }

    private void initInsertIpLabel() {
        c.gridx = 0;
        c.gridy = 0;
        insertIpLabel = new JLabel("IP-адрес и маска: ");
        mainPanel.add(insertIpLabel, c);
    }
}

 class AllowBlankMaskFormatter extends MaskFormatter {
     private boolean allowBlankField = true;
     private String blankRepresentation = " ";
     String IPADDRESS_PATTERN;

    public AllowBlankMaskFormatter(String pattern) {
        super();
        IPADDRESS_PATTERN = pattern;
    }
    public AllowBlankMaskFormatter(String mask, String pattern) throws ParseException {
        super(mask);
        IPADDRESS_PATTERN = pattern;
    }

    public void setAllowBlankField(boolean allowBlankField) {
        this.allowBlankField = allowBlankField;
    }
    public boolean isAllowBlankField() {
        return allowBlankField;
    }

    @Override public void setMask(String mask) throws ParseException {
        super.setMask(mask);
        updateBlankRepresentation();
    }

    @Override public void setPlaceholderCharacter(char placeholder) {
        super.setPlaceholderCharacter(placeholder);
        updateBlankRepresentation();
    }

    @Override public Object stringToValue(String value) throws ParseException {
     //   String IPADDRESS_PATTERN = "(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)";
      //  IPADDRESS_PATTERN = "[0-9 .]{0,15}[\\/][0-9]{0,2}";
       // String IPADDRESS_PATTERN = "[0-9 \\.\\/]{0,18}";
      //  String pat2 = "[0-9 .]{0,15}[\\/][0-9]{0,2}";
        String pat2 = "^.{0,18}$";
       // Pattern pattern = Pattern.compile(IPADDRESS_PATTERN);
        Pattern pattern = Pattern.compile(pat2);

        Matcher matcher = null;
        if (pattern != null) {
            matcher = pattern.matcher(value);
        }
        if (matcher.matches())
            return value;
        return null;
    }

    private void updateBlankRepresentation() {
        try {
            blankRepresentation = valueToString(" ");
        } catch(ParseException e) {
            blankRepresentation = " ";
        }
    }

}