package Sort;
import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class HeapSort {
    public HeapSort(JFrame frame,ArrayList<Integer> number) {
        SwingUtilities.invokeLater(() -> {
        	frame.setTitle("��������ӻ�");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new BorderLayout());
            frame.setSize(1600, 800);

            HeapSortingPanel panel = new HeapSortingPanel(number);
            frame.add(panel, BorderLayout.CENTER);

            JTextPane codePane = new JTextPane();
            codePane.setEditable(false);
            initCodePane(codePane);
            JScrollPane codeScrollPane = new JScrollPane(codePane);
            codeScrollPane.setPreferredSize(new Dimension(600, 800)); // ���ô��������С
            frame.add(codeScrollPane, BorderLayout.EAST);

            panel.setCodePane(codePane); // ����codePane���������

            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            new Thread(panel::sort).start();
        });
    }

    private void initCodePane(JTextPane codePane) {
        String[] codeLines = {
                "public void sort() {",
                "    int n = numbers.length;",
                "    for (int i = n / 2 - 1; i >= 0; i--) ",
                "        heapify(numbers, n, i);",
                "    for (int i = n - 1; i >= 0; i--) {",
                "        int temp = numbers[0];",
                "        numbers[0] = numbers[i];",
                "        numbers[i] = temp",
                "        heapify(numbers, i, 0);",
                "    }",
                "}",
                "private void heapify(int arr[], int n, int i) {",
                "    int largest = i;",
                "    int l = 2 * i + 1;",
                "    int r = 2 * i + 2;",
                "    if (l < n && arr[l] > arr[largest])",
                "        largest = l;",
                "    if (r < n && arr[r] > arr[largest])",
                "        largest = r;",
                "    if (largest != i) {",
                "        int temp = arr[i];",
                "        arr[i] = largest;",
                "        arr[largest] = temp;",
                "        heapify(arr, n, largest);",
                "    }",
                "}",
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

class HeapSortingPanel extends JPanel implements Runnable {
    private static final long serialVersionUID = 1L;
    private final int[] numbers;
    private int currentBar = -1;
    private int swapBar = -1;
    private JTextPane codePane;
    private SimpleAttributeSet defaultAttr;
    private SimpleAttributeSet highlightAttr;

    public HeapSortingPanel(ArrayList<Integer>number) {
    	if(number.size()==0) {
            numbers = new int[20];
            Random rand = new Random();
            for (int i = 0; i < 20; i++) {
                numbers[i] = rand.nextInt(100) + 1;
            }
    	}
    	else {
    		numbers = number.stream().mapToInt(Integer::intValue).toArray();
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
        int width = getWidth() / numbers.length; // ����ÿ������ͼ�Ŀ��
        for (int i = 0; i < numbers.length; i++) {
            int height = numbers[i] * 5; // ��������ͼ�ĸ߶�
            int x = i * width;
            if (i == currentBar || i == swapBar) {
                g.setColor(new Color(0xFFB6C1)); // �����ڱȽϻ򽻻�������ͼ������ɫ
            } else {
                g.setColor(new Color(0xFF8C00)); // ��������ͼ������ɫ
            }
            g.fillRect(x, getHeight() - height, width - 2, height); // ��������ͼ
            Font font = new Font("Century Gothic", Font.PLAIN, 16);//���������С
            g.setFont(font);
            g.drawString(Integer.toString(numbers[i]), x, getHeight() - height - 5); // ��ÿ������ͼ�Ϸ���������
        }
    }
    //�Ѳ���
    private void heapify(int arr[], int n, int i) throws InterruptedException {
        int largest = i;
        int l = 2 * i + 1;
        int r = 2 * i + 2;
        highlightCodeLine(15);
        repaint();
        Thread.sleep(200);
        if (l < n && arr[l] > arr[largest]) {
            highlightCodeLine(16);
            repaint();
            Thread.sleep(200);
            largest = l;
        }
        highlightCodeLine(17);
        repaint();
        Thread.sleep(200);
        if (r < n && arr[r] > arr[largest]) {
            highlightCodeLine(18);
            repaint();
            Thread.sleep(200);
            largest = r;
        }
        highlightCodeLine(19);
        repaint();
        Thread.sleep(200);
        if (largest != i) {
            highlightCodeLine(20);
            repaint();
            Thread.sleep(200);
            currentBar = i;
            swapBar = largest;
            int swap = arr[i];
            highlightCodeLine(21);
            repaint();
            Thread.sleep(200);
            arr[i] = arr[largest];
            highlightCodeLine(22);
            repaint();
            Thread.sleep(200);
            arr[largest] = swap;
            highlightCodeLine(23);
            repaint();
            Thread.sleep(200);
            heapify(arr, n, largest);
        }
    }
    public void sort() {
        try {
            int n = numbers.length;
            for (int i = n / 2 - 1; i >= 0; i--) {
                currentBar = i;
                swapBar = i;
                highlightCodeLine(3); 
                repaint();
                Thread.sleep(200);
                heapify(numbers, n, i);
            }
            for (int i = n - 1; i >= 0; i--) {
                highlightCodeLine(5);
                repaint();
                Thread.sleep(200);    
                currentBar = i;
                swapBar = 0;
                int temp = numbers[0];
                highlightCodeLine(6);
                repaint();
                Thread.sleep(200); 
                numbers[0] = numbers[i];
                highlightCodeLine(7);
                repaint();
                Thread.sleep(200);
                numbers[i] = temp;
                highlightCodeLine(8);
                repaint();
                Thread.sleep(200);
                heapify(numbers, i, 0);
            }
            currentBar=-1;
            swapBar=-1;
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