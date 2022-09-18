package gui;

import java.awt.EventQueue;
import java.awt.Font;
import java.util.Properties;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;

import org.apache.log4j.BasicConfigurator;

public class GuiReceiver extends JFrame {

	private JPanel contentPane;
	private JTextPane textPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GuiReceiver frame = new GuiReceiver();
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
	public GuiReceiver() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 775, 530);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Nh\u1EADn \u0111\u01B0\u1EE3c");
		lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD, 30));
		lblNewLabel.setBounds(132, 10, 157, 52);
		contentPane.add(lblNewLabel);
		
		textPane = new JTextPane();
		textPane.setBounds(20, 61, 694, 422);
		contentPane.add(textPane);
		try {
			Receiver();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void Receiver()throws Exception {

				BasicConfigurator.configure();
	
				Properties settings = new Properties();
				settings.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
				settings.setProperty(Context.PROVIDER_URL, "tcp://localhost:61616");

				Context ctx = new InitialContext(settings);
		
				Object obj = ctx.lookup("ConnectionFactory");
				ConnectionFactory factory = (ConnectionFactory) obj;
		
				Destination destination = (Destination) ctx.lookup("dynamicQueues/phuong");
	
				Connection con = factory.createConnection("admin", "admin");
	
	
				Session session = con.createSession(/* transaction */false, /* ACK */Session.CLIENT_ACKNOWLEDGE);
		
				MessageConsumer receiver = session.createConsumer(destination);
		
				System.out.println("Tý was listened on queue...");
				receiver.setMessageListener(new MessageListener() {

		
					public void onMessage(Message msg) {
						try {
							if (msg instanceof TextMessage) {
								TextMessage tm = (TextMessage) msg;
								String txt = tm.getText();
								System.out.println("Nhận được " + txt);
								textPane.setText(txt);
								msg.acknowledge();
							} else if (msg instanceof ObjectMessage) {
								ObjectMessage om = (ObjectMessage) msg;
								
								System.out.println(om);
							}
		
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
	}

