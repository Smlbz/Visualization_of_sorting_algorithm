package Sort;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.util.Random;

public class BubbleSort {
    public BubbleSort(JFrame frame) {
        SwingUtilities.invokeLater(() -> {
        	frame.setTitle("冒泡排序可视化");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new BorderLayout());
            frame.setSize(1600, 800);

            BubbleSortingPanel panel = new BubbleSortingPanel();
            frame.add(panel, BorderLayout.CENTER);

            JTextPane codePane = new JTextPane();
            codePane.setEditable(false);
            initCodePane(codePane);
            JScrollPane codeScrollPane = new JScrollPane(codePane);
            codeScrollPane.setPreferredSize(new Dimension(600, 800)); // 设置代码区域大小
            frame.add(codeScrollPane, BorderLayout.EAST);

            panel.setCodePane(codePane); // 传递codePane到排序面板

            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            new Thread(panel::sort).start();
        });
    }

    private void initCodePane(JTextPane codePane) {
        String[] codeLines = {
            "public void sort() {",
            "    int n = numbers.length;",
            "    for (int i = 0; i < n - 1; i++) {",
            "        for (int j = 0; j < n - i - 1; j++) {",
            "            if (numbers[j] > numbers[j + 1]) {",
            "                int temp = numbers[j];",
            "                numbers[j] = numbers[j + 1];",
            "                numbers[j + 1] = temp;",
            "            }",
            "        }",
            "    }",
            "}"
        };
        StyledDocument doc = codePane.getStyledDocument();
        SimpleAttributeSet attr = new SimpleAttributeSet();
        StyleConstants.setFontSize(attr,22);
        try {
            for (String line : codeLines) {
                doc.insertString(doc.getLength(), line + "\n", attr);
            }
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }
}

class BubbleSortingPanel extends JPanel implements Runnable {
    private static final long serialVersionUID = 1L;
    private final int[] numbers;
    private int currentBar = -1;
    private int swapBar = -1;
    private JTextPane codePane;
    private SimpleAttributeSet defaultAttr;
    private SimpleAttributeSet highlightAttr;

    public BubbleSortingPanel() {
        numbers = new int[20];
        Random rand = new Random();
        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = rand.nextInt(100) + 1;
        }
        initStyles();
    }

    public void setCodePane(JTextPane codePane) {
        this.codePane = codePane;
    }

    private void initStyles() {
        defaultAttr = new SimpleAttributeSet();
        StyleConstants.setBackground(defaultAttr, Color.WHITE);

        highlightAttr = new SimpleAttributeSet();
        StyleConstants.setBackground(highlightAttr, new Color(255, 255, 0));
    }

    private void highlightCodeLine(int lineNumber) {
        if (codePane == null) return;
        StyledDocument doc = codePane.getStyledDocument();
        if(lineNumber==-1) {
        	doc.setCharacterAttributes(0, doc.getLength(), defaultAttr, false);
        	return;
        }
        Element root = doc.getDefaultRootElement();
        Element lineElem = root.getElement(lineNumber);
        doc.setCharacterAttributes(0, doc.getLength(), defaultAttr, false);
        if (lineElem != null) {
            doc.setCharacterAttributes(lineElem.getStartOffset(), lineElem.getEndOffset() - lineElem.getStartOffset(), highlightAttr, false);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int width = getWidth() / numbers.length; // 计算每个条形图的宽度
        for (int i = 0; i < numbers.length; i++) {
            int height = numbers[i] * 5; // 设置条形图的高度
            int x = i * width;
            if (i == currentBar || i == swapBar) {
                g.setColor(new Color(0xFFB6C1)); // 将正在比较或交换的条形图设置颜色
            } else {
                g.setColor(new Color(0xFF8C00)); // 其他条形图设置颜色
            }
            g.fillRect(x, getHeight() - height, width - 2, height); // 绘制条形图
            g.drawString(Integer.toString(numbers[i]), x, getHeight() - height - 5); // 在每个条形图上方绘制数字
        }
    }

    public void sort() {
        try {
            int n = numbers.length;
            for (int i = 0; i < n - 1; i++) {
                for (int j = 0; j < n - i - 1; j++) {
                    currentBar = j;
                    highlightCodeLine(4); // 执行if 语句
                    repaint();
                    Thread.sleep(200);
                    if (numbers[j] > numbers[j + 1]) {
                        swapBar = j + 1;
                        highlightCodeLine(5);
                        repaint();
                        Thread.sleep(200);
                        highlightCodeLine(6); 
                        repaint();
                        Thread.sleep(200);
                        highlightCodeLine(7); 
                        repaint();
                        Thread.sleep(200);
                        //交换操作
                        int temp = numbers[j];
                        numbers[j] = numbers[j + 1];
                        numbers[j + 1] = temp;
                    }
                    swapBar = -1;
                    repaint();
                }
            }
            currentBar=-1;
            highlightCodeLine(-1);
            while(true) {
            	repaint();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        sort();
    }
}