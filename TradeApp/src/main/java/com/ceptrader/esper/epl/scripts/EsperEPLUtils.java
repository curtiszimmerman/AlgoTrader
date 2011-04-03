package com.ceptrader.esper.epl.scripts;

import java.net.URL;

import com.ceptrader.esper.CEPMan;
import com.ceptrader.esper.epl.EPLDebug;
import com.espertech.esper.client.deploy.DeploymentOptions;
import com.espertech.esper.client.deploy.DeploymentResult;
import com.espertech.esper.client.deploy.EPDeploymentAdmin;
import com.espertech.esper.client.deploy.Module;

public class EsperEPLUtils {
	public static void debug(final String[] stmLst) {
		new EPLDebug(stmLst);
	}
	
	public static void debugAll() {
		new EPLDebug();
	}
	
	public DeploymentResult validate(final String url) {
		try {
			final EPDeploymentAdmin deployAdmin = CEPMan.getCEPMan()
			        .getEpService()
			        .getEPAdministrator()
			        .getDeploymentAdmin();
			final Module module = deployAdmin
			        .read(url);
			
			final DeploymentOptions options = new DeploymentOptions();
			options.setIsolatedServiceProvider("validate");
			options.setValidateOnly(true);
			options.setFailFast(false);
			options.setCompile(true);
			
			final DeploymentResult dr = deployAdmin.deploy(module, options);
			return dr;
		} catch (final Throwable t) {
			throw new IllegalStateException("Deployment error", t);
		}
	}
	
	public DeploymentResult validate(final URL url) {
		try {
			final EPDeploymentAdmin deployAdmin = CEPMan.getCEPMan()
			        .getEpService()
			        .getEPAdministrator()
			        .getDeploymentAdmin();
			final Module module = deployAdmin
			        .read(url);
			
			final DeploymentOptions options = new DeploymentOptions();
			options.setIsolatedServiceProvider("validate");
			options.setValidateOnly(true);
			options.setFailFast(false);
			options.setCompile(true);
			
			final DeploymentResult dr = deployAdmin.deploy(module, options);
			return dr;
		} catch (final Throwable t) {
			throw new IllegalStateException("Deployment error", t);
		}
	}
}
