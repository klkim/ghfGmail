
package main;


import org.jivesoftware.smack.packet.IQ;

public class NewMail extends IQ {
	@Override
	public String getChildElementXML() {
		return "<new-mail xmlns='google:mail:notify'/>";
	}
}
