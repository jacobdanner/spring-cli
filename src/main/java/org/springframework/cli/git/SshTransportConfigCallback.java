/*
 * Copyright 2021-2023 the original author or authors.
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

import com.jcraft.jsch.Session;
import org.eclipse.jgit.api.TransportConfigCallback;
import org.eclipse.jgit.transport.SshSessionFactory;
import org.eclipse.jgit.transport.SshTransport;
import org.eclipse.jgit.transport.Transport;
import org.eclipse.jgit.transport.ssh.jsch.JschConfigSessionFactory;
import org.eclipse.jgit.transport.ssh.jsch.OpenSshConfig;

class SshTransportConfigCallback implements TransportConfigCallback {

	private final SshSessionFactory sshSessionFactory = new JschConfigSessionFactory() {
		@Override
		protected void configure(OpenSshConfig.Host hc, Session session) {
			session.setConfig("StrictHostKeyChecking", "no");
		}
	};

	@Override
	public void configure(Transport transport) {
		SshTransport sshTransport = (SshTransport) transport;
		sshTransport.setSshSessionFactory(sshSessionFactory);
	}

}
