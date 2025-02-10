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

package org.springframework.cli.util;

import java.io.StringWriter;
import java.util.List;

import javax.xml.bind.JAXB;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.PluginExecution;
import org.codehaus.plexus.util.xml.Xpp3Dom;

/**
 * @author Oleg Zhurakousky
 */
public final class ConversionUtils {

	private ConversionUtils() {

	}

	public static String fromDomToString(Xpp3Dom dom) {
		String element = dom.toString();
		return element;
	}

	public static String fromDependencyListToString(List<Dependency> dependencies) {
		StringWriter sw = new StringWriter();
		Dependencies deps = new Dependencies(dependencies);
		JAXB.marshal(deps, sw);
		String xmlString = sw.toString();
		return xmlString;
	}

	public static String fromPluginExecutionListToString(List<PluginExecution> pluginExecutions) {
		StringWriter sw = new StringWriter();
		PluginExecutions pluginExecs = new PluginExecutions(pluginExecutions);
		JAXB.marshal(pluginExecs, sw);
		String xmlString = sw.toString();
		return xmlString;
	}

	/*
	 * IMPORTANT: Because of JAXB, this class must be public and have public setters and
	 * getters, to produce proper <dependencies> XML segment.
	 */
	public static class Dependencies {

		private List<Dependency> dependencies;

		public Dependencies(List<Dependency> dependencies) {
			this.dependencies = dependencies;
		}

		public List<Dependency> getDependencies() {
			return dependencies;
		}

		public void setDependencies(List<Dependency> dependencies) {
			this.dependencies = dependencies;
		}

	}

	/*
	 * IMPORTANT: Because of JAXB, this class must be public and have public setters and
	 * getters, to produce proper <dependencies> XML segment.
	 */
	public static class PluginExecutions {

		private List<PluginExecution> pluginExecutions;

		public PluginExecutions(final List<PluginExecution> pluginExecutions) {
			this.pluginExecutions = pluginExecutions;
		}

		public List<PluginExecution> getPluginExecutions() {
			return pluginExecutions;
		}

		public void setPluginExecutions(final List<PluginExecution> pluginExecutions) {
			this.pluginExecutions = pluginExecutions;
		}
	}

}
