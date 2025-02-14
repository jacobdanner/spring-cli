package org.springframework.cli.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.util.DefaultXmlPrettyPrinter;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.PluginExecution;
import org.codehaus.plexus.util.xml.XmlUtil;
import org.codehaus.plexus.util.xml.XmlWriterUtil;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.codehaus.plexus.util.xml.Xpp3DomUtils;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openrewrite.maven.internal.MavenXmlMapper;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

//@Disabled("This is a WIP Test")
class ConversionUtilsTest {



    @DisplayName("Characterization Test to validate how this current works, note the xml declaration AND the duplicate dependencies element")
    @Disabled("This test exists to show what was actually coming out of ConversionUtil")
    @Test
    void existingFromDependencyListToString() {
        List<Dependency> dependencies = new ArrayList<>();
        Dependency dependency = new Dependency();
        dependency.setGroupId("org.springframework");
        dependency.setArtifactId("spring-core");
        dependency.setVersion("5.2.0.RELEASE");
        dependencies.add(dependency);
        String result = ConversionUtils.fromDependencyListToString(dependencies);
        assertThat(result.replaceAll("\\s",""))
                .isEqualTo("<?xmlversion=\"1.0\"encoding=\"UTF-8\"standalone=\"yes\"?>" +
                        "<dependencies>" +
                        "<dependencies>" +
                        "<artifactId>spring-core</artifactId>" +
                        "<groupId>org.springframework</groupId>" +
                        "<optional>false</optional>" +
                        "<version>5.2.0.RELEASE</version>" +
                        "</dependencies>" +
                        "</dependencies>");

    }

    @DisplayName("After ConversionUtils is corrected, this test should pass")
    @Test
    void validateFromDependencyListToString() {
        List<Dependency> dependencies = new ArrayList<>();
        Dependency dependency = new Dependency();
        dependency.setGroupId("org.springframework");
        dependency.setArtifactId("spring-core");
        dependency.setVersion("5.2.0.RELEASE");
        dependencies.add(dependency);
        String result = ConversionUtils.fromDependencyListToString(dependencies);
        assertThat(result.replaceAll("\\s",""))
                .isEqualTo("<dependencies>" +
                        "<dependency>" +
                        "<artifactId>spring-core</artifactId>" +
                        "<groupId>org.springframework</groupId>" +
                        "<version>5.2.0.RELEASE</version>" +
                        "</dependency>" +
                        "</dependencies>");

    }


    @DisplayName("fromDomToString removes XML declaration")
    @Test
    void fromDomToStringRemovesXmlDeclaration() {
        Xpp3Dom dom = new Xpp3Dom("root");
        Xpp3Dom child = new Xpp3Dom("child");
        child.setValue("value");
        dom.addChild(child);
        String result = ConversionUtils.fromDomToString(dom);
        assertThat(result).doesNotContain("<?xml").contains("<root>").contains("</root>").contains("<child>value</child>");
    }

    @DisplayName("fromDomToString handles empty Xpp3Dom")
    @Test
    void fromDomToStringHandlesEmptyXpp3Dom() {
        Xpp3Dom dom = new Xpp3Dom("root");
        String result = ConversionUtils.fromDomToString(dom);
        assertThat(result).isEqualTo("<root/>");
    }


    @DisplayName("fromPluginExecutionListToString formats plugin executions correctly")
    @Test
    void fromPluginExecutionListToStringFormatsPluginExecutionsCorrectly() {
        List<PluginExecution> executions = new ArrayList<>();
        PluginExecution pluginExecution = new PluginExecution();
        pluginExecution.setId("default");
        pluginExecution.setPhase("compile");
        executions.add(pluginExecution);
        String result = ConversionUtils.fromPluginExecutionListToString(executions);
        assertThat(result.replaceAll("\\s","")).isEqualTo("<executions><execution><id>default</id><phase>compile</phase></execution></executions>");
    }

    @DisplayName("fromPluginExecutionListToString handles empty plugin execution list")
    @Test
    void fromPluginExecutionListToStringHandlesEmptyPluginExecutionList() {
        List<PluginExecution> executions = new ArrayList<>();
        String result = ConversionUtils.fromPluginExecutionListToString(executions);
        assertThat(result.replaceAll("\\s","")).isEqualTo("<executions/>");
    }

    @DisplayName("fromPluginExecutionListToString formats multiple plugin executions correctly")
    @Test
    void fromPluginExecutionListToStringFormatsMultiplePluginExecutionsCorrectly() throws JsonProcessingException {
        List<PluginExecution> executions = new ArrayList<>();
        PluginExecution pluginExecution1 = new PluginExecution();
        pluginExecution1.setId("default1");
        pluginExecution1.setPhase("compile2");
        pluginExecution1.addGoal("foo");
        pluginExecution1.addGoal("baz");
        executions.add(pluginExecution1);

        PluginExecution pluginExecution2 = new PluginExecution();
        pluginExecution2.setId("test");
        pluginExecution2.setPhase("test");
        executions.add(pluginExecution2);
        String xmlMapperString = MavenXmlMapper.readMapper()
                .setDefaultPrettyPrinter(new DefaultXmlPrettyPrinter())
                .writeValueAsString(executions);
//        org.apache.maven.shared.utils.xml.
//        XmlMapper xmlMapper = new XmlMapper();
//        String xmlMapperString = xmlMapper.writeValueAsString(executions);
      	System.out.println("xmlMapper: \n"+xmlMapperString);
        String result = ConversionUtils.fromPluginExecutionListToString(executions);
        assertThat(result.replaceAll("\\s","")).isEqualTo("<executions>" +
                "<execution><id>default1</id><phase>compile2</phase></execution>" +
                "<execution><id>test</id><phase>test</phase></execution>" +
                "</executions>");
    }

    @DisplayName("fromPluginExecutionListToString handles plugin execution with goals")
    @Test
    void fromPluginExecutionListToStringHandlesPluginExecutionWithGoals() {
        List<PluginExecution> executions = new ArrayList<>();
        PluginExecution pluginExecution = new PluginExecution();
        pluginExecution.setId("default2");
        pluginExecution.setPhase("compile2");
        pluginExecution.addGoal("goal1");
        pluginExecution.addGoal("goal2");
        executions.add(pluginExecution);

        String result = ConversionUtils.fromPluginExecutionListToString(executions);
        assertThat(result.replaceAll("\\s","")).isEqualTo("<executions>" +
                "<execution><id>default2</id><goals><goal>goal1</goal><goal>goal2</goal></goals><phase>compile2</phase></execution>" +
                "</executions>");
    }
    @DisplayName("fromPluginExecutionListToString handles plugin execution with goals")
    @Test
    void fromPluginExecutionListToStringRemovedDefaultId() {
        List<PluginExecution> executions = new ArrayList<>();
        PluginExecution pluginExecution = new PluginExecution();
        pluginExecution.setId("default");
        pluginExecution.setPhase("compile2");
        pluginExecution.addGoal("goal1");
        pluginExecution.addGoal("goal2");

        executions.add(pluginExecution);

        String result = ConversionUtils.fromPluginExecutionListToString(executions);
        assertThat(result.replaceAll("\\s","")).isEqualTo("<executions>" +
                "<execution><goals><goal>goal1</goal></goals><phase>compile2</phase></execution>" +
                "</executions>");
    }




}