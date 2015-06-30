(def ks-version "1.1.0")
(def tk-version "1.1.1")

(defproject puppetlabs/trapperkeeper-comidi-metrics "0.1.1"
  :description "Comidi/HTTP Metrics for Trapperkeeper"
  :url "http://github.com/puppetlabs/trapperkeeper-comidi-metrics"

  :pedantic? :abort

  :dependencies [[org.clojure/clojure "1.6.0"]
                 [clj-time "0.7.0"]
                 [slingshot "0.12.2"]
                 [org.slf4j/slf4j-api "1.7.7"]
                 [commons-io "2.4"]

                 [puppetlabs/kitchensink ~ks-version]
                 [puppetlabs/trapperkeeper ~tk-version :exclusions [org.clojure/tools.macro]]
                 [puppetlabs/trapperkeeper-metrics "0.1.1"]
                 [puppetlabs/comidi "0.1.3"]]

  :deploy-repositories [["releases" {:url "https://clojars.org/repo"
                                     :username :env/clojars_jenkins_username
                                     :password :env/clojars_jenkins_password
                                     :sign-releases false}]]

  :profiles {:dev {:source-paths ["dev"]
                   :dependencies [[puppetlabs/trapperkeeper ~tk-version :classifier "test" :exclusions [org.clojure/tools.macro]]
                                  [puppetlabs/kitchensink ~ks-version :classifier "test"]
                                  [puppetlabs/trapperkeeper-status "0.1.1"]
                                  [puppetlabs/http-client "0.4.4"]
                                  [puppetlabs/trapperkeeper-webserver-jetty9 "1.3.1"]]}}

  :aliases {"example" ["run" "-m" "example.comidi-metrics-web-app"]
            "example-data" ["run" "-m" "example.traffic-generator"]})
