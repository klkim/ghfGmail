
package main;


import java.text.SimpleDateFormat;
import java.util.Date;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smackx.ServiceDiscoveryManager;
import org.jivesoftware.smackx.packet.DiscoverInfo;

import pusher.PusherRequest;

public class Main extends Thread {
	static {
		ProviderManager providerManager = ProviderManager.getInstance();
		providerManager.addIQProvider("new-mail", "google:mail:notify", NewMail.class);		
	}	
	
	PusherRequest pusher;
	public Main(String channel, String event) {
		pusher = new PusherRequest(channel, event);
	}
	
	@Override
	public void run() {
		SASLAuthentication.supportSASLMechanism("PLAIN");
		ConnectionConfiguration config = new ConnectionConfiguration("talk.google.com", 80, "gmail.com");
		final XMPPConnection conn = new XMPPConnection(config);
		DiscoverInfo discoInfo = null;

		try {
			conn.connect();
			conn.login(Credential.ID, Credential.PW);			
			ServiceDiscoveryManager discoManager = ServiceDiscoveryManager.getInstanceFor(conn);
			discoInfo = discoManager.discoverInfo("gmail.com");
		} catch (XMPPException e) {
			e.printStackTrace();
		}

//		System.out.println(discoInfo.toXML());

		conn.addPacketListener(new PacketListener() {
			@Override
			public void processPacket(Packet packet) {

				pusher.triggerPush("{\"message\": \"You've Got the Mail!\"}");
								
				System.out.println("----------------------");
				System.out.println("YOU'VE GOT THE MAIL!");
				System.out.println(new SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(new Date().getTime()));
				System.out.println("----------------------");
//				System.out.println(packet.toXML());

				IQ iq = new IQ() {
					public String getChildElementXML() {
						return "";
					}
				};
				iq.setType(IQ.Type.RESULT);
				iq.setPacketID(packet.getPacketID());
				// System.out.println(iq.toXML());
				conn.sendPacket(iq);
				conn.sendPacket(new QueryNotify());
			}
		}, new PacketTypeFilter(NewMail.class));

		IQ iq = new IQ() {
			public String getChildElementXML() {
				return "<usersetting xmlns='google:setting'><mailnotifications value='true' /></usersetting>";
			}
		};
		iq.setType(IQ.Type.SET);
		// System.out.println(iq.toXML());
		conn.sendPacket(iq);

		IQ iq1 = new IQ() {
			public String getChildElementXML() {
				return "<query xmlns='google:mail:notify' />";
			}
		};
		iq1.setType(IQ.Type.GET);
		// System.out.println(iq1.toXML());
		conn.sendPacket(iq1);
		
		// conn.disconnect();
	}
}
