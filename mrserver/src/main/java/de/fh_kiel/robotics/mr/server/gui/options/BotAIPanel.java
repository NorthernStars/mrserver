package de.fh_kiel.robotics.mr.server.gui.options;

import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.EventQueue;

import javax.swing.JButton;
import javax.swing.JLabel;

import de.fh_kiel.robotics.mr.server.core.botai.BotAIManagement;
import de.fh_kiel.robotics.mr.server.core.botai.data.BotAI;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class BotAIPanel extends JPanel {
	private BotAI mBotAI;

	public BotAIPanel( BotAI aAI ) {
		
		mBotAI = aAI;
		
		setLayout(new BorderLayout(0, 0));
		
		JButton btnDisconnect = new JButton("Disconnect");
		btnDisconnect.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				
				new Thread( new Runnable() {

					@Override
					public void run() {
		
						BotAIManagement.getInstance().unregisterBotAI(mBotAI);;
						EventQueue.invokeLater(new Runnable() {
							public void run() {

								Container vParent = getParent();
								vParent.remove( ((JButton) e.getSource()).getParent() );
								vParent.getParent().validate();
								vParent.repaint();
							}
						});
					}
				} ).start();
			
			}
		});
		add(btnDisconnect, BorderLayout.EAST);
		
		JLabel lblBot = new JLabel("Bot");
		lblBot.setText( mBotAI.getName() + " " + mBotAI.getRcId() + ":" + mBotAI.getVtId() + " (Team " + mBotAI.getTeam() + ") " + mBotAI.getSocketAddress().toString() );
		add(lblBot, BorderLayout.CENTER);
	}

}
