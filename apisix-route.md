- account

```json
{
  "uri": "/account/**",
  "name": "account",
  "methods": [
    "GET",
    "POST",
    "PUT",
    "DELETE",
    "PATCH",
    "HEAD",
    "OPTIONS",
    "CONNECT",
    "TRACE"
  ],
  "plugins": {
    "ext-plugin-pre-req": {
      "conf": [
        {
          "name": "KubernetesDiscoveryServiceChooseFilter",
          "value": "{\"service\":\"account\"}"
        }
      ],
      "disable": false
    }
  },
  "upstream": {
    "nodes": [
      {
        "host": "127.0.0.1",
        "port": 8080,
        "weight": 1
      },
      {
        "host": "127.0.0.1",
        "port": 8081,
        "weight": 1
      }
    ],
    "timeout": {
      "connect": 6,
      "send": 6,
      "read": 6
    },
    "type": "chash",
    "hash_on": "header",
    "key": "x-lb-ip-port",
    "scheme": "http",
    "pass_host": "pass",
    "keepalive_pool": {
      "idle_timeout": 60,
      "requests": 1000,
      "size": 320
    }
  },
  "status": 1
}
```

- uaa

```json
{
  "uri": "/uaa/**",
  "name": "uaa",
  "methods": [
    "GET",
    "POST",
    "PUT",
    "DELETE",
    "PATCH",
    "HEAD",
    "OPTIONS",
    "CONNECT",
    "TRACE"
  ],
  "plugins": {
    "ext-plugin-pre-req": {
      "conf": [
        {
          "name": "KubernetesDiscoveryServiceChooseFilter",
          "value": "{\"service\":\"uaa\"}"
        }
      ],
      "disable": false
    }
  },
  "upstream": {
    "nodes": [
      {
        "host": "127.0.0.1",
        "port": 8080,
        "weight": 1
      },
      {
        "host": "127.0.0.1",
        "port": 8081,
        "weight": 1
      }
    ],
    "timeout": {
      "connect": 6,
      "send": 6,
      "read": 6
    },
    "type": "chash",
    "hash_on": "header",
    "key": "x-lb-ip-port",
    "scheme": "http",
    "pass_host": "pass",
    "keepalive_pool": {
      "idle_timeout": 60,
      "requests": 1000,
      "size": 320
    }
  },
  "status": 1
}
```

- organization

```json
{
  "uri": "/organization/**",
  "name": "organization",
  "methods": [
    "GET",
    "POST",
    "PUT",
    "DELETE",
    "PATCH",
    "HEAD",
    "OPTIONS",
    "CONNECT",
    "TRACE"
  ],
  "plugins": {
    "ext-plugin-pre-req": {
      "conf": [
        {
          "name": "KubernetesDiscoveryServiceChooseFilter",
          "value": "{\"service\":\"organization\"}"
        }
      ],
      "disable": false
    }
  },
  "upstream": {
    "nodes": [
      {
        "host": "127.0.0.1",
        "port": 8080,
        "weight": 1
      },
      {
        "host": "127.0.0.1",
        "port": 8081,
        "weight": 1
      }
    ],
    "timeout": {
      "connect": 6,
      "send": 6,
      "read": 6
    },
    "type": "chash",
    "hash_on": "header",
    "key": "x-lb-ip-port",
    "scheme": "http",
    "pass_host": "pass",
    "keepalive_pool": {
      "idle_timeout": 60,
      "requests": 1000,
      "size": 320
    }
  },
  "status": 1
}
```

- proxy

```json
{
  "uri": "/apisixproxy/**",
  "name": "apisixproxy",
  "methods": [
    "GET",
    "POST",
    "PUT",
    "DELETE",
    "PATCH",
    "HEAD",
    "OPTIONS",
    "CONNECT",
    "TRACE"
  ],
  "upstream": {
    "nodes": [
      {
        "host": "127.0.0.1",
        "port": 8080,
        "weight": 1
      },
      {
        "host": "127.0.0.1",
        "port": 8081,
        "weight": 1
      }
    ],
    "timeout": {
      "connect": 6,
      "send": 6,
      "read": 6
    },
    "type": "chash",
    "hash_on": "header",
    "key": "x-lb-ip-port",
    "scheme": "http",
    "pass_host": "pass",
    "keepalive_pool": {
      "idle_timeout": 60,
      "requests": 1000,
      "size": 320
    }
  },
  "status": 1
}
```