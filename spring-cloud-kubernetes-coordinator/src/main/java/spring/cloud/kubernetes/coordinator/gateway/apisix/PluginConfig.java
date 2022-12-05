package spring.cloud.kubernetes.coordinator.gateway.apisix;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wxl
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PluginConfig {

    private String namespace;

    private String service;
}