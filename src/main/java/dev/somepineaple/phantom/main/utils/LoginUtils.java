package dev.somepineaple.phantom.main.utils;

import java.net.Proxy;

import com.mojang.authlib.Agent;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;

import dev.somepineaple.phantom.mixins.IMinecraft;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;

public class LoginUtils {
	public static String login(String email, String password) {
		YggdrasilUserAuthentication auth = (YggdrasilUserAuthentication) (new YggdrasilAuthenticationService(Proxy.NO_PROXY, "")).createUserAuthentication(Agent.MINECRAFT);
		auth.setUsername(email);
		auth.setPassword(password);
		try {
			auth.logIn();
			IMinecraft mc = (IMinecraft) Minecraft.getMinecraft();
			mc.setSession(new Session(
					auth.getSelectedProfile().getName(),
					auth.getSelectedProfile().getId().toString(),
					auth.getAuthenticatedToken(), "mojang"
			));
		} catch (AuthenticationUnavailableException e) {
			return "Cannot contact auth server!";
		} catch (AuthenticationException e) {
			e.printStackTrace();
			
			if (e.getMessage().contains("Invalid username or password.") || e.getMessage().toLowerCase().contains("account migrated"))
				return "Wrong username or password";
		} catch (NullPointerException e) {
			return "Wrong username or password";
		}
		
		return "";
	}
	
	public static void changeCrackedName(String newName) {
		IMinecraft minecraft = (IMinecraft) Minecraft.getMinecraft();
		minecraft.setSession(new Session(newName, "", "", "mojang"));
	}
}
