import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.awt.event.ActionEvent;
import java.awt.Font;
import javax.swing.border.LineBorder;
import javax.swing.JButton;

public class Frame extends JFrame implements ActionListener {

	private JPanel contentPane;
	private JTextField textField;
	private JScrollPane scrollPane;

	private JTextPane textPane = new JTextPane();
	Queue<String> processQueue = Collections.asLifoQueue(new ArrayDeque<String>());

	StyleContext context = new StyleContext();
	StyledDocument document = new DefaultStyledDocument(context);

	StyledDocument doc = textPane.getStyledDocument();
	SimpleAttributeSet left = new SimpleAttributeSet();
	SimpleAttributeSet right = new SimpleAttributeSet();

	String response;
	private boolean progress = false;
	private boolean IsReponseRecived = false;
	String botName = "Bot :";
	String user = "Vasan :";
	String orderStatus = "";
	Map<String, String> slots = new LinkedHashMap<>();
	List<String> bot = new ArrayList<String>();
	List<String> utterances = new ArrayList<String>();

	private final ExecutorService threadpool = Executors.newFixedThreadPool(2);
	private JButton btnNewButton;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Frame frame = new Frame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Frame() {
		setBackground(new Color(255, 255, 255));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 653, 304);
		contentPane = new JPanel();
		contentPane.setForeground(new Color(0, 0, 0));
		contentPane.setBorder(new LineBorder(new Color(0, 0, 139), 2));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		StyleConstants.setAlignment(left, StyleConstants.ALIGN_LEFT);
		StyleConstants.setForeground(left, Color.RED);
		StyleConstants.setFontSize(left, 12);
		StyleConstants.setSpaceAbove(left, 2);
		StyleConstants.setSpaceBelow(left, 2);
		StyleConstants.setLeftIndent(left, 8);
		StyleConstants.setBold(left, true);
		StyleConstants.setItalic(left, true);

		StyleConstants.setAlignment(right, StyleConstants.ALIGN_LEFT);
		StyleConstants.setForeground(right, Color.BLUE);
		StyleConstants.setFontSize(right, 12);
		StyleConstants.setSpaceAbove(right, 2);
		StyleConstants.setSpaceBelow(right, 2);
		StyleConstants.setRightIndent(right, 8);
		StyleConstants.setBold(right, true);
		StyleConstants.setItalic(right, true);

		textField = new JTextField() {

			public void addNotify() {
				super.addNotify();
				requestFocus();
			}
		};
		textField.setFont(new Font("Open Sans", Font.PLAIN, 13));
		textField.setBackground(new Color(255, 255, 224));

		textField.addActionListener(this);
		contentPane.add(textField, BorderLayout.SOUTH);
		textField.setColumns(10);

		scrollPane = new JScrollPane();
		contentPane.add(scrollPane, BorderLayout.CENTER);
		textPane.setBorder(new LineBorder(new Color(0, 0, 128), 1, true));
		textPane.setEditable(false);
		textPane.setFont(new Font("Open Sans", Font.PLAIN, 13));
		textPane.setBackground(new Color(248, 248, 255));
		DefaultCaret caret = (DefaultCaret) textPane.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

		scrollPane.setViewportView(textPane);

		btnNewButton = new JButton("Speak");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

			}
		});
		scrollPane.setColumnHeaderView(btnNewButton);

		int decider = (int) (Math.random() * 3 + 1);
		if (decider == 1) {

			try {
				doc.insertString(doc.getLength(), botName + "May I Help you \n", right);
				doc.setParagraphAttributes(doc.getLength(), 1, right, true);
			} catch (BadLocationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (decider == 2) {
			try {
				doc.insertString(doc.getLength(), botName + "Can I Help you \n", right);
				doc.setParagraphAttributes(doc.getLength(), 1, right, true);
			} catch (BadLocationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (decider == 3) {
			try {
				doc.insertString(doc.getLength(), botName + "What can I do for you \n", right);
				doc.setParagraphAttributes(doc.getLength(), 1, right, true);
			} catch (BadLocationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	public void processFulfilement() throws InterruptedException {
		Validate val = new Validate();
		Future future = threadpool.submit(val);

		while (!future.isDone()) {
			try {
				doc.insertString(doc.getLength(), botName + "Your order has complete\n", right);
			} catch (BadLocationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			doc.setParagraphAttributes(doc.getLength(), 1, right, true);

		}
	}

	public void loadXmls(String tagName) {

		System.out.println("slots beofre :" + slots.size());

		slots.entrySet().removeIf(entry -> entry.getKey().length() == 0);
		System.out.println("slots after :" + slots.size());
		System.out.println("utterances beofre :" + utterances.size());
		utterances.removeAll(utterances);
		System.out.println("utterances after :" + utterances.size());

		File file = new File("/Downloads/myProject/template.xml");

		if (file.exists()) {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			try {

				DocumentBuilder builder = factory.newDocumentBuilder();
				Document document = builder.parse(file);
				Element documentElement = document.getDocumentElement();

				XPathFactory xPathfactory = XPathFactory.newInstance();
				XPath xpath = xPathfactory.newXPath();
				XPathExpression expr = xpath.compile("//bot[@name=\"" + tagName + "\"]");
				NodeList sList = (NodeList) expr.evaluate(document, XPathConstants.NODESET);

				if (sList != null && sList.getLength() > 0) {
					for (int i = 0; i < sList.getLength(); i++) {
						Node node = sList.item(i);
						if (node.getNodeType() == Node.ELEMENT_NODE) {
							Element e = (Element) node;

							NodeList utterancesList = e.getElementsByTagName("utterances");

							if (utterancesList != null && utterancesList.getLength() > 0) {

								for (int j = 0; j < utterancesList.getLength(); j++) {
									try {
										utterances.add(utterancesList.item(j).getChildNodes().item(0).getNodeValue());
										System.out
												.println(utterancesList.item(j).getChildNodes().item(0).getNodeValue());
									} catch (Exception ex) {
										System.out.println("exception occured" + e);
									}
									;
								}

							}

							NodeList slotList = e.getElementsByTagName("slots");

							if (slotList != null && slotList.getLength() > 0) {

								for (int j = 0; j < slotList.getLength(); j++) {
									try {
										String key = slotList.item(j).getAttributes().getNamedItem("name")
												.getNodeValue();
										String value = slotList.item(j).getChildNodes().item(0).getNodeValue();
										slots.put(key, value);

									} catch (Exception ex) {
										System.out.println("exception occured" + e);
									}
								}
							}
						}
					}
				}
			} catch (Exception e) {
				System.out.println("exception occured" + e);
			}
		} else {
			System.out.println("File not exists");
		}

	}

	public boolean validationHook(String validationType, String validation) {
		boolean success = false;
		switch (validationType) {
		case "environments": {
			// if (validationType.equalsIgnoreCase("environments")) {
			if (match(validation, "*" + "dev" + "*")) {

				// if (validation.equalsIgnoreCase("dev")) {
				success = true;
			} else {
				if (IsReponseRecived) {
					try {
						StyledDocument doc = textPane.getStyledDocument();
						doc.insertString(doc.getLength(), botName + "I did not get that \n", right);
						doc.setParagraphAttributes(doc.getLength(), 1, right, true);
					} catch (BadLocationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					// textArea.append(botName + "Please enter valid
					// environment" + "\n");
					IsReponseRecived = false;
				}
			}
		}
			;
			break;
		case "server": {
			// if (validationType.equalsIgnoreCase("environments")) {
			if (match(validation, "*" + "rwxd" + "*")) {
				success = true;
			} else {
				if (IsReponseRecived) {
					try {
						StyledDocument doc = textPane.getStyledDocument();
						doc.insertString(doc.getLength(), botName + "Please provide valid OTIS Server \n", right);
						doc.setParagraphAttributes(doc.getLength(), 1, right, true);
					} catch (BadLocationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					IsReponseRecived = false;
				}
			}
		}
			;
			break;
		case "confirmation": {
			if (validationType.equalsIgnoreCase("no")) {
				success = false;
				orderStatus = "cancelled";
			}
		}
		default: {
			success = true;
			break;
		}
		}
		if (success) {

			// System.out.println("This is the entered input " + validationType
			// + " :" + validation);
			String token = validationType + ":" + validation;
			orderStatus = orderStatus + "\n\t" + token;
		}
		return success;
	}

	class Validate implements Callable {

		public Validate() {			
			StyledDocument doc = textPane.getStyledDocument();
			try {
				doc.insertString(doc.getLength(), botName + "Your order is in progress\n", right);
			} catch (BadLocationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			doc.setParagraphAttributes(doc.getLength(), 1, right, true);
			
			try {

				// Process your call here
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//System.out.println("Complete");
		
			// return true;
		}

		public Future submit(Validate val) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Object call() throws Exception {
			// TODO Auto-generated method stub
			return null;
		}

	}

	public boolean match(String text, String pattern) {
		return text.matches(pattern.replace("?", ".?").replace("*", ".*?"));
	}

	public void botSay(String inputString) throws InterruptedException {
		/*
		 * Validate val = new Validate(); Future future =
		 * threadpool.submit(val);
		 * 
		 * while (!future.isDone()) { // System.out.println(
		 * "Validation task is in progress."); Thread.sleep(1); // sleep for 1
		 * millisecond before checking again
		 * 
		 * }
		 */

		if (slots.size() > 0) {
			slots.forEach((key, value) -> {
				String tempKey = key;

				// textArea.append(botName + slots.get(key) + "\n");
				try {
					StyledDocument doc = textPane.getStyledDocument();

					doc.insertString(doc.getLength(), botName + slots.get(key) + " \n", left);
					doc.setParagraphAttributes(doc.getLength(), 1, left, false);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				textField.setText("");
				response = "";

				while ((response.length() <= 0) || (!validationHook(tempKey, response))) {

					try {
						Thread.sleep(1);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					// System.out.println("Waiting..");
				}
			});
			progress = false;
		} else {
			// textArea.append(botName+ inputString + "\n");
			try {
				StyledDocument doc = textPane.getStyledDocument();

				doc.insertString(doc.getLength(), botName + inputString + " \n", right);
				doc.setParagraphAttributes(doc.getLength(), 1, right, false);
			} catch (BadLocationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		// Process fulfillment request here..
		// Pattern pattern = Pattern.compile(Pattern.quote("|"));
		// String[] orderConfirmation =
		// Pattern.compile(Pattern.quote("|")).split(orderStatus);
		// textArea.append("Bot:" + Arrays.toString(orderConfirmation));
		// textArea.append(botName + orderStatus);

		try {
			StyledDocument doc = textPane.getStyledDocument();

			doc.insertString(doc.getLength(), botName + orderStatus + " \n", right);
			doc.setParagraphAttributes(doc.getLength(), 1, right, false);
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("This request process can begin here");

		orderStatus = "";
		
		processFulfilement();

	}

	public String getSession() {
		String uuid = UUID.randomUUID().toString();
		return uuid;
	}

	public void actionPerformed(ActionEvent e) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				String uText = textField.getText();
				response = uText;

				try {
					StyledDocument doc = textPane.getStyledDocument();

					doc.insertString(doc.getLength(), user + uText + " \n", left);
					doc.setParagraphAttributes(doc.getLength(), 1, left, true);
				} catch (BadLocationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				IsReponseRecived = true;
				if (uText.contains("service") && (!progress)) {
					try {
						progress = true;
						loadXmls("service");
						orderStatus = "\n\tOrder: Service Down";
						botSay(uText);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} else {
					if (uText.contains("patch") && (!progress)) {
						try {
							progress = true;
							loadXmls("patch");
							orderStatus = "\n\tOrder: Patch Deployment";

							botSay(uText);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				// System.out.println(uText);
				textField.setText("");
			}
		}).start();
	}

}
