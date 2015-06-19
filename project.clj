(def ks-version "1.1.0")
(def tk-version "1.1.1")

(defproject puppetlabs/trapperkeeper-comidi-metrics "0.1.0-SNAPSHOT"
  :description "Comidi/HTTP Metrics for Trapperkeeper"
  :url "http://github.com/puppetlabs/trapperkeeper-comidi-metrics"

  :pedantic? :abort

  :dependencies [[org.clojure/clojure "1.6.0"]
                 [clj-time "0.7.0"]

                 [puppetlabs/kitchensink ~ks-version]
                 [puppetlabs/trapperkeeper ~tk-version]]

  :profiles {:dev {:dependencies [[puppetlabs/trapperkeeper ~tk-version :classifier "test"]
                                  [puppetlabs/kitchensink ~ks-version :classifier "test"]]}})
