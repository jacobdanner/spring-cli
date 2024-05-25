package org.springframework.cli.command;

import org.jline.utils.AttributedString;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.cli.config.SpringCliUserConfig;
import org.springframework.cli.support.MockConfigurations;

import static org.assertj.core.api.Assertions.assertThat;

class GithubCommandsTest {

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