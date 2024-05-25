/*
 * Copyright 2022-2023 the original author or authors.
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

package org.springframework.cli.command;

import org.junit.jupiter.api.Test;

import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.cli.config.SpringCliUserConfig;
import org.springframework.cli.support.MockConfigurations;

import static org.assertj.core.api.Assertions.assertThat;

class GithubCommandsTests {

	private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
		.withUserConfiguration(MockConfigurations.MockBaseConfig.class);

	@Test
	void login() {
	}

	@Test
	void logout() {
		this.contextRunner.withUserConfiguration(MockConfigurations.MockFakeUserHostsConfig.class).run((context) -> {
			assertThat(context).hasSingleBean(GithubCommands.class);
			assertThat(context).hasSingleBean(SpringCliUserConfig.class);

			SpringCliUserConfig springCliUserConfig = context.getBean(SpringCliUserConfig.class);
			assertThat(springCliUserConfig.getHosts().size()).isEqualTo(2);

			GithubCommands githubCommands = context.getBean(GithubCommands.class);

			githubCommands.logout("https://github.com");
			assertThat(springCliUserConfig.getHosts().size()).isEqualTo(1);

			githubCommands.logout("https://your.company.codehq.com");
			assertThat(springCliUserConfig.getHosts()).isEmpty();

		});

	}

	@Test
	void status() {
		this.contextRunner.withUserConfiguration(MockConfigurations.MockFakeUserHostsConfig.class).run((context) -> {
			assertThat(context).hasSingleBean(GithubCommands.class);
			GithubCommands githubCommands = context.getBean(GithubCommands.class);
			// TODO: need to mock github interaction

			// AttributedString githubStatus = githubCommands.status(true,
			// "https://github.com");
			// assertThat(githubStatus.toAnsi()).isEqualTo("You are logged into
			// Github(github.com) as");
			// assertThat(capturedOutput.getAll()).contains("Got loginName");
		});

	}

}
