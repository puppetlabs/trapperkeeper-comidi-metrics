## `trapperkeeper-comidi-metrics`

[![Build Status](https://travis-ci.org/puppetlabs/trapperkeeper-comidi-metrics.svg?branch=master)](https://travis-ci.org/puppetlabs/trapperkeeper-comidi-metrics)

[![Clojars Project](http://clojars.org/puppetlabs/trapperkeeper-comidi-metrics/latest-version.svg)](http://clojars.org/puppetlabs/trapperkeeper-comidi-metrics)

`trapperkeeper-comidi-metrics` is a library that provides Ring middleware to
automatically track metrics for requests to all of your
[`comidi`](https://github.com) HTTP routes.

To get the most value out of this library, use it in concert with
[trapperkeeper-metrics](https://github.com/puppetlabs/trapperkeeper-metrics)
(to take advantage of the built-in ring-middleware for tracking HTTP metrics)
and the [Trapperkeeper Status Service](https://github.com/puppetlabs/trapperkeeper-status)
(to expose the most useful metrics data from your app via HTTP).

### Quick Start

This repo includes an example app that is intended to show how to tie all of those
pieces together.  The easiest way to get started is probably to run the example
app, and then take a peek into the code.

To start the example web app, run:

`lein example`

Then, in another shell, run:

`lein example-data`

This will generate some random requests to the web app, and then
issue a request to the status endpoint to print some summary data
about the metrics.

For more info about the HTTP metrics, see the [comidi metrics documentation](./documentation/metrics.md).

### Detailed Info

`trapperkeeper-comidi-metrics` contains a Ring middleware that can be used in concert with a `MetricRegistry`
(from the [dropwizard Metrics library](https://dropwizard.github.io/metrics)) to
automatically track metrics for all of your `comidi` routes.  This can be used in conjunction
with the [Trapperkeeper Status Service](https://github.com/puppetlabs/trapperkeeper-status)
to easily expose debugging / telemetry information via HTTP.

Here's an example of how to use the Ring middleware:

```clj
(defn my-routes
  [url-prefix]
  (comidi/context path
    (comidi/context "/v1"
      (comidi/routes
        (comidi/GET ["/foo"] request
          (handle-foo request))
        (comidi/ANY ["/bar/" :bar] [bar]
          (handle-bar bar))))))

(tk/defservice my-web-service
  [[:WebroutingService add-ring-handler get-route]
   [:MetricsService get-metrics-registry]]
  (init [this context]
    (let [path (get-route this)
          metrics-registry (get-metrics-registry)
          routes (my-routes path)
          route-metadata (comidi/route-metadata routes)
          http-metrics (http-metrics/initialize-http-metrics!
                         metrics-registry
                         "localhost"
                         route-metadata)
          handler (-> (comidi/routes->handler routes)
                      (http-metrics/wrap-with-request-metrics http-metrics)
                      (comidi/wrap-with-route-metadata routes))]
      (add-ring-handler this handler)
      (log/info "REQUEST SUMMARY:" (http-metrics/request-summary http-metrics)))
    context))
```

(Note that `http-metrics/wrap-with-request-metrics` relies on data from the
`comidi/wrap-with-route-metadata` middleware.)

In the example above, the call to `request-summary` will return counts of zero
for all of the routes, because no requests have been handled yet, but the data
structure returned by `request-summary` will look something like this in a real-world
scenario:

```
{:routes
 {:total {:route-id "total", :count 100, :mean 18, :aggregate 1800},
  :other {:route-id "other", :count 0, :mean 0, :aggregate 0},
  "example-v1-foo"
  {:route-id "example-v1-foo",
   :count 34,
   :mean 10,
   :aggregate 340},
  "example-v1-bar-:bar"
  {:route-id "example-v1-bar-:bar",
   :count 30,
   :mean 18,
   :aggregate 540}},
 :sorted-routes
 [{:route-id "total", :count 100, :mean 18, :aggregate 1800},
  {:route-id "example-v1-bar-:bar",
   :count 30,
   :mean 18,
   :aggregate 540},
  {:route-id "example-v1-foo",
   :count 34,
   :mean 10,
   :aggregate 340},
  {:route-id "other", :count 0, :mean 0, :aggregate 0}]}
```

For each route in your `comidi` route tree, `initialize-http-metrics!` will
generate a unique `route-id`.  Then, `request-summary` can be used to retrieve
the metrics data for each route.  The return value of `request-summary`
is a map with two keys; `:routes`, and `:sorted-routes`.  They
contain the same data, but `:routes` contains a nested map whose keys are the
`route-ids`, so that you can look up the data for a specific route, while
the value `:sorted-routes` is a vector sorted by the aggregate amount of time
spent handling requests for each route (in descending order).

For each route, the data returned includes a `:count` of how many requests have
been made, a `:mean` time (in milliseconds) indicating how long it's taken to handle the average
request, and an `:aggregate` time (in milliseconds) showing how much total time has been spent
handling requests for that route.

There are two special route ids included: `:total`, which summarizes all requests
across all routes, and `:other`, which tracks metrics for requests that did not
match any of your routes.

For a complete example that illustrates how to expose the HTTP metrics data
via the [Trapperkeeper Status Service](https://github.com/puppetlabs/trapperkeeper-status),
see the [source code for the sample web application](../dev/example/comidi_metrics_web_app.clj).

## License

See [LICENSE](LICENSE).

## Support

We use the [Trapperkeeper project on
JIRA](https://tickets.puppetlabs.com/browse/TK) for tickets on this project,
although Github issues are welcome too.
