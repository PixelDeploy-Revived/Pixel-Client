package pixel;

import net.arikia.dev.drpc.DiscordEventHandlers;
import net.arikia.dev.drpc.DiscordRPC;
import net.arikia.dev.drpc.DiscordRichPresence;
import net.arikia.dev.drpc.DiscordUser;
import net.arikia.dev.drpc.callbacks.ReadyCallback;

public class Discord {
	private boolean running = true;
	private long createdAt = 0;
	
	public void start() {
		this.createdAt = System.currentTimeMillis();
		
		ReadyCallback readyEventHandler = new ReadyCallback() {
			@Override
			public void apply(DiscordUser user) {
				update("In Splash Screen", "Waiting");
			}
		};
		DiscordEventHandlers handlers = new DiscordEventHandlers.Builder().setReadyEventHandler(readyEventHandler).build();
		
		DiscordRPC.discordInitialize("1460184255354110017", handlers, true);
		
		new Thread("Discord RPC") {
			@Override
			public void run() {
				while (running) {
					DiscordRPC.discordRunCallbacks();
				}
			}
		}.start();
	}
	
	public void shutdown() {
		running = false;
		
		DiscordRPC.discordShutdown();
	}
	
	public void update(String details, String state) {
		DiscordRichPresence.Builder richPresenceBuilder = new DiscordRichPresence.Builder(state);
		
		richPresenceBuilder.setBigImage("large", "large");
		richPresenceBuilder.setDetails(details);
		richPresenceBuilder.setStartTimestamps(createdAt);
		
		DiscordRPC.discordUpdatePresence(richPresenceBuilder.build());
	}
}
