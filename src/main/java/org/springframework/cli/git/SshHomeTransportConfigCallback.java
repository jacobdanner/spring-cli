/*
 * Copyright 2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.cli.git;

import java.io.File;

import com.jcraft.jsch.Session;
import org.eclipse.jgit.api.TransportConfigCallback;
import org.eclipse.jgit.transport.SshSessionFactory;
import org.eclipse.jgit.transport.SshTransport;
import org.eclipse.jgit.transport.Transport;
import org.eclipse.jgit.transport.ssh.jsch.JschConfigSessionFactory;
import org.eclipse.jgit.transport.ssh.jsch.OpenSshConfig;
import org.eclipse.jgit.transport.sshd.JGitKeyCache;
import org.eclipse.jgit.transport.sshd.SshdSessionFactory;
import org.eclipse.jgit.transport.sshd.SshdSessionFactoryBuilder;
import org.eclipse.jgit.util.FS;

class SshHomeTransportConfigCallback implements TransportConfigCallback {

	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(SshHomeTransportConfigCallback.class);

	private static final JGitKeyCache keyCache = new JGitKeyCache();

	private final SshSessionFactory sshSessionFactory = new JschConfigSessionFactory() {
		@Override
		protected void configure(OpenSshConfig.Host host, Session session) {
			// TODO: clean this up, use Paths.get() instead of File.separator?
			File homeSshDir = new File(FS.DETECTED.userHome() + File.separator + ".ssh");
			SshdSessionFactory sshSessionFactory = new SshdSessionFactoryBuilder().setSshDirectory(homeSshDir)
				.setPreferredAuthentications("publickey")
				.setHomeDirectory(FS.DETECTED.userHome())
				.build(keyCache);
			setInstance(sshSessionFactory);
		}
	};

	@Override
	public void configure(Transport transport) {
		SshTransport sshTransport = (SshTransport) transport;
		sshTransport.setSshSessionFactory(sshSessionFactory);
	}

}
