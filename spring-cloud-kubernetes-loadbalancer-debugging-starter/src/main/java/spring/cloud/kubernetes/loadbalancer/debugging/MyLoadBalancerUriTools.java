package spring.cloud.kubernetes.loadbalancer.debugging;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.web.util.UriComponentsBuilder;
import spring.cloud.kubernetes.loadbalancer.Cons;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * @author wxl
 */
public class MyLoadBalancerUriTools {


    private MyLoadBalancerUriTools() {
        throw new IllegalStateException("Can't instantiate a utility class");
    }

    private static final String PERCENTAGE_SIGN = "%";

    private static final String DEFAULT_SCHEME = "http";

    private static final String DEFAULT_SECURE_SCHEME = "https";

    private static final Map<String, String> INSECURE_SCHEME_MAPPINGS;

    static {
        INSECURE_SCHEME_MAPPINGS = new HashMap<>();
        INSECURE_SCHEME_MAPPINGS.put(DEFAULT_SCHEME, DEFAULT_SECURE_SCHEME);
        INSECURE_SCHEME_MAPPINGS.put("ws", "wss");
    }

    // see original
    // https://github.com/spring-cloud/spring-cloud-gateway/blob/main/spring-cloud-gateway-core/
    // src/main/java/org/springframework/cloud/gateway/support/ServerWebExchangeUtils.java
    private static boolean containsEncodedParts(URI uri) {
        boolean encoded = (uri.getRawQuery() != null && uri.getRawQuery().contains(PERCENTAGE_SIGN))
                || (uri.getRawPath() != null && uri.getRawPath().contains(PERCENTAGE_SIGN))
                || (uri.getRawFragment() != null && uri.getRawFragment().contains(PERCENTAGE_SIGN));
        // Verify if it is really fully encoded. Treat partial encoded as unencoded.
        if (encoded) {
            try {
                UriComponentsBuilder.fromUri(uri).build(true);
                return true;
            } catch (IllegalArgumentException ignore) {
            }
            return false;
        }
        return false;
    }

    private static int computePort(int port, String scheme) {
        if (port >= 0) {
            return port;
        }
        if (Objects.equals(scheme, DEFAULT_SECURE_SCHEME)) {
            return 443;
        }
        return 80;
    }

    /**
     * Modifies the URI in order to redirect the request to a service instance of choice.
     *
     * @param serviceInstance the {@link ServiceInstance} to redirect the request to.
     * @param original        the {@link URI} from the original request
     * @param prefix
     * @param podService
     * @return the modified {@link URI}
     */
    public static URI reconstructURI(ServiceInstance serviceInstance, URI original, String prefix, String podService) {
        if (serviceInstance == null) {
            throw new IllegalArgumentException("Service Instance cannot be null.");
        }
        return doReconstructURI(serviceInstance, original, prefix, podService);
    }

    private static URI doReconstructURI(ServiceInstance serviceInstance, URI original, String prefix, String podService) {
        String host = serviceInstance.getHost();
        String scheme = Optional.ofNullable(serviceInstance.getScheme())
                .orElse(computeScheme(original, serviceInstance));
        int port = computePort(serviceInstance.getPort(), scheme);

        if (Objects.equals(host, original.getHost()) && port == original.getPort()
                && Objects.equals(scheme, original.getScheme())) {
            return original;
        }

        boolean encoded = containsEncodedParts(original);
        return UriComponentsBuilder
                .fromPath(prefix + original.getPath())
                .scheme(scheme)
                .host(host)
                .port(port)
                .queryParam(Cons.LB_IP_PORT_PARAM, podService)
                .build(encoded).toUri();
    }

    private static String computeScheme(URI original, ServiceInstance serviceInstance) {
        String originalOrDefault = Optional.ofNullable(original.getScheme()).orElse(DEFAULT_SCHEME);
        if (serviceInstance.isSecure() && INSECURE_SCHEME_MAPPINGS.containsKey(originalOrDefault)) {
            return INSECURE_SCHEME_MAPPINGS.get(originalOrDefault);
        }
        return originalOrDefault;
    }
}
