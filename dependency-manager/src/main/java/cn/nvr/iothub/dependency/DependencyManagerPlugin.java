package cn.nvr.iothub.dependency;

import io.freefair.gradle.plugins.lombok.LombokBasePlugin;
import io.freefair.gradle.plugins.lombok.LombokPlugin;
import io.spring.gradle.dependencymanagement.DependencyManagementPlugin;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.JavaBasePlugin;
import org.gradle.api.plugins.JavaPlugin;
import org.springframework.boot.gradle.plugin.SpringBootPlugin;

/**
 * @author wxl
 */
public class DependencyManagerPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        System.out.println("IotHub Dependency Manager plugin.");
        //通用属性
        project.getExtensions().add("springBootVersion", "2.7.4");
        project.getExtensions().add("springCloudVersion", "2021.0.4");

        //插件
        project.getPlugins().apply(JavaPlugin.class);
        project.getPlugins().apply(JavaBasePlugin.class);

        project.getPlugins().apply(SpringBootPlugin.class);
        project.getPlugins().apply(DependencyManagementPlugin.class);

        project.getPlugins().apply(LombokPlugin.class);
        project.getPlugins().apply(LombokBasePlugin.class);

        //仓库
        project.getRepositories().mavenLocal();
        project.getRepositories().mavenCentral();
        project.getRepositories().maven(mavenArtifactRepository -> mavenArtifactRepository.setUrl("https://maven.aliyun.com/nexus/content/groups/public/"));
        project.getRepositories().maven(mavenArtifactRepository -> mavenArtifactRepository.setUrl("https://repo.spring.io/release"));

//        project.getDependencies().add("implementation", "org.mapstruct:mapstruct:1.5.2.Final");
    }
}
