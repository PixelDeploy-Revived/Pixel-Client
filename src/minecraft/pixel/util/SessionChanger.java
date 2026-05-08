package pixel.util;

import java.util.UUID;

import com.mojang.authlib.Agent;
import com.mojang.authlib.AuthenticationService;
import com.mojang.authlib.UserAuthentication;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;

public class SessionChanger {
	private static SessionChanger instance;
	private final UserAuthentication auth;
	private final Minecraft mc = Minecraft.getMinecraft();
	
	public static SessionChanger getInstance() {
		if (instance == null) {
			instance = new SessionChanger();
		}
		
		return instance;
	}
	
	private SessionChanger() {
		AuthenticationService authService = new YggdrasilAuthenticationService(mc.getProxy(), UUID.randomUUID().toString());
		
		auth = authService.createUserAuthentication(Agent.MINECRAFT);
		
		authService.createMinecraftSessionService();
	}
	
	public void setUserOffline(String username) {
		auth.logOut();
		
		setSession(new Session(username, UUID.randomUUID().toString(), "0", "legacy"));
	}
	
	private void setSession(Session session) {
		mc.session = session;
	}
}
